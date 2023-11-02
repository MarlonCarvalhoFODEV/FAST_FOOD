package com.c.hotburguerdelivery.Collaborator;

import java.io.Serializable;

public class CollaboratorContact implements Serializable {
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
