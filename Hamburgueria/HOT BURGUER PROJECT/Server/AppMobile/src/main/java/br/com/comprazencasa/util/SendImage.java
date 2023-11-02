/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class SendImage implements Serializable{
    
    private int idUser, idProduct ;
    private String imageName, otherImage, subPath;
    private boolean update ;
    private byte[] imageByte ;

    public SendImage(){}

    public SendImage(int id, String title, byte[] img){
        this.idProduct = id ;
        this.imageName = title ;
        this.imageByte = img ;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getOtherImage() {
        return otherImage;
    }

    public void setOtherImage(String otherImage) {
        this.otherImage = otherImage;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
