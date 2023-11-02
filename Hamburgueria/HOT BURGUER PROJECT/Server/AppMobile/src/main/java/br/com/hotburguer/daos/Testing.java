package br.com.hotburguer.daos;

import apis.encrypt.RSA;
import apis.pagarme.recipient.RRecipient;
import apis.pagarme.userbuyer.BAccountBank;
import apis.pagarme.userbuyer.BDocument;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.hotburguer.itens.HotDeliveryMan;
import br.com.megasena.dao.DaoMegaSena;
import br.com.megasena.itens.ITMegaSena;
import br.com.megasena.itens.ITPadraoBinario;
import br.com.megasena.itens.ITPorcentagem;
import me.pagar.model.BankAccount;
import me.pagar.model.PagarMe;
import me.pagar.model.Recipient;
import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.security.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static br.com.comprazencasa.geo.GeoUtils.EARTH_RADIUS_KM;

public class Testing {

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
       /* String json = "{\"social_login\":\"facebook\",\"hash_key\":\"oJyWx4jDj+ZCAb29IzvrYESkUrE=\",\"name\":\"Marlon\",\"id_device\":\"b176660543e88090\",\"email\":\"marlon_guitar_pro1994@hotmail.com\"}";

        HotDeliveryMan hot = new Gson().fromJson(new String(json), HotDeliveryMan.class);

        System.out.println("NAME: "+hot.getName());
        System.out.println("EMAIL: "+hot.getEmail());
        System.out.println("HASH: "+hot.getHashKey());
        System.out.println("DEVICE: "+hot.getIdDevice());*/

        /*new HotMapBox().getMatrixFromCoordinates(
                new LatLng(-22.855114,-43.323454),
                new LatLng(-22.849726,-43.327059));*/

        /*Recipient recipientResponse = new DaoPagarme().getRecipient().createRecipient(recipient, accountBank);
        BankAccount bankAccount = recipientResponse.getBankAccount();

        accountBank.setId(bankAccount.getId());
        accountBank.setName(baName);

        HotDeliveryMan deliveryMan = new HotDeliveryMan();
        deliveryMan.setId(idUser);
        deliveryMan.setIdRecipient(recipientResponse.getId());
        deliveryMan.setIdDevice(idDevice);
        deliveryMan.setBankAccount(accountBank);
        deliveryMan.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));*/

        // SELECTIONAR USRUARIOS QUE NÂO ESTEJEM OPERANDO

        // SELECTIONAR USUARIOS QUE ESTJAM ONLINE OK

        // SELECIONAR USUARIOS QUE TENHA O ID DIFERENTE DO ID DO ENTREGADOR QUE REJEITOU A CORRIDA OK

        // SELECIONAR USUARIO QUE TENHA A DIFERENÇA DE TEMPO DA ULTIMA ENTREGA MAIOR QUE DOS DEMAIS OU NULA OK

        //

        /*DaoHotOrder dao = new DaoHotOrder();
        Map<Integer, HotOrderManager> orders = dao.getOrders(1,"b176660543e88090",null);

        int ordersSize = orders.size();
        HotGroupOrders group = null ;
        ArrayList<HotGroupOrders> groups = new ArrayList<>();
        for (int i = 0; i <= ordersSize; i++) {
            // Grupo criado
            group = new DaoHotOrder().createGroupOrders2(new ArrayList<>(orders.values()));

            if(group != null){
                groups.add(group);

                orders.values().removeAll(group);

                i=0;
                ordersSize = orders.size();

            } else break ;
        }

        for(HotGroupOrders g : groups)
            System.out.println("DISTANCE: "+g.get(0).getIdOrder());*/

        int[] idDeliveryman = new int[]{119,111,106};

