package com.hotburguer.acustom;

import java.io.Serializable;

public class HotConfigurations implements Serializable {

    private Payment payment ;
    private DeliveryMan deliveryMan ;
    private SystemConfig systemConfig ;

    public HotConfigurations(){
        this.payment = new Payment();
        this.deliveryMan = new DeliveryMan();
        this.systemConfig = new SystemConfig();
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

    public class Payment{

    }

    public class DeliveryMan{
        private double feeDelivery ;

        public double getFeeDelivery() {
            return feeDelivery;
        }
    }

    public class SystemConfig{
        private boolean feeDeliveryFree ;

        public boolean getFeeDeliveryFree() {
            return feeDeliveryFree;
        }
    }
}
