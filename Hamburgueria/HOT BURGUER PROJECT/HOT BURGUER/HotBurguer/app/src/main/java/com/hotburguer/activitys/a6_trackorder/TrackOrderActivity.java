package com.hotburguer.activitys.a6_trackorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerActivity;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.adapters.AdapterProductOrder;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.fragments.payment.Payment;
import com.hotburguer.itens.HotOrder;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotRatingProduct;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;

import static com.hotburguer.acustom.CustomApplication.getOrder;

public class TrackOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] descriptionSteps = {"Aguardando", "Preparando", "A caminho", "Chegou"};

    private StateProgressBar stateProgressOrder;
    private TextView tvFeeDelivery, tvTotalValue, tvDelivered;
    private Button btnConfirmeDelivery;
    private FrameLayout progress;
    private ListView lvProductsOrder;

    private AdapterProductOrder adpProduct;

    private HotOrder order;
    private Timer timer;

    private static final String CONFIRM_DELIVERY = "delivered";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_track_order);
        Toolbar toolbar = findViewById(R.id.tb_track_order);
        setSupportActionBar(toolbar);

        adpProduct = new AdapterProductOrder(this);

        progress = findViewById(R.id.fl_base_progress_products_order);
        stateProgressOrder = findViewById(R.id.state_progress_track_order);
        tvFeeDelivery = findViewById(R.id.tv_fee_delivery);
        tvTotalValue = findViewById(R.id.tv_total_value);
        tvDelivered = findViewById(R.id.tv_deliveryed);
        btnConfirmeDelivery = findViewById(R.id.btn_confirm_receive_order);
        lvProductsOrder = findViewById(R.id.lv_products_order);

        stateProgressOrder.setStateDescriptionData(descriptionSteps);
        stateProgressOrder.enableAnimationToCurrentState(true);

        init();
    }

    private void init() {
        order = getOrder();

        String json = CacheAPI.getObjectJson(this, HotOrder.class.getSimpleName(), true);
        if (json != null && json != "")
            order = new Gson().fromJson(json, HotOrder.class);

        boolean status = order.getStatus() != null && !order.getStatus().equals(HotOrder.ARRIVED_LOCAL_CLIENT);
        if (order.getStatuspayment() != null && order.getStatuspayment().equals(Payment.Status.PAID) ||
                order.getStatuspayment() != null && order.getStatuspayment().equals(Payment.Status.PROCESSING) ||
                order.getStatuspayment() != null && order.getStatuspayment().equals(Payment.Status.AUTHORIZED)) {

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    requestStatusOrder();
                }
            }, 30000, 30000);
        }

        adpProduct.addAll(order.values());
        lvProductsOrder.setAdapter(adpProduct);

        tvFeeDelivery.setText(+order.getFeeDelivery() > 0 ? "R$ " + order.getFeeDelivery() : "FREE");
        tvTotalValue.setText("R$ " + String.valueOf(adpProduct.getTotalValue() + order.getFeeDelivery()).replace(".", ","));

        updateStatus(order.getStatus());

        if (order.getStatuspayment() != null && !order.getStatuspayment().equals(Payment.Status.PAID) &&
                order.getStatuspayment() != null && !order.getStatuspayment().equals(Payment.Status.PROCESSING) &&
                order.getStatuspayment() != null && !order.getStatuspayment().equals(Payment.Status.AUTHORIZED))
            btnConfirmeDelivery.setEnabled(false);
        else
            btnConfirmeDelivery.setOnClickListener(this);
    }

    private void requestStatusOrder() {
        HotOrder hotOrder = new HotOrder();
        hotOrder.setId(order.getId());
        hotOrder.setIdUser(order.getIdUser());
        hotOrder.setIdDevice(order.getIdDevice());

        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("requestStatusOrder");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(getString(R.string.domain_connection));
        requestServer.setController(WebController.Order.CONTROLLER);
        requestServer.setFunction(WebController.Order.Functions.GET_STATUS_ORDER);
        requestServer.setObjectTransport(hotOrder);
        requestServer.request(this, new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    HotOrder response = new Gson().fromJson(object.toString(), HotOrder.class);

                    if (response != null) {

                        order.setStatus(response.getStatus());
                        CacheAPI.saveObject(TrackOrderActivity.this, HotOrder.class.getSimpleName(), order, true);

                        updateStatus(response.getStatus());
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private final void updateStatus(String status) {
        if (status != null) {
            switch (status) {
                case HotOrder.STATUS_WAIT: {
                    stateProgressOrder.setCurrentStateNumber(convertPositionToState(0));
                    break;
                }
                case HotOrder.STATUS_IN_PRODUCTION: {
                    stateProgressOrder.setCurrentStateNumber(convertPositionToState(1));
                    break;
                }
                case HotOrder.STATUS_DELIVERY_ON_MY_WAY: {
                    stateProgressOrder.setCurrentStateNumber(convertPositionToState(2));
                    break;
                }
                case HotOrder.ARRIVED_LOCAL_CLIENT: {
                    stateProgressOrder.setCurrentStateNumber(convertPositionToState(3));
                    break;
                }
                case HotOrder.STATUS_FINALIZED: {
                    timer.cancel();
                    tvDelivered.setVisibility(View.VISIBLE);
                    stateProgressOrder.setCurrentStateNumber(convertPositionToState(3));
                    break;
                }
            }
        }
    }

    private void confirmDelivery() {
        HotOrder order = getOrder();

        HotOrder hotOrder = new HotOrder();
        hotOrder.setId(order.getId());
        hotOrder.setIdUser(order.getIdUser());
        hotOrder.setIdDevice(order.getIdDevice());
        hotOrder.setStatus(CONFIRM_DELIVERY);

        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("confirmDelivery");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(getString(R.string.domain_connection));
        requestServer.setController(WebController.Order.CONTROLLER);
        requestServer.setFunction(WebController.Order.Functions.CONFIRM_DELIVERY);
        requestServer.setObjectTransport(hotOrder);
        requestServer.request(this, new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                showLoading(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {
                    HotOrder response = new Gson().fromJson(object.toString(), HotOrder.class);
                    CacheAPI.saveObject(TrackOrderActivity.this, HotOrder.class.getSimpleName(), new HotOrder(), true);

                    if (!HomeBurguerActivity.VISIBLE) {
                        Intent intent = new Intent(TrackOrderActivity.this, HomeBurguerActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showDialogRating() {

        RatingBar ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText etFeedBack = new EditText(this);
        etFeedBack.setHint("Escreva um comentário");

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.addView(ratingBar);
        ll.addView(etFeedBack);

        DialogInformative dialog = new DialogInformative(this);
        dialog.setTitle("Avaliação Rápida");
        dialog.setMessage("Gostaria de avaliar o " + order.getProduct(HotProduct.Categories.SNACKS, 0).getTitle());
        dialog.setPadding(16, true);
        dialog.setCustomView(ll);
        dialog.addButton("Agora Não", Color.TRANSPARENT, view -> {
            dialog.dismiss();
            confirmDelivery();
        });
        dialog.addButton(
                "Avaliar",
                Color.WHITE, getResources().getDrawable(R.drawable.rounded_button_secundary),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showLoading(true);

                        if (ratingBar.getRating() > 0) {
                            HotRatingProduct rating = new HotRatingProduct();
                            rating.setRating(ratingBar.getRating());
                            rating.setFeedBack(etFeedBack.getText().toString());
                            rating.setIdOrder(order.getId());
                            rating.setIdUser(order.getIdUser());

                            int countSnacks = order.getCountProductPerCategorie().get(HotProduct.Categories.SNACKS);
                            if (countSnacks == 1) {
                                HotProduct p = order.getProduct(HotProduct.Categories.SNACKS, 0);

                                if (p != null)
                                    rating.setIdProduct(p.getId());
                            }

                            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                            requestServer.setIdRequisition("requestEvaluation");
                            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
                            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                            requestServer.setDomain(getString(R.string.domain_connection));
                            requestServer.setController(WebController.Products.CONTROLLER);
                            requestServer.setFunction(WebController.Products.Functions.EVALUATION_PRODUCT);
                            requestServer.setObjectTransport(rating);
                            requestServer.request(TrackOrderActivity.this, new InternetRequisitionResponse() {
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

                                            boolean resultRating = response.getBoolean("result_rating");
                                            if (resultRating) {
                                                Toast.makeText(TrackOrderActivity.this, "Obrigado!!", Toast.LENGTH_SHORT).show();
                                                confirmDelivery();
                                            } else {
                                                Toast.makeText(TrackOrderActivity.this, "Erro ao Avaliar", Toast.LENGTH_SHORT).show();
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

                            dialog.dismiss();
                        } else {
                            Toast.makeText(TrackOrderActivity.this, "Defina Quantidade de Estrelas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dialog.show();
    }

    private void showLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private StateProgressBar.StateNumber convertPositionToState(int position) {
        switch (position) {
            case 0:
                return StateProgressBar.StateNumber.ONE;
            case 1:
                return StateProgressBar.StateNumber.TWO;
            case 2:
                return StateProgressBar.StateNumber.THREE;
            case 3:
                return StateProgressBar.StateNumber.FOUR;
            case 4:
                return StateProgressBar.StateNumber.FIVE;
            default:
                return StateProgressBar.StateNumber.ONE;
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        Map<String, Integer> snacks = order.getCountProductPerCategorie();
        Integer inter = snacks.get(HotProduct.Categories.SNACKS);
        int countSnacks = inter!=null?inter:0;
        if (countSnacks == 1) {
            showDialogRating();
        } else {
            DialogInformative dialog = new DialogInformative(this);
            dialog.setTitle("Confirmação");
            dialog.setMessage("Você recebeu o pedido!!");
            dialog.addButton("Não", getResources().getColor(R.color.colorAccent), Color.TRANSPARENT, onclick -> {
                dialog.dismiss();
            });
            dialog.addButton("Sim", getResources().getColor(R.color.colorAccent), Color.TRANSPARENT, onclick -> {
                dialog.dismiss();
                confirmDelivery();
            });
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
    }
}