package br.com.comprazencasa.itens;

import java.io.Serializable;

public class ITRecLocation implements Serializable {

    private String nameFile;
    private byte[] file;

    public ITRecLocation() {
    }

    public ITRecLocation(String nameFile, byte[] file) {
        this.nameFile = nameFile;
        this.file = file;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
