package br.com.comprazencasa.market;

import br.com.comprazencasa.database.DaoAddress;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.user.Endereco;
import br.com.comprazencasa.util.PaymentResult;

import java.io.Serializable;
import java.util.*;

public class Pedido implements Serializable, Comparable<Pedido> {

    private int id,idUser,idStore, idBuyer, idDeliveryMan, parcelas;
    private String title, descricao, CPF, data, dataMarcada, horaMarcada, status, paymentLocal, paymentMode, sendMethod;
    private double feeDelivery, feeInstallment, distancia, latitude, longitude, orderPrice;
    private Endereco endereco ;
    private PaymentResult paymentResult ;
    private Carrinho carrinho ;
    private Set<Integer> idCoupons ;
    private boolean result ;

    public Pedido(){
        this.paymentResult = new PaymentResult();
        this.endereco = new Endereco();
        this.carrinho = new Carrinho();
        this.idCoupons = new HashSet<>();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PaymentResult getPaymentResult() {
        return paymentResult;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataMarcada() {
        return dataMarcada;
    }

    public void setDataMarcada(String dataMarcada) {
        this.dataMarcada = dataMarcada;
    }

    public String getHoraMarcada() {
        return horaMarcada;
    }

    public void setHoraMarcada(String horaMarcada) {
        this.horaMarcada = horaMarcada;
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

    public String getSendMethod() {
        return sendMethod;
    }

    public void setSendMethod(String sendMethod) {
        this.sendMethod = sendMethod;
    }

    public double getFeeDelivery() {
        return feeDelivery;
    }

    public void setFeeDelivery(double feeDelivery) {
        this.feeDelivery = feeDelivery;
    }

    public double getFeeInstallment() {
        return feeInstallment;
    }

    public void setFeeInstallment(double feeInstallment) {
        this.feeInstallment = feeInstallment;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Set<Integer> getIdCoupons() {
        return idCoupons;
    }

    public void setIdCoupons(Set<Integer> idCoupons) {
        this.idCoupons = idCoupons;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Collection<ITProduct> values(){
        return carrinho.values() ;
    }

    public int getTamanhoDoCarrinho(){
        return carrinho.getTamanhoDoCarrinho();
    }

    public void setOrderPrice(double valor){
        this.orderPrice =valor;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public double getValorCompra(){
        return carrinho.getValorCompra();
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    @Override
    public int compareTo(Pedido o) {

        if (this.distancia < o.getDistancia()) {
            return -1;
       }
       if (this.distancia > o.getDistancia()) {
            return 1;
       }
       return 0;
    }
}
