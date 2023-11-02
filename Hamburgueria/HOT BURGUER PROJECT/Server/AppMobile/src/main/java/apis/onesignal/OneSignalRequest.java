package apis.onesignal;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OneSignalRequest {

    private String appID, urlNotification ;

    public OneSignalRequest(String appID, String urlNotification) {
        this.appID = appID;
        this.urlNotification = urlNotification;
    }

    public void request(){
        try {
            String jsonResponse;

            URL url = new URL(urlNotification);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic AAAAyBGmLPE:APA91bFNbq9qsr0cFfOSBt8f_beaToxbD9PA-P8dhnFjd-qtjWGTzK_J-yxTFtjPQ3J4sZcjXRcdu-jWHgiZ83pJiQnwSaO19YQ55eq2v0zjvJnO_Sgbhlf7GAQNUL8L7xVw7pUP51Aa");
            con.setRequestMethod("POST");

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"app_id\": \"");
            sb.append(appID);
            sb.append("\"included_segments\": [\"");

            String strJsonBody = "{"
                    +   "\"app_id\": \"98c46fd1-2bd2-4226-bd83-480ec194b796\","
                    +   "\"included_segments\": [\"All\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \"English Message\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public class Builder{

        private String appID, includedSegments, data, contents, languageMessage ;

        public Builder setIncludedSegment(String segment){
            this.includedSegments = segment ;
            return this ;
        }

        public class Segments{
            public static final String ALL = "all";
        }
    }
}
