package br.com.comprazencasa.itens;

import java.io.Serializable;
import java.util.ArrayList;

public class ITEmail implements Serializable {

    private int id ;
    private String fromEmail ;
    private String fromName ;
    private String subject;
    private String message ;
    private ArrayList<String> destinys ;
    private String dateScheduler ;
    private InfoEmail infoEmail ;

    public ITEmail(){
        this.infoEmail = new InfoEmail();
    }

    public ITEmail(int id, String email){
        this.id = id ;
        this.fromEmail = email ;
        this.infoEmail = new InfoEmail();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getDestinys() {
        return destinys;
    }

    public void setDestinys(ArrayList<String> destinys) {
        this.destinys = destinys;
    }

    public String getDateScheduler() {
        return dateScheduler;
    }

    public void setDateScheduler(String dateScheduler) {
        this.dateScheduler = dateScheduler;
    }

    public InfoEmail getInfoEmail() {
        return infoEmail;
    }

    public class InfoEmail{
        private int idEmail ;
        private int idSender ;
        private int idBussiness ;
        private int idList ;

        private String nameEmail ;
        private String nameList ;
        private String nameBusiness ;
        private String nameSender ;

        public int getIdEmail() {
            return idEmail;
        }

        public void setIdEmail(int idEmail) {
            this.idEmail = idEmail;
        }

        public int getIdSender() {
            return idSender;
        }

        public void setIdSender(int idSender) {
            this.idSender = idSender;
        }

        public int getIdBussiness() {
            return idBussiness;
        }

        public void setIdBussiness(int idBussiness) {
            this.idBussiness = idBussiness;
        }

        public int getIdList() {
            return idList;
        }

        public void setIdList(int idList) {
            this.idList = idList;
        }

        public String getNameEmail() {
            return nameEmail;
        }

        public void setNameEmail(String nameEmail) {
            this.nameEmail = nameEmail;
        }

        public String getNameList() {
            return nameList;
        }

        public void setNameList(String nameList) {
            this.nameList = nameList;
        }

        public String getNameBusiness() {
            return nameBusiness;
        }

        public void setNameBusiness(String nameBusiness) {
            this.nameBusiness = nameBusiness;
        }

        public String getNameSender() {
            return nameSender;
        }

        public void setNameSender(String nameSender) {
            this.nameSender = nameSender;
        }
    }
}
