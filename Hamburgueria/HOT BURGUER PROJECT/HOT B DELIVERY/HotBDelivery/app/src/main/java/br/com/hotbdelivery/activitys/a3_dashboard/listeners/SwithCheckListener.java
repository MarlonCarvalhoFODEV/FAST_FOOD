package br.com.hotbdelivery.activitys.a3_dashboard.listeners;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.utils.EndPointMapping;

public class SwithCheckListener implements CompoundButton.OnCheckedChangeListener {

    private Context context ;

    public SwithCheckListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            compoundButton.setText("Online");
        } else {
            compoundButton.setText("Offline");
        }

        CustomApplication.getAccount().setStatusOperation(b);
        CacheAPI.saveObject(context, HotDeliveryMan.class.getSimpleName(), CustomApplication.getAccount(),true);

        updateStatusOperationDeliveryman(context, b);
    }

    private final void updateStatusOperationDeliveryman(Context context, boolean status) {
        HotDeliveryMan account = CustomApplication.getAccount();

        try {
            JSONObject json = new JSONObject();
            json.put("id", account.getId());
            json.put("id_dev", account.getIdDevice());
            json.put("id_recipient", account.getIdRecipient());
            json.put("activity_status", status);

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("updateStatusOperationDeliveryman()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(context.getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
            requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.UPDATE_STATUS_OPERATION);
            requestServer.setObjectTransport(json);
            requestServer.request(context, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        Gson gson = new Gson();

                        try {
                            JSONObject response = new JSONObject(object.toString());
                            response = response.getJSONObject("map");

                            boolean isOnline = response.getBoolean("result");

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
