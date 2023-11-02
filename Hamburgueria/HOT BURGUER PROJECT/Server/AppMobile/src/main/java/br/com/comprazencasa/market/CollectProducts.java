/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.market;

import br.com.comprazencasa.database.DaoProducts;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.util.ImagemUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class CollectProducts {
    private final String BASE_IMAGE = "C:/COMPRAZEM_CASA_RESOURCES/" ;
    private final Map<Integer,String> categories ;
    private final DaoProducts dao ;
    
    public CollectProducts(){
        this.categories = new HashMap<>();
        this.dao = new DaoProducts();

        categories.put(12, "Morning");


        categories.put(32, "Cookies & Sugarplum");
        categories.put(152, "Cookies & Sugarplum");

        categories.put(112, "Cleaning Perfumery");
        categories.put(102, "Cleaning Perfumery");

        categories.put(42, "Butchery");
        categories.put(82, "Drinks");
        categories.put(182, "Baby");


        categories.put(52, "Cold & Dairy");
        categories.put(62, "Cold & Dairy");

        categories.put(22, "Basic Food");
        categories.put(92, "Basic Food");
        categories.put(2, "Basic Food");

        categories.put(132, "Food, Gardening & Camping");
        categories.put(72, "Salty");

    }
    
    public void getAllProductsFromMarket(String market){
        
        String baseIMG = BASE_IMAGE + market ;
        
        List<String> productsGuanab = new ArrayList<>();
        DaoProducts dao = new DaoProducts();
        
        for (Integer pageNumber : categories.keySet()) {
            
            List<ITProduct> ps = getBlockProducts(baseIMG, pageNumber, categories.get(pageNumber));
            
            for (ITProduct p : ps) {
                productsGuanab.add(p.getTitle());
                
                boolean image = false ;
                    
                //image = new File(baseIMG+p.getPathImg()).exists();
                ITProduct pExist = dao.existProductDB(p.getTitle());

                // Cria o produto no DB se não existir
                int id = -1 ;
                if(pExist == null){
                    id = dao.insertProduct(p);
                    p.setId(id);

                    if(image == false && id > 0){
                       ImagemUtil.saveImage2(p);
                    } else {System.out.print(".");}
                } else{
                    if(pExist.getPerPrice() != p.getPerPrice()){
                        dao.updateActiveProduct(p,"active");
                    }
                }

// Salva a imagem se não existir

                
            }
        }
        
        // Desativar produtos caso não exista os podutos do DB na nova coleção
        // de produtos
        //dao.disableIfNotOnTheList(productsGuanab);
    }
    
    public static synchronized List<ITProduct> getBlockProducts(String baseImg, int pageNumber, String categorie){
        List<ITProduct> products = new ArrayList<>();
        try {
            String url = "https://www.supermercadosguanabara.com.br/produtos/"+pageNumber;
            Document doc = Jsoup.connect(url).get();
            Elements p1 = doc.getElementsByClass("products-list");
            Elements p2 = p1.get(0).getElementsByClass("row");
            String urlImg, title = null, price ;
            String el ;
            
            for (Element e : p2) {
                
                el = e.toString();
                try{
                    urlImg = "https://"+el.substring(el.indexOf("www"),(el.contains(".png")?el.indexOf("png"):el.indexOf("jpg"))+3).replaceAll(":443", "");
                    title = e.getElementsByClass("col text").get(0).getElementsByClass("name").text();
                    price = e.getElementsByClass("price").get(0).getElementsByClass("number").text();
                    //categorie = page.get(0).getElementsByClass("title").text();
                    
                    ITProduct product = new ITProduct();
                    product.setTitle(title);
                    product.setPerPrice(Double.valueOf(convertPriceToDouble(price)));
                    product.setCategorie(categorie);
                    product.setUrl(urlImg);
                    product.setPathImg(ImagemUtil.prepareForUrl(title)+".png");
                    product.setInstalments(0);
                    product.setStatus("active");

                    products.add(product); 
                }
                catch (StringIndexOutOfBoundsException fg){
                    System.err.println("PRODUTO NÂO REGISTRADO - "+title);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CollectProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return products ;
    }
    
    public static double convertPriceToDouble(String price){
            price = price.replace("Por:", "");
            price = price.replace("por:", "");
            price = price.replace("R$ ", "");
            price = price.replace(",", ".");
            price = price.replace("De: ", "");
            price = price.replace("de: ", "");
            return Double.valueOf(!price.isEmpty()?price:"0");
    }
    
    // ---------------------- CLASS ------------------------------------------//
    public class Markets{
        public static final String GUANABARA = "Guanabara/" ;
    }
}
