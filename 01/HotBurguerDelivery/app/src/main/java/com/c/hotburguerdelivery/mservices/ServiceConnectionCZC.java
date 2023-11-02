package com.c.hotburguerdelivery.mservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import com.comprazencasa.colaborador.acitivtys.cbuyer.BuyerActivity;
import com.comprazencasa.colaborador.application.CustomApplication;
import com.comprazencasa.colaborador.service_chat.CZCNotification;
import com.comprazencasa.colaborador.service_chat.UserMessage;

import org.java_websocket.client.WebSocketClient;

import java.util.HashMap;
import java.util.Map;

public class ServiceConnectionCZC extends Service implements MessageManager, ReceptMessageInterface {

    private ResultReceiver resultReceiver ;
    private InfoController controller = new InfoController();

    private ReceptMessage receptMessage ;

    @Override
    public void getMessage(UserMessage msg) {
        Bundle bundle = new Bundle();

        if(msg.getTypeCommunication() == UserMessage.COMMUNICATION){

            if(((CustomApplication)getApplicationContext()).getAccount(getApplicationContext()).getFunction().getCompanyFunction()
                    .equalsIgnoreCase("Buyer")){
                msg.setMessage("NOVO PEDIDO AGUARDANDO...");
                CZCNotification.notificationOne(this, msg);

            }else {
                msg.setMessage("NOVA ENTREGA AGUARDANDO SAÍDA");
                CZCNotification.notificationDeliveryman(this, msg);
            }

            if (BuyerActivity.statusActivity == true) {
                bundle.putSerializable("msg", msg);
                resultReceiver.send(1, bundle);
                bundle.clear();
            }
        }
    }

    @Override
    public void closedConnection(int code, String reason, boolean remote) {

    }

    @Override
    public void error(Exception exception) {

    }

    @Override
    public void connect(WebSocketClient webSocketClient) {

    }

    public class InfoController extends Binder {

        private Map<Integer, String> messages = new HashMap<>();

        public MessageManager getMessageManager(){
            return ServiceConnectionCZC.this;
        }

        public Map<Integer, String> getMessages() {
            return messages;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return controller ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        resultReceiver = intent.getParcelableExtra("result_receiver");

        if(receptMessage == null ){
            receptMessage = new ReceptMessage(this,this);
            receptMessage.start();
        }
        else{
            receptMessage.setStartID(startId);
            receptMessage.setConnected(false);
            receptMessage.setRequested(false);
            receptMessage.setSendedID(false);
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        receptMessage.setStarted(false);
        stopSelf(receptMessage.getStartID());
        receptMessage = null ;
    }

    @Override
    public void sendMessage(String message) {
        if(receptMessage.isConnected() == true && receptMessage.getWebSocket() != null ){
            receptMessage.sendMessage(message);
        }
    }

    @Override
    public void clearMessageFromID(int id) {
        if(controller.getMessages().containsKey(id)){
            controller.getMessages().remove(id);
        }
    }

    @Override
    public void clearAllMessages() {
        controller.getMessages().clear();
    }

}
