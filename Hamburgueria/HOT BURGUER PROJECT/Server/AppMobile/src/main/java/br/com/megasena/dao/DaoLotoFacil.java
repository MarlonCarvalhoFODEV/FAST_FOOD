package br.com.megasena.dao;

import apis.date.DateAPI;
import br.com.comprazencasa.database.DaoAddress;
import br.com.megasena.itens.ITLotoFacil;
import br.com.megasena.itens.ITMegaSena;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoLotoFacil {

    public boolean insertNew(ArrayList<ITLotoFacil> cs) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_lotofacil (concurso, data, cidade, b1, b2, b3, ");
                sqlConstruct.append("b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, arrecadacao_total, gds_15, ");
                sqlConstruct.append("gds_14, gds_13, gds_12, gds_11, val_rateio_15_numeros, val_rateio_14_numeros, val_rateio_13_numeros, val_rateio_12_numeros,  ");
                sqlConstruct.append("val_rateio_11_numeros, acumulado_15, estimativa_premio, val_acumulado_especial) SELECT * FROM (SELECT ? as concurso,? as data, ? as cidade,? as b1,");
                sqlConstruct.append("? as b2,? as b3,? as b4,? as b5,? as b6,? as b7,? as b8,? as b9,? as b10,? as b11,? as b12,? as b13,? as b14,? as b15,? as arrecadacao_total, ");
                sqlConstruct.append("? as gds_15,? as gds_14,? as gds_13,? as gds_12,? as gds_11,? as val_rateio_15_numeros,? as val_rateio_14_numeros,? as val_rateio_13_numeros,");
                sqlConstruct.append("? as val_rateio_12_numeros,? as val_rateio_11_numeros,? as acumulado_15, ? as estimativa_premio, ? as val_acumulado_especial) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_lotofacil WHERE concurso = ? ");
                sqlConstruct.append(") LIMIT 1;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                for (ITLotoFacil c : cs) {
                    pst.setInt(1, c.getConcurso());
                    pst.setString(2, c.getData());
                    pst.setString(3, c.getCidade());
                    pst.setInt(4, c.getB1());
                    pst.setInt(5, c.getB2());
                    pst.setInt(6, c.getB3());
                    pst.setInt(7, c.getB4());
                    pst.setInt(8, c.getB5());
                    pst.setInt(9, c.getB6());
                    pst.setInt(10, c.getB7());
                    pst.setInt(11, c.getB8());
                    pst.setInt(12, c.getB9());
                    pst.setInt(13, c.getB10());
                    pst.setInt(14, c.getB11());
                    pst.setInt(15, c.getB12());
                    pst.setInt(16, c.getB13());
                    pst.setInt(17, c.getB14());
                    pst.setInt(18, c.getB15());

                    pst.setString(19, c.getArrecadacaoTotal());
                    pst.setInt(20, c.getGds15Numeros());
                    pst.setInt(21, c.getGds14Numeros());
                    pst.setInt(22, c.getGds13Numeros());
                    pst.setInt(23, c.getGds12Numeros());
                    pst.setInt(24, c.getGds11Numeros());

                    pst.setString(25, c.getValRateio15Numeros());
                    pst.setString(26, c.getValRateio14Numeros());
                    pst.setString(27, c.getValRateio13Numeros());
                    pst.setString(28, c.getValRateio12Numeros());
                    pst.setString(29, c.getValRateio11Numeros());

                    pst.setString(30, c.getAcumlulado15Numeros());
                    pst.setString(31, c.getEstimativaPremio());
                    pst.setString(32, c.getAcumuladoEspecial());
                    pst.setInt(33, c.getConcurso());

                    pst.addBatch();

                    System.out.println(c.getConcurso());

                }

                int[] results = pst.executeBatch();
                for (int i : results)
                    if (i == 0)
                        return false;

                return true;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return false;
    }

    public boolean insertNew(ITLotoFacil c) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_lotofacil (concurso, data, cidade, b1, b2, b3, ");
                sqlConstruct.append("b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, arrecadacao_total, gds_15, ");
                sqlConstruct.append("gds_14, gds_13, gds_12, gds_11, val_rateio_15_numeros, val_rateio_14_numeros, val_rateio_13_numeros, val_rateio_12_numeros,  ");
                sqlConstruct.append("val_rateio_11_numeros, acumulado_15, estimativa_premio, val_acumulado_especial) SELECT * FROM (SELECT ? as concurso,? as data, ? as cidade,? as b1,");
                sqlConstruct.append("? as b2,? as b3,? as b4,? as b5,? as b6,? as b7,? as b8,? as b9,? as b10,? as b11,? as b12,? as b13,? as b14,? as b15,? as arrecadacao_total, ");
                sqlConstruct.append("? as gds_15,? as gds_14,? as gds_13,? as gds_12,? as gds_11,? as val_rateio_15_numeros,? as val_rateio_14_numeros,? as val_rateio_13_numeros,");
                sqlConstruct.append("? as val_rateio_12_numeros,? as val_rateio_11_numeros,? as acumulado_15, ? as estimativa_premio, ? as val_acumulado_especial) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_lotofacil WHERE concurso = ? ");
                sqlConstruct.append(") LIMIT 1;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, c.getConcurso());
                pst.setString(2, c.getData());
                pst.setString(3, c.getCidade());
                pst.setInt(4, c.getB1());
                pst.setInt(5, c.getB2());
                pst.setInt(6, c.getB3());
                pst.setInt(7, c.getB4());
                pst.setInt(8, c.getB5());
                pst.setInt(9, c.getB6());
                pst.setInt(10, c.getB7());
                pst.setInt(11, c.getB8());
                pst.setInt(12, c.getB9());
                pst.setInt(13, c.getB10());
                pst.setInt(14, c.getB11());
                pst.setInt(15, c.getB12());
                pst.setInt(16, c.getB13());
                pst.setInt(17, c.getB14());
                pst.setInt(18, c.getB15());

                pst.setString(19, c.getArrecadacaoTotal());
                pst.setInt(20, c.getGds15Numeros());
                pst.setInt(21, c.getGds14Numeros());
                pst.setInt(22, c.getGds13Numeros());
                pst.setInt(23, c.getGds12Numeros());
                pst.setInt(24, c.getGds11Numeros());

                pst.setString(25, c.getValRateio15Numeros());
                pst.setString(26, c.getValRateio14Numeros());
                pst.setString(27, c.getValRateio13Numeros());
                pst.setString(28, c.getValRateio12Numeros());
                pst.setString(29, c.getValRateio11Numeros());

                pst.setString(30, c.getAcumlulado15Numeros());
                pst.setString(31, c.getEstimativaPremio());
                pst.setString(32, c.getAcumuladoEspecial());
                pst.setInt(33, c.getConcurso());

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffecteds > 0);
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return false;
    }

    public ITLotoFacil getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *, CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_lotofacil where id > 0 ORDER BY id DESC limit 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITLotoFacil l = null;
                if (rs.next()) {
                    l = new ITLotoFacil();
                    l.setConcurso(rs.getInt("concurso"));
                    l.setData(rs.getString("data1"));
                    l.setCidade(rs.getString("cidade"));
                    l.setB1(rs.getInt("b1"));
                    l.setB2(rs.getInt("b2"));
                    l.setB3(rs.getInt("b3"));
                    l.setB4(rs.getInt("b4"));
                    l.setB5(rs.getInt("b5"));
                    l.setB6(rs.getInt("b6"));
                    l.setB7(rs.getInt("b7"));
                    l.setB8(rs.getInt("b8"));
                    l.setB9(rs.getInt("b9"));
                    l.setB10(rs.getInt("b10"));
                    l.setB11(rs.getInt("b11"));
                    l.setB12(rs.getInt("b12"));
                    l.setB13(rs.getInt("b13"));
                    l.setB14(rs.getInt("b14"));
                    l.setB15(rs.getInt("b15"));

                    l.setArrecadacaoTotal(rs.getString("arrecadacao_total"));
                    l.setGds15Numeros(rs.getInt("gds_15"));
                    l.setGds14Numeros(rs.getInt("gds_14"));
                    l.setGds13Numeros(rs.getInt("gds_13"));
                    l.setGds12Numeros(rs.getInt("gds_12"));
                    l.setGds11Numeros(rs.getInt("gds_11"));

                    l.setValRateio15Numeros(rs.getString("val_rateio_15_numeros"));
                    l.setValRateio14Numeros(rs.getString("val_rateio_14_numeros"));
                    l.setValRateio13Numeros(rs.getString("val_rateio_13_numeros"));
                    l.setValRateio12Numeros(rs.getString("val_rateio_12_numeros"));
                    l.setValRateio11Numeros(rs.getString("val_rateio_11_numeros"));

                    l.setAcumlulado15Numeros(rs.getString("acumulado_15"));
                    l.setEstimativaPremio(rs.getString("estimativa_premio"));
                    l.setAcumuladoEspecial(rs.getString("val_acumulado_especial"));
                    l.setTimeDiff(rs.getString("timer"));
                }

                con.close();
                pst.close();

                return l ;
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);

                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return null;
    }

    public ArrayList<ITLotoFacil> getConcursosLotoFacilFromWeb() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotofacil/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA0sTIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VAcySpRM!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC42046/res/id=historicoHTML/c=cacheLevelPage/=/").get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");

        ArrayList<ITLotoFacil> concs = new ArrayList<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITLotoFacil c = getConcurso(n);
                concs.add(c);
            }
        }
        return concs ;
    }

    public Map<Integer,ITLotoFacil> getConcursosLotoFacilFromWebMap() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotofacil/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA0sTIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VAcySpRM!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC42046/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");

        Map<Integer, ITLotoFacil> concs = new HashMap<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITLotoFacil c = getConcurso(n);
                if(!concs.containsKey(c.getConcurso())){
                    concs.put(c.getConcurso(), c);
                }
            }
        }
        return concs;
    }

    public static synchronized final ITLotoFacil getConcurso(Elements n) {

        Object concurso = n.get(0).text();
        Object data = n.get(1).text();
        Object b1 = n.get(2).text();
        Object b2 = n.get(3).text();
        Object b3 = n.get(4).text();
        Object b4 = n.get(5).text();
        Object b5 = n.get(6).text();
        Object b6 = n.get(7).text();
        Object b7 = n.get(8).text();
        Object b8 = n.get(9).text();
        Object b9 = n.get(10).text();
        Object b10 = n.get(11).text();
        Object b11 = n.get(12).text();
        Object b12 = n.get(13).text();
        Object b13 = n.get(14).text();
        Object b14 = n.get(15).text();
        Object b15 = n.get(16).text();

        Object arrectotal = n.get(17).text();
        Object gds15 = n.get(18).text();
        Object cidade = n.get(19).text();

        Object gds14 = n.get(20).text();
        Object gds13 = n.get(21).text();
        Object gds12 = n.get(22).text();
        Object gds11 = n.get(23).text();
        Object rat15 = n.get(24).text();
        Object rat14 = n.get(25).text();
        Object rat13 = n.get(26).text();
        Object rat12 = n.get(27).text();
        Object rat11 = n.get(28).text();
        Object acumulado15 = n.get(29).text();
        Object estimativaPremio = n.get(30).text();
        Object valAcumuladoEspecial = n.get(31).text();

        int position = n.size()-12;

        gds14 = n.get(position).text();
        position++;
        gds13 = n.get(position).text();
        position++;
        gds12 = n.get(position).text();
        position++;
        gds11 = n.get(position).text();
        position++;
        rat15 = n.get(position).text();
        position++;
        rat14 = n.get(position).text();
        position++;
        rat13 = n.get(position).text();
        position++;
        rat12 = n.get(position).text();
        position++;
        rat11 = n.get(position).text();
        position++;
        acumulado15 = n.get(position).text();
        position++;
        estimativaPremio = n.get(position).text();
        position++;
        valAcumuladoEspecial = n.get(position).text();

        int concursos = Integer.valueOf(concurso.toString());
        String cidades = String.valueOf(cidade.toString());
        String datas = DateAPI.getDateManager().formatDate(data.toString().replace("/","-"),"dd-MM-yyyy","yyyy-MM-dd");

        int col1 = Integer.valueOf(b1.toString());
        int col2 = Integer.valueOf(b2.toString());
        int col3 = Integer.valueOf(b3.toString());
        int col4 = Integer.valueOf(b4.toString());
        int col5 = Integer.valueOf(b5.toString());
        int col6 = Integer.valueOf(b6.toString());
        int col7 = Integer.valueOf(b7.toString());
        int col8 = Integer.valueOf(b8.toString());
        int col9 = Integer.valueOf(b9.toString());
        int col10 = Integer.valueOf(b10.toString());
        int col11 = Integer.valueOf(b11.toString());
        int col12 = Integer.valueOf(b12.toString());
        int col13 = Integer.valueOf(b13.toString());
        int col14 = Integer.valueOf(b14.toString());
        int col15 = Integer.valueOf(b15.toString());

        String arrecTotal = arrectotal.toString();
        int gds15s = Integer.valueOf(!gds15.toString().isEmpty() ? gds15.toString().replace(".", "").replace(",", ".") : "0.0");
        int gds14s = Integer.valueOf(!gds14.toString().isEmpty() ? gds14.toString().replace(".", "").replace(",", ".") : "0.0");
        int gds13s = Integer.valueOf(!gds13.toString().isEmpty() ? gds13.toString().replace(".", "").replace(",", ".") : "0.0");
        int gds12s = Integer.valueOf(gds12.toString().replace(".", "").replace(",", "."));
        int gds11s = Integer.valueOf(gds11.toString().replace(".", "").replace(",", "."));

        String rat15s = rat15.toString();
        String rat14s = rat14.toString();
        String rat13s = rat13.toString();
        String rat12s = rat12.toString();
        String rat11s = rat11.toString();

        String ac15 = acumulado15.toString();
        String estim = estimativaPremio.toString();
        String acuEspecial = valAcumuladoEspecial.toString();

        /*System.out.println("CONC: "+concurso.toString());
        System.out.println("DATA: "+data.toString());
        System.out.println("B1: "+b1.toString());
        System.out.println("B2: "+b2.toString());
        System.out.println("B3: "+b3.toString());
        System.out.println("B4: "+b4.toString());
        System.out.println("B5: "+b5.toString());
        System.out.println("B6: "+b6.toString());
        System.out.println("B7: "+b7.toString());
        System.out.println("B8: "+b8.toString());
        System.out.println("B9: "+b9.toString());
        System.out.println("B10: "+b10.toString());
        System.out.println("B11: "+b11.toString());
        System.out.println("B12: "+b12.toString());
        System.out.println("B13: "+b13.toString());
        System.out.println("B14: "+b14.toString());
        System.out.println("B15: "+b15.toString());
        System.out.println("GDS 15: "+gds15.toString());
        System.out.println("CIDADE: "+cidade.toString());
        System.out.println("GDS 14: "+gds14.toString());
        System.out.println("GDS 13: "+gds13.toString());
        System.out.println("GDS 12: "+gds12.toString());
        System.out.println("GDS 11: "+gds11.toString());

        System.out.println("RAT 15: "+rat15.toString());
        System.out.println("RAT 14: "+rat14.toString());
        System.out.println("RAT 13: "+rat13.toString());
        System.out.println("RAT 12: "+rat12.toString());
        System.out.println("RAT 11: "+rat11.toString());

        System.out.println("ARREC TOTAL: "+arrectotal.toString());
        System.out.println("ACUML 15: "+acumulado15.toString());
        System.out.println("ESTIMA PREMIO: "+estimativaPremio.toString());
        System.out.println("VAL ACUM ESPECIAL: "+valAcumuladoEspecial.toString());
        System.out.println("******************************************\n");*/

        ITLotoFacil l = new ITLotoFacil();
        l.setConcurso(concursos);
        l.setData(datas);
        l.setCidade(cidades);
        l.setB1(col1);
        l.setB2(col2);
        l.setB3(col3);
        l.setB4(col4);
        l.setB5(col5);
        l.setB6(col6);
        l.setB7(col7);
        l.setB8(col8);
        l.setB9(col9);
        l.setB10(col10);
        l.setB11(col11);
        l.setB12(col12);
        l.setB13(col13);
        l.setB14(col14);
        l.setB15(col15);

        l.setArrecadacaoTotal(arrecTotal);
        l.setGds15Numeros(gds15s);
        l.setGds14Numeros(gds14s);
        l.setGds13Numeros(gds13s);
        l.setGds12Numeros(gds12s);
        l.setGds11Numeros(gds11s);

        l.setValRateio15Numeros(rat15s);
        l.setValRateio14Numeros(rat14s);
        l.setValRateio13Numeros(rat13s);
        l.setValRateio12Numeros(rat12s);
        l.setValRateio11Numeros(rat11s);

        l.setAcumlulado15Numeros(ac15);
        l.setEstimativaPremio(estim);
        l.setAcumuladoEspecial(acuEspecial);

        return l;
    }

    public String getLoto() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("C:\\lotofacil.html"));
            StringBuilder sb = new StringBuilder();

            for (String line : lines) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
