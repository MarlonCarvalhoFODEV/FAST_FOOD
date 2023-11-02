/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.products;

import br.com.comprazencasa.database.ConnectionDB_APP;
import br.com.comprazencasa.database.DaoAdmin;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.util.ImagemUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class LoadingProducts{
        
    private String baseImg ;
    private String productList, getTitle, inPrice, perPrice, img, src, dataSrc, installment, market;

    public LoadingProducts(String baseImage) {
        this.baseImg = baseImage ;
    }

    public class AllMarketLinks{
        public class Carrefour{
            public static final String CRF ="crf_products" ;
            public static final String DEC_PRODUCT_LIST = "product" ;
            public static final String DEC_TITLE = "prd-name" ;
            public static final String DEC_IN_PRICE = "prd-price-old" ;
            public static final String DEC_PER_PRICE = "prd-price-new" ;
            public static final String DEC_SRC = "src" ;
            public static final String DEC_IMG = "img" ;
            public static final String DEC_DATA_SRC = "data-src" ;
            public static final String DEC_INSTALLMENT = "prd-price-installments" ;
            

            public class BasicFood {
                public static final String RICE_GRAINS_URL = "https://www.carrefour.com.br/arroz-e-graos?page=";
                public static final String CANDY_URL = "https://www.carrefour.com.br/doces-sobremesas-e-confeitaria?page=";
                public static final String BABY_FOODS_URL = "https://www.carrefour.com.br/alimentos-infantis?page=";
                public static final String PASTA_SAUCES_URL = "https://www.carrefour.com.br/massas-e-molhos?page=";
                public static final String PRESERVES_URL = "https://www.carrefour.com.br/conservas-e-enlatados?page=";
                public static final String OILS_URL = "https://www.carrefour.com.br/oleos-azeites-e-vinagres?page=";
                public static final String SEASONING_URL = "https://www.carrefour.com.br/temperos-e-condimentos?page=";
            }

            public class CleaningPerfumery{
                public static final String CLEANING_PRODUCT_URL = "https://www.carrefour.com.br/produtos-de-limpeza?page=";
                public static final String ORAL_HYGIENE_URL = "https://www.carrefour.com.br/higiene-bucal?page=";
                public static final String TOILET_PAPER_URL = "https://www.carrefour.com.br/papel-higienico?page=";
                public static final String DISCOVERABLE_URL = "https://www.carrefour.com.br/descartaveis?page=";
                
            }

            public class Drinks {
                public static final String WATERS_URL = "https://www.carrefour.com.br/agua-e-agua-de-coco?page=";
                public static final String JUICES_URL = "https://www.carrefour.com.br/sucos-e-refrescos?page=";
                public static final String BEERS_URL = "https://www.carrefour.com.br/cervejas?page=";
                public static final String WINE_FROWNED_URL = "https://www.carrefour.com.br/vinhos-e-espumantes?page=";
                public static final String WHISKY_URL = "https://www.carrefour.com.br/whisky?page=";
                
            }

            public class FreshChilled{
                public static final String FRUITS_URL = "https://www.carrefour.com.br/frutas?page=";
                public static final String EGGS_URL = "https://www.carrefour.com.br/ovos?page=";
                public static final String MEAT_POULTRY_FISH_URL = "https://www.carrefour.com.br/carnes-aves-e-peixes?page=";
                public static final String VEGETABLES_URL = "https://www.carrefour.com.br/verduras-e-legumes?page=";
                public static final String CHEESE_COLDCUTS_URL = "https://www.carrefour.com.br/queijos-e-frios?page=";
                public static final String FROZEN_URL = "https://www.carrefour.com.br/congelados?page=";
                public static final String READY_DISHES_URL = "https://www.carrefour.com.br/pratos-prontos?page=";
                
            }

            public class Healthy{
                public static final String INTEGRALS_URL = "https://www.carrefour.com.br/alimentos-integrais?page=";
                public static final String ORGANIC_URL = "https://www.carrefour.com.br/alimentos-organicos?page=";
                public static final String WITHOUT_ANTIBIOTICS_URL = "https://www.carrefour.com.br/alimentos-sem-antibiotico?page=";
                public static final String WITHOUT_GLUTEN_URL = "https://www.carrefour.com.br/alimentos-sem-gluten?page=";
                public static final String ZERO_URL = "https://www.carrefour.com.br/alimentos-light-e-diet?page=";
                public static final String SEEDS_URL = "https://www.carrefour.com.br/sementes?page=";
                
            }

            public class Morning{
                public static final String ROASTED_BUNS_URL = "https://www.carrefour.com.br/paes-torradas-e-bolos?page=";
                public static final String DAIRY_YOGURTD_URL = "https://www.carrefour.com.br/iogurtes-e-laticinios?page=";
                public static final String CHOCOLATE_URL = "https://www.carrefour.com.br/achocolatados-e-shakes?page=";
                public static final String COFFEE_TEA_URL = "https://www.carrefour.com.br/cafe-e-cha?page=";
                public static final String MILK_URL = "https://www.carrefour.com.br/leite?page=";
                public static final String BISCUIT_CEREALS_URL = "https://www.carrefour.com.br/biscoitos-e-cereais?page=";
                public static final String FLOUR_OATMEAL_PORRIDGE_URL = "https://www.carrefour.com.br/farinhas-aveia-e-mingau?page=";
                
            }
            
        }
        
        public class Guanabara{
            public static final String CRF ="crf_products" ;
            public static final String DEC_PRODUCT_LIST = "product" ;
            public static final String DEC_TITLE = "prd-name" ;
            public static final String DEC_IN_PRICE = "prd-price-old" ;
            public static final String DEC_PER_PRICE = "prd-price-new" ;
            public static final String DEC_SRC = "src" ;
            public static final String DEC_IMG = "img" ;
            public static final String DEC_DATA_SRC = "data-src" ;
            public static final String DEC_INSTALLMENT = "prd-price-installments" ;
        }
    }

    public String getBaseImg() {
        return baseImg;
    }

    public void setBaseImg(String baseImg) {
        this.baseImg = baseImg;
    }

    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }

    public String getGetTitle() {
        return getTitle;
    }

    public void setGetTitle(String getTitle) {
        this.getTitle = getTitle;
    }

    public String getInPrice() {
        return inPrice;
    }

    public void setInPrice(String inPrice) {
        this.inPrice = inPrice;
    }

    public String getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(String perPrice) {
        this.perPrice = perPrice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
    
    public synchronized LinkedList<ITProduct> getBlockProduct(String url, String market, String categorie, int i){
        
        this.market = market ;
        
        StringBuilder urlCreate = new StringBuilder();
        urlCreate.append(url);
        urlCreate.append(i);

        return requestProducts(urlCreate.toString(), categorie) ;
    }

    private boolean verifyProductExist(String productName){
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;
        try {
            String sql = "SELECT title FROM czc_products WHERE title=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, productName);
            ResultSet rs = pst.executeQuery();

            boolean exist = rs.next();

            con.close();
            pst.close();
            return exist;
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false ;
        }
    }
    
    private double convertPriceToDouble(String price){
        price = price.replace("Por:", "");
        price = price.replace("por:", "");
        price = price.replace("R$ ", "");
        price = price.replace(",", ".");
        price = price.replace("De: ", "");
        price = price.replace("de: ", "");
        return Double.valueOf(!price.isEmpty()?price:"0");
    }
    
    private synchronized LinkedList<ITProduct> requestProducts(String url, String categorie){
        try {
            if(src != null && dataSrc != null && img != null && installment != null
                    && productList != null && getTitle != null){
                Document doc = Jsoup.connect(url).get();

                Elements element = doc.getElementsByClass(productList);
                StringBuilder title;
                ITProduct p ;
                LinkedList<ITProduct> products = new LinkedList<>();

                boolean  fileDB = false;
                String inPrice, promo, price;
                DaoAdmin dao = new DaoAdmin();
                for(Element e : element){

                    title = new StringBuilder();
                    title.append(e.getElementsByClass(getTitle).text());

                    inPrice = e.getElementsByClass(this.inPrice).text();
                    promo = e.getElementsByTag(img).attr(src);
                    price = e.getElementsByClass(this.perPrice).text();
                    // Se o titulo e o preço for diferente de vazio, contiua com
                    // o processo
                    
                    if(!title.toString().isEmpty() && !price.isEmpty() &&
                            !inPrice.isEmpty() && promo.isEmpty()){

                        
                        // Cria Objeto do tipo produto
                        // com base nas informações do site
                        p = new ITProduct();
                        p.setTitle(e.getElementsByClass(getTitle).text());
                        p.setInPrice(convertPriceToDouble(inPrice));
                        p.setPerPrice(convertPriceToDouble(price));
                        p.setPathImg(ImagemUtil.prepareForUrl(p.getTitle())+".png");
                        p.setCategorie(categorie);
                        //p.setInstalments(e.getElementsByClass(""+installment).text());
                        p.setUrl(e.getElementsByTag(img).attr(dataSrc));
                        //p.setPercentageDiscount(ImagemUtil.prepareForUrl(p.getTitle())+"_promo.png");

                        if(promo != null && !promo.isEmpty()){
                            p.setNamePromo("promoção");
                            //p.setPercentageDiscount(ImagemUtil.prepareForUrl(p.getTitle())+"_promo.png");
                        }


                        // Verifica se ja existe uma imagem com o mesmo nome do produto
                        // se não existir salva a imagem
                        //image = new File(baseImg+p.getPathImg()).exists();
                        fileDB = verifyProductExist(title.toString());

                        byte[] image = ImagemUtil.downloadAndResizeImage(p.getUrl(), 210);

                        if(image != null){


                            ITProduct returnedProduct ;
                            if((returnedProduct = dao.getDaoProducts().existProductDB(p.getTitle())) != null){

                                boolean equalsPrice = p.getPerPrice() == returnedProduct.getPerPrice();

                                returnedProduct.setPerPrice(p.getPerPrice());
                                if(!equalsPrice)dao.getDaoProducts().updateProduct(returnedProduct);
                                dao.getDaoImage().getProductImage().insertImageProduct(returnedProduct.getId(),p.getPathImg(),image);
                            }
                        }
                        
                        //if(fileDB == false){
                            //products.add(p);
                            //System.out.println("NOVO PRODUTO_DB: "+categorie+" "+p.getTitle());
                        //}
                    }
                }   
                return products ;
            }else{
                JOptionPane.showMessageDialog(null, "Defina as tags", "Definição de Tags", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            return null;
        }
        return null ;
    }

    private synchronized void sleep(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoadingProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

