package br.com.hotburguer.controllers;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import apis.onesignal.OneSignalResponse;
import apis.pagarme.recipient.RRecipient;
import apis.pagarme.userbuyer.BAccountBank;
import apis.pagarme.userbuyer.BDocument;
import apis.pagarme.userbuyer.BPhone;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.hotburguer.daos.*;
import br.com.hotburguer.itens.*;
import com.google.gson.Gson;
import me.pagar.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.*;

@RestController
@RequestMapping("hot-deliveryman")
public class HotDeliveryManager {

    private static final Map<Integer, List<Integer>> rejecteds = new HashMap<>();
    private PrivateKey pKey;

    public HotDeliveryManager() {
        pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
    }

    /* @PostMapping(path = "/login")
     @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
     @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
     public String login(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
         String json = AES.decryptToJson(request);

         HotDeliveryMan deliveryMan = new Gson().fromJson(json, HotDeliveryMan.class);
         deliveryMan.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));

         HotDeliveryMan delivery = new DaoHotDeliveryMan().login(deliveryMan);

         return AES.encryptToAESJson(delivery);
     }
 */
    @PostMapping(path = "/new-email")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String newEmail(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        byte[] bytes = request.getBytes();

        String json = new RSA(null, pKey, 1024).decryptFromJson(new String(bytes));

        JSONObject object = new JSONObject(json);
        object = (object.getJSONObject("nameValuePairs"));

        String name = object.getString("name");
        String email = object.getString("email");
        String idDevice = object.getString("id_device");
        String hashKey = object.getString("hash_key");
        String socialLogin = object.getString("social_login");

        HotDeliveryMan deliveryMan = new HotDeliveryMan();
        deliveryMan.setName(name);
        deliveryMan.setEmail(email);
        deliveryMan.setIdDevice(idDevice);
        deliveryMan.setHashKey(hashKey);
        deliveryMan.setSocialLogin(socialLogin);
        deliveryMan.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));

        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        deliveryMan = dao.newEmail(deliveryMan);

        if (deliveryMan.getId() == 0) {
            deliveryMan = dao.login(deliveryMan);
        }

        return AES.encryptToAESJson(deliveryMan);
    }

    @PostMapping(path = "/new-register")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String newRegister(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        String json = new RSA(null, pKey, 1024).decryptFromJson(request);

        JSONObject object = new JSONObject(json);
        object = (object.getJSONObject("nameValuePairs"));

        int idUser = object.has("id") ? object.getInt("id") : 0;
        String idDevice = object.has("id_dev") ? object.getString("id_dev") : null;
        String name = object.has("name") ? object.getString("name") : null;
        String dateOfBirth = object.has("d_birth") ? object.getString("d_birth") : null;
        String cpf = object.has("cpf") ? object.getString("cpf") : null;
        String cel = object.has("cel") ? object.getString("cel") : null;
        String baAccount = object.has("b_ac") ? object.getString("b_ac") : null;
        String baAccount_dv = object.has("b_ac_dv") ? object.getString("b_ac_dv") : null;
        String baAgency = object.has("b_ag") ? object.getString("b_ag") : null;
        String baAgencyDV = object.has("b_ag_dv") ? object.getString("b_ag_dv") : null;
        String baBankCode = object.has("b_c") ? object.getString("b_c") : null;
        String baName = object.has("b_n") ? object.getString("b_n") : null;
        String operationLocal = object.has("lo") ? object.getString("lo") : null;

        int baType = object.has("b_t") ? object.getInt("b_t") : 0;

        if (idUser > 0 && idDevice != null && name != null && dateOfBirth != null && cpf != null
                && cel != null) {
            BPhone phone = new BPhone();
            phone.setFullNumber("+55" + cel);

            HotDeliveryMan deliveryMan = new HotDeliveryMan();
            deliveryMan.setId(Integer.valueOf(idUser));
            deliveryMan.setIdDevice(idDevice);
            deliveryMan.setName(name);
            deliveryMan.setDateOfBirth(dateOfBirth);
            deliveryMan.getDocument().setCPF(cpf);
            deliveryMan.getDocument().setType(BDocument.Type.CPF);
            deliveryMan.getContact().setPhone(phone);
            deliveryMan.getAddress().setNeighborhood(operationLocal);
            deliveryMan.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));

            deliveryMan = new DaoHotDeliveryMan().update(deliveryMan);

            JSONObject response = new JSONObject();
            response.put("id", deliveryMan.getId());
            response.put("result", (deliveryMan.getId() > 0));
            response.put("result_code", deliveryMan.getResult());

            return AES.encryptToAESJson(response);
        } else if (baAccount != null && baAccount_dv != null && baAgency != null && baAgencyDV != null
                && baBankCode != null) {
            BDocument document = new BDocument();
            document.setType(BDocument.Type.CPF);
            document.setCPF(cpf);

            BAccountBank accountBank = new BAccountBank();
            accountBank.setAccount(baAccount);
            accountBank.setAccountDv(baAccount_dv);
            accountBank.setAgency(baAgency);
            accountBank.setAgencyDv(baAgencyDV);
            accountBank.setBankCode(baBankCode);
            accountBank.setLegalName(name);
            accountBank.setDocument(document);
            accountBank.setType(baType);
            accountBank.setDefaultAccount(true);

            DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
            if (!dao.checkBankAccountExist(idUser, accountBank)) {

                RRecipient recipient = new DaoSystemConfiguration().getPaymentConfig().getRecipient();

                Recipient recipientResponse = new DaoPagarme().getRecipient().createRecipient(recipient, accountBank);
                BankAccount bankAccount = recipientResponse.getBankAccount();

                accountBank.setId(bankAccount.getId());
                accountBank.setName(baName);

                HotDeliveryMan deliveryMan = new HotDeliveryMan();
                deliveryMan.setId(idUser);
                deliveryMan.setIdRecipient(recipientResponse.getId());
                deliveryMan.setIdDevice(idDevice);
                deliveryMan.setBankAccount(accountBank);
                deliveryMan.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));

                DaoHotDeliveryMan daoDeliveryman = new DaoHotDeliveryMan();
                daoDeliveryman.createBankAccount(idUser, accountBank);
                daoDeliveryman.update(deliveryMan);

                JSONObject response = new JSONObject();
                response.put("id_recipient", recipientResponse.getId());
                response.put("result", (recipientResponse.getId() != null));
                response.put("result_code", deliveryMan.getResult());

                return AES.encryptToAESJson(response);
            } else {

                JSONObject response = new JSONObject();
                response.put("id_recipient", "");
                response.put("result", true);

                return AES.encryptToAESJson(response);
            }
        }

        //String baLegalName = object.getString("ba_legal_name");
        //String photoFrontAES = object.getString("photo_front");
        //String photoVerseAES = object.getString("photo_verse");

       /* System.out.println("ID: "+idUser);
        System.out.println("ID DEVICE: "+idDevice);
        System.out.println("NAME: "+name);
        System.out.println("DATE OF BRITH"+dateOfBirth);
        System.out.println("CPF: "+cpf);
        System.out.println("CEL: "+cel);
        System.out.println("ACCOUNT: "+baAccount);
        System.out.println("ACCOUNT DV"+baAccount_dv);
        System.out.println("AGENCY: "+baAgency);
        System.out.println("AGENCY DV: "+baAgencyDV);
        System.out.println("CODE: "+baBankCode);
        System.out.println("TYPE: "+baType);*/


        return AES.encryptToAESJson(new JSONObject());
    }

    @PostMapping(path = "/get-achievements")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getAchivements(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        String json = new RSA(null, pKey, 1024).decryptFromJson(request);

        JSONObject object = new JSONObject(json);
        object = (object.getJSONObject("nameValuePairs"));

        int idDeliveryman = object.has("id") ? object.getInt("id") : 0;
        String idDevice = object.has("id_dev") ? object.getString("id_dev") : null;
        String idRecipient = object.has("id_rec") ? object.getString("id_rec") : null;
        String ip = (httpServletRequest.getHeader("X-FORWARDED-FOR"));

        DaoHotDeliveryMan daoDelivery = new DaoHotDeliveryMan();
        DaoPagarme dao = new DaoPagarme();
        Balance balance = dao.getRecipient().getBalanceRecipient(idRecipient);

        JSONObject response = new JSONObject();
        response.put("result", (balance != null));
        response.put("running", daoDelivery.getCountRunnings(idDeliveryman, idDevice, idRecipient, ip));
        response.put("receivable", balance.getWaitingFunds().getAmount());
        response.put("available", balance.getAvailable().getAmount());
        response.put("transferred", balance.getTransferred().getAmount());

        return AES.encryptToAESJson(response);
    }


    @PostMapping(path = "/register-photo")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String registerPhoto(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        String json = AES.decryptToJson(request);

        PhotoCompat photoCompat = new Gson().fromJson(json, PhotoCompat.class);
        photoCompat.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));

        boolean result = new DaoHotDeliveryMan().updatePhotos
                (
                        photoCompat.getId(),
                        photoCompat.getProfile(),
                        photoCompat.getFrontPhoto(),
                        photoCompat.getVersePhoto(),
                        photoCompat.getIdDevice(),
                        photoCompat.getIpAccess()
                );

        JSONObject response = new JSONObject();
        response.put("result", result ? "saved_succefully" : "saved_failed");

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/update-photo-profile")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String updatePhotoProfile(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        String json = AES.decryptToJson(request);

        PhotoCompat photoCompat = new Gson().fromJson(json, PhotoCompat.class);
        photoCompat.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));
        boolean result = new DaoHotDeliveryMan().updatePhotos
                (
                        photoCompat.getId(),
                        photoCompat.getProfile(),
                        photoCompat.getFrontPhoto(),
                        photoCompat.getVersePhoto(),
                        photoCompat.getIdDevice(),
                        photoCompat.getIpAccess()
                );

        JSONObject response = new JSONObject();
        response.put("result", result ? "saved_succefully" : "saved_failed");

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/check-status")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String checkStatus(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : "";
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : "";
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        String status = new DaoHotDeliveryMan().getStatus(idUser, idDevice, idRecipient, publicIp);

        JSONObject response = new JSONObject();
        response.put("result_status", status);

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/get-performances")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getPerformances(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        HotPanelHome dayLy = dao.getPerformancesDayly(idUser, idDevice, idRecipient, publicIp);
        HotPanelHome weekLy = dao.getPerformancesWeekly(idUser, idDevice, idRecipient, publicIp);
        HotPanelHome monthLy = dao.getPerformancesMonthly(idUser, idDevice, idRecipient, publicIp);

        ArrayList<HotPanelHome> panels = new ArrayList<>();
        panels.add(dayLy);
        panels.add(weekLy);
        panels.add(monthLy);

        return AES.encryptToAESJson(panels);
    }

    @PostMapping(path = "/update-status-operation")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String updateStatusOperation(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");
        boolean statusOperation = c.getBoolean("activity_status");

        boolean status = new DaoHotDeliveryMan().updateStatusOperation(idUser, idDevice, idRecipient, publicIp, statusOperation);

        JSONObject response = new JSONObject();
        response.put("result", status);

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/accept-runnig")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String acceptRunning(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;

        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        ArrayList<Integer> idOrders = new ArrayList<>();

        JSONArray idOrdersJson = c.getJSONObject("id_orders").getJSONArray("values");

        for (int i = 0; i < idOrdersJson.length(); i++) {
            idOrders.add(idOrdersJson.getInt(i));
        }

        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        boolean status = dao.acceptRunning(idUser, idOrders);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       /* ArrayList<LAddress> coordinates = null;
        if (status) {
            coordinates = new DaoHotDeliveryMan().getAddressRunning(idUser, idDevice, publicIp);
        }
*/
        ArrayList<HotDelivery> deliverys = null;
        if (status) {
            deliverys = new DaoHotDeliveryMan().getRunning(idUser, idDevice, publicIp, idOrdersJson);
        }

        return AES.encryptToAESJson(deliverys);
    }

    @PostMapping(path = "/reject-runnig")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String rejectRunning(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        JSONObject data = c.has("data") ? c.getJSONObject("data") : null;
        data = data.getJSONObject("nameValuePairs");

        JSONArray idOrders = data.getJSONObject("id_orders").getJSONArray("values");

        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        List<Integer> listOrdersRejecteds = new ArrayList<>();

        for(int i=0; i < idOrders.length(); i++)
            listOrdersRejecteds.add(idOrders.getInt(i));
        rejecteds.put(idUser, listOrdersRejecteds);

        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();

        ArrayList<Integer> ids = dao.selectDeliveryManForDelivery(idUser, 1);

        String title = "Nova Corrida...";
        String subTitle = "Vamos fazer dinheiro?";
        String message = "Vamos fazer dinheiro?";

        OneSignalResponse resultNotification = null;
        if (ids != null && ids.size() > 0) {
            resultNotification = dao.notifyDeliveryMan(title, subTitle, message, ids.get(0), data);
        } else
            resultNotification = dao.notifyDeliveryMan(title, subTitle, message, idUser, data);

        JSONObject response = new JSONObject();
        response.put("result", (resultNotification != null));

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/get-historic-delivery")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getHistoricDelivery(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        int idLastItem = c.has("id_last_item") ? c.getInt("id_last_item") : 0;

        ArrayList<HotHistoricRegister> registers = new DaoHotDeliveryMan()
                .getHistoricDelivery(idUser, idDevice, idRecipient, idLastItem, publicIp);

        return AES.encryptToAESJson(registers);
    }

    @PostMapping(path = "/arrived-collect-point")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String iArrivedAtTheCoolectionPoint(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        JSONArray idOrdersJson = c.has("id_all_orders") ? c.getJSONArray("id_all_orders") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        String name = c.has("name") ? c.getString("name") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        //dao.checkDataForNotification(idUser, idDevice, id);

        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" - Entregador\n");

        // Atualiza os pedidos Para entregador aguardando
        DaoHotOrder daoOrder = new DaoHotOrder();
        HotManager manager = new DaoHotManager().getHotManagersCentral();

        daoOrder.updateStatusOrder(idUser, idDevice, idRecipient, idOrdersJson, HotOrder.Status.DELIVERYMAN_HOLDING);

        // Cria mensagem de notificação para o manager central
        for (int i = 0; i < idOrdersJson.length(); i++) {
            int idOrder = idOrdersJson.getInt(i);
            if (idOrder > 1) {
                sb.append("Pedido ");
                sb.append(idOrder);
                if (i < idOrdersJson.length() - 1)
                    sb.append("\n");
            }
        }

        // Notifica o manager central que o entregador(es) chegou/chegaram
        OneSignalResponse responseNotification = new DaoHotNotification()
                .notifyManager("Seu Entregador Chegou!!",
                        sb.toString(),
                        sb.toString(),
                        manager.getId(), null);

        JSONObject response = new JSONObject();
        response.put("result", (responseNotification != null));

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/on-my-way")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String onMyWay(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        DaoHotOrder daoHotOrder = new DaoHotOrder();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        JSONArray idOrdersJson = c.has("id_orders") ? c.getJSONArray("id_orders") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        int[] ids = new int[idOrdersJson.length()];
        for (int i = 0; i < idOrdersJson.length(); i++) {
            int id = idOrdersJson.getInt(i);
            if (id > 0)
                ids[i] = idOrdersJson.getInt(i);
        }

        boolean check = daoHotOrder.updateStatusOrder(
                idUser,
                idDevice,
                idRecipient,
                idOrdersJson,
                HotOrder.Status.DELIVERY_ON_MY_WAY
        );

        String title = "Seu Pedido...";
        String subTitle = "Está a caminho!!";
        String message = "Está a caminho!!";

        OneSignalResponse resultNotification = null;

        if (check) {

            ArrayList<String> idsUsers = daoHotOrder.getIdsUsersFromOrdersIds(idUser, idDevice, idRecipient, ids);

            resultNotification = dao.notifyClient(title, subTitle, message, idsUsers, null);
        }

        JSONObject result = new JSONObject();
        result.put("result", (resultNotification != null));

        return AES.encryptToAESJson(result);
    }

    @PostMapping(path = "/arrived-place-client")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String arrivedPlaceClient(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        DaoHotOrder daoOrder = new DaoHotOrder();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idDeliveryman = c.has("id") ? c.getInt("id") : 0;
        int idOrder = c.has("id_order") ? c.getInt("id_order") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        boolean check = dao.checkDataForNotification(idDeliveryman, idDevice, idRecipient, idOrder);

        OneSignalResponse resultNotification = null;

        if (check) {
            HotOrder order = daoOrder.getOrderFromID(idOrder);
            order.setStatus(HotOrder.Status.ARRIVED_LOCAL_CLIENT);
            daoOrder.updateStatusOrder(order);

            ArrayList<String> ids = new ArrayList<>();
            ids.add(String.valueOf(order.getIdUser()));

            String title = "Seu Pedido...";
            String subTitle = "Chegou!!";
            String message = "Chegou!!";
            resultNotification = dao.notifyClient(title, subTitle, message, ids, null);
        }

        JSONObject result = new JSONObject();
        result.put("result", (resultNotification != null));

        return AES.encryptToAESJson(result);
    }

    @PostMapping(path = "/complete-delivery")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String completeDelivery(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        DaoHotOrder daoOrder = new DaoHotOrder();
        DaoHotManager daoManager = new DaoHotManager();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        int idOrder = c.has("id_order") ? c.getInt("id_order") : 0;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        HotOrder orderUpdated = new HotOrder();
        orderUpdated.setId(idOrder);
        orderUpdated.setStatus(HotOrder.Status.FINALIZED);

        boolean check = daoOrder.updateStatusOrder(orderUpdated);

        Transaction transaction = null;
        OneSignalResponse resultNotification = null;
        HotOrder order = null;
        if (check) {
            order = daoOrder.getOrderFromID(idOrder);

            ArrayList<String> ids = new ArrayList<>();
            ids.add(String.valueOf(order.getIdUser()));

            String title = "Seu Pedido...";
            String subTitle = "Foi Entregue!!";
            String message = "Foi entregue!!";

            resultNotification = dao.notifyClient(title, subTitle, message, ids, null);
            double feeDelivery = daoManager.getHotConfiguration().getDeliveryMan().getFeeDelivery();

            int idTransaction = order.getIdTransaction();
            transaction = new DaoPagarme()
                    .getTransactions()
                    .createTransactionCaptureSplit
                            (idTransaction, (int) (100 * order.getPurchaseValue()), idRecipient, (int) (feeDelivery * 100));

            ITOrderValidator validator = new ITOrderValidator(idOrder, 0, 0, 0);
            validator.setIdUser(order.getIdUser());
            validator.setIdTransaction(idTransaction);
            validator.setStatusPayment(transaction.getStatus().name());

            daoOrder.updateStatusPayment(validator);
            dao.saveDeliveryToHistory(idUser, idOrder, feeDelivery, publicIp);
        }

        JSONObject result = new JSONObject();
        result.put("result", (transaction != null));
        result.put("receivable", order != null ? order.getFeeDelivery() : 0);

        return AES.encryptToAESJson(result);
    }

    @PostMapping(path = "/transfer-balance")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String transferBalance(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        DaoHotOrder daoOrder = new DaoHotOrder();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idDeliveryman = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String idRecipient = c.has("id_recipient") ? c.getString("id_recipient") : null;
        int amount = c.has("amount") ? c.getInt("amount") : 0;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        DaoPagarme daoPag = new DaoPagarme();
        Transfer transfer = daoPag.getTransfers().createTransfer(idRecipient, amount);

        JSONObject result = new JSONObject();
        result.put("result", (transfer != null));
        result.put("status", (transfer != null ? transfer.getStatus().name() : null));
        return AES.encryptToAESJson(result);
    }

    @PostMapping(path = "/call-deliveryman")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String callDeliverymanNewOrders(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        /*
            Recolher dados do manager
        */
        int idManager = c.has("id") ? c.getInt("id") : 0;
        int countGroupOrders = c.has("gp_order") ? c.getInt("gp_order") : 1;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        JSONArray idOrders = c.has("id_orders") ? c.getJSONArray("id_orders") : null;
        System.out.println("CALL: "+idOrders.toString());
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        /*  Seleciona o entregador de acordo com as regras seguintes:
            Não esteja entregando, ou que terminou uma corrida e
            O entregador que mais tempo sem entregar
        */

        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        DaoHotOrder daoOrds = new DaoHotOrder();
        daoOrds.updateStatusOrderManager(idManager, idDevice, null, idOrders, HotOrder.Status.FOR_DELIVERY);

        ArrayList<Integer> idsDeliveryman = dao.selectDeliveryManForDelivery(0, countGroupOrders);

        // Notifica o entrega
        OneSignalResponse result = null ;
        boolean instantNotifyed = false;
        for (int i = 0; i < countGroupOrders && idsDeliveryman.size() > 0; i++) {
            Integer idDeliveryman = idsDeliveryman.get(i);

            result = dao.notifyDeliveryManNeoRunning(
                    DaoHotNotification.TypeNotification.Deliveryman.NEW_RUNNING,
                    idDeliveryman,
                    idOrders);

            daoOrds.updateIdDeliverymanNotifyed(idManager, idDevice, idDeliveryman, idOrders);

            Timer timer1 = new Timer();
            int finalI1 = i;
            int finalI2 = i;
            int finalI = i;
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {
                    boolean check = daoOrds.checkIfAcceptedRunning(idOrders.getInt(0));
                    if(!check && !rejecteds.get(idsDeliveryman.get(finalI)).contains(idOrders.getInt(0))){
                        dao.notifyDeliveryManNeoRunning(
                                DaoHotNotification.TypeNotification.Deliveryman.NEW_RUNNING,
                                idsDeliveryman.get(finalI2),
                                idOrders);
                    } else {
                        rejecteds.remove(idsDeliveryman.get(finalI));
                        timer1.cancel();
                    }
                }
            }, 0, 180000);

            /*if (result != null && result.getResponseCode() != 200) {

                Timer timer = new Timer();
                int finalI = i;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        OneSignalResponse result = dao.notifyDeliveryManNeoRunning(
                                idsDeliveryman.get(finalI),
                                DaoHotNotification.TypeNotification.Deliveryman.NEW_RUNNING,
                                idOrders);
                        if (result.getResponseCode() == 200)
                            timer.cancel();
                    }
                }, 0, 60000);
            } else if(result != null && result.getResponseCode() == 200){
                instantNotifyed = true;
                System.out.println("Envio Instantaneo");
            } else {
                System.out.println("Envio ERROR: "+result.getResponseCode());
            }*/
        }

        JSONObject response = new JSONObject();
        response.put("result", (result != null && result.getResponseCode() == 200));
        //result.put("status",(transfer != null?transfer.getStatus().name():null));
        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/notify-deliveryman")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String notifyDeliveryman(@RequestBody String request, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        /*
            Recolher dados do manager
        */
        int idManager = c.has("id") ? c.getInt("id") : 0;
        int idDeliveryman = c.has("id_del") ? c.getInt("id_del") : 0;
        int countGroupOrders = c.has("gp_order") ? c.getInt("gp_order") : 1;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        JSONArray idOrders = c.has("id_orders") ? c.getJSONArray("id_orders") : null;
        String publicIp = httpServletRequest.getHeader("X-FORWARDED-FOR");

        /*  Seleciona o entregador de acordo com as regras seguintes:
            Não esteja entregando, ou que terminou uma corrida e
            O entregador que mais tempo sem entregar
        */

        DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
        //DaoHotOrder daoOrds = new DaoHotOrder();
        //daoOrds.updateStatusOrderManager(idManager, idDevice, null, idOrders, HotOrder.Status.FOR_DELIVERY);

        // Notifica o entrega
        OneSignalResponse result = dao.notifyDeliveryManNeoRunning(
                DaoHotNotification.TypeNotification.Deliveryman.NEW_RUNNING,
                idDeliveryman,
                idOrders) ;

        JSONObject response = new JSONObject();
        response.put("result", (result != null && result.getResponseCode() == 200));
        //result.put("status",(transfer != null?transfer.getStatus().name():null));
        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/get-holding-deliveryman")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getHoldingDeliverymans(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        JSONArray deliverymans = new DaoHotDeliveryMan().getHoldingDeliverymans(idManager, idDevice, publicIp);

        return AES.encryptToAESJson(deliverymans);
    }

    @PostMapping(path = "/get-actives-deliveryman")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getActiveDeliverymans(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        int idDel = c.has("id_del") ? c.getInt("id_del") : 0;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        JSONArray deliverymans = new DaoHotDeliveryMan().getActivesDeliverymans(idManager, idDevice, idDel, publicIp);

        return AES.encryptToAESJson(deliverymans);
    }

    @PostMapping(path = "/get-queue-deliveryman")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getQueueDeliverymans(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        int idDel = c.has("id_del") ? c.getInt("id_del") : 0;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        JSONArray deliverymans = new DaoHotDeliveryMan().getQueueDeliverymans(idManager, idDevice, idDel, publicIp);

        return AES.encryptToAESJson(deliverymans);
    }

    @PostMapping(path = "/active-deliveryman")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String activeDeliveryman(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        int idDel = c.has("id_del") ? c.getInt("id_del") : 0;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        boolean result = new DaoHotDeliveryMan().activeDeliveryman(idManager, idDevice, idDel, publicIp);
        if(result){

            String title = "Parabéns!!";
            String message = "Sua conta está ativa!!!";

            DaoHotDeliveryMan dao = new DaoHotDeliveryMan();
            dao.notifyDeliveryMan(title, message, message, idDel,null);
        }


        JSONObject response = new JSONObject();
        response.put("result",result);

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/search-city")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String searchCity(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String search = c.has("s") ? c.getString("s") : null;
        int idDel = c.has("id_del") ? c.getInt("id_del") : 0;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        ArrayList<String> arrays = new DaoHotAddress().getCityFromName(search);

        return AES.encryptToAESJson(arrays);
    }

    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/get-photo-profile/{id_user}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPhotoProfile(@PathVariable int id_user) {
        return new DaoHotDeliveryMan().getPhotoProfile(id_user);
    }

    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/get-photo-front-identity/{id_user}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getPhotoFrontIdentity(@PathVariable int id_user) {
        return new DaoHotDeliveryMan().getPhotoFrontIdentity(id_user);
    }

    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/get-photo-verse-identity/{id_user}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPhotVerseIdentity(@PathVariable int id_user) {
        return new DaoHotDeliveryMan().getPhotoVerseIdentity(id_user);
    }


}
