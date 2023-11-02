package br.com.hotburguer.geo;

import java.text.DecimalFormat;

public class GeoCoordinate {
    /**
     *
     */
    private static final long serialVersionUID = 201009101110L;
    /**
     * Latitude em graus
     */
    private double latitude;
    /**
     * Longitude em graus
     */
    private double longitude;

    /**
     * Construtor com latitude e longitude em graus
     */
    public GeoCoordinate() {super();}

    /**
     * Construtor com latitude e longitude em graus
     *
     * @param latitude Latitude em graus
     * @param longitude Longitude em graus
     */
    public GeoCoordinate(double latitude, double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoCoordinate(LatLng latlng) {
        super();
        this.latitude = latlng.lat;
        this.longitude = latlng.lng;
    }

    /**
     * Recupera a latitude
     * @return Latitude em graus
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Atribui uma latitude
     * @param latitude Latitude em graus
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Recupera uma longitude
     * @return Longitude em graus
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Atribui uma longitude
     * @param longitude Longitude em graus
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Distância em Quilômetros de uma coordenada para a atual
     * @param coordinate coordenada a ser calcula a distância
     * @return Distância entre as duas coordenadas
     */
    public synchronized double distanceInKm(GeoCoordinate coordinate) {
        DecimalFormat dec = new DecimalFormat("0.##");
        double d = GeoUtils.geoDistanceInKm(this, coordinate)*1.5 ;

        return Double.valueOf(dec.format(d).replace(",","."));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new GeoCoordinate(this.getLatitude(), this.getLatitude());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeoCoordinate other = (GeoCoordinate) obj;
        if (Double.doubleToLongBits(latitude) != Double
                .doubleToLongBits(other.latitude))
            return false;
        if (Double.doubleToLongBits(longitude) != Double
                .doubleToLongBits(other.longitude))
            return false;
        return true;
    }

}
