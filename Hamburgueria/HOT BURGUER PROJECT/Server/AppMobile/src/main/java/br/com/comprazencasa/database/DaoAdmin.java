/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import br.com.comprazencasa.util.ServerConfig;

/**
 *
 * @author Marlon
 */
public class DaoAdmin {

    private static ServerConfig config ;

    private final DaoPayment daoPayment ;
    private final DaoProducts daoProducts;
    private final DaoAddress daoAddress;
    private final DaoCoupons daoCoupons ;
    private final DaoImage daoImage ;
    private final DaoServerConfig daoServerConfig ;

    public DaoAdmin(){
        this.daoPayment = new DaoPayment();
        this.daoProducts = new DaoProducts();
        this.daoAddress = new DaoAddress();
        this.daoCoupons = new DaoCoupons();
        this.daoImage = new DaoImage();
        this.daoServerConfig = new DaoServerConfig();

        this.config = daoServerConfig.getServerConfig();
    }

    public static void setDaoServerConfig(ServerConfig neoConfig ) {
        config=neoConfig;
    }

    public DaoPayment getDaoPayment() {
        return daoPayment;
    }
    
    public DaoProducts getDaoProducts() {
        return daoProducts;
    }

    public DaoAddress getDaoAddress() {
        return daoAddress;
    }

    public DaoCoupons getDaoCoupons() {
        return daoCoupons;
    }

    public DaoImage getDaoImage(){
        return daoImage;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public void setConfig(ServerConfig config) {
        this.config = config;
    }
}
