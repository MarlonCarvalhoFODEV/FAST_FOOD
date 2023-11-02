package br.com.hotburguer.itens;

import apis.internet.WebJson;
import br.com.comprazencasa.geo.LatLng;
import com.google.gson.Gson;
import org.json.JSONObject;

public class HotMapBox {

    public static void getMatrixFromCoordinates(LatLng... cord){

        String url = "https://api.mapbox.com/directions/v5/mapbox/cycling/";

        StringBuilder sb = new StringBuilder();
        sb.append(url);

        for(LatLng cordinate : cord) {
            sb.append(cordinate.lng);
            sb.append(",");
            sb.append(cordinate.lat);
            sb.append(";");
        }
        sb = new StringBuilder(sb.toString().substring(0, sb.toString().length()-1));
        sb.append("?access_token=pk.eyJ1IjoibWFybG9uY3pjIiwiYSI6ImNqcXJoMXJ2aTAxZzQ0MnBxbmQxOXh5amUifQ.QbqfUUz8WteXALxmLkhyTQ");

        System.out.println("URL: "+sb.toString());

        WebJson webJson = new WebJson();
        String response = webJson.getJson(sb.toString());

        JSONObject jsonObject = new JSONObject(response);

        System.out.println(jsonObject.toString());
        Object o = jsonObject.get("waypoints");

        JSONObject oo = new JSONObject(new Gson().toJson(o));
        oo = oo.getJSONArray("myArrayList").getJSONObject(0).getJSONObject("map");

        HotMatrix matrix = new HotMatrix();
        matrix.setDistance(oo.getDouble("distance"));


        System.out.println(matrix.getDistance());
    }
}



