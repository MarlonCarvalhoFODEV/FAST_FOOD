/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;


import br.com.comprazencasa.geo.City;
import br.com.comprazencasa.geo.GeoCoordinate;
import br.com.comprazencasa.geo.LatLng;
import br.com.comprazencasa.geo.Street;
import br.com.comprazencasa.util.ChildSuporte;
import br.com.comprazencasa.util.GrupoSuporte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marlon
 */
public class DaoAddress {

    public boolean registerState(String UF, String NAME) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "Insert into state (uf, name) "
                        + "values (?,?)";

                pst = con.prepareStatement(sql);
                pst.setString(1, UF);
                pst.setString(2, NAME);
                pst.execute();

                con.close();
                pst.close();

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (!con.isClosed()) {
                        con.close();
                        pst.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;
    }

    public synchronized LinkedList<City> getAllCity() {
        LinkedList<City> citys = new LinkedList<>();

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "Select name From city_county";

                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                String name;

                City city;
                while (rs.next()) {
                    city = new City();
                    city.setName(formatCityName(rs.getString("name")));
                    citys.add(city);
                }

                pst.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return citys;
    }

    public LinkedList<String> getAllCityName() {
        LinkedList<String> citys = new LinkedList<>();

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "Select name From city_county";

                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                String name;
                while (rs.next()) {
                    citys.add(formatCityName(rs.getString("name")));
                }

                pst.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return citys;
    }

    public boolean verifyCityExist(String name) {

        Connection con = ConnectionDB_Country.connect();
        ;
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT * FROM city_county WHERE name=?";

                pst = con.prepareStatement(sql);
                pst.setString(1, name);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    pst.close();
                    con.close();
                    return true;
                }
                pst.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (!con.isClosed()) {
                        con.close();
                        pst.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;
    }

