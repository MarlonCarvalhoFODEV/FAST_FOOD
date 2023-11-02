package br.com.hotburguer.itens;

import java.io.Serializable;
import java.util.ArrayList;

public class HotConfigurations implements Serializable {

    private Payment payment ;
    private DeliveryMan deliveryMan ;
    private SystemConfig systemConfig ;
    private CurrentLocation currentLocation ;

    public HotConfigurations(){
        this.payment = new Payment();
        this.deliveryMan = new DeliveryMan();
        this.systemConfig = new SystemConfig();
        this.currentLocation = new CurrentLocation();
    }

    public Payment getPayment() {
        return payment;
    }

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public class Payment{

    }

    public class CurrentLocation{

        private LAddress address ;
        private ArrayList<HotFranchises> franchises ;

        public LAddress getAddress() {
            return address;
        }

        public void setAddress(LAddress address) {
            this.address = address;
        }

        public ArrayList<HotFranchises> getFranchises() {
            return franchises;
        }

        public void setFranchises(ArrayList<HotFranchises> franchises) {
            this.franchises = franchises;
        }
    }

    public class DeliveryMan{
        private double feeDelivery ;

        public double getFeeDelivery() {
            return feeDelivery;
        }

        public void setFeeDelivery(double feeDelivery) {
            this.feeDelivery = feeDelivery;
        }
    }

    public class SystemConfig{

        private boolean feeDeliveryFree ;

        public boolean getFeeDeliveryFree() {
            return feeDeliveryFree;
        }

        public void setFeeDeliveryFree(boolean feeDeliveryFree) {
            this.feeDeliveryFree = feeDeliveryFree;
        }

    }


}
