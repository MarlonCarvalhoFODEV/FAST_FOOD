package apis;

import br.com.comprazencasa.itens.ITEmail;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

public class APIEmailServer {

    private Session session ;
    private PropertiesServer propertiesServer ;
    public static final String SERVER_NAME = "OLink Redirect";
    public static final String SERVER_MAIL = "email-olink@rendalivre.com";

    public APIEmailServer(){
        this.propertiesServer = new PropertiesServer();
        //serviceEmailScheduler = new ServiceEmailScheduler();
    }

    public static APIEmailServer getInstance(String authEmail, String authPass, String smtpHost){
        APIEmailServer apiEmailServer = new APIEmailServer();
        apiEmailServer.setAuthentication(authEmail,authPass);
        apiEmailServer.getPropertiesServer().setSmtpHost(smtpHost);
        apiEmailServer.getPropertiesServer().setSmtpPort("465");
        apiEmailServer.getPropertiesServer().setSmtpSocketFactoryPort("465");
        apiEmailServer.getPropertiesServer().setSSLSocketFactory(true);
        return apiEmailServer;
    }

    public void setAuthentication(String email, String password){
        propertiesServer.setSmtpAuth(email, password);
    }

    public boolean sendEmail(ITEmail email, String emailsDestinysSepareteToComma) {
        try {
            session = Session.getDefaultInstance(propertiesServer.properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication("email@crushideal.com","M@rlon7832@");
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFromEmail(), email.getFromName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailsDestinysSepareteToComma));
            message.setSubject(email.getSubject());//Assunto

            if(email.getMessage() != null && email.getMessage().contains("html")){
                message.setContent(email.getMessage(),"text/html");
            } else {
                message.setText(email.getMessage());
            }
            /**Método para enviar a mensagem criada*/
            Transport.send(message);

            return true ;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false ;
    }

    public boolean sendEmail(String subject, String msg, ArrayList<String> destinys) {

        ITEmail email = new ITEmail();
        email.setFromName(APIEmailServer.SERVER_NAME);
        email.setFromEmail(APIEmailServer.SERVER_MAIL);
        email.setMessage(msg);
        email.setSubject(subject);

        try {
            session = Session.getDefaultInstance(propertiesServer.properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication("email@crushideal.com","M@rlon7832@");
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFromEmail(), email.getFromName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EmailUtil.extractEmails(destinys)));
            message.setSubject(email.getSubject());//Assunto

            if(email.getMessage() != null && email.getMessage().contains("html")){
                message.setContent(email.getMessage(),"text/html");
            } else {
                message.setText(email.getMessage());
            }
            /**Método para enviar a mensagem criada*/
            Transport.send(message);

            return true ;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false ;
    }

    public boolean sendEmail(String subject, String msg, String destiny) {

        ITEmail email = new ITEmail();
        email.setFromName(APIEmailServer.SERVER_NAME);
        email.setFromEmail(APIEmailServer.SERVER_MAIL);
        email.setMessage(msg);
        email.setSubject(subject);

        try {
            session = Session.getDefaultInstance(propertiesServer.properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication(propertiesServer.email,propertiesServer.credencials);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFromEmail(), email.getFromName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destiny));
            message.setSubject(email.getSubject());//Assunto

            if(email.getMessage() != null && email.getMessage().contains("html")){
                message.setContent(email.getMessage(),"text/html");
            } else {
                message.setText(email.getMessage());
            }
            /**Método para enviar a mensagem criada*/
            Transport.send(message);

            return true ;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false ;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public PropertiesServer getPropertiesServer() {
        return propertiesServer;
    }


    public void setSmtHost(String authEmail, String authPass, String smtpHost) {
        setAuthentication(authEmail,authPass);
        getPropertiesServer().setSmtpHost(smtpHost);
        getPropertiesServer().setSmtpPort("465");
        getPropertiesServer().setSmtpSocketFactoryPort("465");
        getPropertiesServer().setSSLSocketFactory(true);
    }

    public class PropertiesServer{

        private String email, credencials ;
        private Properties properties ;

        public PropertiesServer(){
            properties = new Properties();
        }

        public void setSmtpHost(String smtpHost){
            properties.put("mail.smtp.host", smtpHost);
        }

        public void setSmtpSocketFactoryPort(String smtpPort){
            properties.put("mail.smtp.socketFactory.port", smtpPort);
        }

        public void setSmtpPort(String smtpPort){
            properties.put("mail.smtp.port", smtpPort);
        }

        public void setSSLSocketFactory(boolean sslSocketFactory){
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", String.valueOf(sslSocketFactory));
        }

        private void setSmtpAuth(String email, String password){
            this.email = email ;
            this.credencials = password ;

            if (email != null && password != null){
                session = Session.getDefaultInstance(properties,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication()
                            {
                                return new PasswordAuthentication(email,password);
                            }
                        });
            }
        }
    }

    public static class EmailUtil{

        public static String extractEmails(ITEmail email){
            StringBuilder sb = new StringBuilder();

            int counter = 0 ;
            for(String a : email.getDestinys()){
                counter++;

                if(counter > 1)sb.append(",");

                sb.append(a);
            }

            return sb.toString();
        }

        public static String extractEmails(ArrayList<String> destinys){
            StringBuilder sb = new StringBuilder();

            int counter = 0 ;
            for(String a : destinys){
                counter++;

                if(counter > 1)sb.append(",");

                sb.append(a);
            }

            return sb.toString();
        }
    }
}
