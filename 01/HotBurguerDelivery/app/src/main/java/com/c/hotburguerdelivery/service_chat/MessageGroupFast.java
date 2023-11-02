package com.c.hotburguerdelivery.service_chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageGroupFast extends HashMap<Integer,UserMessage> {
    private int idProduct;
    private String productName, pathPhoto;
    private List<Integer> posId ;

    public MessageGroupFast(){
        this.posId = new ArrayList<>();
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public List<Integer> getPosId() {
        return posId;
    }

    public void setPosId(List<Integer> posId) {
        this.posId = posId;
    }

    public int countUsersInterest(){
        return size();
    }

    public  void addUserInterest(UserMessage msg){
        put(msg.getIdSender(),msg);
        posId.add(msg.getIdSender());
    }

    public UserMessage getUserInterest(int position){
        return get(posId.get(position));
    }

}
