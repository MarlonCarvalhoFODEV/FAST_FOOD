package apis.address;

public class AddressAPI {
    private String street ;
    private String neighborhood ;
    private String city ;
    private String contry ;
    private String UF ;
    private String CEP ;
    private int number ; 

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
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

    public String getCompleteAddress(){
        StringBuilder add = new StringBuilder();
        add.append(street);
        add.append(", ");
        add.append(number);
        add.append(" - ");
        add.append(neighborhood);
        add.append(", ");
        add.append(city);
        add.append(" - ");
        add.append(UF);
        add.append(", ");
        add.append(CEP);
        add.append(", ");
        add.append(contry);
        return add.toString();
    }
}