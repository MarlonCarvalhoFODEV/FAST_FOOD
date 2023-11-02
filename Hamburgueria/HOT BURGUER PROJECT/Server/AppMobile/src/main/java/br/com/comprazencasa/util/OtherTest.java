package br.com.comprazencasa.util;

import br.com.hotburguer.daos.DaoHotNotification;
import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;
import java.util.*;

public class OtherTest {
    public static void main(String[] args) throws Exception {

        //boolean status = Transaction.Status.REFUNDED == Transaction.Status.AUTHORIZED;

        //APIPagarme pagarme = new APIPagarme("ak_live_ZtHfMPxyiUh4uFW1HIAcUPPt1c2ZM6");
        //Transaction.Status result = pagarme.getTransactionsManager().createTramsactopmTeste();

        /*FileInputStream fin = new FileInputStream("public.cry");
        ObjectInputStream oin = new ObjectInputStream(fin);

        FileInputStream fin1 = new FileInputStream("private.cry");
        ObjectInputStream oin1 = new ObjectInputStream(fin1);

        PublicKey publicKey = (PublicKey) oin.readObject();
        PrivateKey privateKey = (PrivateKey) oin1.readObject();

        RSA rsa = new RSA(publicKey, privateKey);

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println("PRIVATE KEY: "+publicKeyString);

        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pubRecover = factory.generatePublic(new X509EncodedKeySpec(publicKey.getEncoded()));

        /*byte[] decoded = Base64.getDecoder().decode(publicKeyString);

        PublicKey p = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        rsa = new RSA(pubRecover, privateKey);

        String cardJson = new Gson().toJson("Cagada$#$%#RG FGDGF D{}{}GF{D G}//\\|\"");
        final byte[] transJsonRSA = rsa.encrypt(cardJson);

        System.out.println("CCCC: "+transJsonRSA.length);

        String decript = rsa.decrypt(transJsonRSA);
        System.out.println("DECRIPT: "+decript);

        System.out.println("CHAVE PUBLICA: "+pubRecover.getFormat());*/


        //rsas.geraChave();
       /* Date dateSend = Calendar.getInstance().getTime();
        dateSend = DateAPI.getDateManager().addTimeFromDate(dateSend,0,1,0);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Enviado");
                timer.cancel();
            }
        }, dateSend);*/

        //String appID = "98c46fd1-2bd2-4226-bd83-480ec194b796";
        //String restApiKey = "ZjhiY2M1M2QtYmM2MC00Nzk2LWJmMTItNzViYjNhMTFiZDJi";

        /*String appID = "5627bc3c-23eb-40bb-b45b-6c27b8dd639f";
        String restApiKey = "NmIxOGM5ZGItNjI5OS00NjNlLTkzNTUtMzM3NmQxOTBmYzU5";

        OneSignalNotification not = new OneSignalNotification();
        not.setLanguage(OneSignalNotification.Languages.ENGLISH);
        not.setTitle("Nova Corrida de R$ 3,99");
        not.setSubTitle("sub titulo");
        not.setMessage("Clique Para Iniciar");
        //not.getSegments().setSegmentBoth(OneSignalSegment.Type.INCLUDED_SEGMENT);
        not.getAppearances().setAndroidSmallIcon("ic_notification");
        not.getAppearances().setAndroidLedColor("FA730E");
        not.getAppearances().setAndroidAccentColor("FA730E");

        OneSignalFilter filter = new OneSignalFilter(not);
        //filter.setIncludePlayerIds(Arrays.asList("42"));
        filter.setIncludeExternalUserIds(Arrays.asList("42"));

        JSONArray idOrders = new JSONArray();
        idOrders.put(140);*//*
        idOrders.put(141);
        idOrders.put(142);*//*

        JSONObject json = new JSONObject();
        json.put("type",1);
        json.put("id_orders", idOrders);
        json.put("amount","17,85");
        json.put("count_delivery",1);
        json.put("point1_km","0.5km");
        not.setData(json);

        APIOneSignal oneSignal = new APIOneSignal(appID, restApiKey);;
        oneSignal.getNotificationManager().sendNotification(not);*/

        JSONArray array = new JSONArray();
        //array.put(154);
        //array.put(157);
        array.put(161);
        array.put(162);

         /*JSONObject json = new JSONObject();
        json.put("type",1);
        json.put("id_orders", array);
        json.put("amount","11,97");
        json.put("count_delivery", array.length());
        json.put("point1_km","0.5km");

        OneSignalResponse result = new DaoHotNotification().notifyDeliveryMan("Nova Corrida","Preparese","Preparese",56, json);
       if(result.getResponseCode() != 200){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                }
            },0,30000);
        }

        System.out.println(result.getResponseCode());*/

        new DaoHotNotification().notifyManager("title", "message", "message", 1,null);
    }

    public static class FinanceAPI {

        public double calculateFeePercentual(double value, double fee){
            double dif = (value/(1.0-(fee/100.0))) - value;
            double percentualDif = ruleOfThree(value, dif);
            return  (value+(value*(percentualDif/100)));
        }

        public static double ruleOfThree(double value, double x){
            return x * 100 / value ;
        }

    }

    private static PublicKey getPublicKey(String keyBase64){
        byte[] cc = Base64.getDecoder().decode(keyBase64);

        return null ;
    }

    public static void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void requestServer(){
        HttpURLConnection con = null ;
        String url = "";
        try {
            URL urlO = new URL(url);
            con = (HttpURLConnection) urlO.openConnection();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static String curl(String url, String accepts, String minusD) throws Exception {
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Accept", accepts);
        con.setRequestProperty("Content-Type",  "application/x-www-form-urlencoded");
        con.getOutputStream().write(minusD.getBytes());
        con.getOutputStream().close();

        ByteArrayOutputStream rspBuff = new ByteArrayOutputStream();
        InputStream rspStream = con.getInputStream();

        int c;
        while ((c = rspStream.read()) > 0) {
            rspBuff.write(c);
        }
        rspStream.close();

        return new String(rspBuff.toByteArray());
    }

    public static String parseSemGson(String linha,String campo){
        linha = linha.replaceAll("\"","");
        int indice = linha.indexOf(campo) + 3 + campo.length();
        int ultimo_char = linha.indexOf(",", indice)!=-1?linha.indexOf(",", indice):linha.indexOf("}", indice);
        return linha.substring(indice,ultimo_char);
    }
}


