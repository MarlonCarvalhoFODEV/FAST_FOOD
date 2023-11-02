package br.com.hotburguer.daos;

import br.com.comprazencasa.database.DaoAddress;
import br.com.hotburguer.itens.HotConfigurations;
import br.com.hotburguer.itens.HotManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoHotManager {

    public HotConfigurations getHotConfiguration() {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM hot_configurations WHERE id=1; ");

            try {
                pst = con.prepareStatement(sql.toString());

                ResultSet rs = pst.executeQuery();
                HotConfigurations config = null ;
                if(rs.next()){
                    config = new HotConfigurations();
                    config.getDeliveryMan().setFeeDelivery(rs.getDouble("fee_delivery"));
                    config.getSystemConfig().setFeeDeliveryFree(rs.getBoolean("fee_delivery_free"));
                }

                con.close();
                pst.close();

                return config ;
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

    public ArrayList<HotManager> getHotManagers(int count) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<HotManager> managers = new ArrayList<>();

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM hot_managers limit ?; ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1,count);

                ResultSet rs = pst.executeQuery();
                HotManager m = null ;
                while (rs.next()){
                    m = new HotManager();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setThisDayBilled(rs.getDouble("this_day_billed"));
                    m.setThisMonthBilled(rs.getDouble("this_month_billed"));
                    m.setThisWeekBilled(rs.getDouble("this_week_billed"));
                    m.setThisDayBilled(rs.getDouble("this_day_billed"));
                }

                con.close();
                pst.close();

                return managers ;
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

    public HotManager getHotManagersFromID(int id) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM hot_managers WHERE id=?; ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1,id);

                ResultSet rs = pst.executeQuery();
                HotManager m = null ;
                if (rs.next()){
                    m = new HotManager();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setThisDayBilled(rs.getDouble("this_day_billed"));
                    m.setThisMonthBilled(rs.getDouble("this_month_billed"));
                    m.setThisWeekBilled(rs.getDouble("this_week_billed"));
                    m.setThisDayBilled(rs.getDouble("this_day_billed"));
                }

                con.close();
                pst.close();

                return m ;
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

    public HotManager getHotManagersCentral() {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM hot_managers WHERE id=1; ");

            try {
                pst = con.prepareStatement(sql.toString());

                ResultSet rs = pst.executeQuery();
                HotManager m = null ;
                if (rs.next()){
                    m = new HotManager();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setThisDayBilled(rs.getDouble("this_day_billed"));
                    m.setThisMonthBilled(rs.getDouble("this_month_billed"));
                    m.setThisWeekBilled(rs.getDouble("this_week_billed"));
                    m.setThisDayBilled(rs.getDouble("this_day_billed"));
                }

                con.close();
                pst.close();

                return m ;
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

    public int insertHotManager(String name) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO hot_managers (name) VALUES (?);");
            /*sql.append("INSERT INTO hot_managers (name, count_delivery, total_billing, this_day_billed,");
            sql.append("this_week_billed, this_month_billed, this_year_billed) ");
            sql.append("VALUES ();");*/

            try {
                pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1,name);

                int linesAffecteds = pst.executeUpdate();
                int idGenerated = 0 ;

                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next())
                    idGenerated = rs.getInt(1);

                con.close();
                pst.close();

                return idGenerated ;
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
        return 0;
    }

    public ArrayList<JSONObject> getOpeningHours(int id, String idDevice, String ip) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<JSONObject> hours = new ArrayList<>();

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT opd.id_manager, opd.day, opd.day_name, opd.opening_hour, opd.closing_hour, opd.is_operating FROM hot_managers m ");
            sql.append("LEFT JOIN hot_operations_days opd ");
            sql.append("ON opd.id_manager=m.id ");
            sql.append("WHERE m.id=? AND m.id_device=?;");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1,id);
                pst.setString(2,idDevice);

                ResultSet rs = pst.executeQuery();

                JSONObject ob = null;
                while (rs.next()){
                    ob = new JSONObject();
                    ob.put("day_n", rs.getString("day"));
                    ob.put("day",rs.getString("day_name"));
                    ob.put("opening_hour",rs.getString("opening_hour"));
                    ob.put("closing_hour",rs.getString("closing_hour"));
                    //ob.put("id_manager",rs.getInt("id_manager"));
                    ob.put("is_operating",rs.getBoolean("is_operating"));

                    hours.add(ob);
                }

                con.close();
                pst.close();

                return hours ;
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
        return hours;
    }

    public boolean updateDay(int id, String idDevice, String ip, JSONObject updatedDay) {

        Connection con = DBConnections.connect();

        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE hot_operations_days da LEFT JOIN hot_managers ma ");
            sql.append("ON da.id_manager=ma.id SET da.opening_hour=?, da.closing_hour=?, da.is_operating=? ");
            sql.append("WHERE da.day=? AND da.id_manager=? AND ma.id_device=?; ");

            try {

                pst = con.prepareStatement(sql.toString());
                pst.setString(1, updatedDay.getString("5"));
                pst.setString(2, updatedDay.getString("6"));
                pst.setBoolean(3, updatedDay.getBoolean("4"));
                pst.setInt(4, updatedDay.getInt("3"));
                pst.setInt(5, updatedDay.getInt("1"));
                pst.setString(6, updatedDay.getString("2"));

                System.out.println("SQL: "+pst.toString());

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return linesAffecteds > 0;
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
        return false;
    }
}
