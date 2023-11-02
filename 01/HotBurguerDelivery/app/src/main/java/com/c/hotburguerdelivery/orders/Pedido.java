package com.c.hotburguerdelivery.orders;

import java.io.Serializable;

public class Pedido implements Serializable {
    public static final int STATUS_RECUSADO = -1 ;
    public static final int STATUS_AGUARDANDO_APROVACAO = 0 ;
    public static final int STATUS_APROVADO = 1 ;
    public static final int STATUS_EM_PRODUCAO = 2 ;
    public static final int STATUS_A_CAMINHO = 3 ;
    public static final int STATUS_FINALIZADO = 4 ;
    public static final int STATUS_CANCELADO = 5 ;

    private int idUser, idBuyer, idDeliveryMan, status, parcelas;
    private String id, title, descricao, CPF, data, statusName, paymentLocal, paymentMode;
    private double distancia ;

    public Pedido(){}

    public Pedido(String id, int idUser){
        this.id = id ;
        this.idUser = idUser ;
    }

    public Pedido(String id, int idUser, String statusName){
        this.id = id ;
        this.idUser = idUser ;
        this.statusName = statusName ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public int getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(int idBuyer) {
        this.idBuyer = idBuyer;
    }

    public int getIdDeliveryMan() {
        return idDeliveryMan;
    }

    public void setIdDeliveryMan(int idDeliveryMan) {
        this.idDeliveryMan = idDeliveryMan;
    }

    public String getPaymentLocal() {
        return paymentLocal;
    }

    public void setPaymentLocal(String paymentLocal) {
        this.paymentLocal = paymentLocal;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    private String decodeStatus(int status){
        switch (status){
            case STATUS_RECUSADO: return "Recusado";
            case STATUS_AGUARDANDO_APROVACAO: return "Pendende";
            case STATUS_APROVADO: return "Aprovado";
            case STATUS_EM_PRODUCAO: return "Em produção";
            case STATUS_A_CAMINHO: return "A caminho";
            case STATUS_FINALIZADO: return "Finalizado";
        }
        return null ;
    }
}
