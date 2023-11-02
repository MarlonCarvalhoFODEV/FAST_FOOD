package apis.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebJson {

    private Map<String, HttpURLConnection> connections = new HashMap<>();
    private Map<String, HttpsURLConnection> connectionsSSL = new HashMap<>();
    private static final String WIFI_CONNECTION = "WIFI_CONNECTION";
    private static final String A3G_CONNECTION = "3G_CONNECTION";
    private static final String NOT_CONNECTION = "NOT_CONNECTION";

    private InternetRequisitionAPI api ;
    private Map<String, Map<String, URLConnection>> conections = new HashMap<>();

    public WebJson(){}

    public WebJson(InternetRequisitionAPI api){
        this.api = api ;
    }

    public String getJson(String urlConnection) {
        HttpURLConnection conn = null;
        HttpsURLConnection connSSL = null;

        BufferedReader br = null;
        URL url = null;

        boolean localHost = urlConnection.matches("^[http://]+\\d{3}+\\.+\\d{3}+\\.+\\d{1,3}+\\.+\\d{1,3}+\\:+\\d{1,6}.*");

        try {
            url = new URL(urlConnection);

            if (localHost) {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", auto());
                conn.setRequestProperty("Content-Language", "pt-BR");
                conn.setDoInput(true);
            } else {
                connSSL = (HttpsURLConnection) url.openConnection();
                connSSL.setRequestMethod("GET");
                connSSL.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connSSL.setRequestProperty("Accept", "application/json");
                connSSL.setRequestProperty("Authorization", auto());
                connSSL.setRequestProperty("Content-Language", "pt-BR");
                connSSL.setDoInput(true);
            }

            InputStream inputStream = (localHost ? conn.getInputStream() : connSSL.getInputStream());
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            StringBuilder receiver = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                receiver.append(line);
            }

            if (localHost)
                conn.disconnect();
            else
                connSSL.disconnect();
            br.close();

            return receiver.toString();
        } catch (MalformedURLException e) {
            api.callbackExceptions(e);
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                api.callbackExceptions(ex);
            }
        } catch (ProtocolException e) {
            api.callbackExceptions(e);
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                api.callbackExceptions(ex);
            }
        } catch (IOException e) {
            api.callbackExceptions(e);
            try {
                conn.disconnect();
                if (br != null) br.close();
            } catch (IOException ex) {
                api.callbackExceptions(ex);
            }
        }
        return null;
    }

    public String getJson(String id, String urlConnection) {
        if (haveConnection()) {
            HttpURLConnection conn = null;
            HttpsURLConnection connSSL = null;

            BufferedReader br = null;
            URL url = null;
            boolean localHost = urlConnection.matches("^[http://]+\\d{3}+\\.+\\d{3}+\\.+\\d{1,3}+\\.+\\d{1,3}+\\:+\\d{1,6}.*");

            try {
                url = new URL(urlConnection);

                if (localHost) {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Authorization", auto());
                    conn.setRequestProperty("Content-Language", "pt-BR");
                    conn.setDoInput(true);

                    if (connections.containsKey(id))
                        connections.get(id).disconnect();
                    connections.put(id, conn);

                } else {
                    connSSL = (HttpsURLConnection) url.openConnection();
                    connSSL.setRequestMethod("GET");
                    connSSL.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    connSSL.setRequestProperty("Accept", "application/json");
                    connSSL.setRequestProperty("Authorization", auto());
                    connSSL.setRequestProperty("Content-Language", "pt-BR");
                    connSSL.setDoInput(true);

                    if (connectionsSSL.containsKey(id))
                        connectionsSSL.get(id).disconnect();
                    connectionsSSL.put(id, connSSL);
                }


                InputStream inputStream = (localHost ? conn.getInputStream() : connSSL.getInputStream());
                br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                StringBuilder receiver = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    receiver.append(line);
                }

                if (localHost)
                    conn.disconnect();
                else
                    connSSL.disconnect();
                br.close();

                return receiver.toString();
            } catch (MalformedURLException e) {
                api.callbackExceptions(e);
                try {
                    if (localHost)
                        conn.disconnect();
                    else
                        connSSL.disconnect();
                    br.close();
                } catch (IOException ex) {
                    api.callbackExceptions(ex);
                }
            } catch (ProtocolException e) {
                api.callbackExceptions(e);
                try {
                    if (localHost)
                        conn.disconnect();
                    else
                        connSSL.disconnect();
                    br.close();
                } catch (IOException ex) {
                    api.callbackExceptions(ex);
                }
            } catch (IOException e) {
                api.callbackExceptions(e);
                if (localHost)
                    conn.disconnect();
                else
                    connSSL.disconnect();
            }
        }
        return null;
    }

    public synchronized String jsonRequest(String method, String urlConnection, Object object) {
        if (haveConnection()) {
            HttpURLConnection conn = null;
            HttpsURLConnection connSSL = null;

            BufferedReader br = null;
            boolean localHost = urlConnection.matches("^[http://]+\\d{3}+\\.+\\d{3}+\\.+\\d{1,3}+\\.+\\d{1,3}+\\:+\\d{1,6}.*");
            try {
                URL url = new URL(urlConnection);
                if (localHost) {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod(method);
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Authorization", auto());
                    //conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                } else {
                    connSSL = (HttpsURLConnection) url.openConnection();
                    connSSL.setDoOutput(true);
                    connSSL.setRequestMethod(method);
                    connSSL.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    connSSL.setRequestProperty("Accept", "application/json");
                    connSSL.setRequestProperty("Authorization", auto());
                    //connSSL.setReadTimeout(6000);
                    connSSL.setConnectTimeout(6000);
                }

                OutputStream os = (localHost ? conn.getOutputStream() : connSSL.getOutputStream());
                os.write(new Gson().toJson(object).getBytes());
                os.flush();

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                InputStream inputStream = (localHost ? conn.getInputStream() : connSSL.getInputStream());
                br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                StringBuilder response = new StringBuilder();

                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                os.close();

                if (localHost && conn != null)
                    conn.disconnect();
                else if (connSSL != null)
                    connSSL.disconnect();

                br.close();


                return response.toString();
            } catch (IOException e) {
                api.callbackExceptions(e);

                if (localHost && conn != null)
                    conn.disconnect();
                else if (connSSL != null)
                    connSSL.disconnect();
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    api.callbackExceptions(ex);
                }
            }
        } else {

        }

        return null;
    }

    public synchronized String jsonRequest(String activity, String id, String method, String urlConnection, String json) {
        boolean localHost = urlConnection.matches("^[http://]+\\d{3}+\\.+\\d{3}+\\.+\\d{1,3}+\\.+\\d{1,3}+\\:+\\d{1,6}.*");

        BufferedReader br = null;
        HttpURLConnection http = null;
        HttpsURLConnection https = null;

        try {
            URL url = new URL(urlConnection);

            if (localHost)
                http = (HttpURLConnection) url.openConnection();
            else
                https = (HttpsURLConnection) url.openConnection();

            if (localHost) {
                http.setDoOutput(true);
                http.setRequestMethod(method);
                http.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                http.setRequestProperty("Accept", "application/json");
                http.setRequestProperty("Authorization", auto());
                //http.setReadTimeout(6000);
                http.setConnectTimeout(6000);
            } else {
                https.setDoOutput(true);
                https.setRequestMethod(method);
                https.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                https.setRequestProperty("Accept", "application/json");
                https.setRequestProperty("Authorization", auto());
                //https.setReadTimeout(6000);
                https.setConnectTimeout(6000);
            }

            /*if (conections.containsKey(activity) && conections.get(activity).containsKey(id)) {
                if (localHost) {
                    ((HttpURLConnection) conections.get(activity).get(id)).disconnect();
                }else
                    ((HttpsURLConnection) conections.get(activity).get(id)).disconnect();
            } else if(!conections.containsKey(activity)){
                conections.put(activity, new HashMap<>());
            }*/
            //conections.get(activity).put(id, (localHost ? http : https));

            OutputStream os = (localHost ? http.getOutputStream() : https.getOutputStream());
            os.write(json.getBytes());
            os.flush();

            if ((localHost ? http : https).getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + (localHost ? http : https).getResponseCode());
            }

            InputStream inputStream = (localHost ? http : https).getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

            StringBuilder response = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            os.close();
            br.close();
            inputStream.close();

            if (localHost && (localHost ? http : https) != null)
                (localHost ? http : https).disconnect();

            return response.toString();
        } catch (IOException e) {
            api.callbackExceptions(e);

            if (localHost && (localHost ? http : https) != null)
                (localHost ? http : https).disconnect();

            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                api.callbackExceptions(ex);
            }
        }

        return null;
    }

    public void onDestroyConnections(String activityName) {
        Map<String, URLConnection> connections = conections.get(activityName);
        if (connections != null) {
            for (URLConnection conn : connections.values()) {
                try {
                    HttpURLConnection con = (HttpURLConnection) conn;
                    con.disconnect();
                } catch (ClassCastException e) {
                    HttpsURLConnection con = (HttpsURLConnection) conn;
                    con.disconnect();
                }
            }
            conections.remove(activityName);
        }
    }

    public Map<String, HttpURLConnection> getConnections() {
        return connections;
    }

    public Map<String, HttpsURLConnection> getConnectionsSSL() {
        return connectionsSSL;
    }

    private static final String auto() {
        String creds = String.format("%s:%s", "CzcUser", "M@7832@RloN");
        return "Basic " + Base64Utils.encode(creds.getBytes());
    }

    public static boolean haveConnection() {
        try {
            URL mandarMail = new URL("https://google.com");
            URLConnection conn = mandarMail.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.connect();
            int x = httpConn.getResponseCode();
            if (x == 200) {
                return true;
            }
        } catch (MalformedURLException urlmal) {
            return false;
        } catch (IOException ioexcp) {
            return false;
        }
        return false;
    }

    public static String getTypeConnection(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = WIFI_CONNECTION;
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = A3G_CONNECTION;
                return status;
            }
        } else {
            status = NOT_CONNECTION;
            return status;
        }
        return status;
    }


}
