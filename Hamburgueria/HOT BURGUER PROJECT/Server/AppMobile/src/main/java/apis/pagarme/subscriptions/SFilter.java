package apis.pagarme.subscriptions;

import java.io.Serializable;

public class SFilter implements Serializable {
    private int countPage, page;

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
