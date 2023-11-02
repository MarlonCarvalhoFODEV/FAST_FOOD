package br.com.megasena.test;

import br.com.megasena.dao.*;
import br.com.megasena.itens.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.USER_AGENT;

public class SenaTest {

    public static void main(String[] args) throws IOException {
        ITMegaSena c = new ITMegaSena();
        c.setConcurso(1);
        c.setLocal("Qualquer");
        c.setData("1994-09-22");
        c.setColuna1(01);
        c.setColuna2(02);
        c.setColuna3(03);
        c.setColuna4(04);
        c.setColuna5(05);
        c.setColuna6(06);
        c.setGanhadoreFx1(5);
        c.setGanhadoreFx2(8);
        /*c.setGanhadoreFx3(01);
        c.setRateioFx1(2);
        c.setRateioFx2(4);
        c.setRateioFx3(5);
        c.setCidade("Rio de Janeiro");
        c.setValorArrecadado(118888888.58);
        c.setValorEstimadoProximoConcurso(4.555);
        c.setAcumulado(true);
        c.setSorteioEspecial(true);
        c.setObservacao("Nenhuma");*/

        /*JSONObject ob = new JSONObject(new Gson().toJson(c));
        System.out.println(ob.toString());

        ITMegaSena s = new Gson().fromJson(ob.toString(), ITMegaSena.class);
        System.out.println(s.getLocal());
        BigDecimal value = BigDecimal.valueOf(1000000.58);
        System.out.println(new DecimalFormat("#.00").format(value.movePointLeft(6)));
        //ArrayList<ITMegaSena> cs = new DaoMegaSena().getConcursosMegaSenaFromWeb();*/

        Map<Integer, ITLoteca> cs = new DaoLoteca().getConcursosFromWebMap();


        new DaoLoteca().insertNew(new ArrayList<>(cs.values()));

    }

}
