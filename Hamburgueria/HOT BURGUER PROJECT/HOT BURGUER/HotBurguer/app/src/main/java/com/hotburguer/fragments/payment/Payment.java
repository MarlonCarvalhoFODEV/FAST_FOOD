package com.hotburguer.fragments.payment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Payment implements Serializable {

    public static final int PAID = 0;
    public static final int PROCESSING = 1;
    public static final int CHARGEBACK = 2;
    public static final int REFUSED = 3;
    public static final int PENDING_REFUND = 4;
    public static final int WAITING_PAYMENT = 5;
    public static final int REFUNDED = 6;
    public static final int AUTHORIZED = 7;
    public static final int POSTBACK_URL = 8;
    public static final int ERROR = 9;

    public static final int REQUEST_CODE = 413;
    public static final int RESULT_CODE = 444;
    public static final String INTENT_KEY = "payment_result";

    private int id, idIcon, status;
    private String msgAlert, msgComplement, backgroundColor;
    private boolean blocked ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsgAlert() {
        return msgAlert;
    }

    public void setMsgAlert(String msgAlert) {
        this.msgAlert = msgAlert;
    }

    public String getMsgComplement() {
        return msgComplement;
    }

    public void setMsgComplement(String msgComplement) {
        this.msgComplement = msgComplement;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public static class Status{
        public static final String PROCESSING = "processing";
        public static final String POSTBACK_URL = "postback_url";
        public static final String AUTHORIZED = "authorized";
        public static final String PAID = "paid";
        public static final String REFUSED = "refused";
        public static final String REFUNDED = "refunded";
        public static final String CHARGEDBACK = "chargedback";
        public static final String ANALYZING = "analyzing";
        public static final String PENDING_REVIEW = "pending_review";
        public static final String DELIVERED = "delivered";

        private static Map<String, String> messageStatus = new HashMap<>();
        private static Map<Integer, String> idStatus = new HashMap<>();

        public static String getMessageFrom(String status){
            messageStatus.clear();
            messageStatus.put(PROCESSING,"Transação está em processo de autorização.");
            messageStatus.put(POSTBACK_URL,"");
            messageStatus.put(AUTHORIZED,"Transação foi autorizada. Você possui saldo na conta e este valor foi reservado para futura captura.");
            messageStatus.put(PAID,"Pagamento realizado com sucesso!");
            messageStatus.put(REFUNDED,"Transação estornada completamente.");
            messageStatus.put(REFUSED,"Transação recusada, não autorizada.");
            messageStatus.put(CHARGEDBACK,"Transação sofreu chargeback.");
            messageStatus.put(ANALYZING,"Transação encaminhada para a análise manual feita por um especialista em prevenção a fraude.");
            messageStatus.put(PENDING_REVIEW,"Transação pendente de revisão manual por parte do lojista. Uma transação ficará com esse status por até 48 horas corridas.");
            return messageStatus.get(status.toLowerCase());
        }

        public static String getStatusFrom(int status){

            idStatus.clear();
            idStatus.put(Payment.PROCESSING,"in_process");
            idStatus.put(Payment.PENDING_REFUND,"pending");
            idStatus.put(Payment.POSTBACK_URL, POSTBACK_URL);
            idStatus.put(Payment.AUTHORIZED, "authorized");
            idStatus.put(Payment.PAID, "paid");
            idStatus.put(Payment.REFUNDED, "refunded");
            idStatus.put(Payment.REFUSED, "rejected");
            idStatus.put(Payment.CHARGEBACK, "charged_back");
            return idStatus.get(status);
        }

        public static String getMsgAlert(String status){
            messageStatus.clear();
            messageStatus.put(PROCESSING,"Em Processamento");
            messageStatus.put(AUTHORIZED,"Autorizado");
            messageStatus.put(PAID,"Obrigado");
            messageStatus.put(REFUNDED, "Devolvido");
            messageStatus.put(REFUSED, "Recusado");
            messageStatus.put(CHARGEDBACK, "Estornado");
            return messageStatus.get(status.toLowerCase());
        }

        public static class Billet{

            private static Map<String, String> messageStatus = new HashMap<>();
            public static final String PAID = "paid";
            public static final String PEDING_REFUND = "pending_refund";
            public static final String WAIT_PAYMENT = "waiting_payment";

            public String getMessageFromStatus(String status){
                messageStatus.clear();
                messageStatus.put(PAID,"Transação paga. Foi autorizada e capturada com sucesso.");
                messageStatus.put(PEDING_REFUND,"Transação do tipo Boleto e que está aguardando confirmação do estorno solicitado");
                messageStatus.put(WAIT_PAYMENT,"Transação aguardando pagamento (status válido para Boleto bancário).");
                return messageStatus.get(status.toLowerCase());
            }
        }
    }
}
