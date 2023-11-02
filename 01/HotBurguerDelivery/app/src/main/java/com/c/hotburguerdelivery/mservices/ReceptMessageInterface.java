package com.c.hotburguerdelivery.mservices;

import com.comprazencasa.colaborador.service_chat.UserMessage;

import org.java_websocket.client.WebSocketClient;

public interface ReceptMessageInterface {
    public void getMessage(UserMessage msg);
    public void closedConnection(int code, String reason, boolean remote);
    public void error(Exception exception);
    public void connect(WebSocketClient webSocketClient);
}