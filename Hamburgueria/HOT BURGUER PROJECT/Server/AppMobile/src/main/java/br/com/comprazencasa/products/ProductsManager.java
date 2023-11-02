/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.products;

import br.com.comprazencasa.database.DaoAdmin;
import br.com.comprazencasa.itens.ITProduct;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class ProductsManager {
    private final DaoAdmin daoAdmin ;
    private final LoadingProducts loading ;
    private String market ;
    private int status ;
    
    public ProductsManager(String baseImg){
        this.daoAdmin = new DaoAdmin();
        this.loading = new LoadingProducts(baseImg);
    }
    
    public synchronized int loadingAndSaveProductsCarrefour(String url, String categorie){
        new Thread(){
            @Override
            public void run() {
                setMarket(LoadingProducts.AllMarketLinks.Carrefour.CRF);
                loading.setGetTitle(LoadingProducts.AllMarketLinks.Carrefour.DEC_TITLE);
                loading.setSrc(LoadingProducts.AllMarketLinks.Carrefour.DEC_SRC);
                loading.setDataSrc(LoadingProducts.AllMarketLinks.Carrefour.DEC_DATA_SRC);
                loading.setImg(LoadingProducts.AllMarketLinks.Carrefour.DEC_IMG);
                loading.setInPrice(LoadingProducts.AllMarketLinks.Guanabara.DEC_IN_PRICE);
                loading.setPerPrice(LoadingProducts.AllMarketLinks.Guanabara.DEC_PER_PRICE);
                loading.setInstallment(LoadingProducts.AllMarketLinks.Guanabara.DEC_INSTALLMENT);
                loading.setProductList(LoadingProducts.AllMarketLinks.Guanabara.DEC_PRODUCT_LIST);

                LinkedList<ITProduct> sup ;

                for(int i=1; i < 400; i++){

                    sup = loading.getBlockProduct(url,market, categorie, i);

                    if(sup == null) break;

                    //daoAdmin.getDaoCzc().insertCollectionProducts(sup);
                    //sleeping(100);
                }
                status = 1 ;
            }
        }.start();
      return 0 ;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
    
    public void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
