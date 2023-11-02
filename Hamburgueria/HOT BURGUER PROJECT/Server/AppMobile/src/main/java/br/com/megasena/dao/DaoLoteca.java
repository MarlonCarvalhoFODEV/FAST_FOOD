package br.com.megasena.dao;

import apis.date.DateAPI;
import br.com.comprazencasa.database.DaoAddress;
import br.com.megasena.itens.ITLoteca;
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

public class DaoLoteca {

    public boolean insertNew(ITLoteca c) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_loteca (concurso, cidade, gds_14, gds_13, gds_12, rateio_14, rateio_13, rateio_12, ");
                sqlConstruct.append("c1, c2, c3, c4,c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, acumulado, acumulado_14, arrecadacao_total, estimativa_premio, data)");
                sqlConstruct.append("SELECT * FROM (SELECT ? as concurso, ? as cidade, ? as gds_14, ? as gds_13, ? as gds_12, ? as rateio_14, ? as rateio_13, ? as rateio_12,  ");
                sqlConstruct.append("? as c1,? as  c2,? as  c3,? as  c4,? as c5,? as  c6,? as  c7,? as  c8,? as  c9,? as  c10,? as  c11,? as  c12,? as  c13,? as c14,? as  acumulado,");
                sqlConstruct.append("? as acumulado_14, ? as arrecadacao_total, ? as estimativa_premio,? as  data");
                sqlConstruct.append(") AS temp WHERE NOT EXISTS ( SELECT concurso FROM st_loteca WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, c.getConcurso());
                pst.setString(2, c.getCidade());
                pst.setInt(3, c.getGanhadoresFX14());
                pst.setInt(4, c.getGanhadoresFX13());
                pst.setInt(5, c.getGanhadoresFX12());
                pst.setString(6, c.getRateioFX14());
                pst.setString(7, c.getRateioFX13());
                pst.setString(8, c.getRateioFX12());
                pst.setInt(9, c.getC1());
                pst.setInt(10, c.getC2());
                pst.setInt(11, c.getC3());
                pst.setInt(12, c.getC4());
                pst.setInt(13, c.getC5());
                pst.setInt(14, c.getC6());
                pst.setInt(15, c.getC7());
                pst.setInt(16, c.getC8());
                pst.setInt(17, c.getC9());
                pst.setInt(18, c.getC10());
                pst.setInt(19, c.getC11());
                pst.setInt(20, c.getC12());
                pst.setInt(21, c.getC13());
                pst.setInt(22, c.getC14());
                pst.setBoolean(23, c.isAcumulado());
                pst.setString(24, c.getValorAcumulado14());
                pst.setString(25, c.getArrecadacaoTotal());
                pst.setString(26, c.getEstimativaPremio());
                pst.setString(27, c.getData());
                pst.setInt(28, c.getConcurso());

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

    public boolean insertNew(ArrayList<ITLoteca> cs) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_loteca (concurso, cidade, gds_14, gds_13, gds_12, rateio_14, rateio_13, rateio_12, ");
                sqlConstruct.append("c1, c2, c3, c4,c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, acumulado, acumulado_14, arrecadacao_total, estimativa_premio, data)");
                sqlConstruct.append("SELECT * FROM (SELECT ? as concurso, ? as cidade, ? as gds_14, ? as gds_13, ? as gds_12, ? as rateio_14, ? as rateio_13, ? as rateio_12,  ");
                sqlConstruct.append("? as c1,? as  c2,? as  c3,? as  c4,? as c5,? as  c6,? as  c7,? as  c8,? as  c9,? as  c10,? as  c11,? as  c12,? as  c13,? as c14,? as  acumulado,");
                sqlConstruct.append("? as acumulado_14, ? as arrecadacao_total, ? as estimativa_premio,? as  data");
                sqlConstruct.append(") AS temp WHERE NOT EXISTS ( SELECT concurso FROM st_loteca WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                for (ITLoteca c : cs) {
                    pst.setInt(1, c.getConcurso());
                    pst.setString(2, c.getCidade());
                    pst.setInt(3, c.getGanhadoresFX14());
                    pst.setInt(4, c.getGanhadoresFX13());
                    pst.setInt(5, c.getGanhadoresFX12());
                    pst.setString(6, c.getRateioFX14());
                    pst.setString(7, c.getRateioFX13());
                    pst.setString(8, c.getRateioFX12());
                    pst.setInt(9, c.getC1());
                    pst.setInt(10, c.getC2());
                    pst.setInt(11, c.getC3());
                    pst.setInt(12, c.getC4());
                    pst.setInt(13, c.getC5());
                    pst.setInt(14, c.getC6());
                    pst.setInt(15, c.getC7());
                    pst.setInt(16, c.getC8());
                    pst.setInt(17, c.getC9());
                    pst.setInt(18, c.getC10());
                    pst.setInt(19, c.getC11());
                    pst.setInt(20, c.getC12());
                    pst.setInt(21, c.getC13());
                    pst.setInt(22, c.getC14());
                    pst.setBoolean(23, c.isAcumulado());
                    pst.setString(24, c.getValorAcumulado14());
                    pst.setString(25, c.getArrecadacaoTotal());
                    pst.setString(26, c.getEstimativaPremio());
                    pst.setString(27, c.getData());
                    pst.setInt(28, c.getConcurso());

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

    public ArrayList<ITLoteca> getConcursosFromWebArray() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/loteca/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA38jYEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VAajYsZo!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC42045/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");

        ArrayList<ITLoteca> concs = new ArrayList<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITLoteca c = getConcurso(n);
                concs.add(c);
            }
        }
        return concs;
    }

    public Map<Integer,ITLoteca> getConcursosFromWebMap() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/loteca/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbz8vTxNDRy9_Y2NQ13CDA3cDYEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wBmoxN_FydLAGAgNTKEK8DkRrACPGwpyQyMMMj0VAbNnwlU!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC420O7/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Map<Integer, ITLoteca> concs = new HashMap<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITLoteca c = getConcurso(n);
                if(!concs.containsKey(c.getConcurso())){
                    concs.put(c.getConcurso(), c);
                }
            }
        }
        return concs;
    }

    public static synchronized final ITLoteca getConcurso(Elements n) {
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

        int position = n.size() - 23;

        a5 = n.get(position).text();
        position++;
        a6 = n.get(position).text();
        position++;
        a7 = n.get(position).text();
        position++;
        a8 = n.get(position).text();
        position++;
        a9 = n.get(position).text();
        position++;
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
        position++;
        a22 = n.get(position).text();
        position++;
        a23 = n.get(position).text();
        position++;
        a24 = n.get(position).text();
        position++;
        a25 = n.get(position).text();
        position++;
        a26 = n.get(position).text();
        position++;
        a27 = n.get(position).text();

        ITLoteca loteca = new ITLoteca();
        loteca.setConcurso(Integer.valueOf(a1));
        loteca.setData(DateAPI.getDateManager().formatDate(a2.replace("/", "-"), "dd-MM-yyyy", "yyyy-MM-dd"));
        loteca.setGanhadoresFX14(Integer.valueOf(a3));
        loteca.setCidade(a4);
        loteca.setRateioFX14(a5);
        loteca.setAcumulado(Boolean.valueOf(a6.equals("NAO")?false:true));
        loteca.setValorAcumulado14(a7);
        loteca.setGanhadoresFX13(Integer.valueOf(a8));
        loteca.setRateioFX13(a9);
        loteca.setGanhadoresFX12(Integer.valueOf(a10));
        loteca.setRateioFX12(a11);
        loteca.setC1(Integer.valueOf(a12.matches("[0-9].*")?a12:"0"));
        loteca.setC2(Integer.valueOf(a13.matches("[0-9].*")?a12:"0"));
        loteca.setC3(Integer.valueOf(a14.matches("[0-9].*")?a12:"0"));
        loteca.setC4(Integer.valueOf(a15.matches("[0-9].*")?a12:"0"));
        loteca.setC5(Integer.valueOf(a16.matches("[0-9].*")?a12:"0"));
        loteca.setC6(Integer.valueOf(a17.matches("[0-9].*")?a12:"0"));
        loteca.setC7(Integer.valueOf(a18.matches("[0-9].*")?a12:"0"));
        loteca.setC8(Integer.valueOf(a19.matches("[0-9].*")?a12:"0"));
        loteca.setC9(Integer.valueOf(a20.matches("[0-9].*")?a12:"0"));
        loteca.setC10(Integer.valueOf(a21.matches("[0-9].*")?a12:"0"));
        loteca.setC11(Integer.valueOf(a22.matches("[0-9].*")?a12:"0"));
        loteca.setC12(Integer.valueOf(a23.matches("[0-9].*")?a12:"0"));
        loteca.setC13(Integer.valueOf(a24.matches("[0-9].*")?a12:"0"));
        loteca.setC14(Integer.valueOf(a25.matches("[0-9].*")?a12:"0"));
        loteca.setArrecadacaoTotal(a26);
        loteca.setEstimativaPremio(a27);

        return loteca;
    }

    public ITLoteca getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *, CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_loteca where id > 0 ORDER BY id DESC limit 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITLoteca l = null;
                if (rs.next()) {
                    l = new ITLoteca();
                    l.setConcurso(rs.getInt("concurso"));
                    l.setCidade(rs.getString("cidade"));
                    l.setGanhadoresFX14(rs.getInt("gds_14"));
                    l.setGanhadoresFX13(rs.getInt("gds_13"));
                    l.setGanhadoresFX12(rs.getInt("gds_12"));
                    l.setRateioFX14(rs.getString("rateio_14"));
                    l.setRateioFX13(rs.getString("rateio_13"));
                    l.setRateioFX12(rs.getString("rateio_12"));
                    l.setC1(rs.getInt("c1"));
                    l.setC2(rs.getInt("c2"));
                    l.setC3(rs.getInt("c3"));
                    l.setC4(rs.getInt("c4"));
                    l.setC5(rs.getInt("c5"));
                    l.setC6(rs.getInt("c6"));
                    l.setC7(rs.getInt("c7"));
                    l.setC8(rs.getInt("c8"));
                    l.setC9(rs.getInt("c9"));
                    l.setC10(rs.getInt("c10"));
                    l.setC11(rs.getInt("c11"));
                    l.setC12(rs.getInt("c12"));
                    l.setC13(rs.getInt("c13"));
                    l.setC14(rs.getInt("c14"));
                    l.setAcumulado(rs.getBoolean("acumulado"));
                    l.setValorAcumulado14(rs.getString("acumulado_14"));
                    l.setArrecadacaoTotal(rs.getString("arrecadacao_total"));
                    l.setEstimativaPremio(rs.getString("estimativa_premio"));
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
