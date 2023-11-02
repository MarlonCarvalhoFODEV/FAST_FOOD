package com.c.hotburguerdelivery.mservices;

public interface MessageManager {

    public void sendMessage(String message);

    public void clearMessageFromID(int id);

    public void clearAllMessages();
}
