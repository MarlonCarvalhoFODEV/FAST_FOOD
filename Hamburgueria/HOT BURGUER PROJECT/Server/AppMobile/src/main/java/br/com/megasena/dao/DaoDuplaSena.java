package br.com.megasena.dao;

import apis.date.DateAPI;
import br.com.comprazencasa.database.DaoAddress;
import br.com.megasena.itens.ITDuplaSena;
import br.com.megasena.itens.ITLotoMania;
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

public class DaoDuplaSena {

    public boolean insertNew(ITDuplaSena c) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_dupla_sena(concurso, c1A,c2A,c3A,c4A,c5A,c6A,cidade,acumulado_sena,acumulado_sena_val, arrecadacao_total, ");
                sqlConstruct.append("gds_6A, gds_5A, gds_4A, gds_3A, rateio_6A, rateio_5A, rateio_4A, rateio_3A, c1B, c2B, c3B, c4B, c5B, c6B, gds_6B, gds_5B, gds_4B, gds_3B, ");
                sqlConstruct.append("rateio_6B, rateio_5B, rateio_4B, rateio_3B, estimativa_premio, acumulado_especial, data)");
                sqlConstruct.append("SELECT * FROM (SELECT ? as concurso, ? as c1A,? as c2A,? as c3A,? as c4A,? as c5A,? as c6A,? as cidade,? as acumulado_sena,? as acumulado_sena_val, ");
                sqlConstruct.append("? as  arrecadacao_total,? as gds_6A,? as gds_5A,? as  gds_4A,? as  gds_3A,? as  rateio_6A,? as  rateio_5A,? as  rateio_4A, ");
                sqlConstruct.append("? as rateio_3A,? as  c1B,? as  c2B, ? as c3B,? as  c4B,? as  c5B,? as  c6B,? as  gds_6B,? as  gds_5B,? as  gds_4B,? as  gds_3B, ");
                sqlConstruct.append("? as rateio_6B, ? as rateio_5B, ? as rateio_4B, ? as rateio_3B, ? as estimativa_premio, ? as acumulado_especial, ? as data) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_dupla_sena WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, c.getConcurso());
                pst.setInt(2, c.getC1A());
                pst.setInt(3, c.getC2A());
                pst.setInt(4, c.getC3A());
                pst.setInt(5, c.getC4A());
                pst.setInt(6, c.getC5A());
                pst.setInt(7, c.getC6A());
                pst.setString(8, c.getCidade());
                pst.setBoolean(9, c.isAcumuladoSena());
                pst.setString(10, c.getAcumuladoSenaValor());
                pst.setString(11, c.getArrecadacaoTotal());
                pst.setInt(12, c.getGanhadoresSenaA());
                pst.setInt(13, c.getGanhadoresQuinaA());
                pst.setInt(14, c.getGanhadoresQuadraA());
                pst.setInt(15, c.getGanhadoresTernoA());
                pst.setString(16, c.getRateioSenaA());
                pst.setString(17, c.getRateioQuinaA());
                pst.setString(18, c.getRateioQuadraA());
                pst.setString(19, c.getRateioTernoA());
                pst.setInt(20, c.getC1B());
                pst.setInt(21, c.getC2B());
                pst.setInt(22, c.getC3B());
                pst.setInt(23, c.getC4B());
                pst.setInt(24, c.getC5B());
                pst.setInt(25, c.getC6B());
                pst.setInt(26, c.getGanhadoresSenaB());
                pst.setInt(27, c.getGanhadoresQuinaB());
                pst.setInt(28, c.getGanhadoresQuadraB());
                pst.setInt(29, c.getGanhadoresTernoB());
                pst.setString(30, c.getRateioSenaB());
                pst.setString(31, c.getRateioQuinaB());
                pst.setString(32, c.getRateioQuadraB());
                pst.setString(33, c.getRateioTernoB());
                pst.setString(34, c.getEstimativaPremio());
                pst.setString(35, c.getAcumuladoEspecialPascoa());
                pst.setString(36, c.getData());
                pst.setInt(37, c.getConcurso());

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

    public boolean insertNew(ArrayList<ITDuplaSena> cs) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_dupla_sena(concurso, c1A,c2A,c3A,c4A,c5A,c6A,cidade,acumulado_sena,acumulado_sena_val, arrecadacao_total, ");
                sqlConstruct.append("gds_6A, gds_5A, gds_4A, gds_3A, rateio_6A, rateio_5A, rateio_4A, rateio_3A, c1B, c2B, c3B, c4B, c5B, c6B, gds_6B, gds_5B, gds_4B, gds_3B, ");
                sqlConstruct.append("rateio_6B, rateio_5B, rateio_4B, rateio_3B, estimativa_premio, acumulado_especial, data)");
                sqlConstruct.append("SELECT * FROM (SELECT ? as concurso, ? as c1A,? as c2A,? as c3A,? as c4A,? as c5A,? as c6A,? as cidade,? as acumulado_sena,? as acumulado_sena_val, ");
                sqlConstruct.append("? as  arrecadacao_total,? as gds_6A,? as gds_5A,? as  gds_4A,? as  gds_3A,? as  rateio_6A,? as  rateio_5A,? as  rateio_4A, ");
                sqlConstruct.append("? as rateio_3A,? as  c1B,? as  c2B, ? as c3B,? as  c4B,? as  c5B,? as  c6B,? as  gds_6B,? as  gds_5B,? as  gds_4B,? as  gds_3B, ");
                sqlConstruct.append("? as rateio_6B, ? as rateio_5B, ? as rateio_4B, ? as rateio_3B, ? as estimativa_premio, ? as acumulado_especial, ? as data) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_dupla_sena WHERE concurso = ? ) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                for (ITDuplaSena c : cs) {
                    pst.setInt(1, c.getConcurso());
                    pst.setInt(2, c.getC1A());
                    pst.setInt(3, c.getC2A());
                    pst.setInt(4, c.getC3A());
                    pst.setInt(5, c.getC4A());
                    pst.setInt(6, c.getC5A());
                    pst.setInt(7, c.getC6A());
                    pst.setString(8, c.getCidade());
                    pst.setBoolean(9, c.isAcumuladoSena());
                    pst.setString(10, c.getAcumuladoSenaValor());
                    pst.setString(11, c.getArrecadacaoTotal());
                    pst.setInt(12, c.getGanhadoresSenaA());
                    pst.setInt(13, c.getGanhadoresQuinaA());
                    pst.setInt(14, c.getGanhadoresQuadraA());
                    pst.setInt(15, c.getGanhadoresTernoA());
                    pst.setString(16, c.getRateioSenaA());
                    pst.setString(17, c.getRateioQuinaA());
                    pst.setString(18, c.getRateioQuadraA());
                    pst.setString(19, c.getRateioTernoA());
                    pst.setInt(20, c.getC1B());
                    pst.setInt(21, c.getC2B());
                    pst.setInt(22, c.getC3B());
                    pst.setInt(23, c.getC4B());
                    pst.setInt(24, c.getC5B());
                    pst.setInt(25, c.getC6B());
                    pst.setInt(26, c.getGanhadoresSenaB());
                    pst.setInt(27, c.getGanhadoresQuinaB());
                    pst.setInt(28, c.getGanhadoresQuadraB());
                    pst.setInt(29, c.getGanhadoresTernoB());
                    pst.setString(30, c.getRateioSenaB());
                    pst.setString(31, c.getRateioQuinaB());
                    pst.setString(32, c.getRateioQuadraB());
                    pst.setString(33, c.getRateioTernoB());
                    pst.setString(34, c.getEstimativaPremio());
                    pst.setString(35, c.getAcumuladoEspecialPascoa());
                    pst.setString(36, c.getData());
                    pst.setInt(37, c.getConcurso());

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

    public ArrayList<ITDuplaSena> getConcursosQuinaFromWebArray() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/duplasena/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbwMPI0sDBxNXAOMwrzCjA2cDIAKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wNnUwNHfxcnSwBgIDUyhCvA5EawAjxsKckMjDDI9FQGgnyPS/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC420O6/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");

        ArrayList<ITDuplaSena> concs = new ArrayList<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITDuplaSena c = getConcurso(n);
                concs.add(c);
            }
        }
        return concs;
    }

    public Map<Integer,ITDuplaSena> getConcursosQuinaFromWebMap() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/duplasena/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbwMPI0sDBxNXAOMwrzCjA2cDIAKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wNnUwNHfxcnSwBgIDUyhCvA5EawAjxsKckMjDDI9FQGgnyPS/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K85260Q5OIRSC420O6/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Map<Integer, ITDuplaSena> concs = new HashMap<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITDuplaSena c = getConcurso(n);
                if(!concs.containsKey(c.getConcurso())){
                    concs.put(c.getConcurso(), c);
                }
            }
        }
        return concs;
    }

    public static synchronized final ITDuplaSena getConcurso(Elements n) {
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

        int position = n.size() - 25;
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
        /*position++;
        a37 = n.get(position).text();
        position++;
        a38 = n.get(position).text();
        position++;
        a39 = n.get(position).text();*/

        ITDuplaSena lotoMania = new ITDuplaSena();
        lotoMania.setConcurso(Integer.valueOf(a1));
        lotoMania.setData(DateAPI.getDateManager().formatDate(a2.replace("/", "-"), "dd-MM-yyyy", "yyyy-MM-dd"));
        lotoMania.setC1A(Integer.valueOf(a3));
        lotoMania.setC2A(Integer.valueOf(a4));
        lotoMania.setC3A(Integer.valueOf(a5));
        lotoMania.setC4A(Integer.valueOf(a6));
        lotoMania.setC5A(Integer.valueOf(a7));
        lotoMania.setC6A(Integer.valueOf(a8));
        lotoMania.setArrecadacaoTotal(a9);
        lotoMania.setGanhadoresSenaA(Integer.valueOf(a10));
        lotoMania.setCidade(a11);
        lotoMania.setRateioSenaA(a12);
        lotoMania.setAcumuladoSena(a13.equals("SIM")?true:false);
        lotoMania.setAcumuladoSenaValor(a14);
        lotoMania.setGanhadoresQuinaA(Integer.valueOf(a15));
        lotoMania.setRateioQuinaA(a16);
        lotoMania.setGanhadoresQuadraA(Integer.valueOf(a17));
        lotoMania.setRateioQuadraA(a18);
        lotoMania.setGanhadoresTernoA(Integer.valueOf(a19));
        lotoMania.setRateioTernoA(a20);
        lotoMania.setC1B(Integer.valueOf(a21));
        lotoMania.setC2B(Integer.valueOf(a22));
        lotoMania.setC3B(Integer.valueOf(a23));
        lotoMania.setC4B(Integer.valueOf(a24));
        lotoMania.setC5B(Integer.valueOf(a25));
        lotoMania.setC6B(Integer.valueOf(a26));
        lotoMania.setGanhadoresSenaB(Integer.valueOf(a27));
        lotoMania.setRateioSenaB(a28);
        lotoMania.setGanhadoresQuinaB(Integer.valueOf(a29));
        lotoMania.setRateioQuinaB(a30);
        lotoMania.setGanhadoresQuadraB(Integer.valueOf(a31));
        lotoMania.setRateioQuadraB(a32);
        lotoMania.setGanhadoresTernoB(a33.matches(".*[A-z].*")?0:Integer.valueOf(a33));
        lotoMania.setRateioTernoB(a34);
        lotoMania.setEstimativaPremio(a35);
        lotoMania.setAcumuladoEspecialPascoa(a36);

        return lotoMania;
    }

    public ITDuplaSena getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *, CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_dupla_sena where id > 0 ORDER BY id DESC limit 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITDuplaSena l = null;
                if (rs.next()) {
                    l = new ITDuplaSena();
                    l.setConcurso(rs.getInt("concurso"));
                    l.setC1A(rs.getInt("c1A"));
                    l.setC2A(rs.getInt("c2A"));
                    l.setC3A(rs.getInt("c3A"));
                    l.setC4A(rs.getInt("c4A"));
                    l.setC5A(rs.getInt("c5A"));
                    l.setC6A(rs.getInt("c6A"));
                    l.setCidade(rs.getString("cidade"));
                    l.setAcumuladoSena(rs.getBoolean("acumulado_sena"));
                    l.setArrecadacaoTotal(rs.getString("arrecadacao_total"));
                    l.setGanhadoresSenaA(rs.getInt("gds_6A"));
                    l.setGanhadoresQuinaA(rs.getInt("gds_5A"));
                    l.setGanhadoresQuadraA(rs.getInt("gds_4A"));
                    l.setGanhadoresTernoA(rs.getInt("gds_3A"));
                    l.setRateioSenaA(rs.getString("rateio_6A"));
                    l.setRateioQuinaA(rs.getString("rateio_5A"));
                    l.setRateioQuadraA(rs.getString("rateio_4A"));
                    l.setRateioTernoA(rs.getString("rateio_3A"));
                    l.setC1B(rs.getInt("c1B"));
                    l.setC2B(rs.getInt("c2B"));
                    l.setC3B(rs.getInt("c3B"));
                    l.setC4B(rs.getInt("c4B"));
                    l.setC5B(rs.getInt("c5B"));
                    l.setC6B(rs.getInt("c6B"));
                    l.setGanhadoresSenaB(rs.getInt("gds_6B"));
                    l.setGanhadoresQuinaB(rs.getInt("gds_5B"));
                    l.setGanhadoresQuadraB(rs.getInt("gds_4B"));
                    l.setGanhadoresTernoB(rs.getInt("gds_3B"));
                    l.setRateioSenaB(rs.getString("rateio_6B"));
                    l.setRateioQuinaB(rs.getString("rateio_5B"));
                    l.setRateioQuadraB(rs.getString("rateio_4B"));
                    l.setRateioTernoB(rs.getString("rateio_3B"));
                    l.setEstimativaPremio(rs.getString("estimativa_premio"));
                    l.setAcumuladoEspecialPascoa(rs.getString("acumulado_especial"));
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
