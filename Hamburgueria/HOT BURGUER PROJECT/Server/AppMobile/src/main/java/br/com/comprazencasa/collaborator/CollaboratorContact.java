/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.collaborator;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class CollaboratorContact implements Serializable{
    private int id ;
    private String email, cel, telResidence ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTelResidence() {
        return telResidence;
    }

    public void setTelResidence(String telResidence) {
        this.telResidence = telResidence;
    }
}
