package com.hotburguer.fragments.payment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.bumptech.glide.util.Util;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerInterface;
import com.hotburguer.acustom.Cart;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogCardsSaved;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.itens.HotDayOperation;
import com.hotburguer.itens.HotOrder;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;
import com.hotburguer.utils.CacheApp;
import com.hotburguer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import apis.androidid.APIDeviceID;
import apis.cache.CacheAPI;
import apis.card.CardManager;
import apis.document.APIDocument;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.location.LAddress;
import apis.userbuyer.BAddress;
import apis.userbuyer.BCard;
import apis.userbuyer.BContact;
import apis.userbuyer.BDocument;
import apis.userbuyer.BPhone;
import apis.userbuyer.BProduct;
import apis.userbuyer.UserBuyer;

import static com.hotburguer.acustom.CustomApplication.createBuyer;
import static com.hotburguer.acustom.CustomApplication.getApiRequisition;
import static com.hotburguer.acustom.CustomApplication.getOrder;
import static com.hotburguer.acustom.CustomApplication.getUser;
import static com.hotburguer.acustom.CustomApplication.setUser;
import static com.hotburguer.fragments.payment.DetectBrandListener.detectBrandCard;
import static com.hotburguer.utils.Utils.FieldText.setMaskInEditText;

public class FMT_Payment extends Fragment {

    private Controller controller;
    private BCard card;
    private HomeBurguerInterface interfaceHome;
    private DialogCardsSaved cardsSaveds;

    public FMT_Payment(HomeBurguerInterface homeBurguerInterface) {
        this.card = new BCard();
        this.interfaceHome = homeBurguerInterface;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_payment1, null);

        controller = new Controller(view);
        requestCardsSaved();

