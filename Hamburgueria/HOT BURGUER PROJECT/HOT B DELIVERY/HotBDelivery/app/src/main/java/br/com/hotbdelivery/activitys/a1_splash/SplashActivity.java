package br.com.hotbdelivery.activitys.a1_splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import apis.androidid.APIDeviceID;
import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.internet.WebJson;
import apis.onesignal.APIOneSignal;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.activitys.a2_register.RegisterActivity;
import br.com.hotbdelivery.activitys.a3_dashboard.DashboardActivity;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.fragments.dialogs.DialogInformative;
import br.com.hotbdelivery.itens.HotOrdersDelivery;
import br.com.hotbdelivery.itens.HotRunning;
import br.com.hotbdelivery.utils.CacheApp;
import br.com.hotbdelivery.utils.EndPointMapping;
import br.com.hotbdelivery.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    private HotDeliveryMan ac ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Utils.SystemUI.hideSystemUI(this);

        //CacheApp.saveRunning(this, new HotRunning());

        init();

        checkUpdate();
    }

    private void init(){
        ac = CacheApp.getAccount(this);
        CustomApplication.setAccount(ac);
    }

    private void nextActiviy(int timeMills) {
        //final boolean logged = CustomApplication.getSocialLogin().isLogged();
        //registerUser();

        //animateLogo();
        //animateText();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //animeLogoOut();
                //animateBackOut();
            }
        }, 2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //animateTextOut();
                //animateBackOut();
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean statusNull = ac != null && ac.getStatus() != null;
                boolean account = ac != null && ac.getId() > 0 && ac.getIdDevice() != null && !ac.getIdDevice().isEmpty()
                        && ac.getIdRecipient() != null && !ac.getIdRecipient().isEmpty()
                        || (ac != null && ac.getStatus() != null &&
                        ac.getStatus().equals(HotDeliveryMan.Status.WAITING));

                if(account && statusNull ){
                    try {
                        checkStatusDeliveryMan(ac);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, timeMills);
    }

    private final void checkStatusDeliveryMan(final HotDeliveryMan ac) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id",ac.getId());
        json.put("id_dev",ac.getIdDevice());
        json.put("id_recipient", ac.getIdRecipient());

        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("checkStatusDeliveryMan()");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(getString(R.string.domain_connection));
        requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
        requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.CHECK_STATUS);
        requestServer.setObjectTransport(json);
        requestServer.request(this, new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    try {
                        JSONObject obj = new JSONObject(object.toString());
                        obj = obj.getJSONObject("map");

                        String status = obj.has("")?obj.getString("result_status"):null;

                        ac.setStatus(status);

                        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);

                        int step = 0 ;
                        if(ac == null ){
                            step = 1 ;
                            intent.putExtra("step", step);
                        }
                        else if(ac.getId() > 0 && ac.getIdRecipient() == null){
                            step = 2 ;
                            intent.putExtra("step", step);
                        }
                        else if(ac.getId() > 0 && ac.getIdRecipient() != null && ac.getStatus() != null &&
                                !ac.getStatus().equals(HotDeliveryMan.Status.ACTIVE)){
                            step = 3 ;
                            intent.putExtra("step", step);
                        }
                        else if(ac.getId() > 0 && ac.getIdRecipient() != null && ac.getStatus() != null &&
                                ac.getStatus().equals(HotDeliveryMan.Status.ACTIVE)){
                            step = 4 ;
                            intent.putExtra("step", step);
                        }


                        CustomApplication.setAccount(ac);
                        CacheAPI.saveObject(SplashActivity.this, HotDeliveryMan.class.getSimpleName(), ac, true);

                        startActivity(intent);
                        finish();
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

    private final void checkUpdate(){
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("checkUpdate()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.GET);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(EndPointMapping.System.CONTROLLER);
            requestServer.setFunction(EndPointMapping.System.Functions.CHECK_UPDATE);
            requestServer.addUrlParam("app_version",version);
            //requestServer.setObjectTransport(map);
            requestServer.request(this, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {

                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        JSONObject jo = null;
                        try {
                            jo = new JSONObject(object.toString());
                            jo = jo.getJSONObject("map");

                            boolean asUpdate = jo.getBoolean("as_update");
                            if(asUpdate){
                                showDialogUpdate();
                            } else {
                                nextActiviy(3500);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            nextActiviy(3500);
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    nextActiviy(3500);
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showDialogUpdate(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 0.0000001f;
        params.bottomMargin = 16 ;
        params.leftMargin = 16 ;
        params.rightMargin = 16 ;

        DialogInformative dialog = new DialogInformative(this);
        dialog.setTitle("Nova Atualização");
        dialog.setMessage("Baixe a nova versão disponível");
        dialog.setCancelableTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setPadding(16, true);
        dialog.addButton(
                "ATUALIZAR",
                Color.WHITE,getResources().getDrawable(R.drawable.rounded_button_confirm),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
        dialog.show();
    }

}