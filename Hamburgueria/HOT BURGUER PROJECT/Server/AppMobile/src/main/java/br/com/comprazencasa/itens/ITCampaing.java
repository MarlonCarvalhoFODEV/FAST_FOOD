package br.com.comprazencasa.itens;

import java.io.Serializable;

public class ITCampaing implements Serializable {

    private int id,idStore, idProduct,type, discount;
    private boolean isPercentual, showForUsersRegistereds;
    private String headline, content, cta, exibitionDate, startDate, stopDate,createdDate, urlImage ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isPercentual() {
        return isPercentual;
    }

    public void setPercentual(boolean percentual) {
        isPercentual = percentual;
    }

    public boolean isShowForUsersRegistereds() {
        return showForUsersRegistereds;
    }

    public void setShowForUsersRegistereds(boolean showForUsersRegistereds) {
        this.showForUsersRegistereds = showForUsersRegistereds;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getExibitionDate() {
        return exibitionDate;
    }

    public void setExibitionDate(String exibitionDate) {
        this.exibitionDate = exibitionDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