        Utils.SystemUI.KeyboardUtils.addKeyboardToggleListener(getActivity(), new Utils.SystemUI.KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (!isVisible)
                    CustomApplication.hideSystemUI(getActivity());
            }
        });

        return view;
    }

    private boolean checkContainsOnlyDrinks() {
        Cart cart = getOrder().getCart();

        Integer drinks = cart.getCountProductFromCategorie().get(HotProduct.Categories.DRINKS);
        Integer snacks = cart.getCountProductFromCategorie().get(HotProduct.Categories.SNACKS);

        if (cart != null) {
            if (drinks != null && drinks > 0 &&
                    (snacks != null && snacks == 0 || snacks == null)) {

                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private final void payment(Object hash) {
        CacheApp.clearCart(getContext());
        CacheAPI.saveObject(getContext(), HotOrder.class.getSimpleName(), getOrder(), true);

        UserBuyer buyer = createBuyer();
        CardManager.HashManager hashCard = new CardManager.HashManager(getContext(), getCardComplete());

        String cardHash = hashCard.getCardHash();
        saveCard(cardHash.replaceAll("\\s","")
                .replaceAll("\t","")
                .replaceAll("\n",""));

        buyer.getCard().setId(card.getId());
        buyer.getCard().setCardHash(cardHash);

        if(cardHash != null)
        card.setId(null);

        if (hash != null)
            buyer.setKeyOrder(hash.toString().getBytes());

        if (buyer.validateCustomer(getContext())) {

            interfaceHome.disableNextButton(true);

            try {
                JSONObject doc = new JSONObject();
                doc.put("cpf",buyer.getDocument().get());
                doc.put("typ",buyer.getDocument().getType());

                JSONObject contact = new JSONObject();
                contact.put("ema",buyer.getName());
                contact.put("cel",buyer.getContact().getPhone().getFullNumber());

                JSONObject address = new JSONObject();
                address.put("cou",buyer.getAddressDefault().getCountry());
                address.put("sta",buyer.getAddressDefault().getState());
                address.put("cit",buyer.getAddressDefault().getCity());
                address.put("nei",buyer.getAddressDefault().getNeighborhood());
                address.put("str",buyer.getAddressDefault().getStreet());
                address.put("num",buyer.getAddressDefault().getStreetNumber());
                address.put("com",buyer.getAddressDefault().getComplement());
                address.put("cep",buyer.getAddressDefault().getZipCode());
                /*address.put("lat",buyer.getAddressDefault().getLa);
                address.put("lng","");*/

                JSONObject card = new JSONObject();
                card.put("cid",this.card.getId());
                card.put("cha",this.card.getCardHash());

                JSONObject buy = new JSONObject();
                buy.put("id",buyer.getId());
                buy.put("idc",buyer.getIdCustomer());
                buy.put("ins",buyer.getInstallments());
                buy.put("na",buyer.getName());
                buy.put("bir",buyer.getBirthday());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("request-order");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(WebController.Checkout.CONTROLLER);
            requestServer.setFunction(WebController.Checkout.Functions.CREATE_PAYMENT);
            requestServer.setObjectTransport(buyer);
            requestServer.request(getContext(), new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        interfaceHome.loading(false);

                        Gson gson = new Gson();
                        Payment payment = gson.fromJson(object.toString(), Payment.class);

                        getOrder().setStatuspayment(Payment.Status.getStatusFrom(payment.getStatus()));

                        if (payment.getStatus() == Payment.PAID || payment.getStatus() == Payment.PROCESSING
                        || payment.getStatus() == Payment.AUTHORIZED) {
                            CacheAPI.saveObject(getContext(), HotOrder.class.getSimpleName(), getOrder(), true);
                        }
                        interfaceHome.disableNextButton(false);
                        interfaceHome.resultPayment(payment);
                    }
                }

                @Override
                public void onError(Exception e) {
                    interfaceHome.loading(false);
                    interfaceHome.disableNextButton(false);
                    e.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "Dados do Comprador Pendente!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void makeOrder() {

        HotUser user = getUser();
        //user.setCard(getCardComplete());

        if (user.getBirthOfDate() == null) {
            user.setBirthOfDate(controller.getEtDateOfBirth().getText().toString());
            user.setAddress(getOrder().getAddress());
            user.getContact().setEmail(user.getEmail());
            user.getContact().setPhone(controller.getPhone());
            user.getDocument().setCPF(controller.getEtCpf().getText().toString());
            user.setCpf(controller.getEtCpf().getText().toString());
        }

        boolean validCard = validateCard(user.getCard());
        boolean validUser = validateUser(user);

        if (validUser && validCard) {
            interfaceHome.loading(true);

            try {
                JSONObject send = new JSONObject();
                send.put("id","");
                send.put("n","");
                send.put("cpf","");
                send.put("em","");
                send.put("cl","");
                send.put("dtn","");

                if (user.getIdCustomer() == 0) {
                    InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                    requestServer.setIdRequisition("updateUser");
                    requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                    requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
                    requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                    requestServer.setDomain(getString(R.string.domain_connection));
                    requestServer.setController(WebController.User.CONTROLLER);
                    requestServer.setFunction(WebController.User.Functions.UPDATE_USER);
                    requestServer.setObjectTransport(user);
                    requestServer.request(getContext(), new InternetRequisitionResponse() {
                        @Override
                        public void onLoading(boolean loading) {
                        }

                        @Override
                        public void onSucess(Object object) {
                            if (object != null) {

                                if (user != null) {

                                    HotUser userResponse = new Gson().fromJson(object.toString(), HotUser.class);
                                    if (userResponse != null && user.getIdCustomer() == 0 && user.getAddress().getStreet() == null) {
                                        setUser(userResponse);
                                        CacheAPI.saveObject(getContext(), HotUser.class.getSimpleName(), userResponse, true);
                                    }

                                    HotOrder order = getOrder();
                                    order.setIdUser(user.getId());
                                    //order.setIdDevice(user.getDeviceID());

                                    InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                                    requestServer.setIdRequisition("request-order");
                                    requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                                    requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
                                    requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                                    requestServer.setDomain(getString(R.string.domain_connection));
                                    requestServer.setController(WebController.Order.CONTROLLER);
                                    requestServer.setFunction(WebController.Order.Functions.MAKE_ORDER);
                                    requestServer.setObjectTransport(order);
                                    requestServer.request(getContext(), new InternetRequisitionResponse() {
                                        @Override
                                        public void onLoading(boolean loading) {
                                            interfaceHome.loading(loading);
                                        }

                                        @Override
                                        public void onSucess(Object object) {
                                            if (object != null) {
                                                Gson gson = new Gson();
                                                HotOrder order = gson.fromJson(object.toString(), HotOrder.class);

                                                HotOrder rootOrder = getOrder();
                                                rootOrder.setId(order.getId());
                                                rootOrder.setIdUser(order.getIdUser());

                                                CacheAPI.saveObject(getContext(), HotOrder.class.getSimpleName(), rootOrder, true);

                                                payment(order.getValidator());
                                            }
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                            interfaceHome.loading(false);
                                            e.printStackTrace();
                                        }
                                    });

                                } else {
                                    Toast.makeText(getContext(), "Dados do Cartão Inválido!!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            interfaceHome.loading(false);
                        }
                    });
                } else {

                    HotOrder order = getOrder();
                    order.setIdUser(user.getId());

                    InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                    requestServer.setIdRequisition("request-order");
                    requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                    requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
                    requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                    requestServer.setDomain(getString(R.string.domain_connection));
                    requestServer.setController(WebController.Order.CONTROLLER);
                    requestServer.setFunction(WebController.Order.Functions.MAKE_ORDER);
                    requestServer.setObjectTransport(order);
                    requestServer.request(getContext(), new InternetRequisitionResponse() {
                        @Override
                        public void onLoading(boolean loading) {
                        }

                        @Override
                        public void onSucess(Object object) {
                            if (object != null) {

                                Gson gson = new Gson();
                                HotOrder order = gson.fromJson(object.toString(), HotOrder.class);

                                HotOrder rootOrder = getOrder();
                                rootOrder.setId(order.getId());

                                CacheAPI.saveObject(getContext(), HotOrder.class.getSimpleName(), rootOrder, true);

                                payment(order.getValidator());
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            interfaceHome.loading(false);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeOrder2() {

        HotUser user = getUser();
        //user.setCard(getCardComplete());

        if (user.getBirthOfDate() == null) {
            user.setBirthOfDate(controller.getEtDateOfBirth().getText().toString());
            user.setAddress(getOrder().getAddress());
            user.getContact().setEmail(user.getEmail());
            user.getContact().setPhone(controller.getPhone());
            user.getDocument().setCPF(controller.getEtCpf().getText().toString());
            user.setCpf(controller.getEtCpf().getText().toString());
        }

        if(user.getCard() == null || user.getCard().getCardHash() == null)
            user.setCard(getCardComplete());

        boolean validCard = validateCard(user.getCard());
        boolean validUser = validateUser(user);

        if (validUser && validCard) {
            interfaceHome.loading(true);

            Map<String, Object> send = new HashMap<>();
            send.put("id",user.getId());
            send.put("n",user.getName());
            send.put("cpf",controller.getEtCpf().getText().toString());
            send.put("em",user.getEmail());
            send.put("cl",controller.getPhone().getFullNumber());
            send.put("dtn",controller.getEtDateOfBirth().getText().toString());

            if (user.getIdCustomer() == 0) {
                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("updateUser");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(WebController.User.CONTROLLER);
                requestServer.setFunction(WebController.User.Functions.UPDATE_USER);
                requestServer.setObjectTransport(send);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            if (user != null) {

                                HotUser userResponse = new Gson().fromJson(object.toString(), HotUser.class);
                                if (userResponse != null && user.getIdCustomer() == 0 && user.getAddress().getStreet() == null) {
                                    setUser(userResponse);
                                    CacheAPI.saveObject(getContext(), HotUser.class.getSimpleName(), userResponse, true);
                                }

                                HotOrder order = getOrder();
                                order.setIdUser(user.getId());
                                //order.setIdDevice(user.getDeviceID());

                                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                                requestServer.setIdRequisition("request-order");
                                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
                                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                                requestServer.setDomain(getString(R.string.domain_connection));
                                requestServer.setController(WebController.Order.CONTROLLER);
                                requestServer.setFunction(WebController.Order.Functions.MAKE_ORDER);
                                requestServer.setObjectTransport(order);
                                requestServer.request(getContext(), new InternetRequisitionResponse() {
                                    @Override
                                    public void onLoading(boolean loading) {
                                        interfaceHome.loading(loading);
                                    }

                                    @Override
                                    public void onSucess(Object object) {
                                        if (object != null) {
                                            Gson gson = new Gson();
                                            HotOrder order = gson.fromJson(object.toString(), HotOrder.class);

                                            HotOrder rootOrder = getOrder();
                                            rootOrder.setId(order.getId());
                                            rootOrder.setIdUser(order.getIdUser());

                                            CacheAPI.saveObject(getContext(), HotOrder.class.getSimpleName(), rootOrder, true);

                                            payment(order.getValidator());
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                        interfaceHome.loading(false);
                                        e.printStackTrace();
                                    }
                                });

                            } else {
                                Toast.makeText(getContext(), "Dados do Cartão Inválido!!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        interfaceHome.loading(false);
                    }
                });
            } else {

                HotOrder order = getOrder();
                order.setIdUser(user.getId());

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("request-order");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(WebController.Order.CONTROLLER);
                requestServer.setFunction(WebController.Order.Functions.MAKE_ORDER);
                requestServer.setObjectTransport(order);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            Gson gson = new Gson();
                            HotOrder order = gson.fromJson(object.toString(), HotOrder.class);

                            HotOrder rootOrder = getOrder();
                            rootOrder.setId(order.getId());

                            CacheAPI.saveObject(getContext(), HotOrder.class.getSimpleName(), rootOrder, true);

                            payment(order.getValidator());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        interfaceHome.loading(false);
                    }
                });

            }
        }
    }

    public void checkIsDeliveryOrder() {

        interfaceHome.loading(true);

        try {
            HotOrder order = getOrder();
            LAddress address = order.getAddress();

            JSONObject send = new JSONObject();
            send.put("cy",address.getContry());
            send.put("st",address.getState());
            send.put("ct",address.getCity());
            send.put("nd",address.getNeighborhood());
            send.put("cp",address.getCEP());
            send.put("uf",address.getUF());
            send.put("co",(address.getLatitude() > 0 && address.getLongitude() > 0));

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("checkIsDeliveryOrder()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getContext().getString(R.string.domain_connection));
            requestServer.setController(WebController.Order.CONTROLLER);
            requestServer.setFunction(WebController.Order.Functions.CHECK_IS_DELIVERY_LOCAL);
            requestServer.setObjectTransport(send);
            requestServer.request(getContext(), new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                }

                @Override
                public void onSucess(Object object) {
                    interfaceHome.loading(false);

                    if (object != null) {
                        HotDayOperation hotDay = new Gson().fromJson(object.toString(), HotDayOperation.class);
                        isDeliveryMessage(getContext(), hotDay);
                    }
                }

                @Override
                public void onError(Exception e) {
                    interfaceHome.loading(false);
                    e.printStackTrace();
                    Toast.makeText(getContext(), "MESSAGE: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkProductsAvailables() {
        interfaceHome.loading(true);

        Set<Integer> keySet = getOrder().getCart().keySet();
        int[] ids = new int[keySet.size()];

        int i=0;
        for(Integer id : keySet) {
            ids[i] = id;
            i++;
        }

        try {

            JSONObject ob = new JSONObject();
            ob.put("id",getUser().getId());
            ob.put("id_dev",getUser().getDeviceID());
            ob.put("id_prods",ids);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("checkProductsAvailables()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getContext().getString(R.string.domain_connection));
            requestServer.setController(WebController.Order.CONTROLLER);
            requestServer.setFunction(WebController.Order.Functions.CHECK_PRODUCTS_AVAILABLE);
            requestServer.setObjectTransport(ob);
            requestServer.request(getContext(), new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                }

                @Override
                public void onSucess(Object object) {
                    interfaceHome.loading(false);

                    if (object != null) {
                        Map<String, Integer> array = new Gson().fromJson(object.toString(), HashMap.class);

                        if(array != null && array.size() > 0) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Infelizmente os produtos abaixo esgotaram!!\n");

                            DialogInformative dialog = new DialogInformative(getContext());
                            dialog.setTitle("Opss !!!");
                            for (String key : array.keySet()) {
                                sb.append("• ");
                                sb.append(key);
                                sb.append("\n");
                            }
                            dialog.setMessage(sb.toString());
                            dialog.show();
                        } else {
                            makeOrder2();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    interfaceHome.loading(false);
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final void isDeliveryMessage(Context context, HotDayOperation object) {
        DialogInformative info = new DialogInformative(context);
        info.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                CustomApplication.hideKeyboard(getContext(), getView().getRootView());
                CustomApplication.hideSystemUI(getActivity());
            }
        });

        switch (object.getIdDay()) {
            case 0: {
                info.setTitle("Sentimos Muito!!");
                info.setMessage("No momento não entregamos nesse local...");
                info.show();
                break;
            }
            case 1: {
                info.setTitle("Mil Desculpas!");
                if (object.getDayName() != null)
                    info.setMessage("Não operamos " + object.getDayName());
                else
                    info.setMessage("Não operamos hoje");
                info.show();
                break;
            }
            case 2: {
                info.setTitle("Poxa Vida!");
                info.setMessage("Nosso horário de hoje é de: " + object.getOpeningHour() + " às " + object.getClosingHour());
                info.show();
                break;
            }
            case 3: {
                checkProductsAvailables();
                break;
            }
        }
    }

    private final void saveCard(String cardHash) {
        if (controller.getaSwitch().isChecked()) {

            HotUser sendData = new HotUser();
            sendData.setId(CustomApplication.getUser().getId());
            sendData.setDeviceID(APIDeviceID.getIdDevice(getContext()));
            sendData.setIdCustomer(getUser().getIdCustomer());

            Map<String, Object> map = new HashMap<>();
            map.put("i",getUser().getIdCustomer());
            map.put("id",APIDeviceID.getIdDevice(getContext()));
            map.put("h",cardHash);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("saveCard()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getContext().getString(R.string.domain_connection));
            requestServer.setController(WebController.Checkout.CONTROLLER);
            requestServer.setFunction(WebController.Checkout.Functions.SAVE_CARD);
            requestServer.setObjectTransport(map);
            requestServer.request(getContext(), new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {

                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        try {
                            JSONObject obj = new JSONObject(object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                        }
                    });

                }
            });
        }
    }

    private BCard getCardComplete() {
        HotUser user = getUser();
        if (user != null) {
            card.setIdCustomer(user.getIdCustomer());
            card.setExpirationDate(controller.etExpirationDate.getText().toString().replace("/", ""));
            card.setNumber(controller.etCardNumber.getText().toString().replace(" ", ""));
            card.setHolderName(controller.etHolderName.getText().toString());
            card.setCVV(controller.etCVV.getText().toString().replace("/", ""));
        }
        return card;
    }

    private boolean validateUser(HotUser user) {

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            Toast.makeText(getContext(), "Faltou o EMAIL", Toast.LENGTH_LONG).show();
            return false;
        } else if (user.getName() == null || user.getName().isEmpty()) {
            Toast.makeText(getContext(), "Faltou o NOME", Toast.LENGTH_LONG).show();
            return false;
        } else if (user.getBirthOfDate() == null || user.getBirthOfDate().isEmpty()) {
            Toast.makeText(getContext(), "Faltou a DATA DE NASCIMENTO", Toast.LENGTH_LONG).show();
            return false;
        } else if (user.getCpf() == null && !APIDocument.isCPF(user.getCpf()) || user.getCpf().isEmpty() && !APIDocument.isCPF(user.getCpf())) {
            Toast.makeText(getContext(), "CPF Inválido", Toast.LENGTH_LONG).show();
            return false;
        } else if (user.getContact().getPhone().getFullNumber() == null ||
                user.getContact().getPhone().getFullNumber().isEmpty()) {

            return false;
        } else {
            return true;
        }
    }

    private boolean validateCard(BCard card) {

        if ((card.getId() != null || card.getCardHash() != null)) {
            return true;
        } else {
            if (card.getCVV() == null || card.getCVV().isEmpty()) {
                Toast.makeText(getContext(), "Faltou o CVV", Toast.LENGTH_LONG).show();
                return false;
            } else if (card.getExpirationDate() == null || card.getExpirationDate().isEmpty()) {
                Toast.makeText(getContext(), "Necessário Data de Validade", Toast.LENGTH_LONG).show();
                return false;

            } else if (card.getHolderName() == null || card.getHolderName().isEmpty()) {
                Toast.makeText(getContext(), "Esqueceu de Preencher o Nome", Toast.LENGTH_LONG).show();
                return false;
            } else if (card.getNumber() == null || card.getNumber().isEmpty()) {
                Toast.makeText(getContext(), "É preciso número do cartão", Toast.LENGTH_LONG).show();
                return false;
            } else if (card.getPaymentMethod() == -1) {
                Toast.makeText(getContext(), "Faltou só o meio de pagamento", Toast.LENGTH_LONG).show();
                return false;
            }

            int month = Integer.valueOf(card.getExpirationDate().substring(0, 2));
            int year = Integer.valueOf(card.getExpirationDate().substring(2, 4));

            if (month > 12 || year < month) {
                Toast.makeText(getContext(), "Data de Validade Inválida", Toast.LENGTH_LONG).show();
                return false;
            } else return true;
        }


    }

    public Controller getContProller() {
        return controller;
    }

    private final void showDialogCards(ArrayList<BCard> cards) {
        interfaceHome.loading(false);

        if (cards.size() > 0) {
            cardsSaveds = new DialogCardsSaved(cards, this);
            if (!isDetached())
                cardsSaveds.show(getChildFragmentManager(), "");
        }
    }

    private final void requestCardsSaved() {
        try {
            HotUser user = getUser();

            if (user.getIdCustomer() > 0) {
                Map<String, String> map = new HashMap<>();
                map.put("id_user", String.valueOf(user.getId()));
                map.put("id_customer", String.valueOf(user.getIdCustomer()));
                map.put("device_id", user.getDeviceID());
                map.put("email", user.getEmail());
                map.put("secret", user.getSecret());

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("cards-saved");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(WebController.Checkout.CONTROLLER);
                requestServer.setFunction(WebController.Checkout.Functions.GET_CARDS_SAVED);
                requestServer.setObjectTransport(map);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        interfaceHome.loading(loading);
                    }

                    @Override
                    public void onSucess(Object object) {

                        if (object != null) {
                            Gson gson = new Gson();
                            JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                            ArrayList<BCard> cards = new ArrayList<>();
                            BCard card = null;
                            for (JsonElement e : array) {
                                card = gson.fromJson(e, BCard.class);
                                cards.add(card);
                            }

                            if (cards.size() > 0) {
                                showDialogCards(cards);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        interfaceHome.loading(false);
                        Toast.makeText(getContext(), "ERROR : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            interfaceHome.loading(false);
            e.printStackTrace();
        }
    }

    public final void showSelectedCard(BCard cardSelected, boolean show) {

        View root = controller.getBaseCardSelected();
        TextView cardDesc = root.findViewById(R.id.tv_card_description_selected);
        ImageView icCard = root.findViewById(R.id.iv_card_brand_selected);
        Button close = root.findViewById(R.id.btn_close_card_selected);
        close.setOnClickListener(view -> {
            showSelectedCard(null, false);
        });

        if (show) {
            root.setVisibility(View.VISIBLE);
            icCard.setImageResource(CardManager.detectIcon(cardSelected.getBrand()).getIdIcon());
            cardDesc.setText(cardSelected.getBrand() + " • **** " + cardSelected.getLastDigits());
            card.setId(cardSelected.getId());
        } else {
            root.setVisibility(View.GONE);
            card.setId(null);
        }
    }

    public class Controller implements TextWatcher, View.OnClickListener {
        private final EditText etCardNumber, etExpirationDate, etCVV, etHolderName, etCpf, etDateOfBirth, etCel;
        private final Switch aSwitch;
        private final ImageView ivIcon;
        //private final Button btnAdd;
        private int idResourcerDefined = -1;
        private final BPhone phone;
        private final RelativeLayout baseCardSelected;

        public Controller(View root) {
            this.phone = new BPhone();

            this.baseCardSelected = root.findViewById(R.id.rl_base_card_selected);
            this.etCardNumber = root.findViewById(R.id.et_card_number_neo);
            this.etExpirationDate = root.findViewById(R.id.et_card_valid_date_neo);
            this.etCVV = root.findViewById(R.id.et_card_cvv_neo);
            //this.btnAdd = root.findViewById(R.id.btn_add_neo_card_neo);
            this.ivIcon = root.findViewById(R.id.iv_card_icon_neo);
            this.aSwitch = root.findViewById(R.id.st_save_card_future_payment_neo);
            this.etHolderName = root.findViewById(R.id.et_card_name_neo);
            this.etCpf = root.findViewById(R.id.et_cpf_fmt_payment);
            this.etDateOfBirth = root.findViewById(R.id.et_birth_of_date_fmt_payment);
            this.etCel = root.findViewById(R.id.et_cel_fmt_payment);
            this.etCardNumber.addTextChangedListener(this);

            setMaskInEditText(etCel, "(NN) NNNNN-NNNN");
            setMaskInEditText(etCpf, "NNN.NNN.NNN-NN");
            setMaskInEditText(etDateOfBirth, "NN-NN-NNNN");
            setMaskInEditText(etCardNumber, "NNNN NNNN NNNN NNNN");
            setMaskInEditText(etCVV, "NNN");
            setMaskInEditText(etExpirationDate, "NN/NN");

        }

        public BCard getCardData() {
            card.setIdCustomer(getUser().getIdCustomer());
            card.setCVV(etCVV.getText().toString());
            card.setExpirationDate(etExpirationDate.getText().toString().replace("/", ""));
            card.setNumber(etCardNumber.getText().toString().replace(" ", ""));
            card.setHolderName(etHolderName.getText().toString());
            return card;
        }

        public EditText getEtCpf() {
            return etCpf;
        }

        public BPhone getPhone() {
            String p = etCel.getText().toString();
            if (p != null && !p.isEmpty())
                phone.setFullNumber("+55" + p);
            return phone;
        }

        public EditText getEtDateOfBirth() {
            return etDateOfBirth;
        }

        public EditText getEtCardNumber() {
            return etCardNumber;
        }

        public EditText getEtExpirationDate() {
            return etExpirationDate;
        }

        public EditText getEtCVV() {
            return etCVV;
        }

        public EditText getEtHolderName() {
            return etHolderName;
        }

        public Switch getaSwitch() {
            return aSwitch;
        }

        public ImageView getIvIcon() {
            return ivIcon;
        }

        public int getIdResourcerDefined() {
            return idResourcerDefined;
        }

        public RelativeLayout getBaseCardSelected() {
            return baseCardSelected;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            DetectBrandListener.BrandCard brand = detectBrandCard(s.toString());
            if (brand.getIdIcon() != -1 && brand.getIdIcon() != idResourcerDefined) {
                idResourcerDefined = brand.getIdIcon();

                ivIcon.setImageResource(brand.getIdIcon());

                YoYo.with(Techniques.FlipInX)
                        .duration(500)
                        .repeat(0)
                        .playOn(ivIcon);

                getCardComplete().setBrand(brand.getBrand());
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void onClick(View view) {
            CustomApplication.hideSystemUI(getActivity());
            baseCardSelected.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        getApiRequisition().onDestroyConnections(getContext());
        super.onDetach();
    }

}
