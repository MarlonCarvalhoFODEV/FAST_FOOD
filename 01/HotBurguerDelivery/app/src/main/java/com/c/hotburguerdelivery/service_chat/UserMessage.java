package com.c.hotburguerdelivery.service_chat;

import java.io.Serializable;
import java.util.ArrayList;

public class UserMessage implements Serializable {

    public static final String BUYER= "buyer" ;
    public static final String DELIVERYMAN= "deliveryman" ;
    public static final int COMMUNICATION = 3450 ;
    public static final String USER = "user" ;

    private int idSender, typeCommunication;
    private ArrayList<Integer> idDestinys ;
    private String pathPhotoUser, userName, destinyName, message, typeDestiny, date, hour;
    private String[] idOrders;

    public UserMessage(ArrayList<Integer> idUsers, String destinyName, String message){
        this.idDestinys = idUsers ;
        this.destinyName = destinyName ;
        this.message = message ;
    }

    public UserMessage(){}

    public ArrayList<Integer> getIdDestiny() {
        return idDestinys;
    }

    public void setIdDestinys(ArrayList<Integer> idDestinys) {
        this.idDestinys = idDestinys;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getTypeCommunication() {
        return typeCommunication;
    }

    public void setTypeCommunication(int typeCommunication) {
        this.typeCommunication = typeCommunication;
    }

    public String getPathPhotoUser() {
        return pathPhotoUser;
    }

    public void setPathPhotoUser(String pathPhotoUser) {
        this.pathPhotoUser = pathPhotoUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDestinyName() {
        return destinyName;
    }

    public void setDestinyName(String destinyName) {
        this.destinyName = destinyName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String[] getIdOrder() {
        return idOrders;
    }

    public void setIdOrder(String[] idOrder) {
        this.idOrders = idOrder;
    }

    public String getTypeDestiny() {
        return typeDestiny;
    }

    public void setTypeDestiny(String typeDestiny) {
        this.typeDestiny = typeDestiny;
    }
}