        /*ArrayList<String> ids = new ArrayList<>();
        ids.add("87");
        ids.add("101");
        ids.add("106");
        ids.add("107");
        ids.add("108");
        ids.add("109");
        ids.add("110");
        ids.add("112");
        ids.add("114");
        ids.add("117");
        ids.add("119");
        ids.add("120");
        ids.add("122");
        ids.add("123");

        String title = "Melhor Horário";
        String message = "A partir das 18:00 horas!!" ;
        new DaoHotDeliveryMan().notifyDeliveryMan(title, message, message,ids, null);*/

        /*DaoPagarme pagarMe = new DaoPagarme();

        BDocument document = new BDocument();
        document.setType(BDocument.Type.CPF);
        document.setCPF("035.386.787-07");

        BAccountBank accountBank = new BAccountBank();
        accountBank.setAccount("29195");
        accountBank.setAccountDv("5");
        accountBank.setAgency("0221");
        accountBank.setAgencyDv("6");
        accountBank.setBankCode("104");
        accountBank.setLegalName("");
        accountBank.setDocument(document);
        accountBank.setType(BAccountBank.Type.CURRENT);
        accountBank.setDefaultAccount(true);

        RRecipient recipient = new DaoSystemConfiguration().getPaymentConfig().getRecipient();

        Recipient recipientResponse = new DaoPagarme().getRecipient().createRecipient(recipient, accountBank);
        BankAccount bankAccount = recipientResponse.getBankAccount();

        accountBank.setId(bankAccount.getId());
        accountBank.setName("CAIXA ECONOMICA FEDERAL 104");

        HotDeliveryMan deliveryMan = new HotDeliveryMan();
        deliveryMan.setId(101);
        deliveryMan.setIdRecipient(recipientResponse.getId());
        deliveryMan.setIdDevice("291defc85157ba8c");
        deliveryMan.setBankAccount(accountBank);
        //deliveryMan.setIpAccess(httpServletRequest.getHeader("X-FORWARDED-FOR"));

        DaoHotDeliveryMan daoDeliveryman = new DaoHotDeliveryMan();
        daoDeliveryman.createBankAccount(101, accountBank);
        daoDeliveryman.update(deliveryMan);*/

        /*RSA rsa = new RSA(null,null);
        RSA.Keys keys = rsa.generateKey(4096);

        System.out.println("PUBLIC: "+keys.getPublicKeyBase64());
        System.out.println("PRIVATE: "+keys.getPrivateKeyBase64());*/

        /*DaoHotAddress address = new DaoHotAddress();
        ArrayList<String> li = address.getCityFromName("");

        StringBuilder sb = new StringBuilder();
        sb.append("<string-array name=\"locals\">\n");

        for(String s : li){
            sb.append("\t<item>");
            sb.append(s);
            sb.append("</item>\n");
        }

        sb.append("</string-array>");
*/

