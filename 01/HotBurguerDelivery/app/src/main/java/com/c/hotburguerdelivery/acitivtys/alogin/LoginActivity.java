package com.c.hotburguerdelivery.acitivtys.alogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c.hotburguerdelivery.Collaborator.CollaboratorAccount;
import com.c.hotburguerdelivery.Collaborator.CollaboratorLogin;
import com.c.hotburguerdelivery.R;
import com.c.hotburguerdelivery.acitivtys.bdelivery.DeliveryManActivity;
import com.c.hotburguerdelivery.acitivtys.cbuyer.BuyerActivity;
import com.c.hotburguerdelivery.apis.cache.CacheAPI;
import com.c.hotburguerdelivery.apis.internet.InternetRequisition;
import com.c.hotburguerdelivery.apis.internet.InternetRequisitionAPI;
import com.c.hotburguerdelivery.apis.internet.InternetRequisitionResponse;
import com.c.hotburguerdelivery.application.CustomApplication;
import com.c.hotburguerdelivery.itens.HotOrder;
import com.c.hotburguerdelivery.service_chat.UserMessage;
import com.c.hotburguerdelivery.zutils.CryptoUtil;
import com.c.hotburguerdelivery.zutils.PopSaveAndRestore;
import com.c.hotburguerdelivery.zutils.progress.CustomProgress;
import com.c.hotburguerdelivery.zutils.webservice.RequestWs;
import com.c.hotburguerdelivery.zutils.webservice.WebResponseIT;
import com.c.hotburguerdelivery.zutils.webservice.WebResponses;
import com.c.hotburguerdelivery.zutils.webservice.controller.WebController;
import com.google.gson.Gson;

import static com.c.hotburguerdelivery.application.CustomApplication.getOrder;

public class LoginActivity extends AppCompatActivity {

    private CustomProgress progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = new CustomProgress(this);


    }

    private void login(){

        progress.setOutClickFinish(true);
        progress.progressUpdate("Aguarde um instante...");
        progress.show();

        CollaboratorLogin login = new CollaboratorLogin();
        login.setEmail(((EditText)findViewById(R.id.et_user_login)).getText().toString());
        login.setPassword(CryptoUtil.encryptMD5(((EditText) findViewById(R.id.et_user_password)).getText().toString().getBytes()));

       if(!login.getEmail().isEmpty() && !login.getPassword().isEmpty()){

           InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
           requestServer.setIdRequisition("request-order");
           requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
           requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
           requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
           requestServer.setDomain(getString(R.string.domain_connection));
           requestServer.setController(WebController.Collaborator.CONTROLLER);
           requestServer.setFunction(WebController.Collaborator.Functions.LOGIN);
           requestServer.setObjectTransport(login);
           requestServer.request(this, new InternetRequisitionResponse() {
               @Override
               public void onLoading(boolean loading) {}

               @Override
               public void onSucess(Object object) {
                   progress.dismiss();

                   if (object != null) {

                       Gson gson = new Gson();
                       HotOrder order = gson.fromJson(object.toString(), HotOrder.class);

                       HotOrder rootOrder = getOrder();
                       rootOrder.setId(order.getId());

                       //startAcitivtyCollaborator(null);
                   }
               }

               @Override
               public void onError(Exception e) {
                   e.printStackTrace();
                   progress.dismiss();
               }
           });
       }
       else{
           Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
       }
    }

    private void newRegister(){

    }
}
