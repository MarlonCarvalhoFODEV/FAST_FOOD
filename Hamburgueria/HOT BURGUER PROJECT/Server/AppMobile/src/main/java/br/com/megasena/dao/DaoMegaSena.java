package br.com.megasena.dao;

import apis.date.DateAPI;
import br.com.comprazencasa.database.DaoAddress;
import br.com.megasena.itens.ITMegaSena;
import br.com.megasena.itens.ITPadraoBinario;
import br.com.megasena.itens.ITPorcentagem;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaoMegaSena {

    private static final int LIMIT_LOAD = 18;

    private Porcentagem porcentagem = new Porcentagem();

    private Integer[][] quadrantes =
            {
                    {01, 02, 03, 04, 05, 11, 12, 13, 14, 15, 21, 22, 23, 24, 25},
                    {06, 07, 8, 9, 10, 16, 17, 18, 19, 20, 26, 27, 28, 29, 30},
                    {31, 32, 33, 34, 35, 41, 42, 43, 44, 45, 51, 52, 53, 54, 55},
                    {36, 37, 38, 39, 40, 46, 47, 48, 49, 50, 56, 57, 58, 59, 60}
            };

    public Porcentagem getPorcentagem() {
        return porcentagem;
    }

    public boolean insertNew(ITMegaSena c) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_mega_sena (concurso, local, data, col_1, col_2, col_3, ");
                sqlConstruct.append("col_4, col_5, col_6, gds_fx1, gds_fx2, gds_fx3, rateio_fx1, rateio_fx2, ");
                sqlConstruct.append("rateio_fx3, cidade, val_arrecadado, val_est_prox_conc, acumulado, sorteio_especial, ");
                sqlConstruct.append("observacao, val_acumulado) SELECT * FROM (SELECT ? as concurso,? as local, ? as data,? as col_1,");
                sqlConstruct.append("? as col_2,? as col_3,? as col_4,? as col_5,? as col_6,? as gds_fx1,? as gds_fx2, ");
                sqlConstruct.append("? gds_fx3,? as rateio_fx1,? as rateio_fx2,? as rateio_fx3,? as cidade,");
                sqlConstruct.append("? as val_arrecadado,? as val_est_prox_conc,? as acumulado,? as sorteio_especial,? as observacao, ? as val_acumulado) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_mega_sena WHERE concurso = ? ");
                sqlConstruct.append(") LIMIT 1;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, c.getConcurso());
                pst.setString(2, c.getLocal());
                pst.setString(3, c.getData());
                pst.setInt(4, c.getColuna1());
                pst.setInt(5, c.getColuna2());
                pst.setInt(6, c.getColuna3());
                pst.setInt(7, c.getColuna4());
                pst.setInt(8, c.getColuna5());
                pst.setInt(9, c.getColuna6());
                pst.setInt(10, c.getGanhadoreFx1());
                pst.setInt(11, c.getGanhadoreFx2());
                pst.setInt(12, c.getGanhadoreFx3());
                pst.setString(13, c.getRateioFx1());
                pst.setString(14, c.getRateioFx2());
                pst.setString(15, c.getRateioFx3());
                pst.setString(16, c.getCidade());
                pst.setString(17, c.getValorArrecadado());
                pst.setString(18, c.getValorEstimadoProximoConcurso());
                pst.setBoolean(19, c.isAcumulado());
                pst.setBoolean(20, c.isSorteioEspecial());
                pst.setString(21, c.getObservacao());
                pst.setInt(22, c.getConcurso() + 5);
                pst.setString(23, c.getValorAcumulado());

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

    public boolean insertNew(ArrayList<ITMegaSena> cs) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO st_mega_sena (concurso, local, data, col_1, col_2, col_3, ");
                sqlConstruct.append("col_4, col_5, col_6, gds_fx1, gds_fx2, gds_fx3, rateio_fx1, rateio_fx2, ");
                sqlConstruct.append("rateio_fx3, cidade, val_arrecadado, val_est_prox_conc, acumulado, sorteio_especial, ");
                sqlConstruct.append("observacao, val_acumulado) SELECT * FROM (SELECT ? as concurso,? as local, ? as data,? as col_1,");
                sqlConstruct.append("? as col_2,? as col_3,? as col_4,? as col_5,? as col_6,? as gds_fx1,? as gds_fx2, ");
                sqlConstruct.append("? gds_fx3,? as rateio_fx1,? as rateio_fx2,? as rateio_fx3,? as cidade,");
                sqlConstruct.append("? as val_arrecadado,? as val_est_prox_conc,? as acumulado,? as sorteio_especial,? as observacao,? as val_acumulado) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT concurso FROM st_mega_sena WHERE concurso = ? ");
                sqlConstruct.append(") LIMIT 1;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                for (ITMegaSena c : cs) {
                    pst.setInt(1, c.getConcurso());
                    pst.setString(2, c.getLocal());
                    pst.setString(3, c.getData());
                    pst.setInt(4, c.getColuna1());
                    pst.setInt(5, c.getColuna2());
                    pst.setInt(6, c.getColuna3());
                    pst.setInt(7, c.getColuna4());
                    pst.setInt(8, c.getColuna5());
                    pst.setInt(9, c.getColuna6());
                    pst.setInt(10, c.getGanhadoreFx1());
                    pst.setInt(11, c.getGanhadoreFx2());
                    pst.setInt(12, c.getGanhadoreFx3());
                    pst.setString(13, c.getRateioFx1());
                    pst.setString(14, c.getRateioFx2());
                    pst.setString(15, c.getRateioFx3());
                    pst.setString(16, c.getCidade());
                    pst.setString(17, c.getValorArrecadado());
                    pst.setString(18, c.getValorEstimadoProximoConcurso());
                    pst.setBoolean(19, c.isAcumulado());
                    pst.setBoolean(20, c.isSorteioEspecial());
                    pst.setString(21, c.getObservacao());
                    pst.setString(22, c.getValorAcumulado());
                    pst.setInt(23, c.getConcurso());

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

    public ArrayList<ITMegaSena> getSorteios(int concurso, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<ITMegaSena> itens = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM st_mega_sena WHERE concurso > ? LIMIT ?;");


                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, concurso);
                pst.setInt(2, LIMIT_LOAD);

                ResultSet rs = pst.executeQuery();

                ITMegaSena c;
                while (rs.next()) {
                    c = new ITMegaSena();
                    c.setConcurso(rs.getInt("concurso"));
                    c.setLocal(rs.getString("local"));
                    c.setData(rs.getString("data"));
                    c.setColuna1(rs.getInt("col_1"));
                    c.setColuna2(rs.getInt("col_2"));
                    c.setColuna3(rs.getInt("col_3"));
                    c.setColuna4(rs.getInt("col_4"));
                    c.setColuna5(rs.getInt("col_5"));
                    c.setColuna6(rs.getInt("col_6"));
                    c.setGanhadoreFx1(rs.getInt("gds_fx1"));
                    c.setGanhadoreFx2(rs.getInt("gds_fx2"));
                    c.setGanhadoreFx3(rs.getInt("gds_fx3"));
                    c.setRateioFx1(rs.getString("rateio_fx1"));
                    c.setGanhadoreFx2(rs.getInt("rateio_fx2"));
                    c.setGanhadoreFx3(rs.getInt("rateio_fx3"));
                    c.setCidade(rs.getString("cidade"));
                    c.setValorArrecadado(rs.getString("val_arrecadado"));
                    c.setValorEstimadoProximoConcurso(rs.getString("val_est_prox_conc"));
                    c.setValorAcumulado(rs.getString("val_acumulado"));
                    c.setAcumulado(rs.getBoolean("acumulado"));
                    c.setSorteioEspecial(rs.getBoolean("sorteio_especial"));
                    c.setObservacao(rs.getString("observacao"));

                    itens.add(c);
                }

                con.close();
                pst.close();
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

        return itens;
    }

    public ITMegaSena getUltimoSorteio(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *,CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1, CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer FROM st_mega_sena ORDER BY id DESC LIMIT 2;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();

                ITMegaSena c = null;
                while (rs.next()) {
                    if(c == null){
                        c = new ITMegaSena();
                        c.setConcurso(rs.getInt("concurso"));
                        c.setLocal(rs.getString("local"));
                        c.setData(rs.getString("data1"));
                        c.setColuna1(rs.getInt("col_1"));
                        c.setColuna2(rs.getInt("col_2"));
                        c.setColuna3(rs.getInt("col_3"));
                        c.setColuna4(rs.getInt("col_4"));
                        c.setColuna5(rs.getInt("col_5"));
                        c.setColuna6(rs.getInt("col_6"));
                        c.setGanhadoreFx1(rs.getInt("gds_fx1"));
                        c.setGanhadoreFx2(rs.getInt("gds_fx2"));
                        c.setGanhadoreFx3(rs.getInt("gds_fx3"));
                        c.setRateioFx1(rs.getString("rateio_fx1"));
                        c.setRateioFx2(rs.getString("rateio_fx2"));
                        c.setRateioFx3(rs.getString("rateio_fx3"));
                        c.setCidade(rs.getString("cidade"));
                        c.setValorArrecadado(rs.getString("val_arrecadado"));
                        c.setValorEstimadoProximoConcurso(rs.getString("val_est_prox_conc"));
                        c.setValorAcumulado(rs.getString("val_acumulado"));
                        c.setAcumulado(rs.getBoolean("acumulado"));
                        c.setSorteioEspecial(rs.getBoolean("sorteio_especial"));
                        c.setObservacao(rs.getString("observacao"));
                        c.setTimeDiff(rs.getString("timer"));
                    }
                    else {
                        c.setHaveMore(true);
                        break;
                    }
                }

                con.close();
                pst.close();

                return c ;
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

    public ITMegaSena getSorteio(int concurso, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *,CONCAT(DAYNAME(data),', ',DAY(data),' de ',MONTHNAME(data),' de ',YEAR(data), ' às 20:00') data1,");
                sqlConstruct.append("CONCAT(getDayProxSorteio(),' e ', TIME_FORMAT(TIMEDIFF('20:00', TIME_FORMAT(now(),'%H:%i')),'%H:%i')) timer ");
                sqlConstruct.append("FROM st_mega_sena WHERE concurso<=? ORDER BY id DESC LIMIT 2;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                pst.setInt(1, concurso);

                ResultSet rs = pst.executeQuery();

                ITMegaSena c = null;
                while (rs.next()) {
                    if(c == null){
                        c = new ITMegaSena();
                        c.setConcurso(rs.getInt("concurso"));
                        c.setLocal(rs.getString("local"));
                        c.setData(rs.getString("data1"));
                        c.setColuna1(rs.getInt("col_1"));
                        c.setColuna2(rs.getInt("col_2"));
                        c.setColuna3(rs.getInt("col_3"));
                        c.setColuna4(rs.getInt("col_4"));
                        c.setColuna5(rs.getInt("col_5"));
                        c.setColuna6(rs.getInt("col_6"));
                        c.setGanhadoreFx1(rs.getInt("gds_fx1"));
                        c.setGanhadoreFx2(rs.getInt("gds_fx2"));
                        c.setGanhadoreFx3(rs.getInt("gds_fx3"));
                        c.setRateioFx1(rs.getString("rateio_fx1"));
                        c.setRateioFx2(rs.getString("rateio_fx2"));
                        c.setRateioFx3(rs.getString("rateio_fx3"));
                        c.setCidade(rs.getString("cidade"));
                        c.setValorArrecadado(rs.getString("val_arrecadado"));
                        c.setValorEstimadoProximoConcurso(rs.getString("val_est_prox_conc"));
                        c.setValorAcumulado(rs.getString("val_acumulado"));
                        c.setAcumulado(rs.getBoolean("acumulado"));
                        c.setSorteioEspecial(rs.getBoolean("sorteio_especial"));
                        c.setObservacao(rs.getString("observacao"));
                        c.setTimeDiff(rs.getString("timer"));
                    }
                    else {
                        c.setHaveMore(true);
                        break;
                    }
                }

                con.close();
                pst.close();

                return c ;
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

    public synchronized Map<Integer, Integer> getNumerosQueMaisSaem(int coluna) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        Map<Integer, Integer> itens = new HashMap<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                if (coluna == 1)
                    sqlConstruct.append("SELECT col_1 num, count(*) count FROM st_mega_sena GROUP BY col_1 ORDER BY count DESC;");
                else if (coluna == 2)
                    sqlConstruct.append("SELECT col_2 num, count(*) count FROM st_mega_sena GROUP BY col_2 ORDER BY count DESC;");
                else if (coluna == 3)
                    sqlConstruct.append("SELECT col_3 num, count(*) count FROM st_mega_sena GROUP BY col_3 ORDER BY count DESC;");
                else if (coluna == 4)
                    sqlConstruct.append("SELECT col_4 num, count(*) count FROM st_mega_sena GROUP BY col_4 ORDER BY count DESC;");
                else if (coluna == 5)
                    sqlConstruct.append("SELECT col_5 num, count(*) count FROM st_mega_sena GROUP BY col_5 ORDER BY count DESC;");
                else if (coluna == 6)
                    sqlConstruct.append("SELECT col_6 num, count(*) count FROM st_mega_sena GROUP BY col_6 ORDER BY count DESC;");

                pst = con.prepareStatement(sqlConstruct.toString());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int number = rs.getInt("num");
                    itens.put(number, rs.getInt("count"));
                }

                con.close();
                pst.close();
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

        return itens;
    }

    public synchronized ArrayList<ITPadraoBinario> getPadraoBinario(String dateInit, String dateFinal) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<ITPadraoBinario> a = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT (SELECT MONTHNAME(data)) mes,data, concurso, imparPar(col_1)c1, imparPar(col_2)c2,imparPar(col_3)c3,imparPar(col_4)c4, imparPar(col_5)c5,imparPar(col_6) c6 ");
                sqlConstruct.append("FROM st_mega_sena WHERE date_format(data,'%y-%m') between date_format(?,'%y-%m') AND date_format(?,'%y-%m');");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, dateInit);
                pst.setString(2, dateFinal);

                ResultSet rs = pst.executeQuery();

                ITPadraoBinario pad ;
                while (rs.next()) {
                    pad = new ITPadraoBinario();
                    pad.add(rs.getInt("c1"));
                    pad.add(rs.getInt("c2"));
                    pad.add(rs.getInt("c3"));
                    pad.add(rs.getInt("c4"));
                    pad.add(rs.getInt("c5"));
                    pad.add(rs.getInt("c6"));
                    pad.setData(rs.getString("data"));
                    pad.setMes(rs.getString("mes"));
                    pad.setConcurso(rs.getInt("concurso"));

                    a.add(pad);
                }

                con.close();
                pst.close();
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

        return a;
    }

    public synchronized ArrayList<ITPadraoBinario> getFromBinario(int c1, int c2, int c3, int c4, int c5, int c6, String dateInit, String dateFinal) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<ITPadraoBinario> a = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT (SELECT MONTHNAME(data)) mes,data, concurso,imparPar(col_1)c1, imparPar(col_2)c2,imparPar(col_3)c3,imparPar(col_4)c4, imparPar(col_5)c5,imparPar(col_6) c6 ");
                sqlConstruct.append("FROM st_mega_sena WHERE imparPar(col_1)=? AND imparPar(col_2)=? AND imparPar(col_3)=? AND imparPar(col_4)=? AND imparPar(col_5)=? AND imparPar(col_6)=? ");
                sqlConstruct.append("AND date_format(data,'%y-%m') between date_format(?,'%y-%m') AND date_format(?,'%y-%m');");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, c1);
                pst.setInt(2, c2);
                pst.setInt(3, c3);
                pst.setInt(4, c4);
                pst.setInt(5, c5);
                pst.setInt(6, c6);
                pst.setString(7, dateInit);
                pst.setString(8, dateFinal);

                ResultSet rs = pst.executeQuery();

                ITPadraoBinario pad ;
                while (rs.next()) {
                    pad = new ITPadraoBinario();
                    pad.add(rs.getInt("c1"));
                    pad.add(rs.getInt("c2"));
                    pad.add(rs.getInt("c3"));
                    pad.add(rs.getInt("c4"));
                    pad.add(rs.getInt("c5"));
                    pad.add(rs.getInt("c6"));
                    pad.setData(rs.getString("data"));
                    pad.setMes(rs.getString("mes"));
                    pad.setConcurso(rs.getInt("concurso"));

                    a.add(pad);
                }

                con.close();
                pst.close();
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

        return a;
    }

    public synchronized ArrayList<ITPadraoBinario> getFromBinario(int c1, int c2, int c3, int c4, int c5, int c6, String date) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<ITPadraoBinario> a = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT (SELECT MONTHNAME(data)) mes,data, concurso,imparPar(col_1)c1, imparPar(col_2)c2,imparPar(col_3)c3,imparPar(col_4)c4, imparPar(col_5)c5,imparPar(col_6) c6 ");
                sqlConstruct.append("FROM st_mega_sena WHERE imparPar(col_1)=? AND imparPar(col_2)=? AND imparPar(col_3)=? AND imparPar(col_4)=? AND imparPar(col_5)=? AND imparPar(col_6)=? ");
                sqlConstruct.append("AND date_format(data,'%y-%m') = date_format(?,'%y-%m');");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, c1);
                pst.setInt(2, c2);
                pst.setInt(3, c3);
                pst.setInt(4, c4);
                pst.setInt(5, c5);
                pst.setInt(6, c6);
                pst.setString(7, date);

                ResultSet rs = pst.executeQuery();

                ITPadraoBinario pad ;
                while (rs.next()) {
                    pad = new ITPadraoBinario();
                    pad.add(rs.getInt("c1"));
                    pad.add(rs.getInt("c2"));
                    pad.add(rs.getInt("c3"));
                    pad.add(rs.getInt("c4"));
                    pad.add(rs.getInt("c5"));
                    pad.add(rs.getInt("c6"));
                    pad.setData(rs.getString("data"));
                    pad.setMes(rs.getString("mes"));
                    pad.setConcurso(rs.getInt("concurso"));

                    a.add(pad);
                }

                con.close();
                pst.close();
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

        return a;
    }


    public ArrayList<ITPadraoBinario> getPBQueMaisSaem(String date) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<ITPadraoBinario> a = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT count(*) count, MONTHNAME(data) mes, imparPar(col_1)c1, imparPar(col_2)c2,imparPar(col_3)c3,imparPar(col_4)c4, imparPar(col_5)c5,imparPar(col_6) c6 ");
                sqlConstruct.append("FROM st_mega_sena WHERE MONTHNAME(data) = ? ");
                sqlConstruct.append("GROUP BY imparPar(col_1), imparPar(col_2),imparPar(col_3),imparPar(col_4), imparPar(col_5),imparPar(col_6) ");
                sqlConstruct.append("ORDER BY count DESC;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                pst.setString(1, date);

                ResultSet rs = pst.executeQuery();

                ITPadraoBinario  pad;
                while (rs.next()) {

                    pad = new ITPadraoBinario();
                    pad.add(rs.getInt("c1"));
                    pad.add(rs.getInt("c2"));
                    pad.add(rs.getInt("c3"));
                    pad.add(rs.getInt("c4"));
                    pad.add(rs.getInt("c5"));
                    pad.add(rs.getInt("c6"));
                    pad.setMes(rs.getString("mes"));
                    pad.setCount(rs.getInt("count"));

                    a.add(pad);
                }

                con.close();
                pst.close();
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

        return a;
    }

    public ArrayList<ITPadraoBinario> getPBQueMaisSaem(String monthName, String dateInit, String dateFinal) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<ITPadraoBinario> a = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT count(*) count, MONTHNAME(data) mes, imparPar(col_1)c1, imparPar(col_2)c2,imparPar(col_3)c3,imparPar(col_4)c4, imparPar(col_5)c5,imparPar(col_6) c6 ");
                sqlConstruct.append("FROM st_mega_sena WHERE MONTHNAME(data) = ? AND data between ? AND ? ");
                sqlConstruct.append("GROUP BY imparPar(col_1), imparPar(col_2),imparPar(col_3),imparPar(col_4), imparPar(col_5),imparPar(col_6) ");
                sqlConstruct.append("ORDER BY count ;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                pst.setString(1, monthName);
                pst.setString(2, dateInit);
                pst.setString(3, dateFinal);

                ResultSet rs = pst.executeQuery();

                ITPadraoBinario  pad;
                while (rs.next()) {
                    pad = new ITPadraoBinario();
                    pad.add(rs.getInt("c1"));
                    pad.add(rs.getInt("c2"));
                    pad.add(rs.getInt("c3"));
                    pad.add(rs.getInt("c4"));
                    pad.add(rs.getInt("c5"));
                    pad.add(rs.getInt("c6"));
                    pad.setMes(rs.getString("mes"));

                    a.add(pad);
                }

                con.close();
                pst.close();
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

        return a;
    }

    public Map<Integer, Map<Integer, Integer>> getMaisSorteadosPorColuna(){
        Map<Integer, Integer> col1 = getNumerosQueMaisSaem(1);
        Map<Integer, Integer> col2 = getNumerosQueMaisSaem(2);
        Map<Integer, Integer> col3 = getNumerosQueMaisSaem(3);
        Map<Integer, Integer> col4 = getNumerosQueMaisSaem(4);
        Map<Integer, Integer> col5 = getNumerosQueMaisSaem(5);
        Map<Integer, Integer> col6 = getNumerosQueMaisSaem(6);

        Map<Integer, Map<Integer, Integer>> full = new HashMap<>();
        full.put(1, col1);
        full.put(2, col2);
        full.put(3, col3);
        full.put(4, col4);
        full.put(5, col5);
        full.put(6, col6);

        return full ;
    }

    public synchronized Map<Integer,Integer> getMaisSorteadosPorQuadrant(int quadrant) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        Map<Integer,Integer> a = new HashMap<>();

        if (con != null) {
            try {

                String quadrantNumeros =
                        Arrays.toString(quadrantes[quadrant-1])
                                .replace("[","")
                                .replace("]","");


                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT DISTINCT col_1, count(*) count FROM st_mega_sena WHERE col_1 IN ("+quadrantNumeros+") GROUP BY col_1 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_2, count(*) count FROM st_mega_sena WHERE col_2 IN ("+quadrantNumeros+") GROUP BY col_2 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_3, count(*) count FROM st_mega_sena WHERE col_3 IN ("+quadrantNumeros+") GROUP BY col_3 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_4, count(*) count FROM st_mega_sena WHERE col_4 IN ("+quadrantNumeros+") GROUP BY col_4 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_5, count(*) count FROM st_mega_sena WHERE col_5 IN ("+quadrantNumeros+") GROUP BY col_5 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_6, count(*) count FROM st_mega_sena WHERE col_6 IN ("+quadrantNumeros+") GROUP BY col_6;");

                pst = con.prepareStatement(sqlConstruct.toString());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int numero = rs.getInt("col_1");
                    int count = rs.getInt("count");

                    if(a.containsKey(numero)) {
                        int savedCount = a.get(numero);
                        a.put(numero, (count+savedCount));
                    }
                    else
                        a.put(numero,count);
                }

                con.close();
                pst.close();
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

        return a;
    }

    public synchronized Map<Integer,Integer> getMaisSorteadosPorQuadrant(int quadrant, String meses) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        Map<Integer,Integer> a = new HashMap<>();

        if (con != null) {
            try {

                String quadrantNumeros =
                        Arrays.toString(quadrantes[quadrant-1])
                                .replace("[","")
                                .replace("]","");


                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT DISTINCT col_1, count(*) count FROM st_mega_sena WHERE col_1 IN ("+quadrantNumeros+") AND MONTHNAME(data) = ? GROUP BY col_1 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_2, count(*) count FROM st_mega_sena WHERE col_2 IN ("+quadrantNumeros+") AND MONTHNAME(data) = ? GROUP BY col_2 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_3, count(*) count FROM st_mega_sena WHERE col_3 IN ("+quadrantNumeros+") AND MONTHNAME(data) = ? GROUP BY col_3 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_4, count(*) count FROM st_mega_sena WHERE col_4 IN ("+quadrantNumeros+") AND MONTHNAME(data) = ? GROUP BY col_4 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_5, count(*) count FROM st_mega_sena WHERE col_5 IN ("+quadrantNumeros+") AND MONTHNAME(data) = ? GROUP BY col_5 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_6, count(*) count FROM st_mega_sena WHERE col_6 IN ("+quadrantNumeros+") AND MONTHNAME(data) = ? GROUP BY col_6;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET lc_time_names = 'pt_BR';");
                pst.executeBatch();

                pst.setString(1, meses);
                pst.setString(2, meses);
                pst.setString(3, meses);
                pst.setString(4, meses);
                pst.setString(5, meses);
                pst.setString(6, meses);

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int numero = rs.getInt("col_1");
                    int count = rs.getInt("count");

                    if(a.containsKey(numero)) {
                        int savedCount = a.get(numero);
                        a.put(numero, (count+savedCount));
                    }
                    else
                        a.put(numero,count);
                }

                con.close();
                pst.close();
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

        return a;
    }

    public synchronized Map<Integer,Integer> getMaisSorteadosPorQuadrant(int quadrant, int mes, int ano) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        Map<Integer,Integer> a = new HashMap<>();

        if (con != null) {
            try {

                String quadrantNumeros =
                        Arrays.toString(quadrantes[quadrant-1])
                                .replace("[","")
                                .replace("]","");


                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT DISTINCT col_1, count(*) count FROM st_mega_sena WHERE col_1 IN ("+quadrantNumeros+") AND DATE_FORMAT(data,'%y-%m') = DATE_FORMAT(?,'%y-%m') GROUP BY col_1 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_2, count(*) count FROM st_mega_sena WHERE col_2 IN ("+quadrantNumeros+") AND DATE_FORMAT(data,'%y-%m') = DATE_FORMAT(?,'%y-%m') GROUP BY col_2 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_3, count(*) count FROM st_mega_sena WHERE col_3 IN ("+quadrantNumeros+") AND DATE_FORMAT(data,'%y-%m') = DATE_FORMAT(?,'%y-%m') GROUP BY col_3 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_4, count(*) count FROM st_mega_sena WHERE col_4 IN ("+quadrantNumeros+") AND DATE_FORMAT(data,'%y-%m') = DATE_FORMAT(?,'%y-%m') GROUP BY col_4 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_5, count(*) count FROM st_mega_sena WHERE col_5 IN ("+quadrantNumeros+") AND DATE_FORMAT(data,'%y-%m') = DATE_FORMAT(?,'%y-%m') GROUP BY col_5 UNION ");
                sqlConstruct.append("SELECT DISTINCT col_6, count(*) count FROM st_mega_sena WHERE col_6 IN ("+quadrantNumeros+") AND DATE_FORMAT(data,'%y-%m') = DATE_FORMAT(?,'%y-%m') GROUP BY col_6;");

                StringBuilder sb = new StringBuilder();
                sb.append(ano);
                sb.append("-");
                sb.append(mes);
                sb.append("-");
                sb.append("01");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, sb.toString());
                pst.setString(2, sb.toString());
                pst.setString(3, sb.toString());
                pst.setString(4, sb.toString());
                pst.setString(5, sb.toString());
                pst.setString(6, sb.toString());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int numero = rs.getInt("col_1");
                    int count = rs.getInt("count");

                    if(a.containsKey(numero)) {
                        int savedCount = a.get(numero);
                        a.put(numero, (count+savedCount));
                    }
                    else
                        a.put(numero,count);
                }

                con.close();
                pst.close();
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

        return a;
    }

    public Map<Integer, Map<Integer, Integer>> getProbabilidadeColor(){
        Map<Integer, Map<Integer, Integer>> mapGeneral = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> mais = new DaoMegaSena().getMaisSorteadosPorColuna();
        for(int coluna=1; coluna <= mais.size(); coluna++){

            // CALCULA A MEDIA
            int media = 0;
            int count = 0;
            for(Integer number: mais.get(coluna).keySet()){
                int qtX = mais.get(coluna).get(number);

                if(qtX > 95){
                    count++;
                    media += qtX;
                }
            }

            media = (media/count);
            int media2 = (media/2);
            int media3 = (media/3);

            Map<Integer, Integer> definerColor = new HashMap<>();
            // CATEGORIZA POR COR MAIOR PROB
            for(Integer number: mais.get(coluna).keySet()){
                int qtX = mais.get(coluna).get(number);

                if(qtX >= media){
                    definerColor.put(number, 1);
                }
                else if(qtX < media && qtX >= media2){
                    definerColor.put(number, 2);
                }
                else if(qtX < media2 && qtX >= media3){
                    definerColor.put(number, 3);
                }
                else if(qtX < media3){
                    definerColor.put(number, 4);
                }
            }

            mapGeneral.put(coluna, definerColor);

        }

        return mapGeneral;
    }

    public Map<Integer, Integer> getProbabilidadeColor(int colunm){
        Map<Integer, Integer> mais = new DaoMegaSena().getNumerosQueMaisSaem(colunm);

        int media = 0;
        int count = 0;
        int first = 0 ;
        for(Integer number: mais.keySet()){
            if(first == 0)first=number;

            int qtX = mais.get(number);

            if(qtX > 95){
                count++;
                media += qtX;
            }
        }

        media = (media/count);
        int media1 = ((media+first)/2);
        int media2 = (media/2);
        int media3 = (media/3);

        Map<Integer, Integer> definerColor = new HashMap<>();
        // CATEGORIZA POR COR MAIOR PROB
        for(Integer number: mais.keySet()){
            int qtX = mais.get(number);

            if(qtX >= media){
                definerColor.put(number, 1);
            }
            else if(qtX < media && qtX >= media2){
                definerColor.put(number, 2);
            }
            else if(qtX < media2 && qtX >= media3){
                definerColor.put(number, 3);
            }
            else if(qtX < media3){
                definerColor.put(number, 4);
            }
        }

        return definerColor;
    }

    public double[][] getPercentualParImpar(){
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_1) = 0)*100)/(SELECT count(*) FROM st_mega_sena)per_col1, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_2) = 0)*100)/(SELECT count(*) FROM st_mega_sena)per_col2, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_3) = 0)*100)/(SELECT count(*) FROM st_mega_sena)per_col3, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_4) = 0)*100)/(SELECT count(*) FROM st_mega_sena)per_col4, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_5) = 0)*100)/(SELECT count(*) FROM st_mega_sena)per_col5, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_6) = 0)*100)/(SELECT count(*) FROM st_mega_sena)per_col6, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_1) = 1)*100)/(SELECT count(*) FROM st_mega_sena)per_col1i, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_2) = 1)*100)/(SELECT count(*) FROM st_mega_sena)per_col2i, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_3) = 1)*100)/(SELECT count(*) FROM st_mega_sena)per_col3i, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_4) = 1)*100)/(SELECT count(*) FROM st_mega_sena)per_col4i, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_5) = 1)*100)/(SELECT count(*) FROM st_mega_sena)per_col5i, ");
                sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_6) = 1)*100)/(SELECT count(*) FROM st_mega_sena)per_col6i; ");

                pst = con.prepareStatement(sqlConstruct.toString());

                ResultSet rs = pst.executeQuery();

                double[][] percents = null;
                if (rs.next()) {
                    double pc1 = rs.getInt("per_col1");
                    double pc2 = rs.getInt("per_col2");
                    double pc3 = rs.getInt("per_col3");
                    double pc4 = rs.getInt("per_col4");
                    double pc5 = rs.getInt("per_col5");
                    double pc6 = rs.getInt("per_col6");

                    double pc1i = rs.getInt("per_col1i");
                    double pc2i = rs.getInt("per_col2i");
                    double pc3i = rs.getInt("per_col3i");
                    double pc4i = rs.getInt("per_col4i");
                    double pc5i = rs.getInt("per_col5i");
                    double pc6i = rs.getInt("per_col6i");

                    percents = new double[][]
                            {
                                    {pc1, pc2, pc3, pc4, pc5,pc6},
                                    {pc1i, pc2i, pc3i, pc4i, pc5i,pc6i}
                            };

                }

                con.close();
                pst.close();

                return percents ;
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
        return  null ;
    }

    public ArrayList<ITMegaSena> getConcursosMegaSenaFromWebArray() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbwMPI0sDBxNXAOMwrzCjA0sjIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wNnUwNHfxcnSwBgIDUyhCvA5EawAjxsKckMjDDI9FQE-F4ca/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K8DBC0QPVN93KQ10G1/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(1).getElementsByTag("tr");

        ArrayList<ITMegaSena> concs = new ArrayList<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITMegaSena c = getConcurso(n);
                concs.add(c);
            }
        }
        return concs;
    }

    public Map<Integer,ITMegaSena> getConcursosMegaSenaFromWebMap() throws IOException {
        Document doc = Jsoup.connect("http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbwMPI0sDBxNXAOMwrzCjA0sjIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wNnUwNHfxcnSwBgIDUyhCvA5EawAjxsKckMjDDI9FQE-F4ca/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0K8DBC0QPVN93KQ10G1/res/id=historicoHTML/c=cacheLevelPage/=/").header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .get();

        Elements lines = doc.getElementsByTag("tbody").get(1).getElementsByTag("tr");

        Map<Integer, ITMegaSena> concs = new HashMap<>();
        for (Element linha : lines) {

            Elements n = linha.getElementsByTag("td");

            if (n.size() > 2) {
                ITMegaSena c = getConcurso(n);
                if(!concs.containsKey(c.getConcurso())){
                    concs.put(c.getConcurso(), c);
                }
            }
        }
        return concs;
    }

    public static synchronized final ITMegaSena getConcurso(Elements n) {
        Object concurso = n.get(0).text();
        Object local = n.get(1).text();
        Object data = n.get(2).text();
        Object coluna1 = n.get(3).text();
        Object coluna2 = n.get(4).text();
        Object coluna3 = n.get(5).text();
        Object coluna4 = n.get(6).text();
        Object coluna5 = n.get(7).text();
        Object coluna6 = n.get(8).text();
        Object gFx1 = n.get(9).text();
        Object gFx2 = n.get(10).text();
        Object gFx3 = n.get(11).text();
        Object rFx1 = n.get(12).text();
        Object rFx2 = n.get(13).text();
        Object rFx3 = n.get(14).text();
        Object cida = n.get(15).text();

        Object valAr = n.get(16).text();
        Object estP = n.get(17).text();
        Object valAcum = n.get(18).text();
        Object acum = n.get(19).text();
        Object strE = n.get(20).text();
        Object obs = n.get(21).text();


        int position = n.size() - 6;

        valAr = n.get(position).text();
        position++;
        estP = n.get(position).text();
        position++;
        valAcum = n.get(position).text();
        position++;
        acum = n.get(position).text();
        position++;
        strE = n.get(position).text();
        position++;
        obs = n.get(position).text();

        int concursos = Integer.valueOf(concurso.toString());
        String locals = String.valueOf(local.toString());
        String datas = DateAPI.getDateManager().formatDate(data.toString().replace("/", "-"), "dd-MM-yyyy", "yyyy-MM-dd");

        int col1 = Integer.valueOf(coluna1.toString());
        int col2 = Integer.valueOf(coluna2.toString());
        int col3 = Integer.valueOf(coluna3.toString());
        int col4 = Integer.valueOf(coluna4.toString());
        int col5 = Integer.valueOf(coluna5.toString());
        int col6 = Integer.valueOf(coluna6.toString());
        int gdsFx1 = Integer.valueOf(gFx1.toString());
        int gdsFx2 = Integer.valueOf(gFx2.toString());
        int gdsFx3 = Integer.valueOf(gFx3.toString());
        //double rateioFx1 = Double.valueOf(!rFx1.toString().isEmpty() ? rFx1.toString().replace(".", "").replace(",", ".") : "0.0");
        //double rateioFx2 = Double.valueOf(!rFx2.toString().isEmpty() ? rFx2.toString().replace(".", "").replace(",", ".") : "0.0");
        //double rateioFx3 = Double.valueOf(!rFx3.toString().isEmpty() ? rFx3.toString().replace(".", "").replace(",", ".") : "0.0");
        String cidade = String.valueOf(cida.toString());


        //float valArecad = Float.valueOf(!valAr.toString().isEmpty() ? valAr.toString().replace(".", "").replace(",", ".") : "0.0");
        //float valEstProxC = Float.valueOf(!estP.toString().isEmpty() ? estP.toString().replace(".", "").replace(",", ".") : "0.0");
        //float valAcumulado = Float.valueOf(valAcum.toString().replace(".", "").replace(",", "."));
        boolean acumulado = acum.toString().equals("SIM") ? true : false;
        boolean srtEspecial = strE.toString().equals("SIM") ? true : false;
        String observacao = String.valueOf(obs.toString());

        double totalArreca= Double.valueOf(valAr.toString().replace(".","").replace(",","."));
        totalArreca = totalArreca-(0.4335*totalArreca);

        double accMegaVirada = totalArreca-((0.35*totalArreca)+(0.19*totalArreca)+(0.19*totalArreca)+(0.22*totalArreca));
        double accZeroCinco = totalArreca-((0.35*totalArreca)+(0.19*totalArreca)+(0.19*totalArreca)+(0.05*totalArreca));

        Locale localeBR = new Locale("pt","BR");
        NumberFormat dinheiro = NumberFormat.getCurrencyInstance(localeBR);

        String acMega = dinheiro.format(accMegaVirada);
        String acZero = dinheiro.format(accZeroCinco);


        ITMegaSena c = new ITMegaSena();
        c.setConcurso(concursos);
        c.setLocal(locals);
        c.setData(datas);
        c.setColuna1(col1);
        c.setColuna2(col2);
        c.setColuna3(col3);
        c.setColuna4(col4);
        c.setColuna5(col5);
        c.setColuna6(col6);
        c.setGanhadoreFx1(gdsFx1);
        c.setGanhadoreFx2(gdsFx2);
        c.setGanhadoreFx3(gdsFx3);
        c.setRateioFx1(rFx1.toString());
        c.setRateioFx2(rFx2.toString());
        c.setRateioFx3(rFx3.toString());
        c.setCidade(cidade);
        c.setValorArrecadado(valAr.toString());
        c.setValorAcumulado(valAcum.toString());
        c.setValorEstimadoProximoConcurso(estP.toString());
        c.setAcumulado(acumulado);
        c.setSorteioEspecial(srtEspecial);
        c.setObservacao(observacao);
        return c;
    }


    public class Porcentagem{

        public synchronized ITPorcentagem getParImpar(boolean par, String nomeMes, int ano ) {
            Connection con = DBConnections.connect();
            PreparedStatement pst = null;

            if (con != null) {
                try {

                    StringBuilder sqlConstruct = new StringBuilder();
                    sqlConstruct.append("SELECT  ((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_1)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc1, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_2)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc2, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_3)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc3, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_4)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc4, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_5)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc5, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_6)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc6; ");

                    pst = con.prepareStatement(sqlConstruct.toString());
                    pst.addBatch("SET lc_time_names = 'pt_BR';");
                    pst.executeBatch();

                    pst.setBoolean(1, par);
                    pst.setString(2, nomeMes);
                    pst.setInt(3, ano);
                    pst.setString(4, nomeMes);
                    pst.setInt(5, ano);

                    pst.setBoolean(6, par);
                    pst.setString(7, nomeMes);
                    pst.setInt(8, ano);
                    pst.setString(9, nomeMes);
                    pst.setInt(10, ano);

                    pst.setBoolean(11, par);
                    pst.setString(12, nomeMes);
                    pst.setInt(13, ano);
                    pst.setString(14, nomeMes);
                    pst.setInt(15, ano);

                    pst.setBoolean(16, par);
                    pst.setString(17, nomeMes);
                    pst.setInt(18, ano);
                    pst.setString(19, nomeMes);
                    pst.setInt(20, ano);

                    pst.setBoolean(21, par);
                    pst.setString(22, nomeMes);
                    pst.setInt(23, ano);
                    pst.setString(24, nomeMes);
                    pst.setInt(25, ano);

                    pst.setBoolean(26, par);
                    pst.setString(27, nomeMes);
                    pst.setInt(28, ano);
                    pst.setString(29, nomeMes);
                    pst.setInt(30, ano);

                    ResultSet rs = pst.executeQuery();

                    ITPorcentagem p = null;
                    if (rs.next()) {
                        p = new ITPorcentagem();
                        p.setColuna1(rs.getDouble("pc1"));
                        p.setColuna2(rs.getDouble("pc2"));
                        p.setColuna3(rs.getDouble("pc3"));
                        p.setColuna4(rs.getDouble("pc4"));
                        p.setColuna5(rs.getDouble("pc5"));
                        p.setColuna6(rs.getDouble("pc5"));
                    }

                    con.close();
                    pst.close();

                    return p ;
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

        public synchronized ArrayList<ITPorcentagem> getParImpar(boolean par, String nomeMes, int anoInit, int anoFinal ) {
            Connection con = DBConnections.connect();
            PreparedStatement pst = null;

            ArrayList<ITPorcentagem> itens = new ArrayList<>();

            if (con != null) {
                try {

                    StringBuilder sqlConstruct = new StringBuilder();
                    sqlConstruct.append("SELECT  ((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_1)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc1, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_2)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc2, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_3)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc3, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_4)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc4, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_5)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc5, ");
                    sqlConstruct.append("((SELECT count(*) FROM st_mega_sena WHERE imparPar(col_6)=? AND MONTHNAME(data) = ? AND year(data)=?)*100)/");
                    sqlConstruct.append("(SELECT count(*) FROM st_mega_sena WHERE MONTHNAME(data) = ? AND year(data)=?) pc6; ");

                    pst = con.prepareStatement(sqlConstruct.toString());
                    pst.addBatch("SET lc_time_names = 'pt_BR';");
                    pst.executeBatch();

                    while(anoInit < anoFinal){
                        pst.setBoolean(1, par);
                        pst.setString(2, nomeMes);
                        pst.setInt(3, anoInit);
                        pst.setString(4, nomeMes);
                        pst.setInt(5, anoInit);

                        pst.setBoolean(6, par);
                        pst.setString(7, nomeMes);
                        pst.setInt(8, anoInit);
                        pst.setString(9, nomeMes);
                        pst.setInt(10, anoInit);

                        pst.setBoolean(11, par);
                        pst.setString(12, nomeMes);
                        pst.setInt(13, anoInit);
                        pst.setString(14, nomeMes);
                        pst.setInt(15, anoInit);

                        pst.setBoolean(16, par);
                        pst.setString(17, nomeMes);
                        pst.setInt(18, anoInit);
                        pst.setString(19, nomeMes);
                        pst.setInt(20, anoInit);

                        pst.setBoolean(21, par);
                        pst.setString(22, nomeMes);
                        pst.setInt(23, anoInit);
                        pst.setString(24, nomeMes);
                        pst.setInt(25, anoInit);

                        pst.setBoolean(26, par);
                        pst.setString(27, nomeMes);
                        pst.setInt(28, anoInit);
                        pst.setString(29, nomeMes);
                        pst.setInt(30, anoInit);
                        pst.addBatch();

                        anoInit++;
                    }


                    ResultSet rs = pst.executeQuery();

                    ITPorcentagem p = null;
                    while (rs.next()) {
                        p = new ITPorcentagem();
                        p.setColuna1(rs.getDouble("pc1"));
                        p.setColuna2(rs.getDouble("pc2"));
                        p.setColuna3(rs.getDouble("pc3"));
                        p.setColuna4(rs.getDouble("pc4"));
                        p.setColuna5(rs.getDouble("pc5"));
                        p.setColuna6(rs.getDouble("pc5"));

                        itens.add(p);
                    }

                    con.close();
                    pst.close();

                    return itens ;
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

        private final String getColumnName(int column){
            switch (column){
                case 1:return "col_1";
                case 2:return "col_2";
                case 3:return "col_3";
                case 4:return "col_4";
                case 5:return "col_5";
                case 6:return "col_6";
            }
            return null ;
        }
    }
}
