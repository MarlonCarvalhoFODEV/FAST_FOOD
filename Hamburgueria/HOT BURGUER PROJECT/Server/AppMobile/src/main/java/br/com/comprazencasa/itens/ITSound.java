package br.com.comprazencasa.itens;

import java.io.Serializable;

public class ITSound implements Serializable {

    private int id;
    private String nameSound, dateTime ;
    private byte[] recording;
    private String url ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSound() {
        return nameSound;
    }

    public void setNameSound(String nameSound) {
        this.nameSound = nameSound;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public byte[] getRecording() {
        return recording;
    }

    public void setRecording(byte[] recording) {
        this.recording = recording;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
