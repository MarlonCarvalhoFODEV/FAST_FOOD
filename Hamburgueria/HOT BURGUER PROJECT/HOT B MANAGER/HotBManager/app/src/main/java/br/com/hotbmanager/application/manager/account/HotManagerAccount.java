package br.com.hotbmanager.application.manager.account;

import java.io.Serializable;

public class HotManagerAccount implements Serializable {

    private int id ;
    private String idDevice, hash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
