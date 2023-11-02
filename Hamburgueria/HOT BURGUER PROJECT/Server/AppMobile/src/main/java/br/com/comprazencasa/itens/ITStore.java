package br.com.comprazencasa.itens;

import br.com.comprazencasa.controllers.PaymentManager;
import br.com.comprazencasa.user.Endereco;

import java.io.Serializable;

public class ITStore implements Serializable {

    private int id, idCategorie, idRequisitor, type, resource ;
    private String idRecipient, storeName, categorie, cpf, cnpj;
    private byte[] logo, background ;
    private boolean selected, haveMore, favorited;
    private Endereco address ;

    private ITStoreConfiguration storeConfiguration ;

    public ITStore(){
        this.address = new Endereco();
        this.storeConfiguration = new ITStoreConfiguration();
    }

    public ITStore(int type, int resource, String categorie){
        this.type = type ;
        this.resource = resource ;
        this.categorie = categorie ;
    }

    public ITStore(int type){
        this.type = type ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public int getIdRequisitor() {
        return idRequisitor;
    }

    public void setIdRequisitor(int idRequisitor) {
        this.idRequisitor = idRequisitor;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public boolean isSelected() {
        return selected;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public byte[] getBackground() {
        return background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public Endereco getAddress() {
        return address;
    }

    public ITStoreConfiguration getStoreConfiguration() {
        return storeConfiguration;
    }
}