       /* int numero = 5;

        System.out.println(String.format("%02d", numero));

        DaoMegaSena dao = new DaoMegaSena();

        // PEGA TODOS OS PADROES BINARIOS DE JANEIRO DE UMA DATA ESPECIFICA
        ArrayList<ITPadraoBinario> padraoJaneiro = dao.getPBQueMaisSaem("março");

        System.out.println("PADS: "+padraoJaneiro.size());
        // JUNÇÂO DE NUMEROS E QUANTIDADES (AGRUPAMENTO DE VALORES)
        Map<ITPadraoBinario,Integer> padroesQueMaisSairam = new HashMap<>();

        for(ITPadraoBinario pad : padraoJaneiro){
            if(padroesQueMaisSairam.containsKey(pad)) {
                int count = padroesQueMaisSairam.get(pad);
                pad.setCount((count+1));
                padroesQueMaisSairam.put(pad,(count+1));
                System.out.println("PADS: "+Arrays.toString(pad.toArray())+" - "+pad.getCount()+"x");
            }
            else{
                padroesQueMaisSairam.put(pad,pad.getCount());
            }

        }

        // CRIA UMA LISTA DE PADROES QUE SAIRAM MAIS DE UMA VEZ
        ArrayList<ITPadraoBinario> padroesQueSairamMaisDeUmaVez = new ArrayList<>();
        for(ITPadraoBinario pad : padroesQueMaisSairam.keySet()){
            pad.setCount(padroesQueMaisSairam.get(pad));
            if(pad.getCount() > 1) {
                padroesQueSairamMaisDeUmaVez.add(pad);
            }
        }

        // PEGA OS NUMEROS MAIS SORTEADOS DE CADA COLUNA
        Map<Integer, Map<Integer, Integer>> m = new DaoMegaSena().getMaisSorteadosPorColuna();
        int coluna = 1 ;
        for(Map<Integer, Integer> maisSort : m.values()){
            System.out.println("COLUNA: "+coluna);
            for(Integer numb : maisSort.keySet()){
                System.out.println("NUMERO: "+numb+" - "+maisSort.get(numb)+"x");
            }
            coluna++;
        }

        // Padrao de maior Probabilidade
        double[][] ppi = new DaoMegaSena().getPercentualParImpar();

        int pc1 = ppi[0][0]>ppi[1][0]?0:1;
        int pc2 = ppi[0][1]>ppi[1][1]?0:1;
        int pc3 = ppi[0][2]>ppi[1][2]?0:1;
        int pc4 = ppi[0][3]>ppi[1][3]?0:1;
        int pc5 = ppi[0][4]>ppi[1][4]?0:1;
        int pc6 = ppi[0][5]>ppi[1][5]?0:1;

        *//*double pc1 = ppi[0][0]>ppi[1][0]?ppi[0][0]:ppi[1][0];
        double pc2 = ppi[0][1]>ppi[1][1]?ppi[0][1]:ppi[1][1];
        double pc3 = ppi[0][2]>ppi[1][2]?ppi[0][2]:ppi[1][2];
        double pc4 = ppi[0][3]>ppi[1][3]?ppi[0][3]:ppi[1][3];
        double pc5 = ppi[0][4]>ppi[1][4]?ppi[0][4]:ppi[1][4];
        double pc6 = ppi[0][5]>ppi[1][5]?ppi[0][5]:ppi[1][5];*//*

        System.out.println("COL1: "+pc1+" COL2: "+pc2+" COL3: "+pc3+" COL4: "+pc4+" COL5: "+pc5+" COL6: "+pc6);
        // PEGA TODOS OS NUMEROS SORTEADOS NO QUADRANTE 1 DO MES JANEIRO
        Map<Integer, Integer> quad = new DaoMegaSena().getMaisSorteadosPorQuadrant(4, "março");
        for(Integer key : quad.keySet()){
            System.out.println("MAIS SORT QUADR: "+key+" - "+quad.get(key)+"x");
        }
*/
        /*DaoMegaSena m = new DaoMegaSena();
        ITPorcentagem p = null;

        int anoI = 1996, anoF= 2020;
        while(anoI <= anoF){
            p = m.getPorcentagem().getParImpar(true,"março",anoI);
            System.out.print("ANO: "+anoI+" - ");
            System.out.println("C1: "+p.getColuna1()+" C2: "+p.getColuna2()+" C3: "+p.getColuna3()+" C4: "+p.getColuna4()+" C5: "+p.getColuna6());

            anoI++;
        }*/

        // numeros de  1 à 60

        /*Map<Integer, Map<Integer, Integer>> mapGeneral = new DaoMegaSena().getProbabilidadeColor();

        for(Integer column : mapGeneral.keySet()){
            System.out.println("COLUNA: "+column);

            for(Integer number: mapGeneral.get(column).keySet()){
                int color = mapGeneral.get(column).get(number);

                System.out.println("NUMBER: "+number+" - COLR: "+color);
            }

            break;
        }*/

