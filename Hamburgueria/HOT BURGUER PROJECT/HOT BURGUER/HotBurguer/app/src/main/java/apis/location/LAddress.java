package apis.location;

import java.io.Serializable;

public class LAddress implements Serializable {

    private String UF;
    private String contry;
    private String state;
    private String city;
    private String street;
    private String neighborhood;
    private String CEP;
    private int number;
    private double latitude, longitude;
    private String complement;
    private boolean confirmed = false;

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getfull() {

        StringBuilder f = new StringBuilder();
        f.append(street);
        f.append(", N° ");
        f.append(number);

        if (complement != null && !complement.isEmpty()) {
            f.append(" ( ");
            f.append(complement != null ? complement : "");
            f.append(" ) ");
        }
        f.append(" - ");
        f.append(neighborhood);

        if (UF != null && !UF.isEmpty()) {
            f.append(" ( ");
            f.append(UF);
            f.append(" )");
        }

        if(contry != null){
            f.append(" ");
            f.append(contry);
        }


        return f.toString();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
