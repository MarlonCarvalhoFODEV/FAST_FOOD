package com.hotburguer.fragments.address;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerInterface;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogAddress;
import com.hotburguer.fragments.dialogs.DialogResuseAddress;
import com.hotburguer.itens.HotReuseAddress;
import com.hotburguer.itens.HotUser;
import com.hotburguer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.location.APILocation;
import apis.location.LAddress;

public class FMT_Address extends Fragment implements AddressInterface {

    private Context context;
    private Controller controller;
    private HomeBurguerInterface homeInteface;
    private int countRequestLocation = 0;

    private APILocation location;
    private boolean requested;

    private boolean availableNewRequestAddres = true;

    public FMT_Address(Context context) {
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homeInteface = ((HomeBurguerInterface) getActivity());

        location = new APILocation(((Activity) context));
        location.askPermission();

        requestReuseAddress();

        View view = inflater.inflate(R.layout.fmt_address, null);
        controller = new Controller(view);

        Utils.SystemUI.KeyboardUtils.addKeyboardToggleListener(getActivity(), new Utils.SystemUI.KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (!isVisible)
                    CustomApplication.hideSystemUI(getActivity());
            }
        });

        return view;
    }

    private void getActualLocation() {
        homeInteface.loading(true);
        Address address = location.getAddressActualPosition();

        if (address != null) {

            String fullAddress = address.getAddressLine(0);
            String street = fullAddress.split(",")[0];
            String number = fullAddress.split(",")[1].substring(0, fullAddress.split(",")[1].indexOf(" -")).replace(" ", "");
            String neighborhood = fullAddress.split(",")[1].substring(fullAddress.split(",")[1].indexOf("-") + 2);

            String country = fullAddress.split(",")[4].replace(" ", "");
            String cep = fullAddress.split(",")[3].replace(" ", "");
            String uf = fullAddress.split(",")[2].substring(fullAddress.split(",")[2].indexOf(" - ") + 3);

            LAddress ad = new LAddress();
            ad.setStreet(street);
            ad.setNumber(Integer.valueOf(number.replace("-", "").replaceAll("[^0-9.]","")));
            ad.setNeighborhood(neighborhood);
            ad.setContry(country);
            ad.setCEP(cep);
            ad.setLatitude(address.getLatitude());
            ad.setLongitude(address.getLongitude());
            ad.setUF(uf);

            getAddressFromCEP(ad);
        } else if (countRequestLocation < 2) {
            countRequestLocation++;
            new Handler().postDelayed(() -> {
                getActualLocation();
            }, 1500);
        } else {
            homeInteface.loading(false);
            Toast.makeText(context, "Erro em obter a localização!", Toast.LENGTH_SHORT).show();
        }

    }

    private synchronized void requestAddressFromCEP(final String cep) {
        availableNewRequestAddres = false ;
        android.location.Address address = location.getAddressActualPosition();

        try {
            JSONObject ob = new JSONObject();
            ob.put("cep", cep);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("requestAddressFromCEP()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(context.getString(R.string.domain_connection));
            requestServer.setController(WebController.Address.CONTROLLER);
            requestServer.setFunction(WebController.Address.Functions.GET_ADDRESS_FROM_CEP);
            requestServer.setObjectTransport(ob);
            requestServer.request(context, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    requested = loading;
                    homeInteface.loading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    availableNewRequestAddres = true ;
                    if (object != null && address != null) {

                        String fullAddress = address.getAddressLine(0);
                        String street = fullAddress.split(",")[0];
                        String number = fullAddress.split(",")[1].substring(0, fullAddress.split(",")[1].indexOf(" -")).replace(" ", "");
                        String neighborhood = fullAddress.split(",")[1].substring(fullAddress.split(",")[1].indexOf("-") + 2);
                        String country = fullAddress.split(",")[4].replace(" ", "");
                        String cep = fullAddress.split(",")[3].replace(" ", "");

                        LAddress ad = new Gson().fromJson(object.toString(), LAddress.class);

                        if (cep != null) {
                            if (cep.replace("-", "").equals(ad.getCEP().replace("-", ""))) {
                                ad.setNumber(Integer.valueOf(number.replace("-", "")));
                                ad.setLatitude(address.getLatitude());
                                ad.setLongitude(address.getLongitude());
                                ad.setContry(country);
                            }
                        }

                        ad.setContry(country);

                        DialogAddress dgAddress = new DialogAddress(ad, FMT_Address.this);
                        dgAddress.show(getActivity().getSupportFragmentManager(), dgAddress.DIALOG_TAG);
                    }
                }

                @Override
                public void onError(Exception e) {
                    availableNewRequestAddres = true ;
                    Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    homeInteface.loading(false);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final void requestReuseAddress() {
        if (CustomApplication.getUser() != null) {

            try {
                JSONObject ob = new JSONObject();
                ob.put("id", CustomApplication.getUser().getId());
                ob.put("id_dev", CustomApplication.getUser().getDeviceID());

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("requestReuseAddress()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(context.getString(R.string.domain_connection));
                requestServer.setController(WebController.Address.CONTROLLER);
                requestServer.setFunction(WebController.Address.Functions.GET_ADDRESS_FROM_USER);
                requestServer.setObjectTransport(ob);
                requestServer.request(context, new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        homeInteface.loading(loading);
                    }

                    @Override
                    public void onSucess(Object array) {
                        if (array != null && array.toString() != null) {

                            Gson gson = new Gson();

                            JsonArray list = gson.fromJson(array.toString(), JsonArray.class);
                            ArrayList<HotReuseAddress> addresses = new ArrayList<>();

                            for (JsonElement e : list) {
                                addresses.add(gson.fromJson(e, HotReuseAddress.class));
                            }

                            if (addresses.size() > 0) {
                                DialogResuseAddress dialog = new DialogResuseAddress(addresses, FMT_Address.this);

                                if (getActivity() != null)
                                    dialog.show(getActivity().getSupportFragmentManager(), dialog.DIALOG_TAG);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        homeInteface.loading(false);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Error Fatal", Toast.LENGTH_SHORT).show();
        }
    }

    private final void getAddressFromCEP(final LAddress addressF) {

        try {
            JSONObject ob = new JSONObject();
            ob.put("cep", addressF.getCEP());

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("getAddressFromCEP()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(context.getString(R.string.domain_connection));
            requestServer.setController(WebController.Address.CONTROLLER);
            requestServer.setFunction(WebController.Address.Functions.GET_ADDRESS_FROM_CEP);
            requestServer.setObjectTransport(ob);
            requestServer.request(context, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    homeInteface.loading(loading);
                }

                @Override
                public void onSucess(Object array) {
                    if (array != null) {

                        LAddress address = new Gson().fromJson(array.toString(), LAddress.class);
                        address.setNumber(addressF.getNumber());
                        address.setLatitude(addressF.getLatitude());
                        address.setLongitude(addressF.getLongitude());
                        //address.setUF(addressF.getUF());
                        address.setContry(addressF.getContry());
                        address.setCEP(addressF.getCEP());

                        DialogAddress dgAddress = new DialogAddress(address, FMT_Address.this);
                        dgAddress.show(getActivity().getSupportFragmentManager(), dgAddress.DIALOG_TAG);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    homeInteface.loading(false);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        location.setOnRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setAddressFull(LAddress address) {

        if (address.isConfirmed()) {
            controller.tvAddressDefined.setText(address.getfull());
            CustomApplication.getUser().setAddress(address);
            CustomApplication.getOrder().setAddress(address);
        }

        CustomApplication.hideKeyboard(getContext(), getView().getRootView());
        CustomApplication.hideSystemUI(getActivity());
    }

    public class Controller implements View.OnClickListener {

        private final TextView tvAddressDefined;
        private final EditText etCep;
        private final Button btnLocation;

        public Controller(View view) {

            tvAddressDefined = view.findViewById(R.id.tv_address_defined_address);

            etCep = view.findViewById(R.id.et_cep_fmt_address);
            Utils.FieldText.setMaskInEditText(etCep, "NNNNN-NNN");
            etCep.addTextChangedListener(new CepListener());

            btnLocation = view.findViewById(R.id.btn_location_address);
            btnLocation.setOnClickListener(this);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CustomApplication.getOrder().getAddress() != null &&
                            CustomApplication.getOrder().getAddress().getStreet() != null)
                        setAddressFull(CustomApplication.getOrder().getAddress());
                }
            }, 500);
        }

        @Override
        public void onClick(View view) {
            if (!location.isEnabledGPS()) {
                location.startActivityEnabledGPS();
            } else {
                getActualLocation();
            }
        }
    }

    public class CepListener implements TextWatcher {

        private String zipCode;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            zipCode = s.toString().replace("-", "");

            if (zipCode.length() == 8 && requested == false) {
                CustomApplication.hideKeyboard(getContext(), getView().getRootView());

                new Thread() {
                    @Override
                    public void run() {
                        if (!location.isEnabledGPS()) {
                            location.startActivityEnabledGPS();
                        } else if(availableNewRequestAddres){
                            requestAddressFromCEP(zipCode);
                        }
                    }
                }.start();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
