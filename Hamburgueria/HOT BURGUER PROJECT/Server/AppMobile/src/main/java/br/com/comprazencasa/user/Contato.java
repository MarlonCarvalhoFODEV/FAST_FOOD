/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.user;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class Contato implements Serializable {

    private String email, cel, tel, facebook, instagram ;

    public Contato(){}

    public Contato(String email, String cel, String tel, String facebook, String instagram){
        this.email = email ;
        this.cel = cel ;
        this.tel = tel ;
        this.facebook = facebook ;
        this. instagram = instagram ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
}
