package apis.location;

import java.io.Serializable;

public class LatLng implements Serializable {

    public double lat ;
    public double lng ;

    public LatLng() { }

    public LatLng(double latitude, double longitude) {
        this.lat = latitude;
        this.lng = longitude;
    }
}