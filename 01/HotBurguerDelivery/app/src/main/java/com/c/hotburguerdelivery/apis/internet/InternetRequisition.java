package com.c.hotburguerdelivery.apis.internet;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HttpsURLConnection;

import apis.encrypt.AES;
import apis.encrypt.RSA;

public class InternetRequisition {

    private String id;
    private InternetRequisitionResponse response;
    private String domain, controller, function;
    private Object objectTransport;

    private int typeRequest;
    private int useCryptographToSend;
    private int useCryptographToReceiver;

    private WebJson jsonRequest;
    private CryptographRSA cryptographRSA;
    private CryptographAES cryptographAES;
    private static InternetRequisitionAPI api ;

    public InternetRequisition() {
        this.jsonRequest = new WebJson();
    }

    public InternetRequisition(int typeRequest, InternetRequisitionResponse newResponse) {
        this.response = newResponse;
        this.typeRequest = typeRequest;
        this.jsonRequest = new WebJson();
    }

    public InternetRequisition(int typeRequest) {
        this.typeRequest = typeRequest;
        this.jsonRequest = new WebJson();
    }

    public InternetRequisition(InternetRequisitionAPI api) {
        this.api = api ;
        this.jsonRequest = new WebJson(api);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getController() {
        return controller;
    }

    public String getFunction() {
        return function;
    }

    public Object getObjectTransport() {
        return objectTransport;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setObjectTransport(Object objectTransport) {
        this.objectTransport = objectTransport;
    }

    public int getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(int typeRequest) {
        this.typeRequest = typeRequest;
    }

    public void setUseCryptographToSend(int typeCriptograph) {
        this.useCryptographToSend = typeCriptograph;
    }

    public void setUseCryptographToReceiver(int typeCriptograph) {
        this.useCryptographToReceiver = typeCriptograph;
    }

    public void setCryptographRSA(CryptographRSA cryptographRSA) {
        this.cryptographRSA = cryptographRSA;
    }

    public void setCryptographAES(CryptographAES cryptographAES) {
        this.cryptographAES = cryptographAES;
    }

    public InternetRequisitionResponse getResponse() {
        return response;
    }

    public void setResponse(InternetRequisitionResponse response) {
        this.response = response;
    }

    public synchronized void resquest(Context context) {
        response.onLoading(true);

        StringBuilder sb = new StringBuilder();
        sb.append(domain);
        sb.append(controller);
        sb.append(function);

        new Thread() {
            @Override
            public void run() {

                switch (typeRequest) {
                    case TypeRequest.GET: {

                        String json = null;

                        if (id != null)
                            json = jsonRequest.getJson(id, sb.toString());
                        else
                            json = jsonRequest.getJson(sb.toString());

                        Object object = null;
                        if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                            object = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                            if (json != null) object = cryptographAES.decryptToJson(json);
                        }

                        Object finalV = object;
                        ((Activity) context).runOnUiThread(() -> {
                            response.onSucess(finalV);
                            response.onLoading(false);
                        });
                        break;
                    }
                    case TypeRequest.POST: {
                        try {

                            String sendJson = null;
                            if (useCryptographToSend == TypeCryptograph.RSA && cryptographRSA != null) {
                                sendJson = cryptographRSA.encryptToJson(objectTransport);
                            } else if (useCryptographToSend == TypeCryptograph.AES && cryptographAES != null) {
                                sendJson = cryptographAES.encryptToAESJson(objectTransport);
                            }

                            String json = null;
                            if (id != null) {

                                String activity = ((Activity) context).getClass().getSimpleName();
                                json = jsonRequest.jsonRequest(activity, id, "POST", sb.toString(), sendJson);
                            } else
                                json = jsonRequest.jsonRequest("POST", sb.toString(), sendJson);

                            Object obj = null;
                            if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                                if (json != null)
                                    obj = json;
                            } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                                if (json != null)
                                    obj = cryptographAES.decryptToJson(json);
                            }

                            Object finalObj = obj;
                            ((Activity) context).runOnUiThread(() -> {
                                response.onSucess(finalObj);
                                response.onLoading(false);
                            });
                        } catch (Exception e) {
                            ((Activity) context).runOnUiThread(() -> {
                                response.onError(e);
                                response.onLoading(false);
                            });
                        }

                        break;
                    }
                    case TypeRequest.PUT: {

                        break;
                    }
                    case TypeRequest.UPDATE: {

                        break;
                    }
                    case TypeRequest.DELETE: {

                        break;
                    }
                }
            }
        }.start();
    }

    public synchronized String resquest(String activityRequestName) {
        if (response != null)
            response.onLoading(true);

        StringBuilder sb = new StringBuilder();
        sb.append(domain);
        sb.append(controller);
        sb.append(function);

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> futureResponse = service.submit(new Callable<String>() {
            @Override
            public String call() {
                switch (typeRequest) {
                    case TypeRequest.GET: {

                        String json = null;

                        if (id != null)
                            json = jsonRequest.getJson(id, sb.toString());
                        else
                            json = jsonRequest.getJson(sb.toString());

                        Object object = null;
                        if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                            object = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                            if (json != null) object = cryptographAES.decryptToJson(json);
                        }

                        return object.toString();
                    }
                    case TypeRequest.POST: {
                        try {
                            String sendJson = null;
                            if (useCryptographToSend == TypeCryptograph.RSA && cryptographRSA != null) {
                                sendJson = cryptographRSA.encryptToJson(objectTransport);

                            } else if (useCryptographToSend == TypeCryptograph.AES && cryptographAES != null) {
                                sendJson = cryptographAES.encryptToAESJson(objectTransport);
                            }

                            String json = null;
                            if (id != null) {

                                json = jsonRequest.jsonRequest(activityRequestName, id, "POST", sb.toString(), sendJson);
                            } else
                                json = jsonRequest.jsonRequest("POST", sb.toString(), sendJson);

                            Object obj = null;
                            if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                                if (json != null)
                                    obj = json;
                            } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                                if (json != null)
                                    obj = cryptographAES.decryptToJson(json);
                            }

                            return obj.toString();
                        } catch (Exception e) {
                            api.callbackExceptions(e);
                        }

                        break;
                    }
                    case TypeRequest.PUT: {

                        break;
                    }
                    case TypeRequest.UPDATE: {

                        break;
                    }
                    case TypeRequest.DELETE: {

                        break;
                    }
                }

                return null;
            }
        });

        String response = null;
        try {
            response = futureResponse.get();
        } catch (ExecutionException e) {
            api.callbackExceptions(e);
        } catch (InterruptedException e) {
            api.callbackExceptions(e);
        }

        return response;
    }

    public void onDestroyConnections(String activitySimpleName) {
        jsonRequest.onDestroyConnections(activitySimpleName);
    }

    public void disconnect() {
        HttpURLConnection con = jsonRequest.getConnections().get(id);
        HttpsURLConnection conSSl = jsonRequest.getConnectionsSSL().get(id);

        if (con != null)
            con.disconnect();

        if (conSSl != null)
            conSSl.disconnect();
    }

    public static class TypeRequest {
        public static final int GET = 0;
        public static final int POST = 1;
        public static final int PUT = 2;
        public static final int DELETE = 3;
        public static final int UPDATE = 4;
    }

    public static class CryptographRSA {

        private RSA rsa;

        public CryptographRSA(String publicKeyBASE64) {
            try {
                byte[] objectBytes = Base64.decode(publicKeyBASE64, Base64.DEFAULT);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(objectBytes));
                rsa = new RSA(publicKey, null);
            } catch (NoSuchAlgorithmException e) {
                api.callbackExceptions(e);
            } catch (InvalidKeySpecException e) {
                api.callbackExceptions(e);
            }
        }

        public final byte[] encrypt(Object object) {
            try {
                return rsa.encrypt(new Gson().toJson(object));
            } catch (InvalidKeyException e) {
                api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                api.callbackExceptions(e);
            }
            return null;
        }

        public final String encryptToJson(Object object) {
            try {
                return rsa.encryptToJson(object);
            } catch (InvalidKeyException e) {
                api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                api.callbackExceptions(e);
            }
            return null;
        }
    }

    public static class CryptographAES {

        private AES aes;

        public CryptographAES(String key) {
            aes = new AES();
            aes.setKey(key);
        }

        public byte[] encrypt(Object object) {
            try {
                return aes.encrypt(new Gson().toJson(object));
            } catch (InvalidKeyException e) {
                api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                api.callbackExceptions(e);
            }
            return null;
        }

        public String encryptToAESJson(Object object) {
            try {
                return aes.encryptToAESJson(object);
            } catch (InvalidKeyException e) {
                api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                api.callbackExceptions(e);
            }
            return null;
        }

        public Object decrypt(byte[] objectEncryptedJson) {
            Gson gson = new Gson();
            //byte[] buyerJsonCrypt = gson.fromJson(objectEncryptedJson, byte[].class);
            String buyerJson = null;
            try {
                buyerJson = new String(AES.decrypt(objectEncryptedJson));
            } catch (InvalidKeyException e) {
                api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                api.callbackExceptions(e);
            }
            return gson.fromJson(buyerJson, Object.class);
        }

        public String decryptToJson(String objectEncryptedJson) {
            try {
                return AES.decryptToObject(objectEncryptedJson);
            } catch (InvalidKeyException e) {
                api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                api.callbackExceptions(e);
            }
            return null;
        }
    }

    public static class TypeCryptograph {
        public static final int RSA = 1;
        public static final int AES = 2;
    }
}
