package com.c.hotburguerdelivery.mservices;

public interface ConfigChatListener {

    public void sendMessage(String message);

    public void clearMessages();

    public void clearMessages(int id);

}
