package br.com.hotburguer.itens;

import java.io.Serializable;

public class PhotoCompat implements Serializable {

    private int id ;
    private String idDevice, ipAccess;
    private byte[] profile, frontPhoto, versePhoto ;

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

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public byte[] getFrontPhoto() {
        return frontPhoto;
    }

    public void setFrontPhoto(byte[] frontPhoto) {
        this.frontPhoto = frontPhoto;
    }

    public byte[] getVersePhoto() {
        return versePhoto;
    }

    public void setVersePhoto(byte[] versePhoto) {
        this.versePhoto = versePhoto;
    }

    public String getIpAccess() {
        return ipAccess;
    }

    public void setIpAccess(String ipAccess) {
        this.ipAccess = ipAccess;
    }
}
