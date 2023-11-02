package apis.onesignal.notification;

import apis.onesignal.OneSignalRequest;
import apis.onesignal.OneSignalResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NotificationManager {

    private String appID;
    private String restApiKey ;

    private String urlNotification;

    public NotificationManager(String URL_ROOT, String appID, String restApiKey) {
        this.appID = appID;
        this.restApiKey = restApiKey;
        this.urlNotification = URL_ROOT + "notifications";
    }

    public OneSignalResponse sendNotification(OneSignalNotification notification){
        return request(notification);
    }

    public void cancelNotification(String id){
        try {
            String jsonResponse;

            URL url = new URL(urlNotification);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic "+restApiKey);
            con.setRequestMethod("POST");

            int httpResponse = con.getResponseCode();

            if (httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            } else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
             new OneSignalResponse(httpResponse , jsonResponse) ;

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private OneSignalResponse request(OneSignalNotification notification) {
        try {
            String jsonResponse;

            URL url = new URL(urlNotification);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic "+restApiKey);
            con.setRequestMethod("POST");

            notification.put("app_id",appID);

            //System.out.println("NOTIF: "+notification.toString());

            byte[] sendBytes = notification.toString().getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();

            if (httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            } else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            return new OneSignalResponse(httpResponse , jsonResponse) ;

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return  null ;
    }

}
