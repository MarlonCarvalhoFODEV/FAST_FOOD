package br.com.megasena.itens;

public class ITService {

    private int id;
    private String name ;
    private boolean available ;
    private int expirateDays ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getExpirateDays() {
        return expirateDays;
    }

    public void setExpirateDays(int expirateDays) {
        this.expirateDays = expirateDays;
    }
}
