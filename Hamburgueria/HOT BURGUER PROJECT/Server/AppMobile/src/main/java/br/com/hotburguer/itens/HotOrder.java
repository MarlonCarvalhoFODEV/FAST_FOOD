package br.com.hotburguer.itens;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class HotOrder implements Serializable, Comparable<HotOrder>{

    private int id,idUser, idTransaction, idBuyer, idDeliveryMan, parcelas;
    private String title, description, status, statuspayment, brandCard, timeDelivery, dateCreated, idDevice, validator;
    private double feeDelivery, feeInstallment, distancia, latitude, longitude, orderPrice;
    private LAddress address ;
    private Cart cart ;
    private Set<Integer> idCoupons ;
    private boolean result ;

    public HotOrder(){
        this.address = new LAddress();
        this.cart = new Cart();
        this.idCoupons = new HashSet<>();
        this.description = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
    }

    public HotOrder(int id, int idUser){
        this.id = id ;
        this.idUser = idUser ;
        this.cart = new Cart();
        this.idCoupons = new HashSet<>();
        this.description = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
    }

    public HotOrder(int id, int idUser, String statusName){
        this.id = id ;
        this.idUser = idUser ;
        this.status = statusName ;
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

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatuspayment() {
        return statuspayment;
    }

    public void setStatuspayment(String statuspayment) {
        this.statuspayment = statuspayment;
    }

    public String getBrandCard() {
        return brandCard;
    }

    public void setBrandCard(String brandCard) {
        this.brandCard = brandCard;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
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

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public LAddress getAddress() {
        return address;
    }

    public void setAddress(LAddress address) {
        this.address = address;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getSize(){
        return cart.getSize();
    }

    public double getPurchaseValue(){
        return cart.getPurchaseValue();
    }

    public Collection<HotProduct> values(){
        return cart.values() ;
    }

    public Map<String, Integer> getCountProductFromCategorie(){
        Map<String, Integer> map = new HashMap<>();

        List<HotProduct> products = new ArrayList<>(values());
        for(HotProduct p : products){
                if(!map.containsKey(p.getCategorie()))
                    map.put(p.getCategorie(), p.getCount());
                else {
                    int count = map.get(p.getCategorie());
                    count += p.getCount();
                    map.put(p.getCategorie(), count);
                }
        }

        return map ;
    }

    public class Status{
        public static final String WAIT = "waiting";
        public static final String IN_PRODUCTION = "in_production";
        public static final String FOR_DELIVERY = "for_delivery";
        public static final String DELIVERYMAN_HOLDING = "deliveryman_holding";
        public static final String DELIVERY_ON_MY_WAY = "on_my_way";
        public static final String ARRIVED_LOCAL_CLIENT = "arrived_local_client";
        public static final String FINALIZED = "finalized";
        public static final String CANCELED = "canceled";
    }

    @Override
    public int compareTo(HotOrder o) {

        if (this.distancia < o.getDistancia()) {
            return -1;
        }
        if (this.distancia > o.getDistancia()) {
            return 1;
        }
        return 0;
    }
}

