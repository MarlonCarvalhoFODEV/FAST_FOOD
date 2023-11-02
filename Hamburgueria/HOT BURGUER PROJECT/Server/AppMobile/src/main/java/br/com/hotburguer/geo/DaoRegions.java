package br.com.hotburguer.geo;

import java.text.DecimalFormat;

public class DaoRegions {
    public static synchronized double calculateDistanceKM(double startLat, double startLng, double andLat, double andLng){
        GeoCoordinate geoStart = new GeoCoordinate(new LatLng(startLat, startLng));
        GeoCoordinate geoFinal = new GeoCoordinate(new LatLng(andLat, andLng));

        return geoStart.distanceInKm(geoFinal);
    }

    public static synchronized double calculateDistance(double startLat, double startLng, double andLat, double andLng, String unit) {
        return distance(startLat, startLng, andLat, andLng,unit);
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }

            DecimalFormat dec = new DecimalFormat("0.##");

            return Double.valueOf(dec.format(dist).replace(",", "."));
        }
    }
}
