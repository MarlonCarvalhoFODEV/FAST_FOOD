package com.c.hotburguerdelivery.mservices;

import android.content.Context;

import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.PopSaveAndRestore;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ReceptMessage extends Thread {

    private Context context ;
    private WebSocketClient webSocketClient ;

    private boolean connected, requested, sendedID, started = true ;
    private int startID;
    private final int FIRST_CONNECTION = 1000 ;
    private ReceptMessageInterface recept ;

    public ReceptMessage(Context context, ReceptMessageInterface recept){
        this.context = context ;
        this.recept = recept ;
    }

    @Override
    public void run() {
        while(started){
            if(connected == false && requested == false){
                startConnectionServer();
                //Log.i("CZC_CLIENT","REQUEST CONNECTION");
            }else if(requested == true && connected == false){
                //Log.i("CZC_CLIENT","WAIT CONNECTION");
            }
            else if(connected == true){
                //Log.i("CZC_CLIENT","CONNECTED ");
                sendMyID();

            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        started = false ;
    }

    private synchronized void startConnectionServer(){

        webSocketClient = new WebSocketClient(URI.create(context.getString(R.string.connectionServer))) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                connected = true ;
                requested = true ;

            }
            final Gson gson = new Gson();
            @Override
            public void onMessage(String json) {
                UserMessage msg = gson.fromJson(json, UserMessage.class);
                recept.getMessage(msg);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                connected = false ;
                requested = false ;
                sendedID = false ;
                recept.closedConnection(code, reason, remote);
            }

            @Override
            public void onError(Exception ex) {
                connected = false ;
                requested = false ;
                sendedID = false ;
                recept.error(ex);
            }
        };
        webSocketClient.connect();
        requested = true;
        sendedID = false ;
        //recept.connect(webSocketClient);
    }

    private synchronized void sendMyID(){

        if(sendedID == false && connected == true){

            int id = PopSaveAndRestore.getId(context.getApplicationContext(),"com.firtinnovationti.czccollaborator.acitivtys.alogin");

            if(id > 0){
                UserMessage p = new UserMessage();
                p.setTypeCommunication(FIRST_CONNECTION);
                p.setTypeDestiny(UserMessage.BUYER);
                p.setIdSender(id);
                delay(3000);

                webSocketClient.send(new Gson().toJson(p));
                sendedID = true ;
            }
        }
    }

    private void delay(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public void setSendedID(boolean sendedID) {
        this.sendedID = sendedID;
    }

    public void setStartID(int startID) {
        this.startID = startID;
    }

    public int getStartID() {
        return startID;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    public WebSocketClient getWebSocket() {
        return webSocketClient;
    }

    public void sendMessage(String message) {
        webSocketClient.send(message);
    }
}
