package br.com.megasena.itens;

import java.util.HashMap;
import java.util.Map;

public class ITUser {

    private int id ;
    private String name ;
    private String email ;
    private String idDevice ;
    private Map<Integer, ITService> availableServices ;

    public ITUser() {
        this.availableServices = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public Map<Integer, ITService> getAvailableServices() {
        return availableServices;
    }
}
