package br.com.comprazencasa.itens;

import java.io.Serializable;

public class ITFavorite implements Serializable {

    private int id, idUser, idStore;
    private ITStore store ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public ITStore getStore() {
        return store;
    }

    public void setStore(ITStore store) {
        this.store = store;
    }
}
