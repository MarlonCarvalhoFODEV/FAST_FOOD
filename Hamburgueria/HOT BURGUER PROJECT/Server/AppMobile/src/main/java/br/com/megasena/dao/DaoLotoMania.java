package br.com.megasena.dao;

import apis.date.DateAPI;
import br.com.comprazencasa.database.DaoAddress;
import br.com.megasena.itens.ITLotoMania;
import br.com.megasena.itens.ITQuina;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoLotoMania {

    public boolean insertNew(ITLotoMania c) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_lotomania (uf, cidade, concurso, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12,b13,b14,b15,b16,b17,b18,b19,b20,");
                sqlConstruct.append("gds_20, gds_19, gds_18, gds_17, gds_16, gds_nenhum, rateio_20, rateio_19, rateio_18, rateio_17, rateio_16, rateio_nenhum, ");
                sqlConstruct.append("acumulado_20, estimativa_premio, acumulado_especial, arrecadacao_total, data) SELECT * FROM (SELECT ? as uf, ? as cidade, ");
                sqlConstruct.append("? as concurso, ? as b1, ? as b2, ? as b3, ? as b4, ? as b5, ? as b6, ? as b7, ? as b8, ? as b9, ? as b10, ? as b11, ? as b12, ");
                sqlConstruct.append("? as b13, ? as b14, ? as b15, ? as b16, ? as b17, ? as b18, ? as b19, ? as b20, ? as gds_20, ? as gds_19, ? as gds_18, ? as gds_17, ");
                sqlConstruct.append("? as gds_15, ? as gds_nenhum, ? as rateio_20, ? as rateio_19, ? as rateio_18, ? as rateio_17, ? as rateio_16, ? as rateio_nenhum, ");
                sqlConstruct.append("? as acumulado_20, ? as estimativa_premio, ? as acumulado_especial, ? as arrecadacao_total, ? as data) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_lotomania WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, c.getUf());
                pst.setString(2, c.getCidade());
                pst.setInt(3, c.getConcurso());
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
                pst.setInt(19, c.getB16());
                pst.setInt(20, c.getB17());
                pst.setInt(21, c.getB18());
                pst.setInt(22, c.getB19());
                pst.setInt(23, c.getB20());
                pst.setInt(24, c.getGanhadoresFX20());
                pst.setInt(25, c.getGanhadoresFX19());
                pst.setInt(26, c.getGanhadoresFX18());
                pst.setInt(27, c.getGanhadoresFX17());
                pst.setInt(28, c.getGanhadoresFX16());
                pst.setString(29, c.getGanhadoresNenhum());
                pst.setString(30, c.getRateioFX20());
                pst.setString(31, c.getRateioFX19());
                pst.setString(32, c.getRateioFX18());
                pst.setString(33, c.getRateioFX17());
                pst.setString(34, c.getRateioFX16());
                pst.setString(35, c.getRateioNenhum());
                pst.setString(36, c.getAcumulado20());
                pst.setString(37, c.getEstimativaPremio());
                pst.setString(38, c.getAcumuladoEspecial());
                pst.setString(39, c.getArrecadacaoTotal());
                pst.setString(40, c.getData());
                pst.setInt(41, c.getConcurso());

                System.out.println("PST: "+pst.toString());
                try {
                    pst.executeUpdate();

                    ResultSet rs = pst.getGeneratedKeys();
                    if (rs.next())
                        c.setIdDB(rs.getInt(1));

                    con.close();
                    pst.close();

                    if (c.getIdDB() > 0)
                        return true;

                } catch (SQLIntegrityConstraintViolationException e) {
                    return false;
                }
                return false;
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

    public boolean insertNew(ArrayList<ITLotoMania> cs) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_lotomania (uf, cidade, concurso, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12,b13,b14,b15,b16,b17,b18,b19,b20,");
                sqlConstruct.append("gds_20, gds_19, gds_18, gds_17, gds_16, gds_nenhum, rateio_20, rateio_19, rateio_18, rateio_17, rateio_16, rateio_nenhum, ");
                sqlConstruct.append("acumulado_20, estimativa_premio, acumulado_especial, arrecadacao_total, data) SELECT * FROM (SELECT ? as uf, ? as cidade, ");
                sqlConstruct.append("? as concurso, ? as b1, ? as b2, ? as b3, ? as b4, ? as b5, ? as b6, ? as b7, ? as b8, ? as b9, ? as b10, ? as b11, ? as b12, ");
                sqlConstruct.append("? as b13, ? as b14, ? as b15, ? as b16, ? as b17, ? as b18, ? as b19, ? as b20, ? as gds_20, ? as gds_19, ? as gds_18, ? as gds_17, ");
                sqlConstruct.append("? as gds_15, ? as gds_nenhum, ? as rateio_20, ? as rateio_19, ? as rateio_18, ? as rateio_17, ? as rateio_16, ? as rateio_nenhum, ");
                sqlConstruct.append("? as acumulado_20, ? as estimativa_premio, ? as acumulado_especial, ? as arrecadacao_total, ? as data) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_lotomania WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                for (ITLotoMania c : cs) {
                    pst.setString(1, c.getUf());
                    pst.setString(2, c.getCidade());
                    pst.setInt(3, c.getConcurso());
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
                    pst.setInt(19, c.getB16());
                    pst.setInt(20, c.getB17());
                    pst.setInt(21, c.getB18());
                    pst.setInt(22, c.getB19());
                    pst.setInt(23, c.getB20());
                    pst.setInt(24, c.getGanhadoresFX20());
                    pst.setInt(25, c.getGanhadoresFX19());
                    pst.setInt(26, c.getGanhadoresFX18());
                    pst.setInt(27, c.getGanhadoresFX17());
                    pst.setInt(28, c.getGanhadoresFX16());
                    pst.setString(29, c.getGanhadoresNenhum());
                    pst.setString(30, c.getRateioFX20());
                    pst.setString(31, c.getRateioFX19());
                    pst.setString(32, c.getRateioFX18());
                    pst.setString(33, c.getRateioFX17());
                    pst.setString(34, c.getRateioFX16());
                    pst.setString(35, c.getRateioNenhum());
                    pst.setString(36, c.getAcumulado20());
                    pst.setString(37, c.getEstimativaPremio());
                    pst.setString(38, c.getAcumuladoEspecial());
                    pst.setString(39, c.getArrecadacaoTotal());
                    pst.setString(40, c.getData());
                    pst.setInt(41, c.getConcurso());

                    pst.addBatch();
                }

                try {
                    int[] results = pst.executeBatch();

                    for (int i : results)
                        if (i == 0)
                            return false;
                        else {
                            ResultSet rs = pst.getGeneratedKeys();
                            if(rs.next()) {
                                // notifica os usuários do novo sorteio
                            }
                        }

                }
                catch (SQLIntegrityConstraintViolationException | BatchUpdateException e){ }

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

    public ArrayList<ITLotoMania> getConcursosQuinaFromWebArray() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotomania/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA38jYEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VAajYsZo!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC42045/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");

        ArrayList<ITLotoMania> concs = new ArrayList<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITLotoMania c = getConcurso(n);
                concs.add(c);
            }
        }
        return concs;
    }

    public Map<Integer,ITLotoMania> getConcursosQuinaFromWebMap() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotomania/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA38jYEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VAajYsZo!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC42045/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Map<Integer, ITLotoMania> concs = new HashMap<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITLotoMania c = getConcurso(n);
                if(!concs.containsKey(c.getConcurso())){
                    concs.put(c.getConcurso(), c);
                }
            }
        }
        return concs;
    }

    public static synchronized final ITLotoMania getConcurso(Elements n) {
        //System.out.println(n.toString());
        // concurso
        String a1 = n.get(0).text();
        // data
        String a2 = n.get(1).text();
        // c1
        String a3 = n.get(2).text();
        // c2
        String a4 = n.get(3).text();
        // c3
        String a5 = n.get(4).text();
        // c4
        String a6 = n.get(5).text();
        // c5
        String a7 = n.get(6).text();
        // arrecadacao total
        String a8 = n.get(7).text();
        // ganhadores quina
        String a9 = n.get(8).text();
        // cidade
        String a10 = n.get(9).text();
        // rateio quina
        String a11 = n.get(10).text();
        // ganbhadores quadra
        String a12 = n.get(11).text();
        // rateio quadra
        String a13 = n.get(12).text();
        String a14 = n.get(13).text();
        String a15 = n.get(14).text();
        String a16 = n.get(15).text();

        String a17 = n.get(16).text();
        String a18 = n.get(17).text();
        String a19 = n.get(18).text();
        String a20 = n.get(19).text();
        String a21 = n.get(20).text();
        String a22 = n.get(21).text();
        String a23 = n.get(22).text();
        String a24 = n.get(23).text();
        String a25 = n.get(24).text();
        String a26 = n.get(25).text();
        String a27 = n.get(26).text();
        String a28 = n.get(27).text();
        String a29 = n.get(28).text();
        String a30 = n.get(29).text();
        String a31 = n.get(30).text();
        String a32 = n.get(31).text();
        String a33 = n.get(32).text();
        String a34 = n.get(33).text();
        String a35 = n.get(34).text();
        String a36 = n.get(35).text();
        String a37 = n.get(36).text();
        String a38 = n.get(37).text();
        String a39 = n.get(38).text();

       /* System.out.println("A01: "+a1);
        System.out.println("A02: "+a2);
        System.out.println("A03: "+a3);
        System.out.println("A04: "+a4);
        System.out.println("A05: "+a5);
        System.out.println("A06: "+a6);
        System.out.println("A07: "+a7);
        System.out.println("A08: "+a8);
        System.out.println("A09: "+a9);

        System.out.println("A10: "+a10);
        System.out.println("A11: "+a11);
        System.out.println("A12: "+a12);
        System.out.println("A13: "+a13);
        System.out.println("A14: "+a14);
        System.out.println("A15: "+a15);
        System.out.println("A16: "+a16);
        System.out.println("A17: "+a17);
        System.out.println("A18: "+a18);
        System.out.println("A19: "+a19);
        System.out.println("A20: "+a20);
        System.out.println("A21: "+a21);
        System.out.println("A22: "+a22);
        System.out.println("A23: "+a23);
        System.out.println("A24: "+a24);
        System.out.println("A25: "+a25);
        System.out.println("A26: "+a26);
        System.out.println("A27: "+a27);
        System.out.println("A28: "+a28);
        System.out.println("A29: "+a29);
        System.out.println("A30: "+a30);
        System.out.println("A31: "+a31);
        System.out.println("A32: "+a32);
        System.out.println("A33: "+a33);
        System.out.println("A34: "+a34);
        System.out.println("A35: "+a35);
        System.out.println("A36: "+a36);
        System.out.println("A37: "+a37);
        System.out.println("A38: "+a38);
        System.out.println("A39: "+a39);*/

        int position = n.size() - 14;
        a26 = n.get(position).text();
        position++;
        a27 = n.get(position).text();
        position++;
        a28 = n.get(position).text();
        position++;
        a29 = n.get(position).text();
        position++;
        a30 = n.get(position).text();
        position++;
        a31 = n.get(position).text();
        position++;
        a32 = n.get(position).text();
        position++;
        a33 = n.get(position).text();
        position++;
        a34 = n.get(position).text();
        position++;
        a35 = n.get(position).text();
        position++;
        a36 = n.get(position).text();
        position++;
        a37 = n.get(position).text();
        position++;
        a38 = n.get(position).text();
        position++;
        a39 = n.get(position).text();
        /*position++;
        a24 = n.get(position).text();
        position++;
        a25 = n.get(position).text();
        position++;
        a26 = n.get(position).text();
        position++;
        a27 = n.get(position).text();
        position++;
        a28 = n.get(position).text();
        position++;
        a29 = n.get(position).text();
        position++;
        a30 = n.get(position).text();
        position++;
        a31 = n.get(position).text();
        position++;
        a32 = n.get(position).text();
        position++;
        a33 = n.get(position).text();
        position++;
        a34 = n.get(position).text();
        position++;
        a35 = n.get(position).text();
        position++;
        a36 = n.get(position).text();
        position++;
        a37 = n.get(position).text();
        position++;
        a38 = n.get(position).text();
        position++;
        a39 = n.get(position).text();*/

        ITLotoMania lotoMania = new ITLotoMania();
        lotoMania.setConcurso(Integer.valueOf(a1));
        lotoMania.setData(DateAPI.getDateManager().formatDate(a2.replace("/", "-"), "dd-MM-yyyy", "yyyy-MM-dd"));
        lotoMania.setB1(Integer.valueOf(a3));
        lotoMania.setB2(Integer.valueOf(a4));
        lotoMania.setB3(Integer.valueOf(a5));
        lotoMania.setB4(Integer.valueOf(a6));
        lotoMania.setB5(Integer.valueOf(a7));
        lotoMania.setB6(Integer.valueOf(a8));
        lotoMania.setB7(Integer.valueOf(a9));
        lotoMania.setB8(Integer.valueOf(a10));
        lotoMania.setB9(Integer.valueOf(a11));
        lotoMania.setB10(Integer.valueOf(a12));
        lotoMania.setB11(Integer.valueOf(a13));
        lotoMania.setB12(Integer.valueOf(a14));
        lotoMania.setB13(Integer.valueOf(a15));
        lotoMania.setB14(Integer.valueOf(a16));
        lotoMania.setB15(Integer.valueOf(a17));
        lotoMania.setB16(Integer.valueOf(a18));
        lotoMania.setB17(Integer.valueOf(a19));
        lotoMania.setB18(Integer.valueOf(a20));
        lotoMania.setB19(Integer.valueOf(a21));
        lotoMania.setB20(Integer.valueOf(a22));
        lotoMania.setArrecadacaoTotal(a23);
        lotoMania.setGanhadoresFX20(Integer.valueOf(a24));
        lotoMania.setCidade(a25);
        lotoMania.setUf(a26);
        lotoMania.setGanhadoresFX19(Integer.valueOf(a27));
        lotoMania.setGanhadoresFX18(Integer.valueOf(a28));
        lotoMania.setGanhadoresFX17(Integer.valueOf(a29));
        lotoMania.setGanhadoresFX16(Integer.valueOf(a30));
        lotoMania.setGanhadoresNenhum(a31);
        lotoMania.setRateioFX20(a32);
        lotoMania.setRateioFX19(a33);
        lotoMania.setRateioFX18(a34);
        lotoMania.setRateioFX17(a35);
        lotoMania.setRateioFX16(a36);
        lotoMania.setRateioNenhum(a37);
        lotoMania.setAcumulado20(a38);
        lotoMania.setEstimativaPremio(a39);

        return lotoMania;
    }

    public ITLotoMania getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *, CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_lotomania where id > 0 ORDER BY id DESC limit 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITLotoMania l = null;
                if (rs.next()) {
                    l = new ITLotoMania();
                    l.setCidade(rs.getString("uf"));
                    l.setData(rs.getString("cidade"));
                    l.setConcurso(rs.getInt("concurso"));
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
                    l.setB16(rs.getInt("b16"));
                    l.setB17(rs.getInt("b17"));
                    l.setB18(rs.getInt("b18"));
                    l.setB19(rs.getInt("b19"));
                    l.setB20(rs.getInt("b20"));
                    l.setGanhadoresFX20(rs.getInt("gds_20"));
                    l.setGanhadoresFX19(rs.getInt("gds_19"));
                    l.setGanhadoresFX18(rs.getInt("gds_18"));
                    l.setGanhadoresFX17(rs.getInt("gds_17"));
                    l.setGanhadoresFX16(rs.getInt("gds_16"));
                    l.setGanhadoresNenhum(rs.getString("gds_nenhum"));
                    l.setAcumulado20(rs.getString("acumulado_20"));
                    l.setEstimativaPremio(rs.getString("estimativa_premio"));
                    l.setAcumuladoEspecial(rs.getString("acumulado_especial"));
                    l.setArrecadacaoTotal(rs.getString("arrecadacao_total"));
                    l.setData(rs.getString("data1"));
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
}
