package br.com.hotbdelivery.fragments.register;

import java.io.Serializable;

public class PhotoCompat implements Serializable {

    private int id ;
    private String idDevice ;
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
}
