package br.com.comprazencasa.itens;

public class ITListShopping {
    private int id ;
    private String title ;
    private int idLastProduct;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIdLastProduct(int id) {
        this.idLastProduct = id ;
    }

    public int getIdLastProduct() {
        return idLastProduct ;
    }
}
