package apis;

import br.com.comprazencasa.database.DaoProducts;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.market.CollectProducts;
import br.com.comprazencasa.util.DataPadraoUtil;
import br.com.comprazencasa.util.ImagemUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.comprazencasa.market.CollectProducts.convertPriceToDouble;
/*
*  Essa API acessa o site do Guanabara, carrega os produtos e imagens, cria um objeto ITProduct
*  E Salva no banco se não existir, se exitir o produto ele atualiza
*  RESUMO DESATIVA TODOS OS PRODUTOS QUE NÃO TEM NO SITE E ATIVA OS QUE TEM
* */
public class APIGuanabara extends Thread{

    private static final String SITE_URL = "https://www.supermercadosguanabara.com.br/produtos/";

    private DaoProducts dao;

    public APIGuanabara() {
        this.dao = new DaoProducts();
    }

    private Map<String, Integer> getAllCategoriesOnList() {
        Map<String, Integer> siteUrlCategories = new HashMap<>();
        siteUrlCategories.put("Drinks", 82);
        siteUrlCategories.put("Butchery", 42);
        siteUrlCategories.put("Cookies & Sugarplum", 32);
        siteUrlCategories.put("Cookies & Sugarplum", 152);
        siteUrlCategories.put("Baby", 203);
        siteUrlCategories.put("Basic Food", 2);
        siteUrlCategories.put("Basic Food", 92);
        siteUrlCategories.put("Cold & Dairy", 52);
        siteUrlCategories.put("Cold & Dairy", 62);
        siteUrlCategories.put("Cleaning Perfumery", 112);
        siteUrlCategories.put("Hortifruti", 142);
        siteUrlCategories.put("Cleaning Perfumery", 102);
        siteUrlCategories.put("Basic Food", 22);
        siteUrlCategories.put("Morning", 12);
        siteUrlCategories.put("Salty", 72);

        return siteUrlCategories;
    }

    private void startCheckingProduct() {
        Map<String, Integer> paginasSite = getAllCategoriesOnList();
        for (String categorie : paginasSite.keySet()) {

            int page = paginasSite.get(categorie);

            String url = SITE_URL + page;
            scanProducts(url, categorie);
        }
    }


    private synchronized void scanProducts(String url, String categorie) {
        List<ITProduct> products = new ArrayList<>();
        try {

            Document doc = Jsoup.connect(url).get();
            Elements p1 = doc.getElementsByClass("products-list");
            Elements p2 = p1.get(0).getElementsByClass("row");
            String urlImg, title = null, price;
            String el;

            for (Element e : p2) {

                el = e.toString();
                try {
                    urlImg = "https://" + el.substring(el.indexOf("www"), (el.contains(".png") ? el.indexOf("png") : el.indexOf("jpg")) + 3).replaceAll(":443", "");
                    title = e.getElementsByClass("col text").get(0).getElementsByClass("name").text();
                    price = e.getElementsByClass("price").get(0).getElementsByClass("number").text();
                    //categorie = page.get(0).getElementsByClass("title").text();

                    ITProduct product = new ITProduct();
                    product.setTitle(title);
                    product.setPerPrice(Double.valueOf(convertPriceToDouble(price)));
                    product.setCategorie(categorie);
                    product.setUrl(urlImg);
                    product.setPathImg(ImagemUtil.prepareForUrl(title) + ".png");
                    product.setInstalments(0);
                    product.setStatus("active");
                    products.add(product);

                } catch (StringIndexOutOfBoundsException fg) {
                    System.err.println("PRODUTO NÂO REGISTRADO - " + title);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CollectProducts.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(products.size()>0){
            analiseProduct(products);
        }

    }

    private synchronized void analiseProduct(List<ITProduct> prods) {

        List<String> productsGuanab = new ArrayList<>();

        for (ITProduct p : prods) {

            ITProduct pExist = dao.existProductDB(p.getTitle());
            productsGuanab.add(p.getTitle());

            boolean image = false ;

            // Cria o produto no DB se não existir
            int id = -1 ;
            if(pExist == null){

                id = dao.insertProduct(p);
                p.setId(id);

                if(id > 0){
                    ImagemUtil.saveImage2(p);
                }
            } else{

                //if(pExist.getPerPrice() != p.getPerPrice()){
                    dao.updateActiveProduct(p,"active");
                //}
            }
        }
        dao.disableIfNotOnTheList(productsGuanab,prods.get(0).getCategorie());
    }

    @Override
    public void run() {
        while (true){

            OffsetDateTime offsetDateTime = DataPadraoUtil.getDateTimeDefault();

            if(offsetDateTime.getHour() == 8 && offsetDateTime.getMinute() == 00){
                startCheckingProduct();
            }

            sleeping(1000);
        }
    }

    private void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
