package com.c.hotburguerdelivery.Collaborator;

import java.io.Serializable;

public class CollaboratorLogin implements Serializable {

    private int id ;
    private String email, password ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
