package br.com.comprazencasa.database;

import br.com.config.server.OpeningHour;
import br.com.comprazencasa.util.DataPadraoUtil;
import br.com.comprazencasa.util.ServerConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DaoServerConfig {

    public ServerConfig getServerConfig(){

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            String sql = "SELECT * FROM server_config WHERE id>0   ";
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                ServerConfig config = null ;
                while(rs.next()){
                    String openingDate = rs.getString("opening_date");
                    if(openingDate != null){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        OffsetDateTime dateTime = LocalDateTime.parse(openingDate, formatter)
                                .atOffset(ZoneOffset.UTC);

                        config = new ServerConfig();
                        config.getOpeningDate().setMonth(dateTime.getMonth().getValue());
                        config.getOpeningDate().setDay(Integer.valueOf(dateTime.getDayOfMonth()));
                        config.getOpeningDate().setYear(Integer.valueOf(dateTime.getYear()));
                        config.getOpeningDate().setHour(Integer.valueOf(dateTime.getHour()));
                        config.getOpeningDate().setMinute(Integer.valueOf(dateTime.getMinute()));
                        config.getLocation().setCEP(rs.getString("base_location_cep"));
                        config.getLocation().setLatitude(rs.getDouble("base_location_lat"));
                        config.getLocation().setLongitude(rs.getDouble("base_location_lng"));

                        config.setReleaseMakeWish(rs.getBoolean("is_receving_order"));
                        config.setLimitOrders(rs.getInt("limit_orders_day"));
                    }
                    else if(DataPadraoUtil.getDayOfWeek() == rs.getInt("day_of_week")){
                        OpeningHour open = new OpeningHour();
                        open.setDayOfWeek(rs.getInt("day_of_week"));
                        open.setStartHour(rs.getString("initial_hour"));
                        open.setFinalHour(rs.getString("final_hour"));
                        config.setOpeningHour(open);

                        break;
                    }
                }

                con.close();
                pst.close();

                return config ;
            } catch (SQLException e) {
                e.printStackTrace();
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            } finally {
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null ;
    }

    public ServerConfig getServerConfig2(){

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            String sql = "SELECT * FROM server_config WHERE id>0 limit 1  ";
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                Map<Integer, OpeningHour> openingHour = new HashMap<>();

                ServerConfig config = null ;
                while(rs.next()){
                    if(rs.getString("opening_date") != null){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        OffsetDateTime dateTime = LocalDateTime.parse(rs.getString("opening_date"), formatter)
                                .atOffset(ZoneOffset.UTC);

                        config = new ServerConfig();
                        config.getOpeningDate().setMonth(Integer.valueOf(dateTime.getDayOfMonth()));
                        config.getOpeningDate().setDay(Integer.valueOf(dateTime.getMonth().getValue()));
                        config.getOpeningDate().setYear(Integer.valueOf(dateTime.getYear()));
                        config.getOpeningDate().setHour(Integer.valueOf(dateTime.getHour()));
                        config.getOpeningDate().setMinute(Integer.valueOf(dateTime.getMinute()));

                        config.setReleaseMakeWish(rs.getBoolean("is_receving_order"));
                        config.setLimitOrders(rs.getInt("limit_orders_day"));
                    }
                    else if(DataPadraoUtil.getDayOfWeek() == rs.getInt("day_of_week")){
                        OpeningHour open = new OpeningHour();
                        open.setDayOfWeek(rs.getInt("day_of_week"));
                        open.setStartHour(rs.getString("initial_hour"));
                        open.setFinalHour(rs.getString("final_hour"));

                        openingHour.put(rs.getInt("day_of_week"),open);
                    }
                }

                con.close();
                pst.close();

                con.close();
                pst.close();

                return config ;
            } catch (SQLException e) {
                e.printStackTrace();
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            } finally {
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null ;
    }

    public ServerConfig updateServerConfig(ServerConfig cfg){

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE server_config SET initial_hour?, final_hour=?, is_receving_order=?, ");
            sql.append("limit_orders_day=? WHERE id>0 ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1,cfg.getOpeningHour().getInitialHour());
                pst.setString(2,cfg.getOpeningHour().getEndHour());
                pst.setBoolean(3,cfg.isReleaseMakeWish());
                pst.setInt(4, cfg.getLimitOrders());

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return cfg ;
            } catch (SQLException e) {
                e.printStackTrace();
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            } finally {
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null ;
    }
}
