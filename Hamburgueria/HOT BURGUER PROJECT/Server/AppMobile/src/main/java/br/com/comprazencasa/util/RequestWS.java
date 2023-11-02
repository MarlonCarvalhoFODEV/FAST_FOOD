/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class RequestWS {
   
    private static final int HTTP_COD_SUCESSO = 200 ;
    private static final Gson gson = new Gson();
    private static final String URL_REQUEST = "null" ;
    
    public static synchronized String requestJson(String typeRequest, String controller, String method, String param){
       
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            
            Future<String> futureResult = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuilder urlContruction = new StringBuilder();
                    urlContruction.append(URL_REQUEST);
                    urlContruction.append(controller);
                    urlContruction.append(method);
                    
                    if(param!=null) urlContruction.append(param);

                    URL url = new URL(urlContruction.toString());

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod(typeRequest);
                    con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

                    if (con.getResponseCode() != HTTP_COD_SUCESSO){
                        throw new RuntimeException("HTTP ERROR CODE: "+con.getResponseCode());
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder response = new StringBuilder();
                    String line ;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    con.disconnect();
                    reader.close();
                    
                    return response.toString();
                }
            });
            executor.shutdown();
            return futureResult.get();
        } catch (InterruptedException | ExecutionException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível acessar o servidor ou banco de dados\nverifique se está configurado corretamente!","Erro ao tentar acessar o serviço",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(RequestWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null ;
    }
    
    public static String requestJsonFromID(String typeRequest, String controller, String method, int id){
        
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            
            Future<String> futureResult = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuilder urlContruction = new StringBuilder();
                    urlContruction.append(URL_REQUEST);
                    urlContruction.append(controller);
                    urlContruction.append(method);
                    urlContruction.append(String.valueOf(id));

                    URL url = new URL(urlContruction.toString());

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod(typeRequest);
                    con.setRequestProperty("Accept", "application/json");

                    if (con.getResponseCode() != HTTP_COD_SUCESSO){
                        throw new RuntimeException("HTTP ERROR CODE: "+con.getResponseCode());
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder response = new StringBuilder();
                    String line ;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    con.disconnect();
                    reader.close();

                    return response.toString();
                }
            });
            executor.shutdown();
            return futureResult.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(RequestWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null ;
    }
    
    public static String requestJsonSearch(String typeRequest, String controller, String method, Object object){
        
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            
            Future<String> futureResult = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuilder urlContruction = new StringBuilder();
                    urlContruction.append(URL_REQUEST);
                    urlContruction.append(controller);
                    urlContruction.append(method);

                    URL url = new URL(urlContruction.toString());

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod(typeRequest);
                    con.setRequestProperty("Content-Type", "application/json");

                    OutputStream output = con.getOutputStream();
                    output.write(gson.toJson(object).getBytes("UTF-8"));
                    output.flush();
                    
                    
                    if (con.getResponseCode() != HTTP_COD_SUCESSO){
                        throw new RuntimeException("HTTP ERROR CODE: "+con.getResponseCode());
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder response = new StringBuilder();
                    String line ;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    con.disconnect();
                    reader.close();

                    return response.toString();
                }
            });
            executor.shutdown();
            return futureResult.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(RequestWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null ;
    }
}