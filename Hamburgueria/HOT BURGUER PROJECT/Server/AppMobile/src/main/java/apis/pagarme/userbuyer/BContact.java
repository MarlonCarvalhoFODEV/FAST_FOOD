package apis.pagarme.userbuyer;

import java.io.Serializable;

public class BContact implements Serializable {

    private int id ;
    private String email;
    private BPhone phone ;

    public BContact(){
        phone = new BPhone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BPhone getPhone() {
        return phone;
    }

    public void setPhone(BPhone phone) {
        this.phone = phone;
    }
}
