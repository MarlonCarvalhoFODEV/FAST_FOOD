package br.com.comprazencasa.util;

import com.google.gson.Gson;
import org.apache.http.auth.AUTH;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Base64;

public class WebJson {

    public static final String AUTH_SERVER = auto();

    public String getJson(String urlConnection, String auth) {
        HttpURLConnection conn = null;
        BufferedReader br = null ;
        URL url = null;
        try {
            url = new URL(urlConnection);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //if(auth != null)conn.setRequestProperty("Authorization",auth);
            //conn.setRequestProperty("Content-Language", "pt-BR");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder receiver = new StringBuilder();
            String line ;
            while ((line = br.readLine()) != null) {
                receiver.append(line);
                receiver.append("\n");
            }

            conn.disconnect();
            br.close();

            return receiver.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null ;
    }

    public String postJson(String method, String urlConnection, Object object, String auth) {

        BufferedReader br ;
        HttpURLConnection conn = null ;
        try {
            URL url = new URL(urlConnection);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("api_key", auth);
            //if(auth != null)conn.setRequestProperty("Authorization",auth);

            OutputStream os = conn.getOutputStream();
            os.write(new Gson().toJson(object).getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder response = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

            br.close();
            os.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String jsonRequest(String method, String urlConnection, Object object, String auth){
        HttpURLConnection conn = null ;
        BufferedReader br = null ;

        try {
            URL url = new URL(urlConnection);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty("Accept", "application/json");
           // if(auth != null)conn.setRequestProperty("Authorization", auth);

            OutputStream os = conn.getOutputStream();
            os.write(new Gson().toJson(object).getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder response = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            os.close();
            conn.disconnect();
            br.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();

            conn.disconnect();
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null ;
    }

    public String jsonRequest(String method, String urlConnection, String auth) {
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            URL url = new URL(urlConnection);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", auth);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder receiver = new StringBuilder();
            String line ;
            while ((line = br.readLine()) != null) {
                receiver.append(line);
            }

            conn.disconnect();
            br.close();

            return receiver.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null ;
    }

    private static boolean existFile(String path){
        return Files.exists(Paths.get(path));
    }

    private static final String auto(){
        String creds = String.format("%s:%s","CzcUser","M@7832@RloN");
        return "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
    }

    public static boolean haveConnection(){
        try{
            java.net.URL mandarMail = new java.net.URL("https://google.com");
            java.net.URLConnection conn = mandarMail.openConnection();

            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
            httpConn.connect();
            int x = httpConn.getResponseCode();
            if(x == 200){
                return true ;
            }
        }
        catch(java.net.MalformedURLException urlmal){
            return false ;
        }
        catch(java.io.IOException ioexcp){
            return false ;
        }
        return false ;
    }

    public static void closeApplication(){
        System.exit(0);
    }

    public static void startApplication(){
        try {
            new ProcessBuilder("C:\\Program Files (x86)\\Renda Livre.com\\OLink Redirect.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
