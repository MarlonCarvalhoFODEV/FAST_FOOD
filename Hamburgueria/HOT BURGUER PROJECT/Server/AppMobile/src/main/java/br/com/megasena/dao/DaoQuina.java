package br.com.megasena.dao;

import apis.date.DateAPI;
import br.com.comprazencasa.database.DaoAddress;
import br.com.megasena.itens.ITLotoFacil;
import br.com.megasena.itens.ITQuina;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoQuina {

    public boolean insertNew(ITQuina c) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_quina(cidade, concurso, c1, c2, c3, c4, c5, gds_5, gds_4, gds_3, gds_2, rateio_5,");
                sqlConstruct.append("rateio_4, rateio_3, rateio_2, acumulado, val_acumulado, arrecadacao_total,");
                sqlConstruct.append("estimativa_premio, val_acumulado_especial, data) SELECT * FROM (SELECT ? as cidade, ? as concurso, ? as c1, ");
                sqlConstruct.append("? as c2, ? as c3, ? as c4, ? as c5, ? as gds_5, ? as gds_4, ? as gds_3, ? as gds_2, ");
                sqlConstruct.append("? as rateio_5, ? as rateio_4, ? as rateio_3, ? as rateio_2,? as acumulado, ? as val_acumulado,");
                sqlConstruct.append("? as arrecadacao_total, ? as estimativa_premio, ? as val_acumulado_especial, ? as data) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_quina WHERE concurso = ? ) LIMIT 1;");


                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, c.getCidade());
                pst.setInt(2, c.getConcurso());
                pst.setInt(3, c.getC1());
                pst.setInt(4, c.getC2());
                pst.setInt(5, c.getC3());
                pst.setInt(6, c.getC4());
                pst.setInt(7, c.getC5());
                pst.setInt(8, c.getGanhadoresFx5());
                pst.setInt(9, c.getGanhadoresFx4());
                pst.setInt(10, c.getGanhadoresFx3());
                pst.setInt(11, c.getGanhadoresFx2());
                pst.setString(12, c.getRateioFx5());
                pst.setString(13, c.getRateioFx4());
                pst.setString(14, c.getRateioFx3());
                pst.setString(15, c.getRateioFx2());
                pst.setBoolean(16, c.isAcumulado());
                pst.setString(17, c.getAcumuladoValor());
                pst.setString(18, c.getArrecadacaoTotal());
                pst.setString(19, c.getEstimativaPremioProximoSorteio());
                pst.setString(20, c.getAcumuladoEspecial());
                pst.setString(21, c.getData());
                pst.setInt(22, c.getConcurso());

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

    public boolean insertNew(ArrayList<ITQuina> cs) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_quina(cidade, concurso, c1, c2, c3, c4, c5, gds_5, gds_4, gds_3, gds_2, rateio_5,");
                sqlConstruct.append("rateio_4, rateio_3, rateio_2, acumulado, val_acumulado, arrecadacao_total,");
                sqlConstruct.append("estimativa_premio, val_acumulado_especial, data) SELECT * FROM (SELECT ? as cidade, ? as concurso, ? as c1, ");
                sqlConstruct.append("? as c2, ? as c3, ? as c4, ? as c5, ? as gds_5, ? as gds_4, ? as gds_3, ? as gds_2, ");
                sqlConstruct.append("? as rateio_5, ? as rateio_4, ? as rateio_3, ? as rateio_2,? as acumulado, ? as val_acumulado,");
                sqlConstruct.append("? as arrecadacao_total, ? as estimativa_premio, ? as val_acumulado_especial, ? as data) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_quina WHERE concurso = ? ) LIMIT 1;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                for (ITQuina c : cs) {
                    pst.setString(1, c.getCidade());
                    pst.setInt(2, c.getConcurso());
                    pst.setInt(3, c.getC1());
                    pst.setInt(4, c.getC2());
                    pst.setInt(5, c.getC3());
                    pst.setInt(6, c.getC4());
                    pst.setInt(7, c.getC5());
                    pst.setInt(8, c.getGanhadoresFx5());
                    pst.setInt(9, c.getGanhadoresFx4());
                    pst.setInt(10, c.getGanhadoresFx3());
                    pst.setInt(11, c.getGanhadoresFx2());
                    pst.setString(12, c.getRateioFx5());
                    pst.setString(13, c.getRateioFx4());
                    pst.setString(14, c.getRateioFx3());
                    pst.setString(15, c.getRateioFx2());
                    pst.setBoolean(16, c.isAcumulado());
                    pst.setString(17, c.getAcumuladoValor());
                    pst.setString(18, c.getArrecadacaoTotal());
                    pst.setString(19, c.getEstimativaPremioProximoSorteio());
                    pst.setString(20, c.getAcumuladoEspecial());
                    pst.setString(21, c.getData());
                    pst.setInt(22, c.getConcurso());

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

    public ArrayList<ITQuina> getConcursosQuinaFromWebArray() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/quina/!ut/p/a1/jc69DoIwAATgZ_EJepS2wFgoaUswsojYxXQyTfgbjM9vNS4Oordd8l1yxJGBuNnfw9XfwjL78dmduIikhYFGA0tzSFZ3tG_6FCmP4BxBpaVhWQuA5RRWlUZlxR6w4r89vkTi1_5E3CfRXcUhD6osEAHA32Dr4gtsfFin44Bgdw9WWSwj/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC420O4/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(1).getElementsByTag("tr");

        ArrayList<ITQuina> concs = new ArrayList<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITQuina c = getConcurso(n);
                concs.add(c);
            }
        }
        return concs;
    }

    public Map<Integer,ITQuina> getConcursosQuinaFromWebMap() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/quina/!ut/p/a1/jc69DoIwAATgZ_EJepS2wFgoaUswsojYxXQyTfgbjM9vNS4Oordd8l1yxJGBuNnfw9XfwjL78dmduIikhYFGA0tzSFZ3tG_6FCmP4BxBpaVhWQuA5RRWlUZlxR6w4r89vkTi1_5E3CfRXcUhD6osEAHA32Dr4gtsfFin44Bgdw9WWSwj/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC420O4/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Map<Integer, ITQuina> concs = new HashMap<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITQuina c = getConcurso(n);
                if(!concs.containsKey(c.getConcurso())){
                    concs.put(c.getConcurso(), c);
                }
            }
        }
        return concs;
    }

    public static synchronized final ITQuina getConcurso(Elements n) {
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
        System.out.println("A21: "+a21);*/


        int position = n.size() - 12;

        a10 = n.get(position).text();
        position++;
        a11 = n.get(position).text();
        position++;
        a12 = n.get(position).text();
        position++;
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

        ITQuina quina = new ITQuina();
        quina.setConcurso(Integer.valueOf(a1));
        quina.setData(DateAPI.getDateManager().formatDate(a2.replace("/", "-"), "dd-MM-yyyy", "yyyy-MM-dd"));
        quina.setC1(Integer.valueOf(a3));
        quina.setC2(Integer.valueOf(a4));
        quina.setC3(Integer.valueOf(a5));
        quina.setC4(Integer.valueOf(a6));
        quina.setC5(Integer.valueOf(a7));
        quina.setArrecadacaoTotal(a8);
        quina.setGanhadoresFx5(Integer.valueOf(a9));
        quina.setCidade(a10);
        quina.setRateioFx5(a11);
        quina.setGanhadoresFx4(Integer.valueOf(a12));
        quina.setRateioFx4(a13);
        quina.setGanhadoresFx3(Integer.valueOf(a14));
        quina.setRateioFx3(a15);
        quina.setGanhadoresFx2(Integer.valueOf(a16));
        quina.setRateioFx2(a17);
        quina.setAcumulado(a18.equals("NAO")?false:true);
        quina.setEstimativaPremioProximoSorteio(a19);
        quina.setAcumuladoValor(a20);
        quina.setAcumuladoEspecial(a21);

        return quina;
    }

    public ITQuina getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *, CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_quina where id > 0 ORDER BY id DESC limit 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITQuina l = null;
                if (rs.next()) {
                    l = new ITQuina();
                    l.setCidade(rs.getString("cidade"));
                    l.setData(rs.getString("data1"));
                    l.setConcurso(rs.getInt("concurso"));
                    l.setC1(rs.getInt("c1"));
                    l.setC2(rs.getInt("c2"));
                    l.setC3(rs.getInt("c3"));
                    l.setC4(rs.getInt("c4"));
                    l.setC5(rs.getInt("c5"));
                    l.setGanhadoresFx5(rs.getInt("gds_5"));
                    l.setGanhadoresFx4(rs.getInt("gds_4"));
                    l.setGanhadoresFx3(rs.getInt("gds_3"));
                    l.setGanhadoresFx2(rs.getInt("gds_2"));
                    l.setRateioFx5(rs.getString("rateio_5"));
                    l.setRateioFx4(rs.getString("rateio_4"));
                    l.setRateioFx3(rs.getString("rateio_3"));
                    l.setRateioFx2(rs.getString("rateio_2"));
                    l.setAcumulado(rs.getBoolean("acumulado"));
                    l.setAcumuladoValor(rs.getString("val_acumulado"));
                    l.setEstimativaPremioProximoSorteio(rs.getString("estimativa_premio"));
                    l.setAcumuladoEspecial(rs.getString("val_acumulado_especial"));
                    l.setArrecadacaoTotal(rs.getString("arrecadacao_total"));
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
