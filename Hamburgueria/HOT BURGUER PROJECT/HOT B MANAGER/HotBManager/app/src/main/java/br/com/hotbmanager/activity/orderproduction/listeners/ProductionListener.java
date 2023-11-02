package br.com.hotbmanager.activity.orderproduction.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.orderproduction.OrderProductionActivity;
import br.com.hotbmanager.activity.orderproduction.ProductionInterface;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.utils.CacheApp;
import br.com.hotbmanager.utils.EndPointMapping;

public class ProductionListener implements View.OnLongClickListener {

    private Activity activity;
    private ProductionInterface prodInterface;
    private int PROGRESS = 0;

    private static final String INIT_PRODUCTION = "Iniciar Produção";
    private static final String CALL_DELIVERYMAN = "Chamar Entregador";

    private Button btn;

    public ProductionListener(Activity activity, Button btn) {
        this.activity = activity;
        this.btn = btn;
        this.prodInterface = (ProductionInterface) activity;
        this.PROGRESS = getProgressCache();
        this.btn.setText(PROGRESS == 0 ? INIT_PRODUCTION : CALL_DELIVERYMAN);
        prodInterface.enableList(PROGRESS == 0 ? false : true);
    }

    private final void startProduction(final Button btn) {
        try {

            final HotManagerAccount ac = CustomApplication.getAccount();

            JSONObject json = new JSONObject();
            json.put("id", ac.getId());
            json.put("id_dev", ac.getIdDevice());
            json.put("id_ords", getIdsAllOrders2());

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("startProduction()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(activity.getResources().getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.Orders.CONTROLLER);
            requestServer.setFunction(EndPointMapping.Orders.Functions.START_PRODUCTION);
            requestServer.setObjectTransport(json);
            requestServer.request(activity, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    prodInterface.showMessage(null, false);
                    prodInterface.showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {

                        try {
                            JSONObject response = new JSONObject(object.toString());
                            response = response.getJSONObject("map");

                            boolean result = response.getBoolean("result");
                            if (result) {
                                prodInterface.enableList(true);

                                PROGRESS++;
                                btn.setTag(PROGRESS);
                                btn.setText(CALL_DELIVERYMAN);
                                btn.setEnabled(true);

                                /*JSONObject progress = new JSONObject();
                                progress.put("progress", PROGRESS);
                                progress.put("order_id", );*/

                                updateProgressCache(PROGRESS);

                            } else {
                                btn.setEnabled(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            prodInterface.showMessage(e.getMessage(), true);
                            btn.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    prodInterface.showMessage(e.getMessage(), true);
                    btn.setEnabled(true);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            prodInterface.showMessage(e.getMessage(), true);
            btn.setEnabled(true);
        }
    }

    private final void notifyDeliverymanForDelivery() {
        try {

            final HotManagerAccount ac = CustomApplication.getAccount();

            JSONObject json = new JSONObject();
            json.put("id", ac.getId());
            json.put("id_dev", ac.getIdDevice());
            json.put("id_orders", getIdsAllOrders2());

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("notifyDeliverymanForDelivery()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(activity.getResources().getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
            requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.CALL_DELIVERYMAN);
            requestServer.setObjectTransport(json);
            requestServer.request(activity, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    prodInterface.showMessage(null, false);
                    prodInterface.showLoading(loading);
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {

                        try {
                            JSONObject response = new JSONObject(object.toString());
                            response = response.getJSONObject("map");

                            boolean result = response.getBoolean("result");
                            if (result) {
                                PROGRESS++;

                                removeProgress();

                                Toast.makeText(activity, "Entregador Notificado", Toast.LENGTH_SHORT).show();

                                activity.setResult(OrderProductionActivity.RESULT_ACTIVITY, new Intent());
                                activity.finish();
                            } else {
                                btn.setEnabled(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            prodInterface.showMessage(e.getMessage(), true);
                            btn.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    prodInterface.showMessage(e.getMessage(), true);
                    btn.setEnabled(true);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            prodInterface.showMessage(e.getMessage(), true);
            btn.setEnabled(true);
        }
    }

    private JSONArray getIdsAllOrders() {
        JSONArray ids = new JSONArray();
        for (int i = 0; i < prodInterface.getOrders().size(); i++)
            ids.put(prodInterface.getOrders().get(i).getIdOrder());

        return ids;
    }

    private int[] getIdsAllOrders2() {
        int[] ids = new int[prodInterface.getOrders().size()];
        for (int i = 0; i < prodInterface.getOrders().size(); i++)
            ids[i] = (prodInterface.getOrders().get(i).getIdOrder());

        return ids;
    }

    private final int getProgressCache() {
        Map<String, Integer> map = CacheApp.getProgressProduction(activity);
        if (map != null) {
            Integer integer = CacheApp.getProgressProduction(activity).get(Arrays.toString(getIdsAllOrders2()));
            return integer != null? integer:0;
        }else
            return 0;
    }

    private final Map<String, Integer> getProgress() {
        return CacheApp.getProgressProduction(activity);
    }

    private final void saveProgressCache(Map<String, Integer> progress) {
        CacheApp.saveProgressProduction(activity, progress);
    }

    private final void removeProgress() {
        Map<String, Integer> progressCache = getProgress();
        progressCache.remove(Arrays.toString(getIdsAllOrders2()));
        saveProgressCache(progressCache);
    }

    private final void updateProgressCache(int progress) {
        Map<String, Integer> progressCache = getProgress();
        if (progressCache != null) {
            progressCache.put(Arrays.toString(getIdsAllOrders2()), progress);
        } else {
            progressCache = new HashMap<>();
            progressCache.put(Arrays.toString(getIdsAllOrders2()), progress);
            saveProgressCache(progressCache);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        btn = (Button) view;
        btn.setEnabled(false);

        switch (PROGRESS) {
            case 0: {
                startProduction(btn);
                break;
            }
            case 1: {
                notifyDeliverymanForDelivery();
                break;
            }
        }
        return false;
    }
}
