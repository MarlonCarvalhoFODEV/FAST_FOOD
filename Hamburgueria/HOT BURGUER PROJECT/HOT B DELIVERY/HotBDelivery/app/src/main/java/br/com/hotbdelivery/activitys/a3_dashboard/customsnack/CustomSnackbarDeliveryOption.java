package br.com.hotbdelivery.activitys.a3_dashboard.customsnack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.location.LAddress;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.activitys.a4_running.MapActivity;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.fragments.register.FMT_CompleteRegister;
import br.com.hotbdelivery.itens.HotDelivery;
import br.com.hotbdelivery.itens.HotOrdersDelivery;
import br.com.hotbdelivery.itens.HotRunning;
import br.com.hotbdelivery.utils.CacheApp;
import br.com.hotbdelivery.utils.EndPointMapping;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;

//import br.com.hotbdelivery.activitys.a4_running.RunningActivity;

public class CustomSnackbarDeliveryOption {

    private Context context;
    private Snackbar snackbar;
    private Snackbar.SnackbarLayout snackbarLayout;
    private JSONObject infOrders;
    private Controller controller;

    private boolean acceptedRunning = false;

    public static final int REQUEST_CODE_ACCEPT_RUNNING = 572;

    public CustomSnackbarDeliveryOption(Context context, View viewRoot, JSONObject data) {
        this.context = context;
        this.snackbar = Snackbar.make(viewRoot, CustomSnackbarDeliveryOption.class.getSimpleName(), Snackbar.LENGTH_INDEFINITE);
        this.snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (acceptedRunning == false) {
                    try {
                        rejectRunning();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        this.infOrders = data;
        View customSanckbar = LayoutInflater.from(context).inflate(R.layout.custom_snackbar_delivery_option, null);

        controller = new Controller(customSanckbar);

        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(customSanckbar);

        for (int i = 0; i < snackbarLayout.getChildCount(); i++) {
            View parent = snackbarLayout.getChildAt(i);
            if (parent instanceof LinearLayout) {
                parent.setRotation(180);
                break;
            }
        }
    }

    private final void rejectRunning() throws JSONException {

        HotDeliveryMan account = CustomApplication.getAccount();

        JSONObject ua = new JSONObject();
        ua.put("id", account.getId());
        ua.put("id_dev", account.getIdDevice());
        ua.put("id_recipient", account.getIdRecipient());
        ua.put("data", infOrders);

        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("rejectRunning()");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(context.getString(R.string.domain_connection));
        requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
        requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.REJECT_RUNNIG);
        requestServer.setObjectTransport(ua);
        requestServer.request(context, new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {

            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {
                    JSONObject response = null;
                    try {
                        response = new JSONObject(object.toString());
                        response = response.getJSONObject("map");

                        boolean result = response.getBoolean("result");
                        if (result) {

                            Toast.makeText(context, "CORRIDA REJEITADA!!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public class Controller implements View.OnLongClickListener {

        private TextView tvAmount, tvCountDelivery;
        private StateProgressBar stateProgressBar;
        private CircularProgressButton btnAccept;
        private String[] descriptionData = {"0,0km\nPonto 1", "Coleta\npedidos", "Conclusão\nde Entrega", "Percurso\ntotal"};

        public Controller(View view) {
            try {
                tvAmount = view.findViewById(R.id.tv_amount_delivery);
                tvCountDelivery = view.findViewById(R.id.tv_count_delivery);

                tvAmount.setText(infOrders.has("amount") ? "R$ " + infOrders.getString("amount") : "R$ 0,00");
                tvCountDelivery.setText(infOrders.has("count_delivery") ? infOrders.getString("count_delivery") + " Entregas" : "0 Entregas");

                stateProgressBar = view.findViewById(R.id.state_progress);
                stateProgressBar.setStateDescriptionData(descriptionData);

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_check_);

                btnAccept = view.findViewById(R.id.btn_accept);
                btnAccept.resetProgress();
                btnAccept.doneLoadingAnimation(context.getResources().getColor(R.color.success), bitmap);
                btnAccept.revertAnimation(new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        Toast.makeText(context, "Insss", Toast.LENGTH_SHORT).show();
                    }
                });
                btnAccept.setOnLongClickListener(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private final void acceptRunning() {
            acceptedRunning = true;
            HotDeliveryMan account = CustomApplication.getAccount();
            try {
                JSONObject json = new JSONObject();
                json.put("id", account.getId());
                json.put("id_dev", account.getIdDevice());
                json.put("id_orders", infOrders.getJSONArray("id_orders"));

                loading();

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("acceptRunning()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(context.getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.ACCEPT_RUNNIG);
                requestServer.setObjectTransport(json);
                requestServer.request(context, new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {
                            try {
                                sucess();

                                Gson gson = new Gson();

                                ArrayList<HotDelivery> deliverys = new ArrayList<>();
                                try {
                                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                                    HotDelivery delivery;
                                    for (JsonElement e : array) {
                                        delivery = gson.fromJson(e, HotDelivery.class);
                                        deliverys.add(delivery);
                                    }

                                    if (deliverys.size() > 0) {
                                        startActivityMap(deliverys);
                                    }
                                }catch(JsonSyntaxException e){
                                    failed();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                failed();
                            }
                        } else {
                            failed();

                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Sem resposta do Servidor!!", Toast.LENGTH_SHORT).show();
                        failed();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                //failed();
            }
        }

        private void loading() {
            btnAccept.startAnimation();
            btnAccept.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }

        private void failed() {
            //btnAccept.setText("FALHA, TENTE NOVAMENTE");
            btnAccept.setBackgroundColor(context.getResources().getColor(R.color.failed));
            btnAccept.revertAnimation();
            acceptedRunning = false;
        }

        private void sucess() {
            //btnAccept.setText("ACEITA COM SUCESSO");
            btnAccept.setBackgroundColor(context.getResources().getColor(R.color.success));
            btnAccept.revertAnimation();
            acceptedRunning = true;
        }

        @Override
        public boolean onLongClick(View view) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    acceptRunning();
                }
            }, 1500);
            return false;
        }
    }

    public void showSnackbar() {
        snackbar.show();

        /*YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .repeat(0)
                .playOn(snackbar.getView());*/
    }

    public void hideSnackbar() {
        YoYo.with(Techniques.SlideOutUp)
                .duration(500)
                .repeat(0)
                .playOn(snackbar.getView());
        snackbar.dismiss();
    }

    public boolean isShowing() {
        return snackbar.isShown();
    }

    private final void startActivityRunning(JsonArray dataResponse) {

       /* Intent intent = new Intent(context, RunningActivity.class);
        intent.putExtra("data_running", dataResponse.toString());

        if(context != null)
            ((Activity)context).startActivityForResult(intent, REQUEST_CODE_ACCEPT_RUNNING);*/
    }
/*

    private final void startActivityMap(JsonArray dataResponse) throws JSONException {

        HotOrdersDelivery delivery = new HotOrdersDelivery();
        delivery.setCountDelivery(infOrders.getInt("count_delivery"));
        delivery.setAmount(Double.valueOf(infOrders.getString("amount").replace(",", ".")));
        delivery.setAddress(convertToAddress(dataResponse));

        CacheApp.saveRunning(context, delivery);

        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("data_running", delivery);

        if (context != null)
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_ACCEPT_RUNNING);
    }
*/

    private final void startActivityMap(ArrayList<HotDelivery> deliverys) throws JSONException {

        HotRunning running = new HotRunning();
        running.setCountDelivery(infOrders.getInt("count_delivery"));
        running.setTotalValue(Double.valueOf(infOrders.getString("amount").replace(",", ".")));
        running.setDeliverys(deliverys);

        CacheApp.saveRunning(context, running);

        Intent intent = new Intent(context, MapActivity.class);

        hideSnackbar();
        if (context != null)
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_ACCEPT_RUNNING);
    }

    private final ArrayList<LAddress> convertToAddress(JsonArray array) {

        ArrayList<LAddress> addresses = new ArrayList<>();

        LAddress address;
        Gson gson = new Gson();
        for (JsonElement e : array) {
            address = gson.fromJson(e, LAddress.class);
            addresses.add(address);
        }

        return addresses;
    }

}