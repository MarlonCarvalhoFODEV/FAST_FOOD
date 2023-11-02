package br.com.hotburguer.daos;

import br.com.comprazencasa.database.ConnectionDB_Country;
import br.com.comprazencasa.database.DaoAddress;
import br.com.comprazencasa.geo.Street;
import br.com.hotburguer.itens.LAddress;
import br.com.hotburguer.itens.HotReuseAddress;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoHotAddress {

    public Street getAddressFromCep(String cep){
        return new DaoAddress().getStreetFromCEP(cep);
    }

    public ArrayList<HotReuseAddress> getAddressFromUser(int idUser, String idDevice){
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<HotReuseAddress> addresses = new ArrayList<>();

        if (con != null) {
            try {
                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ords.id, country, state, city, neighborhood, street, cep, number, latitude, longitude, complement, ");
                sqlConstruct.append("CONVERT(CAST(street as BINARY) USING utf8) as street FROM hot_orders ords ");
                sqlConstruct.append("LEFT JOIN hot_users user ON user.id=id_user ");
                sqlConstruct.append("WHERE id_user=? AND user.device_id=? AND reuse_address=1 GROUP BY street; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET NAMES UTF8 ;");
                pst.executeBatch();

                pst.setInt(1, idUser);
                pst.setString(2, idDevice);

                ResultSet rs = pst.executeQuery();

                HotReuseAddress reuse ;
                while (rs.next()) {
                    LAddress address = new LAddress();
                    //address.setId(rs.getInt("id"));
                    address.setContry(rs.getString("country"));
                    address.setState(rs.getString("state"));
                    address.setCity(rs.getString("city"));
                    address.setNeighborhood(rs.getString("neighborhood"));
                    address.setStreet(rs.getString("street"));
                    address.setCEP(rs.getString("cep"));
                    address.setNumber(rs.getInt("number"));
                    address.setLatitude(rs.getDouble("latitude"));
                    address.setLongitude(rs.getDouble("longitude"));
                    address.setComplement(rs.getString("complement"));

                    reuse = new HotReuseAddress();
                    reuse.setIdUser(idUser);
                    reuse.setIdOrder(rs.getInt("id"));
                    reuse.setAddress(address);

                    addresses.add(reuse);

                }

                con.close();
                pst.close();

                return addresses ;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean disableReUserAddress(int idUser, int idOrder){
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_orders SET reuse_address=0 ");
                sqlConstruct.append(" WHERE id=? AND id_user=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idOrder);
                pst.setInt(2, idUser);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffecteds > 0) ;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

        return false ;
    }

    public LAddress insertNeighborhood(LAddress address) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sqlNeighborhood = new StringBuilder();
            sqlNeighborhood.append("INSERT INTO hot_operations_neighborhood (uf, country, state, city, neighborhood) ");
            sqlNeighborhood.append("SELECT * FROM (SELECT ?, ?, ?, ?, ?) AS tmp ");
            sqlNeighborhood.append("WHERE NOT EXISTS ( ");
            sqlNeighborhood.append("SELECT neighborhood FROM hot_operations_neighborhood WHERE neighborhood = ? ");
            sqlNeighborhood.append(") LIMIT 1; ");

            try {
                pst = con.prepareStatement(sqlNeighborhood.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, address.getUF());
                pst.setString(2, address.getContry());
                pst.setString(3, address.getState());
                pst.setString(4, address.getCity());
                pst.setString(5, address.getNeighborhood());

                int linesAffecteds = pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()){
                    address.setId(rs.getInt(1));
                }

                con.close();
                pst.close();

                return address;
            } catch (SQLException e) {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean insertStreet(int idlocal, String street, String cep) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO hot_operations_street (id_operation_neighborhood, street, cep) ");
            sql.append("SELECT * FROM (SELECT ?, ?, ?) AS tmp ");
            sql.append("WHERE NOT EXISTS ( ");
            sql.append("SELECT cep FROM hot_operations_street WHERE cep=? ");
            sql.append(") LIMIT 1; ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idlocal);
                pst.setString(2, street);
                pst.setString(3, cep);
                pst.setString(4, cep);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffecteds > 0);
            } catch (SQLException e) {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public ArrayList<String> getNeighborhoodsFromName(String neighborhood){
        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        ArrayList<String> addresses = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder search = new StringBuilder();
                search.append(neighborhood);
                search.append("%");

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT neighborhood FROM streets str ");
                sqlConstruct.append("WHERE neighborhood LIKE ? GROUP BY neighborhood ");

                pst = con.prepareStatement(sqlConstruct.toString());
                //pst.setInt(1, idUser);
                pst.setString(1, search.toString());

                System.out.println("PST: "+pst.toString());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    addresses.add(rs.getString("neighborhood"));
                }

                con.close();
                pst.close();

                return addresses ;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

    public ArrayList<String> getCityFromName(String city){
        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        ArrayList<String> addresses = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder search = new StringBuilder();
                search.append(city);
                search.append("%");

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT city_name FROM streets str ");
                sqlConstruct.append("WHERE city_name LIKE ? GROUP BY city_name ");

                pst = con.prepareStatement(sqlConstruct.toString());
                //pst.setInt(1, idUser);
                pst.setString(1, search.toString());

                System.out.println("PST: "+pst.toString());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    addresses.add(rs.getString("city_name"));
                }

                con.close();
                pst.close();

                return addresses ;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