        /*Map<Integer,Integer> mapGenera = new DaoMegaSena().getProbabilidadeColor(1);

        for(Integer number: mapGenera.keySet()){
            int color = mapGenera.get(number);

            System.out.println("NUMBER: "+number+" - COLR: "+color);
        }*/

        int position = 10 ;
        System.out.println("POSIT: "+(position % 9));
        System.out.println((position % 9 == 0));
    }

    public static final ITMegaSena formula(){



        return  null;
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2){
        double firstLatToRad = Math.toRadians(lat1);
        double secondLatToRad = Math.toRadians(lat2);

// Diferença das longitudes
        double deltaLongitudeInRad = Math.toRadians(lng2
                - lng1);

// Cálcula da distância entre os pontos
        return Math.acos(Math.cos(firstLatToRad) * Math.cos(secondLatToRad)
                * Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
                * Math.sin(secondLatToRad))
                * EARTH_RADIUS_KM;
    }

    public static String convertToBinary(String input, String encoding)
            throws UnsupportedEncodingException {
        byte[] encoded_input = Charset.forName(encoding)
                .encode(input)
                .array();
        return IntStream.range(0, encoded_input.length)
                .map(i -> encoded_input[i])
                .mapToObj(e -> Integer.toBinaryString(e ^ 255))
                .map(e -> String.format("%1$" + Byte.SIZE + "s", e).replace(" ", "0"))
                .collect(Collectors.joining(" "));
    }

    public static String decodeText(String input, String encoding) throws IOException {
        return
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(input.getBytes()),
                                Charset.forName(encoding)))
                        .readLine();
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String banks() {
        String banks = "\n" +
                "BCO DO BRASIL S.A.\t1\n" +
                "BRB – BCO DE BRASILIA S.A.\t70\n" +
                "Selic\tn/a\n" +
                "Bacen\tn/a\n" +
                "AGK CC S.A.\t272\n" +
                "CONF NAC COOP CENTRAIS UNICRED\t136\n" +
                "CAIXA ECONOMICA FEDERAL\t104\n" +
                "STN\tn/a\n" +
                "BANCO INTER\t77\n" +
                "BCO RIBEIRAO PRETO S.A.\t741\n" +
                "BCO CETELEM S.A.\t739\n" +
                "BANCO SEMEAR\t743\n" +
                "PLANNER CV S.A.\t100\n" +
                "BCO B3 S.A.\t96\n" +
                "BCO RABOBANK INTL BRASIL S.A.\t747\n" +
                "BCO COOPERATIVO SICREDI S.A.\t748\n" +
                "BCO BNP PARIBAS BRASIL S A\t752\n" +
                "CCCM UNICRED CENTRAL RS\t91\n" +
                "KIRTON BANK\t399\n" +
                "PORTOCRED S.A. – CFI\t108\n" +
                "BANCOOB\t756\n" +
                "BCO KEB HANA DO BRASIL S.A.\t757\n" +
                "XP INVESTIMENTOS CCTVM S/A\t102\n" +
                "UNIPRIME NORTE DO PARANÁ – CC\t84\n" +
                "CM CAPITAL MARKETS CCTVM LTDA\t180\n" +
                "BCO MORGAN STANLEY S.A.\t66\n" +
                "UBS BRASIL CCTVM S.A.\t15\n" +
                "TREVISO CC S.A.\t143\n" +
                "CIP Siloc\tn/a\n" +
                "HIPERCARD BM S.A.\t62\n" +
                "BCO. J.SAFRA S.A.\t74\n" +
                "UNIPRIME CENTRAL CCC LTDA.\t99\n" +
                "BCO ALFA S.A.\t25\n" +
                "PI DTVM S.A.\t315\n" +
                "BCO ABN AMRO S.A.\t75\n" +
                "BCO CARGILL S.A.\t40\n" +
                "TERRA INVESTIMENTOS DTVM\t307\n" +
                "SERVICOOP\t190\n" +
                "VISION S.A. CC\t296\n" +
                "BANCO BRADESCARD\t63\n" +
                "NOVA FUTURA CTVM LTDA.\t191\n" +
                "GOLDMAN SACHS DO BRASIL BM S.A\t64\n" +
                "CIP Sitraf\tn/a\n" +
                "CCC NOROESTE BRASILEIRO LTDA.\t97\n" +
                "CCM DESP TRÂNS SC E RS\t16\n" +
                "SOROCRED CFI S.A.\t299\n" +
                "BANCO INBURSA\t12\n" +
                "BCO DA AMAZONIA S.A.\t3\n" +
                "CONFIDENCE CC S.A.\t60\n" +
                "BCO DO EST. DO PA S.A.\t37\n" +
                "CASA CREDITO S.A. SCM\t159\n" +
                "ALBATROSS CCV S.A\t172\n" +
                "COOP CENTRAL AILOS\t85\n" +
                "CENTRAL COOPERATIVA DE CRÉDITO NO\t114\n" +
                "BCO BBI S.A.\t36\n" +
                "BCO BRADESCO FINANC. S.A.\t394\n" +
                "BCO DO NORDESTE DO BRASIL S.A.\t4\n" +
                "BCO CCB BRASIL S.A.\t320\n" +
                "HS FINANCEIRA\t189\n" +
                "LECCA CFI S.A.\t105\n" +
                "BCO KDB BRASIL S.A.\t76\n" +
                "BANCO TOPÁZIO S.A.\t82\n" +
                "CCR DE OURO\t286\n" +
                "PÓLOCRED SCMEPP LTDA.\t93\n" +
                "CCR DE SÃO MIGUEL DO OESTE\t273\n" +
                "PAGSEGURO\t290\n" +
                "ICAP DO BRASIL CTVM LTDA.\t157\n" +
                "SOCRED S.A. SCM\t183\n" +
                "STATE STREET BR S.A. BCO COMERCIAL\t14\n" +
                "CARUANA SCFI\t130\n" +
                "CODEPE CVC S.A.\t127\n" +
                "BCO ORIGINAL DO AGRO S/A\t79\n" +
                "BANCOSEGURO S.A.\t81\n" +
                "CRESOL CONFEDERAÇÃO\t133\n" +
                "BCO AGIBANK S.A.\t121\n" +
                "BCO DA CHINA BRASIL S.A.\t83\n" +
                "GET MONEY CC LTDA\t138\n" +
                "BCO BANDEPE S.A.\t24\n" +
                "OM DTVM LTDA\t319\n" +
                "TRAVELEX BANCO DE CÂMBIO S.A.\t95\n" +
                "BANCO FINAXIS\t94\n" +
                "STANDARD CHARTERED BI S.A.\t118\n" +
                "SENFF S.A. – CFI\t276\n" +
                "BRK S.A. CFI\t92\n" +
                "BCO DO EST. DE SE S.A.\t47\n" +
                "BEXS BCO DE CAMBIO S.A.\t144\n" +
                "BR PARTNERS BI\t126\n" +
                "BPP IP S.A.\t301\n" +
                "BRL TRUST DTVM SA\t173\n" +
                "BCO WESTERN UNION\t119\n" +
                "CAMBIONET CC LTDA\t309\n" +
                "PARANA BCO S.A.\t254\n" +
                "BARIGUI CH\t268\n" +
                "BCO BOCOM BBM S.A.\t107\n" +
                "BCO CAPITAL S.A.\t412\n" +
                "BCO WOORI BANK DO BRASIL S.A.\t124\n" +
                "FACTA S.A. CFI\t149\n" +
                "STONE PAGAMENTOS S.A.\t197\n" +
                "BROKER BRASIL CC LTDA.\t142\n" +
                "BCO MERCANTIL DO BRASIL S.A.\t389\n" +
                "BCO ITAÚ BBA S.A.\t184\n" +
                "BCO TRIANGULO S.A.\t634\n" +
                "SENSO CCVM S.A.\t545\n" +
                "ICBC DO BRASIL BM S.A.\t132\n" +
                "VIPS CC LTDA.\t298\n" +
                "NU PAGAMENTOS S.A. (Nubank)\t260\n" +
                "UBS BRASIL BI S.A.\t129\n" +
                "MS BANK S.A. BCO DE CÂMBIO\t128\n" +
                "PARMETAL DTVM LTDA\t194\n" +
                "VORTX DTVM LTDA.\t310\n" +
                "COMMERZBANK BRASIL S.A. – BCO MÚLTIPLO\t163\n" +
                "AVISTA S.A. CFI\t280\n" +
                "GUITTA CC LTDA\t146\n" +
                "CCR DE PRIMAVERA DO LESTE\t279\n" +
                "DACASA FINANCEIRA S/A – SCFI\t182\n" +
                "GENIAL INVESTIMENTOS CVM S.A.\t278\n" +
                "IB CCTVM LTDA\t271\n" +
                "BCO BANESTES S.A.\t21\n" +
                "BCO ABC BRASIL S.A.\t246\n" +
                "BS2 DTVM S.A.\t292\n" +
                "B3 Cetip\tn/a\n" +
                "CIP C3\tn/a\n" +
                "SCOTIABANK BRASIL\t751\n" +
                "BANCO BTG PACTUAL S.A.\t208\n" +
                "BCO MODAL S.A.\t746\n" +
                "BCO CLASSICO S.A.\t241\n" +
                "BCO C6 S.A.\t336\n" +
                "BCO GUANABARA S.A.\t612\n" +
                "BCO INDUSTRIAL DO BRASIL S.A.\t604\n" +
                "BCO CREDIT SUISSE (BRL) S.A.\t505\n" +
                "FAIR CC S.A.\t196\n" +
                "BCO LA NACION ARGENTINA\t300\n" +
                "CITIBANK N.A.\t477\n" +
                "BCO CEDULA S.A.\t266\n" +
                "BCO BRADESCO BERJ S.A.\t122\n" +
                "BCO J.P. MORGAN S.A.\t376\n" +
                "BCO CAIXA GERAL BRASIL S.A.\t473\n" +
                "BCO CITIBANK S.A.\t745\n" +
                "BCO RODOBENS S.A.\t120\n" +
                "BCO FATOR S.A.\t265\n" +
                "BNDES\t7\n" +
                "ATIVA S.A. INVESTIMENTOS CCTVM\t188\n" +
                "BGC LIQUIDEZ DTVM LTDA\t134\n" +
                "BCO ALVORADA S.A.\t641\n" +
                "BANCO ITAÚ CONSIGNADO S.A.\t29\n" +
                "BCO MÁXIMA S.A.\t243\n" +
                "HAITONG BI DO BRASIL S.A.\t78\n" +
                "OLIVEIRA TRUST DTVM S.A.\t111\n" +
                "PORTOPAR DTVM LTDA\t306\n" +
                "BNY MELLON BCO S.A.\t17\n" +
                "PERNAMBUCANAS FINANC S.A. CFI\t174\n" +
                "BCO LA PROVINCIA B AIRES BCE\t495\n" +
                "BRASIL PLURAL S.A. BCO.\t125\n" +
                "JPMORGAN CHASE BANK\t488\n" +
                "BCO ANDBANK S.A.\t65\n" +
                "ING BANK N.V.\t492\n" +
                "LEVYCAM CCV LTDA\t145\n" +
                "BCV\t250\n" +
                "BCO REP ORIENTAL URUGUAY BCE\t494\n" +
                "BEXS CC S.A.\t253\n" +
                "HSBC BANCO DE INVESTIMENTO\t269\n" +
                "BCO ARBI S.A.\t213\n" +
                "BMFBOVESPA\tn/a\n" +
                "INTESA SANPAOLO BRASIL S.A. BM\t139\n" +
                "BCO TRICURY S.A.\t18\n" +
                "BCO SAFRA S.A.\t422\n" +
                "BCO INTERCAP S.A.\t630\n" +
                "BCO FIBRA S.A.\t224\n" +
                "BCO LUSO BRASILEIRO S.A.\t600\n" +
                "BANCO PAN\t623\n" +
                "BCO BRADESCO CARTOES S.A.\t204\n" +
                "BCO VOTORANTIM S.A.\t655\n" +
                "BCO ITAUBANK S.A.\t479\n" +
                "BCO MUFG BRASIL S.A.\t456\n" +
                "BCO SUMITOMO MITSUI BRASIL S.A.\t464\n" +
                "ITAÚ UNIBANCO S.A.\t341\n" +
                "BCO BRADESCO S.A.\t237\n" +
                "OMNI BANCO S.A.\t613\n" +
                "ITAÚ UNIBANCO HOLDING S.A.\t652\n" +
                "BCO SOFISA S.A.\t637\n" +
                "BMF Câmbio\tn/a\n" +
                "BCO INDUSVAL S.A.\t653\n" +
                "BCO CREFISA S.A.\t69\n" +
                "BCO MIZUHO S.A.\t370\n" +
                "BANCO INVESTCRED UNIBANCO S.A.\t249\n" +
                "BCO BMG S.A.\t318\n" +
                "BCO FICSA S.A.\t626\n" +
                "SAGITUR CC LTDA\t270\n" +
                "BCO SOCIETE GENERALE BRASIL\t366\n" +
                "MAGLIANO S.A. CCVM\t113\n" +
                "TULLETT PREBON BRASIL CVC LTDA\t131\n" +
                "C.SUISSE HEDGING-GRIFFO CV S/A\t11\n" +
                "BCO PAULISTA S.A.\t611\n" +
                "BOFA MERRILL LYNCH BM S.A.\t755\n" +
                "CCR REG MOGIANA\t89\n" +
                "BCO PINE S.A.\t643\n" +
                "EASYNVEST – TÍTULO CV SA\t140\n" +
                "BCO DAYCOVAL S.A\t707\n" +
                "CAROL DTVM LTDA.\t288\n" +
                "RENASCENCA DTVM LTDA\t101\n" +
                "DEUTSCHE BANK S.A.BCO ALEMAO\t487\n" +
                "BANCO CIFRA\t233\n" +
                "GUIDE\t177\n" +
                "BCO RENDIMENTO S.A.\t633\n" +
                "BCO BS2 S.A.\t218\n" +
                "BCO OLÉ BONSUCESSO CONSIGNADO S.A.\t169\n" +
                "LASTRO RDV DTVM LTDA\t293\n" +
                "FRENTE CC LTDA.\t285\n" +
                "B&T CC LTDA.\t80\n" +
                "NOVO BCO CONTINENTAL S.A. – BM\t753\n" +
                "BCO CRÉDIT AGRICOLE BR S.A.\t222\n" +
                "CCR COOPAVEL\t281\n" +
                "BANCO SISTEMA\t754\n" +
                "CREDIALIANÇA CCR\t98\n" +
                "BCO VR S.A.\t610\n" +
                "BCO OURINVEST S.A.\t712\n" +
                "CREDICOAMO\t10\n" +
                "RB CAPITAL INVESTIMENTOS DTVM LTDA.\t283\n" +
                "BCO SANTANDER (BRASIL) S.A.\t33\n" +
                "BANCO JOHN DEERE S.A.\t217\n" +
                "BCO DO ESTADO DO RS S.A.\t41\n" +
                "ADVANCED CC LTDA\t117\n" +
                "BCO A.J. RENNER S.A.\t654\n" +
                "BANCO ORIGINAL\t212\n" +
                "DECYSEO CC LTDA.\t289";
        return banks;
    }
}
