/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.geo;

import br.com.comprazencasa.database.DaoAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class Api {
	private String sendGet(String url) {
            try {
                    StringBuilder buffer = new StringBuilder();
                    String inputLine;
                    URL obj = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection)obj.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "CepAberto");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Authorization", "Token token=d7bfeff7dfef5a67d0a073d5fe9b5b7b");
                    
                    int responseCode = conn.getResponseCode();
                    
                    //System.out.println("Response Code: " + responseCode);
                    
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    while ((inputLine = in.readLine()) != null) {
                            buffer.append(inputLine);
                    }

                    in.close();
                    return buffer.toString();
            } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
            } catch (IOException e) {
                    System.err.println("UM CEP NÃO PODE SER CARREGADO!");
            }
            return null;
	}
        
        private static String sendGetGOOGLE(String url) {
            try {
                    StringBuilder buffer = new StringBuilder();
                    String inputLine;
                    URL obj = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection)obj.openConnection();
                    conn.setRequestMethod("GET");
                    //conn.setRequestProperty("User-Agent", "CepAberto");
                    conn.setRequestProperty("Accept", "application/json");
                    //conn.setRequestProperty("Authorization", "Token token=d7bfeff7dfef5a67d0a073d5fe9b5b7b");
                    
                    int responseCode = conn.getResponseCode();
                    
                    //System.out.println("Response Code: " + responseCode);
                    
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    while ((inputLine = in.readLine()) != null) {
                            buffer.append(inputLine);
                            buffer.append("\n");
                    }

                    in.close();
                    return buffer.toString();
            } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
            } catch (IOException e) {
                    System.err.println("UM CEP NÃO PODE SER CARREGADO!");
            }
            return null;
	}
	
        public synchronized static List<String> getAllCepFromCity(String city){
            List<String> allCeps = new ArrayList<>();
            
            try {
                File file = new File("C:\\Users\\Marlon\\Desktop\\CEPS.txt");
                FileWriter fout = new FileWriter(file);
              
                DaoAddress dao = new DaoAddress();
                for(int i=1; i < 1000; i++){
                 
                    System.out.println("CEP: "+getOneCepFromCITY(city, i));
                    if(!allCeps.contains(getOneCepFromCITY(city, i))){
                        allCeps.addAll(getCepFromCity(city, i));
                        
                        for (String cep : allCeps) {
                            
                            dao.registerCepTest(cep);
                            fout.write((cep+"\r\n"));
                            fout.flush();
                        } 
                    }
                    System.out.println(i+"° "+allCeps.get(allCeps.size()-1)+" SIZE: "+allCeps.size());
                }   
                fout.close();
                return allCeps ;
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            } 
            return allCeps ;
        }
        
        public static String getOneCepFromCITY(String city, int page){
             List<String> ceps = new ArrayList<>();
            try {
                city = city.replaceAll(" ", "-");
                
                StringBuilder sb = new StringBuilder();
                sb.append("https://www.guiadecompra.com/cep/cidade/");
                sb.append(city);
                sb.append("-RJ/page/");
                sb.append(page);

                Document doc = Jsoup.connect(sb.toString()).get();
                Elements element = doc.getElementsByClass("tablecep");

                List<String> ruasCidade = element.get(0).getElementsByTag("a").eachText();
                
                Street streetOB ;
                
                String cep ;
                for (String street : ruasCidade) {
                    
                    street = street.substring(0, street.length()-5);
                    cep = street.substring(0,street.indexOf("⇨")-1);
                    
                    return cep ;
                }
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null ;
        }
        
        public synchronized static boolean teste(String city, int count, FileWriter fout){
            
            try {
                city = city.replaceAll(" ", "-");
                
                StringBuilder sb = new StringBuilder();
                sb.append("https://www.guiadecompra.com/cep/cidade/");
                sb.append(city);
                sb.append("-RJ/page/");
                sb.append(count);
                
                Document doc = Jsoup.connect(sb.toString()).get();
                Elements element = doc.getElementsByClass("tablecep");

                List<String> ruasCidade = element.get(0).getElementsByTag("a").eachText();
                
                Street streetOB ;
                
                int cont=1 ;
                String cep ;
                
                LinkedList<String> ceps = new DaoAddress().getAllCeps();
                
                for (String street : ruasCidade) {
                    
                    street = street.substring(0, street.length()-5);
                    cep = street.substring(0,street.indexOf("⇨")-1);
                    
                    cep = cep.replaceAll("-", "").replaceAll(" ", "");
                    
                    if(cep == null || cep.isEmpty()) return false;
                    
                    fout.append(cep);
                    fout.append("\r\n");
                    fout.flush();
                 
                }
                return true ;
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false ;
        }
        
        private synchronized static List<String> getCepFromCity(String city, int count){
            
            List<String> ceps = new ArrayList<>();
            try {
                city = city.replaceAll(" ", "-");
                
                StringBuilder sb = new StringBuilder();
                sb.append("https://www.guiadecompra.com/cep/cidade/");
                sb.append(city);
                sb.append("-RJ/page/");
                sb.append(count);
                
                Document doc = Jsoup.connect(sb.toString()).get();
                Elements element = doc.getElementsByClass("tablecep");

                List<String> ruasCidade = element.get(0).getElementsByTag("a").eachText();
                
                Street streetOB ;
                
                String cep ;
                for (String street : ruasCidade) {
                    
                    street = street.substring(0, street.length()-5);
                    cep = street.substring(0,street.indexOf("⇨")-1);
                    ceps.add(cep);
                }
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
            return ceps ;
        }
        
        public static synchronized Street getStreetFromCEP(String cep){
            Street street = null;
            try {
                String json = new Api().sendGet("http://www.cepaberto.com/api/v3/cep?cep="+cep);
                if(json != null){
                    JSONObject obj = new JSONObject(json);
                    JSONObject obj2 = obj.getJSONObject("cidade");
                    JSONObject obj3 = obj.getJSONObject("estado");

                    street = new Street();
                    street.setAltitude(obj.has("altitude")?obj.getDouble("altitude"):0.0);
                    street.setCep(cep);
                    street.setName(obj.has("logradouro")?obj.getString("logradouro"):null);
                    street.setLatitude(obj.has("latitude")?obj.getDouble("latitude"):0);
                    street.setLongitude(obj.has("longitude")?obj.getDouble("longitude"):0);
                    street.setCityName(obj2.has("nome")?obj2.getString("nome"):null);
                    street.setNeighborhood(obj.getString("bairro").length()>5?obj.getString("bairro"):null);
                    street.setIBGE(obj2.has("ibge")?obj2.getInt("ibge"):0);
                    street.setDdd(obj2.has("ddd")?obj2.getInt("ddd"):0);
                    street.setInitials(obj3.has("sigla")?obj3.getString("sigla"):null);

                    sleeping(100);
                }
              return street ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null ;
        }
        
        public static List<String> getCityOrContyFromINITIALS(String initials){
            List<String> list = new ArrayList<>();
            try {
                String json = new Api().sendGet("http://www.cepaberto.com/api/v3/cities?estado="+initials);
                JSONArray array = new JSONArray(json);
                
                for(int i=0; i <= array.length()-1; i++){
                    JSONObject obj = array.getJSONObject(i);
                    list.add(obj.getString("nome"));
                    System.out.println(obj.getString("nome"));
                }
                
            } catch (JSONException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
            return list ;
        }
        
        public static City getInfosFromCity(String initials, String cityName){
            City city = new City();
            try {
                String json = new Api().sendGet("http://www.cepaberto.com/api/v3/address?estado="+initials+"&cidade="+cityName);
                JSONObject obj = new JSONObject(json);
                
                String result = obj.toString();
               
                city.setName(cityName.replaceAll("\\+", " "));
                if(result != null && !result.isEmpty() && result.length() > 4){
                    JSONObject obj2 = obj.getJSONObject("cidade");
                    JSONObject obj3 = obj.getJSONObject("estado");
                    
                    city.setAltitude(obj.getDouble("altitude"));
                    city.setCep(obj.getString("cep"));
                    city.setName(obj.getString("logradouro"));
                    city.setLatitude(Double.valueOf(obj.getString("latitude")));
                    city.setLongitude(Double.valueOf(obj.getString("longitude")));
                    city.setIBGE(obj2.has("ibge")?obj2.getInt("ibge"):null);
                    city.setDdd(obj2.has("ddd")?obj2.getInt("ddd"):null);
                    city.setUf(obj3.getString("sigla"));
                }
                
            } catch (JSONException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
            return city ;
        }
        
        private static String formatCityName(String city){
            if(city.contains("(")){
                    city = city.substring(0, city.indexOf(" ("));
            }
            
            String cityName = city
                .replaceAll(" ", "+")
                .replaceAll("á", "a")
                .replaceAll("é", "e")
                .replaceAll("í", "i")
                .replaceAll("ó", "o")
                .replaceAll("ú", "u")
                .replaceAll("ã", "a")
                .replaceAll("õ", "o")
                .replaceAll("ç", "c")
                .replaceAll("ô", "o")
                .replaceAll("ê", "e")
                .replaceAll("â", "a");
            return cityName;
        }
        
        public static void registerCityAndStreets(String fileNameTemp,int ini, int and){
            FileWriter fout = null ;
            try{
                List<String> citys = getCityOrContyFromINITIALS("RJ");

                File file = new File("C:\\Users\\Marlon\\Desktop\\"+fileNameTemp+".txt");
                fout = new FileWriter(file);
                
                for (String city : citys) {
                    System.out.println("CIDADE: "+city);
                    for (int i = ini; i < and; i++) {
                        try {
                            city = city.replaceAll(" ", "-");

                            StringBuilder sb = new StringBuilder();
                            sb.append("https://www.guiadecompra.com/cep/cidade/");
                            sb.append(city);
                            sb.append("-RJ/page/");
                            sb.append(i);

                            Document doc = Jsoup.connect(sb.toString()).get();
                            Elements element = doc.getElementsByClass("tablecep");

                            List<String> ruasCidade = element.get(0).getElementsByTag("a").eachText();

                            Street streetOB ;

                            int cont=1 ;
                            String cep ;

                            LinkedList<String> ceps = new DaoAddress().getAllCeps();

                            for (String street : ruasCidade) {

                                street = street.substring(0, street.length()-5);
                                cep = street.substring(0,street.indexOf("⇨")-1);

                                cep = cep.replaceAll("-", "").replaceAll(" ", "");
                                System.out.println("Rua CEPS: "+ cep);
                                if(cep == null || cep.isEmpty()) break;

                                fout.append(cep);
                                fout.append("\r\n");
                                fout.flush();

                            }
                            break;
                        } catch (IOException ex) {
                            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                }
                
                FileReader fReader = new FileReader(file);
                BufferedReader buf = new BufferedReader(fReader);
                
                DaoAddress dao = new DaoAddress();
                LinkedList<String> ceps = dao.getAllCeps();
                
                String cepReader ;
                while((cepReader = buf.readLine()) != null || !(cepReader = buf.readLine()).isEmpty()){
                    
                    boolean resultCEP = ceps.contains(cepReader);
                   
                    dao.registerCepTest(cepReader);
                    
                    /*if(resultCEP == false){
                        System.out.println("NOVO CEP: "+cepReader);
                        Street street = getStreetFromCEP(cepReader);
                       
                        if(street != null){
                            boolean result = dao.registerStreet(street);
                        }
                    }
                    else{
                        System.out.println("CEP já Registrado! > "+cepReader);
                    }*/
                }
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fout.close();
                } catch (IOException ex) {
                    Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public static boolean updateNeighborhood(String neighborhood, int ini, int and){
            
            LinkedList<String> ceps = new LinkedList<>();
            for(int i=ini; i <= and; i++){
              
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("https://cep.guiamais.com.br/busca/");
                    sb.append(neighborhood);
                    sb.append("?page=");
                    sb.append(i);
                    
                    Document doc = Jsoup.connect(sb.toString()).get();
                    Elements element = doc.getElementsByTag("tbody");
                    
                    Elements els = element.get(0).getElementsByTag("td");
                    
                    int counter = 0 ;
                    
                    if(els.isEmpty()) break;
                    
                    for (Element e : els.val("a")) {
                        counter++;
                        if(counter == 5){
                            
                            ceps.add(e.text());
                            
                            counter = 0 ;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            DaoAddress dao = new DaoAddress();
            System.out.println("SIZE: "+ceps.size());
            for (String cep : ceps) {
                
                cep = cep.replaceAll("-", "");
                
                //if(dao.updateNeighborhoodFromCEP("Irajá",cep) == true){
                //    System.out.println("ATUALIZADO COM SUCESSO!");
                //}
                //else{
                //    System.err.println("ERRO AO ATUALIZAR!");
                //}
            }
           
            return false ;
        }
     
	public static void main(String[] args) {
            
            String neighborhood = "iraja-rio+de+janeiro-rj";
            int i = 1, f = 2 ;
            //Street r = getStreetFromCEP("23590830");
            //System.out.println(new Gson().toJson(r));
            //updateNeighborhood(neighborhood, i, f);
            //tetetete("rio+de+janeiro-rj",1,1000);
            
           DaoAddress dao = new DaoAddress();
           //dao.deleteDuplicateStreet();//
           
           new Thread(){
                @Override
                public void run() {
                    LinkedList<String> citys = dao.getAllCityName();
                    int counterCity = 0 ;
                    for (String city : citys) {
                     counterCity++;
                     System.out.println(counterCity+" °---------- "+city+" ------------");

                     if(counterCity >= 209){
                        getNeighborhoodFromCity("rio-de-janeiro", city);
                     }
 
                     System.out.println(counterCity+" °---------- "+city+" ------------");
                    }
                }
            }.start();
        }
        
        private static void sleeping(int timeMils){
            try {
                Thread.sleep(timeMils);
            } catch (InterruptedException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        
        // LAST TEST
        //
      
        private static void getCompleteInfoStreet(String state, String city){
            
            DaoAddress dao = new DaoAddress();
            //LinkedList<Street> list = new LinkedList<>();
            
            try {
               
                // busca os bairros de uma cidade
                StringBuilder sb0 = new StringBuilder("https://cepbrasil.org/");
                sb0.append(state);
                sb0.append("/");
                sb0.append(city);
                
                Document doc0 = Jsoup.connect(sb0.toString()).get();
                Elements element0 = getElementsFromSite(sb0, "col-md-6 coluna", "a");
                
                String neighborhood ;
                for (Element ee : element0) {
                    neighborhood = DaoAddress.formatCityName(ee.text());
                    // busca as ruas de um bairro
                    StringBuilder sb = new StringBuilder("https://cepbrasil.org/");
                    sb.append(state);
                    sb.append("/");
                    sb.append(city);
                    sb.append("/");
                    sb.append(neighborhood);

                    Elements element = getElementsFromSite(sb, "col-md-6 coluna coluna-cep", "a");
                    
                    String cep ;
                    for (Element e : element) {
                        cep = e.getElementsByClass("box").text();
                        cep = cep.substring(4, cep.indexOf(" -"));
                        cep = cep.replaceAll("-", "");

                        StringBuilder sb2 = new StringBuilder("https://cepbrasil.org/");
                        sb2.append(state);
                        sb2.append("/");
                        sb2.append(city);
                        sb2.append("/");
                        sb2.append(neighborhood);
                        sb2.append("/");
                        sb2.append(cep);

                        Elements element2 = getElementsFromSite(sb2, "col-md-6 coluna coluna-cep", "p");
                        
                        Street street = new Street();

                        int counter = 0 ;
                        String val ;
                        for (Element e2 : element2) {

                            val = e2.getElementsByClass("title").text();
                            if(val.contains(": ")) val = val.substring(val.indexOf(": ")+2);

                            counter++;
                            switch(counter){
                                case 3:{
                                    street.setName(val);
                                    //System.out.println("RUA: "+val);
                                    break;
                                }

                                case 4:{
                                    street.setNeighborhood(val);
                                    //System.out.println("BAIRRO: "+val);
                                    break;
                                }

                                case 5:{
                                    street.setCityName(val);
                                    //System.out.println("CIDADE: "+val);
                                    break;
                                }

                                case 6:{
                                    //street.set
                                    //System.out.println("ESTADO: "+val);
                                    break;
                                }

                                case 7:{
                                    street.setInitials(val);
                                    //System.out.println("SIGLA: "+val);
                                    break;
                                }

                                case 10:{
                                    street.setLongitude(Double.valueOf(val));
                                    //System.out.println("LONGITUDE: "+val);
                                    break;
                                }
                                case 11:{
                                    street.setLatitude(Double.valueOf(val));
                                    street.setCep(cep);
                                    
                                    if(dao.registerStreet(street)){
                                        System.out.println("NOVO REGISTRO");
                                    }
                                    else{
                                        System.err.println("NÂO FOI REGISTRADO!");
                                    }
                                    street = new Street();
                                    //list.add(street);
                                    //System.out.println("LATITUDE: "+val);
                                    //System.out.println("------------------------------");
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
            //return list ;
        }
        
        private static synchronized Elements getElementsFromSite(StringBuilder b, String byClass, String byTag){
            try {
                Document doc2 = Jsoup.connect(b.toString()).get();
                return doc2.getElementsByClass("col-md-6 coluna coluna-cep").get(0).getElementsByTag("p");
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null ;
        }
              
        
        private static synchronized void getNeighborhoodFromCity(String state,String city){
            try {
                StringBuilder sb0 = new StringBuilder("https://cepbrasil.org/");
                sb0.append(state);
                sb0.append("/");
                sb0.append(city);
                
                Document doc0 = Jsoup.connect(sb0.toString()).get();
                Elements element0 = doc0.getElementsByClass("col-md-6 coluna").get(0).getElementsByTag("a");
                
                String neighborhood ;
                for (Element ee : element0) {
                    neighborhood = DaoAddress.formatCityName(ee.text());
                    
                    getStreetFromNeighborhood(state, city, neighborhood);
                }
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private static synchronized void getStreetFromNeighborhood(String state,String city, String neighborhood){
            
            DaoAddress dao = new DaoAddress();
            try {
                // busca as ruas de um bairro
                StringBuilder sb = new StringBuilder("https://cepbrasil.org/");
                sb.append(state);
                sb.append("/");
                sb.append(city);
                sb.append("/");
                sb.append(neighborhood);
                
                Document doc = Jsoup.connect(sb.toString()).get();
                Elements element = doc.getElementsByClass("col-md-6 coluna coluna-cep").get(0).getElementsByTag("a");
                
                String cep ;
                for (Element e : element) {
                    cep = e.getElementsByClass("box").text();
                    cep = cep.substring(4, cep.indexOf(" -"));
                    cep = cep.replaceAll("-", "");
                    
                    StringBuilder sb2 = new StringBuilder("https://cepbrasil.org/");
                    sb2.append(state);
                    sb2.append("/");
                    sb2.append(city);
                    sb2.append("/");
                    sb2.append(neighborhood);
                    sb2.append("/");
                    sb2.append(cep);

                    Document doc2 = Jsoup.connect(sb2.toString()).get();
                    Elements element2 = doc2.getElementsByClass("col-md-6 coluna coluna-cep").get(0).getElementsByTag("p");

                    Street street = new Street();

                    int counter = 0 ;
                    String val ;
                    for (Element e2 : element2) {

                        val = e2.getElementsByClass("title").text();
                        if(val.contains(": ")) val = val.substring(val.indexOf(": ")+2);

                        counter++;
                        switch(counter){
                            case 3:{
                                street.setName(val);
                                //System.out.println("RUA: "+val);
                                break;
                            }

                            case 4:{
                                street.setNeighborhood(val);
                                //System.out.println("BAIRRO: "+val);
                                break;
                            }

                            case 5:{
                                street.setCityName(val);
                                //System.out.println("CIDADE: "+val);
                                break;
                            }

                            case 6:{
                                //street.set
                                //System.out.println("ESTADO: "+val);
                                break;
                            }

                            case 7:{
                                street.setInitials(val);
                                //System.out.println("SIGLA: "+val);
                                break;
                            }

                            case 10:{
                                street.setLongitude(Double.valueOf(val));
                                //System.out.println("LONGITUDE: "+val);
                                break;
                            }
                            case 11:{
                                street.setLatitude(Double.valueOf(val));
                                street.setCep(cep);

                                if(dao.registerStreet(street)){
                                    System.out.println("NOVO REGISTRO - "+street.getNeighborhood().toUpperCase()+" : "+street.getName());
                                }
                                else{
                                    System.out.println(counter+" REGISTRO EXISTENTE - ATUALIZADO");
                                   
                                }
                                street = new Street();
                                break;
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}