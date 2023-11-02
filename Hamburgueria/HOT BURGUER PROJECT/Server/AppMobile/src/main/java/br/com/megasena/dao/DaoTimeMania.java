package br.com.megasena.dao;

import apis.date.DateAPI;
import br.com.comprazencasa.database.DaoAddress;
import br.com.megasena.itens.ITLotoMania;
import br.com.megasena.itens.ITTimeMania;
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

public class DaoTimeMania {
    public boolean insertNew(ITTimeMania c) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_timemania (cidade, concurso, b1,b2,b3,b4,b5,b6,b7, time_coracao, gds_7,");
                sqlConstruct.append("gds_6, gds_5, gds_4, gds_3, gds_time_coracao, rateio_7,rateio_6,rateio_5,rateio_4,rateio_3, ");
                sqlConstruct.append("acumulado, estimativa_premio, arrecadacao_total, data) ");
                sqlConstruct.append("SELECT * FROM (SELECT ? as cidade, ?  as concurso, ?  as b1, ?  as b2, ?  as b3, ? as b4, ?  as b5, ?  as b6, ?  as b7, ?  as time_coracao, ?  as gds_7, ");
                sqlConstruct.append("?  as  gds_6, ?  as  gds_5,?  as gds_4,?  as gds_3,?  as gds_time_coracao,?  as rateio_7,?  as rateio_6,? as rateio_5,? as rateio_4,? as rateio_3, ");
                sqlConstruct.append("? as acumulado, ? as estimativa_premio, ? as arrecadacao_total, ? as data");
                sqlConstruct.append(") AS tmp WHERE NOT EXISTS ( SELECT concurso FROM st_timemania WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, c.getCidade());
                pst.setInt(2, c.getConcurso());
                pst.setInt(3, c.getB1());
                pst.setInt(4, c.getB2());
                pst.setInt(5, c.getB3());
                pst.setInt(6, c.getB4());
                pst.setInt(7, c.getB5());
                pst.setInt(8, c.getB6());
                pst.setInt(9, c.getB7());
                pst.setString(10, c.getTimeDeCoracao());
                pst.setInt(11, c.getGanhadoresFX7());
                pst.setInt(12, c.getGanhadoresFX6());
                pst.setInt(13, c.getGanhadoresFX5());
                pst.setInt(14, c.getGanhadoresFX4());
                pst.setInt(15, c.getGanhadoresFX3());
                pst.setInt(16, c.getGanhadoresTimeCoracao());
                pst.setString(17, c.getRateioFX7());
                pst.setString(18, c.getRateioFX6());
                pst.setString(19, c.getRateioFX5());
                pst.setString(20, c.getRateioFX4());
                pst.setString(21, c.getRateioFX3());
                pst.setString(22, c.getValorAcumulado());
                pst.setString(23, c.getEstimativaPremio());
                pst.setString(24, c.getValorArrecadado());
                pst.setString(25, c.getData());
                pst.setInt(26, c.getConcurso());

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

    public boolean insertNew(ArrayList<ITTimeMania> cs) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_timemania (cidade, concurso, b1,b2,b3,b4,b5,b6,b7, time_coracao, gds_7,");
                sqlConstruct.append("gds_6, gds_5, gds_4, gds_3, gds_time_coracao, rateio_7,rateio_6,rateio_5,rateio_4,rateio_3, ");
                sqlConstruct.append("acumulado, estimativa_premio, arrecadacao_total, data) ");
                sqlConstruct.append("SELECT * FROM (SELECT ? as cidade, ?  as concurso, ?  as b1, ?  as b2, ?  as b3, ? as b4, ?  as b5, ?  as b6, ?  as b7, ?  as time_coracao, ?  as gds_7, ");
                sqlConstruct.append("?  as  gds_6, ?  as  gds_5,?  as gds_4,?  as gds_3,?  as gds_time_coracao,?  as rateio_7,?  as rateio_6,? as rateio_5,? as rateio_4,? as rateio_3, ");
                sqlConstruct.append("? as acumulado, ? as estimativa_premio, ? as arrecadacao_total, ? as data");
                sqlConstruct.append(") AS tmp WHERE NOT EXISTS ( SELECT concurso FROM st_timemania WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                for (ITTimeMania c : cs) {
                    pst.setString(1, c.getCidade());
                    pst.setInt(2, c.getConcurso());
                    pst.setInt(3, c.getB1());
                    pst.setInt(4, c.getB2());
                    pst.setInt(5, c.getB3());
                    pst.setInt(6, c.getB4());
                    pst.setInt(7, c.getB5());
                    pst.setInt(8, c.getB6());
                    pst.setInt(9, c.getB7());
                    pst.setString(10, c.getTimeDeCoracao());
                    pst.setInt(11, c.getGanhadoresFX7());
                    pst.setInt(12, c.getGanhadoresFX6());
                    pst.setInt(13, c.getGanhadoresFX5());
                    pst.setInt(14, c.getGanhadoresFX4());
                    pst.setInt(15, c.getGanhadoresFX3());
                    pst.setInt(16, c.getGanhadoresTimeCoracao());
                    pst.setString(17, c.getRateioFX7());
                    pst.setString(18, c.getRateioFX6());
                    pst.setString(19, c.getRateioFX5());
                    pst.setString(20, c.getRateioFX4());
                    pst.setString(21, c.getRateioFX3());
                    pst.setString(22, c.getValorAcumulado());
                    pst.setString(23, c.getEstimativaPremio());
                    pst.setString(24, c.getValorArrecadado());
                    pst.setString(25, c.getData());
                    pst.setInt(26, c.getConcurso());

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

    public ArrayList<ITTimeMania> getConcursosQuinaFromWebArray() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/timemania/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA1MzIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VASrq9qk!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC42047/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");

        ArrayList<ITTimeMania> concs = new ArrayList<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITTimeMania c = getConcurso(n);
                concs.add(c);
            }
        }
        return concs;
    }

    public Map<Integer,ITTimeMania> getConcursosFromWebMap() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/timemania/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA1MzIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VASrq9qk!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC42047/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Map<Integer, ITTimeMania> concs = new HashMap<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITTimeMania c = getConcurso(n);
                if(!concs.containsKey(c.getConcurso())){
                    concs.put(c.getConcurso(), c);
                }
            }
        }

        doc = Jsoup.connect("https://campograndesantos.wordpress.com/38-relacao-de-postscolocados-neste-blog/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        lines = doc.getElementsByClass("entry");
        int counter = 0;
        for (Element linha : lines) {

            String n = linha.getElementsByTag("p")
                    .toString()
                    .replaceAll("<p>","")
                    .replaceAll("<br> ","\n")
                    .replace("\n" +
                            "_________________________________________________________</p>\n" +
                            "________________________________________</p>\n" +
                            "_______________________________________________</p>\n" +
                            "Veja também resultado da:</p>\n" +
                            "Dupla-sena</p>\n" +
                            "<a href=\"../85-todos-os-resultados-da-dupla-sena-lista-de-concursos/\">https://campograndesantos.wordpress.com/85-todos-os-resultados-da-dupla-sena-lista-de-concursos/</a></p>\n" +
                            "Lotofácil</p>\n" +
                            "<a href=\"../86-todos-os-resultados-da-lotofacil-lista-de-concursos/\">https://campograndesantos.wordpress.com/86-todos-os-resultados-da-lotofacil-lista-de-concursos/</a></p>\n" +
                            "Lotomania</p>\n" +
                            "<a href=\"../todos-os-resultados-da-lotomania/\">https://campograndesantos.wordpress.com/todos-os-resultados-da-lotomania/</a></p>\n" +
                            "Quina</p>\n" +
                            "<a href=\"../88-todos-os-resultados-da-quina-lista-de-concursos/\">https://campograndesantos.wordpress.com/88-todos-os-resultados-da-quina-lista-de-concursos/</a></p>\n" +
                            "Mega-Sena</p>\n" +
                            "<a href=\"../64-todos-os-resultados-da-mega-sena-2/\">https://campograndesantos.wordpress.com/64-todos-os-resultados-da-mega-sena-2/</a></p>","")
                    .replace("<a href=\"http://loterias.caixa.gov.br/wps/portal/loterias/landing/timemania\" rel=\"nofollow\">http://loterias.caixa.gov.br/wps/portal/loterias/landing/timemania</a></p>","");

            String[] results = n.split("\n");

            for(String li : results){
                if(li != null && !li.isEmpty()){
                    int concurso = Integer.valueOf(li.split(" ")[0].replace("\n",""));
                    int n1 = Integer.valueOf(li.split(" ")[2]);
                    int n2 = Integer.valueOf(li.split(" ")[3]);
                    int n3 = Integer.valueOf(li.split(" ")[4]);
                    int n4 = Integer.valueOf(li.split(" ")[5]);
                    int n5 = Integer.valueOf(li.split(" ")[6]);
                    int n6 = Integer.valueOf(li.split(" ")[7]);
                    int n7 = Integer.valueOf(li.split(" ")[8]);

                    if(concs.containsKey(concurso)){
                        ITTimeMania time = concs.get(concurso);
                        time.setB1(n1);
                        time.setB2(n2);
                        time.setB3(n3);
                        time.setB4(n4);
                        time.setB5(n5);
                        time.setB6(n6);
                        time.setB7(n7);
                    }
                }
            }
        }

        return concs;
    }

    public static synchronized final ITTimeMania getConcurso(Elements n) {
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
        /*String a27 = n.get(26).text();
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
        String a39 = n.get(38).text();*/

        /*System.out.println("A01: "+a1);
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
        System.out.println("A26: "+a26);*/
         /*System.out.println("A27: "+a27);
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
        a13 = n.get(position).text();
        position++;
        a14 = n.get(position).text();
        position++;
        a15 = n.get(position).text();
        position++;
        a16 = n.get(position).text();
        position++;
        a17 = n.get(position).text();
        position++;
        a18 = n.get(position).text();
        position++;
        a19 = n.get(position).text();
        position++;
        a20 = n.get(position).text();
        position++;
        a21 = n.get(position).text();
        position++;
        a22 = n.get(position).text();
        position++;
        a23 = n.get(position).text();
        position++;
        a24 = n.get(position).text();
        position++;
        a25 = n.get(position).text();
        position++;
        //a39 = n.get(position).text();
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

        ITTimeMania timeMania = new ITTimeMania();
        timeMania.setConcurso(Integer.valueOf(a1));
        timeMania.setData(DateAPI.getDateManager().formatDate(a2.replace("/", "-"), "dd-MM-yyyy", "yyyy-MM-dd"));
        timeMania.setB1(0);
        timeMania.setB2(0);
        timeMania.setB3(0);
        timeMania.setB4(0);
        timeMania.setB5(0);
        timeMania.setB6(0);
        timeMania.setB7(0);
        timeMania.setTimeDeCoracao(a10);
        timeMania.setValorArrecadado(a11);
        timeMania.setGanhadoresFX7(Integer.valueOf(a12));
        timeMania.setCidade(a13);
        timeMania.setGanhadoresFX6(Integer.valueOf(a14));
        timeMania.setGanhadoresFX5(Integer.valueOf(a15));
        timeMania.setGanhadoresFX4(Integer.valueOf(a16));
        timeMania.setGanhadoresFX3(Integer.valueOf(a17));

        timeMania.setGanhadoresTimeCoracao(Integer.valueOf(a18));
        timeMania.setRateioFX7(a19);
        timeMania.setRateioFX6(a20);
        timeMania.setRateioFX5(a21);
        timeMania.setRateioFX4(a22);
        timeMania.setRateioFX3(a23);
        timeMania.setValorTimeCoracao(a24);
        timeMania.setValorAcumulado(a25);
        timeMania.setEstimativaPremio(a26);

        return timeMania;
    }

    public ITTimeMania getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *, CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_timemania where id > 0 ORDER BY id DESC limit 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITTimeMania l = null;
                if (rs.next()) {
                    l = new ITTimeMania();
                    l.setCidade(rs.getString("cidade"));
                    l.setData(rs.getString("data1"));
                    l.setConcurso(rs.getInt("concurso"));
                    l.setB1(rs.getInt("b1"));
                    l.setB2(rs.getInt("b2"));
                    l.setB3(rs.getInt("b3"));
                    l.setB4(rs.getInt("b4"));
                    l.setB5(rs.getInt("b5"));
                    l.setB6(rs.getInt("b6"));
                    l.setB7(rs.getInt("b7"));
                    l.setTimeDeCoracao(rs.getString("time_coracao"));
                    l.setGanhadoresFX7(rs.getInt("gds_7"));
                    l.setGanhadoresFX6(rs.getInt("gds_6"));
                    l.setGanhadoresFX5(rs.getInt("gds_5"));
                    l.setGanhadoresFX4(rs.getInt("gds_4"));
                    l.setGanhadoresFX3(rs.getInt("gds_3"));
                    l.setGanhadoresTimeCoracao(rs.getInt("gds_time_coracao"));
                    l.setRateioFX7(rs.getString("rateio_7"));
                    l.setRateioFX6(rs.getString("rateio_6"));
                    l.setRateioFX5(rs.getString("rateio_5"));
                    l.setRateioFX4(rs.getString("rateio_4"));
                    l.setRateioFX3(rs.getString("rateio_3"));
                    l.setValorTimeCoracao(rs.getString("val_time_coracao"));
                    l.setValorAcumulado(rs.getString("acumulado"));
                    l.setEstimativaPremio(rs.getString("estimativa_premio"));
                    l.setValorArrecadado(rs.getString("arrecadacao_total"));
                    l.setData(rs.getString("data"));
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
/*
    public ITTimeMania getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *, CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_timeMania where id > 0 ORDER BY id DESC limit 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITTimeMania l = null;
                if (rs.next()) {
                    l = new ITTimeMania();
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
    }*/
}