    public synchronized boolean registerCity(City city) {
        if (!verifyCityExist(city.getName())) {

            Connection con = ConnectionDB_Country.connect();
            PreparedStatement pst = null;

            if (con != null) {
                try {
                    String sql = "Insert into city_county (id_state,ddd,uf, ibge, name, "
                            + "altitude,latitude, longitude,cep, area_min_cep,area_max_cep) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?)";

                    pst = con.prepareStatement(sql);
                    pst.setInt(1, city.getIdState());
                    pst.setInt(2, city.getDdd());
                    pst.setString(3, city.getUf());
                    pst.setInt(4, city.getIBGE());
                    pst.setString(5, city.getName());
                    pst.setDouble(6, city.getAltitude());
                    pst.setDouble(7, city.getLatitude());
                    pst.setDouble(8, city.getLongitude());
                    pst.setString(9, city.getCep());
                    pst.setString(10, city.getAreaMinCEP());
                    pst.setString(11, city.getAreaMaxCEP());
                    pst.execute();

                    con.close();
                    pst.close();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (!con.isClosed()) {
                            con.close();
                            pst.close();
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return false;
    }

    public boolean registerNeighbohood(int idCity, String name, double latitude, double longitude) {
        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "Insert into neighborhood (id_city_county, name, longitude, latitude) "
                        + "values (?,?,?,?)";

                pst = con.prepareStatement(sql);
                pst.setInt(1, idCity);
                pst.setString(2, name);
                pst.setDouble(3, latitude);
                pst.setDouble(4, longitude);
                pst.execute();

                con.close();
                pst.close();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

    public synchronized boolean registerStreet(Street street) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            if (street.getName() != null && street.getCep() != null
                    && !existStreet(street.getName(), street.getCep())) {


                try {
                    String sql = "Insert into streets "
                            + "(uf, ibge, ddd, name,city_name, altitude, latitude, longitude"
                            + ",neighborhood, cep) "
                            + "values (?,?,?,?,?,?,?,?,?,?)";

                    pst = con.prepareStatement(sql);
                    pst.setString(1, street.getInitials());
                    pst.setInt(2, street.getIBGE());
                    pst.setInt(3, street.getDdd());
                    pst.setString(4, street.getName());
                    pst.setString(5, street.getCityName());
                    pst.setDouble(6, street.getAltitude());
                    pst.setDouble(7, street.getLatitude());
                    pst.setDouble(8, street.getLongitude());
                    pst.setString(9, street.getNeighborhood());
                    pst.setString(10, street.getCep());
                    pst.execute();

                    con.close();
                    pst.close();

                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (street.getName() != null && street.getCep() != null) {
                try {
                    if (con != null && !con.isClosed()) {
                        con.close();
                    }
                } catch (SQLException d) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, d);
                }
//            updateNeighborhoodFromCEP(street.getNeighborhood(), street.getCep());
            }
        }

        return false;
    }

    public synchronized boolean existStreet(String streetName, String cep) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        try {
            String sql = "SELECT * FROM streets "
                    + "WHERE name like '%" + streetName + "%' AND cep=? ";


            pst = con.prepareStatement(sql);
            pst.setString(1, cep);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                con.close();
                pst.close();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public synchronized boolean verifyStreetExistFromCEP(String cep) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT * FROM streets "
                        + "WHERE cep=? ";

                pst = con.prepareStatement(sql);
                pst.setString(1, cep);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    con.close();
                    pst.close();
                    return true;
                }

                con.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (!con.isClosed()) {
                        con.close();
                        pst.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;
    }

    public synchronized Street getStreetFromCEP(String CEP) {
        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("select * from streets street ");
                sqlConstruct.append("WHERE cep= ? AND id>0 ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.addBatch("SET NAMES UTF8 ;");
                pst.setString(1, CEP.replace("-","").replaceAll(" ","").replace("\"",""));
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    Street street = new Street();
                    street.setId(rs.getInt("id"));
                    street.setInitials(rs.getString("uf"));
                    street.setIBGE(rs.getInt("ibge"));
                    street.setDdd(rs.getInt("ddd"));
                    street.setName(rs.getString("name"));
                    street.setCityName(rs.getString("city_name"));
                    street.setAltitude(rs.getDouble("altitude"));
                    street.setLatitude(rs.getDouble("latitude"));
                    street.setLongitude(rs.getDouble("longitude"));
                    street.setNeighborhood(rs.getString("neighborhood"));
                    street.setCep(rs.getString("cep"));
                    street.setUF(rs.getString("uf"));
                    street.setStateName(decodeUFToCountyName(rs.getString("uf")));

                    con.close();
                    pst.close();
                    return street;
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

        return null;
    }

    private String decodeUFToCountyName(String uf) {
        switch (uf.toLowerCase()) {
            case "rj": return "Rio de Janeiro";


            default:
                return null;
        }
    }

    public synchronized LinkedList<String> getAllCeps() {
        LinkedList<String> ceps = new LinkedList<>();


        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT cep FROM streets";

                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    ceps.add(rs.getString("cep"));
                }

                con.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (!con.isClosed()) {
                        con.close();
                        pst.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return ceps;
    }

    public synchronized double calculateDistanceKM(String startCep, String andCep) {
        GeoCoordinate geoStart = new GeoCoordinate(getLatAndLongFromCEP(startCep));
        GeoCoordinate geoFinal = new GeoCoordinate(getLatAndLongFromCEP(andCep));

        return geoStart.distanceInKm(geoFinal);
    }

    public synchronized double calculateDistanceKM(double startLat, double startLng, double andLat, double andLng) {
        GeoCoordinate geoStart = new GeoCoordinate(new LatLng(startLat, startLng));
        GeoCoordinate geoFinal = new GeoCoordinate(new LatLng(andLat, andLng));

        return geoStart.distanceInKm(geoFinal);
    }

    public synchronized double calculateDistance(double startLat, double startLng, double andLat, double andLng, String unit) {
        GeoCoordinate geoStart = new GeoCoordinate(new LatLng(startLat, startLng));
        GeoCoordinate geoFinal = new GeoCoordinate(new LatLng(andLat, andLng));

        return geoStart.distance(startLat, startLng, andLat, andLng,unit);
    }

    private synchronized LatLng getLatAndLongFromCEP(String cep) {

        if (cep != null && !cep.isEmpty()) cep = cep.replaceAll("-", "");

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT latitude, longitude FROM streets WHERE cep=? ";

                pst = con.prepareStatement(sql);
                pst.setString(1, cep);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {

                    LatLng lat = new LatLng();
                    lat.lat = rs.getDouble("latitude");
                    lat.lng = rs.getDouble("longitude");

                    con.close();
                    pst.close();

                    return lat;
                }
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return null;
    }

    public synchronized boolean updateNeighborhoodFromCEP(String neighborhood, String cep) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "UPDATE streets SET neighborhood=? WHERE id=? AND cep=? ";

                Street street = getStreetFromCEP(cep);

                if (street != null) {

                    pst = con.prepareStatement(sql);
                    pst.setString(1, neighborhood);
                    pst.setInt(2, street.getId());
                    pst.setString(3, cep);
                    pst.executeUpdate();

                    con.close();
                    pst.close();

                    NewClass.sleeping(1);

                    return true;
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return false;
    }

    public synchronized void registerCepTest(String cep) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;
        try {
            String sql = "INSERT INTO fff (cep) VALUES (?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, cep);
            pst.execute();

            con.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void deleteDuplicateStreet() {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT id,cep, count(cep) c FROM streets "
                        + "GROUP BY cep having c > 1 ";

                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    deleteStreet(rs.getString("cep"));
                }

                con.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized boolean deleteStreet(String cep) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "DELETE FROM streets WHERE cep=? limit 1 ";

                pst = con.prepareStatement(sql);
                pst.setString(1, cep);
                pst.execute();

                con.close();
                pst.close();

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

    public synchronized static String formatCityName(String city) {
        if (city.contains("(") && city.contains(")")) {
            try {
                city = city
                        .replaceAll("[ (]", "-")
                        .replaceAll("[) ]", "");
            } catch (java.util.regex.PatternSyntaxException e) {
                e.printStackTrace();
            }
        }

        String cityName = city
                .replaceAll("'", "")
                .replaceAll("%C3%", "")
                .replaceAll("--", "-")
                .replaceAll(" ", "-")
                .replaceAll("á", "a")
                .replaceAll("é", "e")
                .replaceAll("í", "i")
                .replaceAll("ó", "o")
                .replaceAll("ú", "u")
                .replaceAll("ã", "a")
                .replaceAll("õ", "o")
                .replaceAll("ç", "c")
                .replaceAll("ô", "o")
                .replaceAll("ê", "e")
                .replaceAll("â", "a");
        return cityName.toLowerCase();
    }

    public LatLng getLatLng(String cep) {

        Connection con = ConnectionDB_Country.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT latitude, longitude FROM streets WHERE cep=? ";

                pst = con.prepareStatement(sql);
                pst.setString(1, cep.replace(".","").replace("-",""));
                ResultSet rs = pst.executeQuery();

                LatLng lat = null;
                while (rs.next()) {
                    lat = new LatLng(rs.getDouble("latitude"), rs.getDouble("longitude"));
                }

                con.close();
                pst.close();

                return lat;
            } catch (SQLException ex) {
                Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

    public Street getStreetFromCoordinate(LatLng latLng) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM streets WHERE latitude=? AND longitude=? ";

        if (con != null) {
            try {
                DecimalFormat dec = new DecimalFormat("0.######");

                double newLat = Double.valueOf(dec.format(latLng.lat));
                double newLng = Double.valueOf(dec.format(latLng.lng));

                pst = con.prepareStatement(sql);
                pst.setDouble(1, newLat);
                pst.setDouble(2, newLng);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    Street street = new Street();
                    street.setId(rs.getInt("id"));
                    street.setInitials(rs.getString("uf"));
                    street.setIBGE(rs.getInt("ibge"));
                    street.setDdd(rs.getInt("ddd"));
                    street.setName(rs.getString("name"));
                    street.setCityName(rs.getString("city_name"));
                    street.setAltitude(rs.getDouble("altitude"));
                    street.setLatitude(rs.getDouble("latitude"));
                    street.setLongitude(rs.getDouble("longitude"));
                    street.setNeighborhood(rs.getString("neighborhood"));
                    street.setCep(rs.getString("cep"));
                    street.setUF(rs.getString("uf"));
                    street.setStateName(decodeUFToCountyName(rs.getString("uf")));
                    con.close();
                    pst.close();

                    return street;
                }

            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        }

        return null;
    }

    public String getCepFromCoordinate(LatLng latLng) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT cep FROM streets WHERE latitude=? AND longitude=? ";

        if (con != null) {
            try {
                DecimalFormat dec = new DecimalFormat("0.#######");

                double newLat = Double.valueOf(dec.format(latLng.lat));
                double newLng = Double.valueOf(dec.format(latLng.lng));

                pst = con.prepareStatement(sql);
                pst.setDouble(1, newLat);
                pst.setDouble(2, newLng);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {

                    return rs.getString("cep");
                }

            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        }

        return null;
    }

    public Street getStreetFromAddress(String address) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM streets WHERE name LIKE '%" + address + "%' ";

        if (con != null) {
            try {

                pst = con.prepareStatement(sql);
                pst.addBatch("SET NAMES UTF8 ;");
                pst.executeBatch();

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    Street street = new Street();
                    street.setId(rs.getInt("id"));
                    street.setInitials(rs.getString("uf"));
                    street.setIBGE(rs.getInt("ibge"));
                    street.setDdd(rs.getInt("ddd"));
                    street.setName(rs.getString("name"));
                    street.setCityName(rs.getString("city_name"));
                    street.setAltitude(rs.getDouble("altitude"));
                    street.setLatitude(rs.getDouble("latitude"));
                    street.setLongitude(rs.getDouble("longitude"));
                    street.setNeighborhood(rs.getString("neighborhood"));
                    street.setCep(rs.getString("cep"));
                    street.setUF(rs.getString("uf"));
                    street.setStateName(decodeUFToCountyName(rs.getString("uf")));
                    con.close();
                    pst.close();

                    return street;
                }

            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        }
        return null;
    }

    public List<GrupoSuporte> getLocalsDelivery() {

        Map<String, String> map = new HashMap<>();
        List<GrupoSuporte> locals = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {

            String sql = "SELECT city, neighborhood FROM operation_neighborhood WHERE is_active=1 ";
            try {
                pst = con.prepareStatement(sql);
                pst.addBatch("SET NAMES UTF8 ;");
                pst.executeBatch();
                ResultSet rs = pst.executeQuery();

                int position = -1;
                GrupoSuporte grupo;
                while (rs.next()) {
                    if (!map.containsKey(rs.getString("city"))) {
                        map.put(rs.getString("city"), null);

                        grupo = new GrupoSuporte();
                        grupo.setTitulo(rs.getString("city"));
                        locals.add(grupo);
                        position++;
                    } else {
                        //map.get(rs.getString("city"));

                        ChildSuporte childSuporte = new ChildSuporte();
                        childSuporte.setTitulo(rs.getString("neighborhood"));

                        locals.get(position).addSubItem(childSuporte);
                    }
                }

                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        }
        return locals;
    }

    public Street verifyOperationNeighborhood(String cep) {

        Street street = getStreetFromCEP(cep);

        if(street != null){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null;

            if (con != null) {
                try {

                    StringBuilder sql = new StringBuilder();
                    sql.append("SELECT * FROM operation_neighborhood ");
                    sql.append("WHERE uf=? AND county=? AND city LIKE '%");
                    sql.append(street.getCityName());
                    sql.append("%' ");
                    sql.append("AND neighborhood='");
                    sql.append(street.getNeighborhood());
                    sql.append("' AND is_active=1 ");

                    pst = con.prepareStatement(sql.toString());
                    pst.setString(1, street.getUF());
                    pst.setString(2, street.getStateName());

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {

                        con.close();
                        pst.close();
                        return street;
                    }

                    con.close();
                    pst.close();
                } catch (SQLException ex) {
                    try {
                        Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
                        con.close();
                        if(pst!=null)pst.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } finally {
                    try {
                        con.close();
                        if(pst!=null)pst.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        }
        return null;
    }

}
