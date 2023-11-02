package com.c.hotburguerdelivery.Collaborator;

import java.io.Serializable;

public class CollaboratorAccount implements Serializable {

    private int id ;
    private String imgPath, name, lastName, cpf, date_birth, status , date;
    private CollaboratorContact contact ;
    private CollaboratorAddress address ;
    private CollaboratorFunction function ;
    private CollaboratorLogin login ;

    public CollaboratorAccount(){
        this.contact = new CollaboratorContact();
        this.address = new CollaboratorAddress();
        this.function = new CollaboratorFunction();
        this.login = new CollaboratorLogin();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        getAddress().setId(id);
        getContact().setId(id);
        getFunction().setId(id);
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(String date_birth) {
        this.date_birth = date_birth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CollaboratorContact getContact() {
        return contact;
    }

    public void setContact(CollaboratorContact contact) {
        this.contact = contact;
    }

    public CollaboratorAddress getAddress() {
        return address;
    }

    public void setAddress(CollaboratorAddress address) {
        this.address = address;
    }

    public CollaboratorFunction getFunction() {
        return function;
    }

    public void setFunction(CollaboratorFunction function) {
        this.function = function;
    }

    public CollaboratorLogin getLogin() {
        return login;
    }

    public void setLogin(CollaboratorLogin login) {
        this.login = login;
    }
}
