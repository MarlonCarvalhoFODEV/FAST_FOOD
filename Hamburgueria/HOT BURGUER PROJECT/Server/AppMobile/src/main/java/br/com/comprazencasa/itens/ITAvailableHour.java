package br.com.comprazencasa.itens;

import br.com.comprazencasa.database.DaoAddress;
import br.com.comprazencasa.market.Pedido;

import java.io.Serializable;

public class ITAvailableHour implements Serializable, Comparable<String> {

    private int id,idStore, idUser, position ;
    private String hour, idOrder, dateRequisition ;
    private boolean selected ;

    public ITAvailableHour(){}

    public ITAvailableHour(int position, String hour){
        this.id = position ;
        this.hour = hour ;
    }

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public void setDateRequisition(String date) {
        dateRequisition = date ;
    }

    public String getDateRequisition() {
        return dateRequisition;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(String o) {
        if (this.hour.equals(o)) {
            return 0;
        }
        return 1 ;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ITAvailableHour){
            ITAvailableHour p = (ITAvailableHour) o;
            return this.hour.equals(p.getHour());
        } else
            return false;
    }
}