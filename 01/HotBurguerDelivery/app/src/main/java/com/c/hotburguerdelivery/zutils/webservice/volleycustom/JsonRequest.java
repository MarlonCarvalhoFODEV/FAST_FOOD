package com.c.hotburguerdelivery.zutils.webservice.volleycustom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Marlon on 10/10/2017.
 */

public class JsonRequest {

    public static String request(String uri){
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
            String line ;

            while ((line = r.readLine()) != null){
                jsonString.append(line);
            }
            urlConnection.disconnect();
            return jsonString.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
