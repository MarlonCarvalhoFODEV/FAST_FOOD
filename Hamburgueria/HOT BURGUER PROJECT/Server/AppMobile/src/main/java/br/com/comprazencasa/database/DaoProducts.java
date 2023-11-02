/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import br.com.comprazencasa.itens.ITFlavor;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.itens.ITSubCategorieResponse;
import br.com.comprazencasa.products.LoadingProducts;
import br.com.comprazencasa.util.ImagemUtil;
import br.com.comprazencasa.util.SearchProduct;
import br.com.comprazencasa.util.SendImage;
import br.com.comprazencasa.util.UtilPrices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marlon Carvalho CON CLOSED OK
 */
public class DaoProducts {

    private static final int COUNT_PRODUCTS = 21;

    public synchronized LinkedList<ITProduct> searchProducts(SearchProduct it, boolean active) {

        ITProduct p;
        LinkedList<ITProduct> products = new LinkedList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT prod.id, prod.path_img, prod.percentage_discount, prod.title, prod.in_price, prod.per_price, prod.is_weight, prod.is_tangible, prod.id_flavor,");
                sqlConstruct.append("rate.rate_per_czc, rate.rate_per_credit, prod.per_price, prod.instalments, prod.categorie, prod.sub_categorie,");
                sqlConstruct.append("flav.id, flav.flavor FROM czc_products prod  ");
                sqlConstruct.append("JOIN rate_services rate ");
                sqlConstruct.append("LEFT JOIN flavors flav  ");
                sqlConstruct.append("ON flav.id=prod.id_flavor ");
                sqlConstruct.append("WHERE prod.id > ? ");
                sqlConstruct.append("AND prod.title LIKE '%");
                sqlConstruct.append(it.getTitle());
                sqlConstruct.append("%' ");
                sqlConstruct.append("AND prod.categorie=? ");
                sqlConstruct.append(" AND prod.status =");
                sqlConstruct.append((active ? " 'active' " : " 'inactive' "));
                sqlConstruct.append("AND rate.date=(select date from rate_services order by date DESC limit 1) ");
                sqlConstruct.append("ORDER BY prod.id limit ");
                sqlConstruct.append(COUNT_PRODUCTS);

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, it.getIdLastProduct());
                pst.setString(2, it.getCategorie());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {

                    p = new ITProduct();
                    p.setId(rs.getInt("prod.id"));
                    p.setPathImg(rs.getString("prod.path_img"));
                    p.setPercentageDiscount(rs.getInt("prod.percentage_discount"));
                    p.setTitle(rs.getString("prod.title"));
                    p.setTangible(rs.getBoolean("is_tangible"));

                    double inPrice = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.in_price"));
                    double price = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.per_price"));

                    p.setInPrice(rs.getInt("percentage_discount") > 0 ? inPrice : 0);

                    p.setPerPrice(active ? price : rs.getDouble("prod.per_price"));
                    p.setInstalments(rs.getInt("prod.instalments"));
                    p.setCategorie(rs.getString("prod.categorie"));
                    //p.setSubCategorie(rs.getString("prod.sub_categorie"));
                    p.setWeight(rs.getBoolean("prod.is_weight"));
                    p.setFlavors(rs.getInt("id_flavor") > 0 ? true : false);

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));

                    p.setFlavor(f);

                    if (products.size() < COUNT_PRODUCTS - 1) {
                        products.add(p);
                    } else {
                        products.get(products.size() - 1).setHaveMore(true);
                        break;
                    }
                }

                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return products;
    }

    public synchronized LinkedList<ITProduct> getProducts(ITProduct it, boolean active) {

        ITProduct p;
        LinkedList<ITProduct> products = new LinkedList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT prod.id, prod.path_img, prod.percentage_discount, prod.title, prod.title, prod.in_price, prod.per_price, ");
                sql.append("rate.rate_per_czc, rate.rate_per_credit, prod.per_price, prod.instalments, prod.categorie, prod.sub_categorie, prod.is_tangible, ");
                sql.append("prod.is_weight, prod.id_flavor, flav.id, flav.flavor FROM czc_products prod ");
                sql.append("JOIN rate_services rate ");
                sql.append("LEFT JOIN flavors flav ");
                sql.append("ON flav.id=prod.id_flavor ");
                sql.append("WHERE prod.id > ? ");
                sql.append("AND prod.categorie=? ");
                //sql.append("AND prod.sub_categorie=? ");
                sql.append("AND prod.status = ");
                sql.append((active ? "'active' " : "'inactive' "));
                sql.append("AND rate.date=(select date from rate_services order by date DESC limit 1) ");
                sql.append("ORDER BY prod.id ASC limit ");
                sql.append(COUNT_PRODUCTS);

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, it.getId());
                pst.setString(2, it.getCategorie());
                //pst.setString(3, it.getSubCategorie());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    p = new ITProduct();
                    p.setId(rs.getInt("prod.id"));
                    p.setPathImg(rs.getString("prod.path_img"));
                    p.setPercentageDiscount(rs.getInt("prod.percentage_discount"));
                    p.setTitle(rs.getString("prod.title"));

                    double inPrice = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.in_price"));
                    double price = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.per_price"));

                    p.setInPrice(rs.getInt("percentage_discount") > 0 ? inPrice : 0);
                    p.setPerPrice(active ? price : rs.getDouble("prod.per_price"));
                    p.setInstalments(rs.getInt("prod.instalments"));
                    p.setCategorie(rs.getString("prod.categorie"));
                    p.setSubCategorie(rs.getString("prod.sub_categorie"));
                    p.setWeight(rs.getBoolean("prod.is_weight"));
                    p.setFlavors(rs.getInt("prod.id_flavor") > 0 ? true : false);
                    p.setTangible(rs.getBoolean("is_tangible"));

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));

                    p.setFlavor(f);

                    if (products.size() < COUNT_PRODUCTS - 1) {
                        products.add(p);
                    } else {
                        products.get(products.size() - 1).setHaveMore(true);
                        break;
                    }
                }

                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return products;
    }

    public synchronized LinkedList<ITProduct> getProducts(boolean active) {

        ITProduct p;
        LinkedList<ITProduct> products = new LinkedList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT prod.id, prod.path_img, prod.percentage_discount, prod.title, prod.title, prod.in_price, prod.per_price, ");
                sql.append("rate.rate_per_czc, rate.rate_per_credit, prod.per_price, prod.instalments, prod.categorie, prod.sub_categorie, prod.is_tangible, ");
                sql.append("prod.is_weight, prod.id_flavor, flav.id, flav.flavor FROM czc_products prod ");
                sql.append("JOIN rate_services rate ");
                sql.append("LEFT JOIN flavors flav ");
                sql.append("ON flav.id=prod.id_flavor ");
                sql.append("WHERE prod.id > 0 ");
                sql.append("AND prod.categorie='Morning' ");
                //sql.append("AND prod.sub_categorie!='' ");
                sql.append("AND prod.status = ");
                sql.append((active ? "'active' " : "'inactive' "));
                sql.append("AND rate.date=(select date from rate_services order by date DESC limit 1) ");
                sql.append("ORDER BY prod.id ASC limit ");
                sql.append(COUNT_PRODUCTS);

                pst = con.prepareStatement(sql.toString());
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    p = new ITProduct();
                    p.setId(rs.getInt("prod.id"));
                    p.setPathImg(rs.getString("prod.path_img"));
                    p.setPercentageDiscount(rs.getInt("prod.percentage_discount"));
                    p.setTitle(rs.getString("prod.title"));

                    double inPrice = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.in_price"));
                    double price = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.per_price"));

                    p.setInPrice(rs.getInt("percentage_discount") > 0 ? inPrice : 0);

                    p.setPerPrice(active ? price : rs.getDouble("prod.per_price"));
                    p.setInstalments(rs.getInt("prod.instalments"));
                    p.setCategorie(rs.getString("prod.categorie"));
                    //p.setSubCategorie(rs.getString("prod.sub_categorie"));
                    p.setWeight(rs.getBoolean("prod.is_weight"));
                    p.setTangible(rs.getBoolean("prod.is_tangible"));
                    p.setFlavors(rs.getInt("prod.id_flavor") > 0 ? true : false);

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));

                    p.setFlavor(f);

                    if (products.size() < COUNT_PRODUCTS - 1) {
                        products.add(p);
                    } else {
                        products.get(products.size() - 1).setHaveMore(true);
                        break;
                    }
                }

                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return products;
    }

    public synchronized ITProduct getProductFromID(int id) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        ITProduct p = null;

        if (con != null) {
            try {
                String sql = "SELECT * FROM czc_products WHERE id=? ORDER BY id ASC";

                pst = con.prepareStatement(sql);
                pst.setInt(1, id);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    p = new ITProduct();
                    p.setId(rs.getInt("id"));
                    p.setPathImg(rs.getString("path_img"));
                    p.setPercentageDiscount(rs.getInt("prod.percentage_discount"));
                    p.setTitle(rs.getString("title"));
                    p.setInPrice(rs.getDouble("in_price"));
                    p.setPerPrice(rs.getDouble("per_price"));
                    p.setInstalments(rs.getInt("instalments"));
                    p.setCategorie(rs.getString("categorie"));
                    p.setTangible(rs.getBoolean("is_tangible"));
                    p.setWeight(rs.getBoolean("is_weight"));

                }

                pst.close();
                con.close();

                return p;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    public synchronized int insertProduct(ITProduct product) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO czc_products ");
                sql.append("(title, in_price, per_price, path_img, percentage_discount, instalments, name_promo, categorie, is_weight, is_tangible) ");
                sql.append("VALUES (?,?,?,?,?,?,?,?,?,?) ");

                pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, product.getTitle());
                pst.setDouble(2, product.getInPrice());
                pst.setDouble(3, product.getPerPrice());
                pst.setString(4, product.getPathImg());
                pst.setInt(5, product.getPercentageDiscount());
                pst.setInt(6, product.getInstalments());
                pst.setString(7, product.getNamePromo());
                pst.setString(8, product.getCategorie());
                pst.setBoolean(9, product.isWeight());
                pst.setBoolean(10, product.isTangible());
                // pst.setString(9, product.getStatus());
                pst.executeUpdate();
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pst.close();
                    con.close();
                    return id;
                }

                pst.close();
                con.close();


            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return -1;
    }

    public synchronized int insertProduct(ITProduct product, byte[] image) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO czc_products ");
                sql.append("(title, in_price, per_price, path_img, percentage_discount, instalments, name_promo, categorie, is_weight, is_tangible, status) ");
                sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?) ");

                pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, product.getTitle());
                pst.setDouble(2, product.getInPrice());
                pst.setDouble(3, product.getPerPrice());
                pst.setString(4, product.getPathImg());
                pst.setInt(5, product.getPercentageDiscount());
                pst.setInt(6, product.getInstalments());
                pst.setString(7, product.getNamePromo());
                pst.setString(8, product.getCategorie());
                pst.setBoolean(9, product.isWeight());
                pst.setBoolean(10, product.isTangible());
                pst.setString(11, product.getStatus());
                // pst.setString(9, product.getStatus());
                pst.executeUpdate();
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);

                    pst.close();
                    con.close();

                    new DaoImage().getProductImage().insertImageProduct(id, product.getPathImg(), image);
                    return id;
                }

                pst.close();
                con.close();


            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return -1;
    }

    public synchronized boolean insertCollectionProducts(LinkedList<ITProduct> collection) {
        collection.forEach((itemProduct) -> {

            byte[] image = ImagemUtil.downloadAndResizeImage(itemProduct.getUrl(), 210);

            ITProduct p = existProductDB(itemProduct.getTitle());
            if (p == null) {
                insertProduct(itemProduct, image);
            } else if (p.getInPrice() != itemProduct.getInPrice() &&
                    p.getPerPrice() != itemProduct.getPerPrice()) {
                itemProduct.setId(p.getId());
                updateProduct(itemProduct, image);
            }
        });
        return true;
    }

    public synchronized void updateProduct(ITProduct p) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;
        try {
            String sql = "UPDATE czc_products SET in_price=?, per_price=?, instalments=?, status=?, is_weight, is_tangible WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setDouble(1, p.getInPrice());
            pst.setDouble(2, p.getPerPrice());
            pst.setInt(3, p.getInstalments());
            pst.setString(4, p.getStatus());
            pst.setInt(5, p.getId());
            pst.setBoolean(6, p.isWeight());
            pst.setBoolean(7, p.isTangible());
            int result = pst.executeUpdate();

            pst.close();
            con.close();

        } catch (SQLException ex) {
            ex.printStackTrace();

            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void updateProduct(ITProduct p, byte[] image) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        try {
            if (con != null) {
                String sql = "UPDATE czc_products SET in_price=?, per_price=?, instalments=?, status=?, is_weight, is_tangible WHERE id = ?";

                pst = con.prepareStatement(sql);

                pst.setDouble(1, p.getInPrice());
                pst.setDouble(2, p.getPerPrice());
                pst.setInt(3, p.getInstalments());
                pst.setString(4, p.getStatus());
                pst.setInt(5, p.getId());
                pst.setBoolean(6, p.isWeight());
                pst.setBoolean(7, p.isTangible());
                pst.executeUpdate();

                pst.close();
                con.close();

                new DaoImage().getProductImage().insertImageProduct(p.getId(), p.getPathImg(), image);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean updateActiveProduct(ITProduct p, String status) {
        int newIdFlavor = 0;
        if (p.getFlavors().size() >= 1 && p.getFlavors().get(0) != null && !p.getFlavors().get(0).getFlavor().isEmpty()) {
            newIdFlavor = saveFlavors(p);
        }

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                if (newIdFlavor > 0) {
                    String sql = "UPDATE czc_products SET in_price=?, per_price=?, instalments=?, status=? ,id_flavor=?, is_weight=?, is_tangible WHERE title = ? ";

                    pst = con.prepareStatement(sql);
                    pst.setDouble(1, p.getInPrice());
                    pst.setDouble(2, p.getPerPrice());
                    pst.setInt(3, p.getInstalments());
                    pst.setString(4, status);
                    pst.setInt(5, newIdFlavor);
                    pst.setBoolean(6, p.isWeight());
                    pst.setString(7, p.getTitle());
                    //pst.setString(8, p.getCategorie());
                } else {
                    String sql = "UPDATE czc_products SET in_price=?, per_price=?, instalments=?, status=? , is_weight=? WHERE title = ? ";

                    pst = con.prepareStatement(sql);
                    pst.setDouble(1, p.getInPrice());
                    pst.setDouble(2, p.getPerPrice());
                    pst.setInt(3, p.getInstalments());
                    pst.setString(4, status);
                    pst.setBoolean(5, p.isWeight());
                    pst.setString(6, p.getTitle());
                   //pst.setString(7, p.getCategorie());
                }

                pst.executeUpdate();

                pst.close();
                con.close();

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                if (con != null) {
                    try {
                        con.close();
                        if(pst!=null)pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }

    public synchronized ITProduct existProductDB(String title) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT * FROM czc_products WHERE title=? ";

                pst = con.prepareStatement(sql);
                pst.setString(1, title);

                ResultSet rs = pst.executeQuery();
                ITProduct p;
                if (rs.next()) {

                    p = new ITProduct();
                    p.setId(rs.getInt("id"));
                    p.setPathImg(rs.getString("path_img"));
                    p.setPercentageDiscount(rs.getInt("percentage_discount"));
                    p.setTitle(rs.getString("title"));
                    p.setInPrice(rs.getDouble("in_price"));
                    p.setPerPrice(rs.getDouble("per_price"));
                    p.setInstalments(rs.getInt("instalments"));
                    p.setCategorie(rs.getString("categorie"));
                    p.setWeight(rs.getBoolean("is_weight"));
                    p.setTangible(rs.getBoolean("is_tangible"));

                    pst.close();
                    con.close();
                    return p;
                }

                pst.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    pst.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    public synchronized boolean insertProductBuy(ITProduct product) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO czc_products ");
                sql.append("(title, in_price, per_price, path_img, percentage_discount, instalments, name_promo, categorie) ");
                sql.append("VALUES (?,?,?,?,?,?,?,?) ");

                pst = con.prepareStatement(sql.toString());

                pst.setString(1, product.getTitle());
                pst.setDouble(2, product.getInPrice());
                pst.setDouble(3, product.getPerPrice());
                pst.setString(4, product.getPathImg());
                pst.setInt(5, product.getPercentageDiscount());
                pst.setInt(6, product.getInstalments());
                pst.setString(7, product.getNamePromo());
                pst.setString(8, product.getCategorie());

                pst.execute();
                pst.close();
                con.close();

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    public synchronized boolean deleteProduct(ITProduct product) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "DELETE FROM czc_products WHERE id=? ";

                pst = con.prepareStatement(sql);
                pst.setInt(1, product.getId());
                pst.execute();

                pst.close();
                con.close();

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    public synchronized boolean deleteAllProducts() {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "DELETE FROM czc_products WHERE id != -1 ";

                pst = con.prepareStatement(sql);
                pst.execute();
                pst.close();
                con.close();

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    public void disableIfNotOnTheList(List<String> productsGuanab, String categorie) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                String sql = "SELECT * FROM czc_products WHERE status='active' AND categorie=? limit 5000";

                pst = con.prepareStatement(sql);
                pst.setString(1, categorie);
                ResultSet rs = pst.executeQuery();

                ITProduct p = null;
                while (rs.next()) {
                    p = new ITProduct();
                    p.setId(rs.getInt("id"));
                    p.setTitle(rs.getString("title"));
                    p.setPerPrice(rs.getDouble("per_price"));
                    p.setCategorie(rs.getString("categorie"));
                    p.setPathImg(rs.getString("path_img"));
                    p.setInstalments(0);
                    p.setStatus("active");

                    if (!productsGuanab.contains(p.getTitle())) {
                        p.setStatus("inactive");
                        updateProduct(p);
                    } else {
                        p.setStatus("active");
                        updateProduct(p);
                    }
                }
                con.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoadingProducts.class.getName()).log(Level.SEVERE, null, ex);

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized boolean disableProduct(ITProduct productDEL) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "UPDATE czc_products SET status=? WHERE id=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, "inactive");
                pst.setInt(2, productDEL.getId());
                pst.executeUpdate();

                con.close();
                pst.close();

                return true;
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);

                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex1);
                }
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

    public boolean verifyFreeProduct(ITProduct p) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT status FROM czc_products WHERE id=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, p.getId());
                ResultSet rs = pst.executeQuery();

                boolean ok = false;
                if (rs.next()) {
                    String status = rs.getString("status");
                    ok = status.equals("active") ? false : true;

                    con.close();
                    pst.close();
                }

                con.close();
                pst.close();

                return ok;
            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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

    public List<SendImage> getAllProducts() {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        List<SendImage> img = new ArrayList<>();

        if (con != null) {
            try {
                String sql = "SELECT id, path_img, title FROM czc_products ";

                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                SendImage s;
                while (rs.next()) {

                    if (!rs.getString("title").contains("Carrefour") && !rs.getString("title").contains("carrefour")) {
                        s = new SendImage();
                        s.setIdProduct(rs.getInt("id"));
                        s.setSubPath(rs.getString("path_img"));
                        img.add(s);
                    }
                }

                pst.close();
                con.close();

                return img;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    public List<ITFlavor> getFlavors(int idProduct) {

        List<ITFlavor> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM czc_products prod ");
        sql.append("JOIN flavors flav ");
        sql.append("ON flav.id_product=prod.id ");
        sql.append("WHERE flav.id_product=? ");
        sql.append("AND flav.status='active' ");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idProduct);

                ResultSet rs = pst.executeQuery();

                ITFlavor it;
                while (rs.next()) {
                    it = new ITFlavor();
                    it.setId(rs.getInt("flav.id"));
                    it.setFlavor(rs.getString("flav.flavor"));
                    itens.add(it);
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
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return itens;
    }

    public synchronized int saveFlavors(ITProduct product) {
        activeOrDeactiveAllFlavorsFromProduct(product, false);

        int idFlavorUpdated = 0 ;
        int countFlavors = 0 ;
        int counter = (countFlavors = product.getFlavors().size());

        StringBuilder sqlContruct = new StringBuilder();
        sqlContruct.append("INSERT INTO flavors (id_product, flavor) ");
        sqlContruct.append("VALUES ");

        for (ITFlavor flavor : product.getFlavors().values()) {
            if ((idFlavorUpdated=verifyFlavorExist(flavor))==0 && !flavor.getFlavor().isEmpty() && flavor.getFlavor().length() >= 2) {
                counter--;
                sqlContruct.append("(");
                sqlContruct.append(flavor.getId_product());
                sqlContruct.append(",'");
                sqlContruct.append(flavor.getFlavor());
                sqlContruct.append("') ");
                sqlContruct.append(counter == 0 ? " ; " : ",");
            } else {
                activeOrDeactiveFlavorsFromProduct(flavor, true);
            }
        }

        if(counter != countFlavors){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null;

            if (con != null) {
                try {

                    try {
                        pst = con.prepareStatement(sqlContruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                        pst.executeUpdate();
                    } catch (SQLSyntaxErrorException e) {
                        e.printStackTrace();
                    }
                    ResultSet rs = pst.getGeneratedKeys();

                    int idFlavor = 0;
                    if (rs.next()) {
                        idFlavor = rs.getInt(1);
                    }

                    con.close();
                    pst.close();

                    return idFlavor;
                } catch (SQLException e) {
                    e.printStackTrace();

                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else return idFlavorUpdated;
        return 0;
    }

    private synchronized int verifyFlavorExist(ITFlavor flavor) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM flavors WHERE id_product=? AND flavor=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, flavor.getId_product());
                pst.setString(2, flavor.getFlavor());
                ResultSet rs = pst.executeQuery();

                int idFlavor = 0 ;
                if(rs.next())idFlavor=rs.getInt("id");

                con.close();
                pst.close();

                return idFlavor;
            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    private boolean desactiveFlavor(ITFlavor flavor) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "UPDATE flavors SET status=? WHERE id=? AND flavor=? ";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, "inactive");
            pst.setInt(2, flavor.getId());
            pst.setString(3, flavor.getFlavor());
            pst.executeUpdate();

            con.close();
            pst.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                con.close();
                pst.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private synchronized boolean activeOrDeactiveAllFlavorsFromProduct(ITProduct product, boolean active) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "UPDATE flavors SET status=? WHERE id>0 AND id_product=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, active ? "active" : "deactive");
                pst.setInt(2, product.getId());

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return linesAffecteds > 0;
            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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

    private synchronized boolean activeOrDeactiveFlavorsFromProduct(ITFlavor flavor, boolean active) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "UPDATE flavors SET status=? WHERE id_product=? AND flavor=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                pst.setString(1, active ? "active" : "deactive");
                pst.setInt(2, flavor.getId_product());
                pst.setString(3, flavor.getFlavor());
                int idUpdated = pst.executeUpdate();

                con.close();
                pst.close();

                return idUpdated>0;
            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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

    public List<ITSubCategorieResponse> getSubCategoriesFromCategorie(ITSubCategorieResponse it, boolean b) {

        List<ITSubCategorieResponse> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT categorie, sub_categorie FROM czc_products WHERE categorie=? ");
            sql.append("and sub_categorie!='' ");
            sql.append("and status='active' ");
            sql.append("GROUP BY sub_categorie limit 6");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, it.getCategorie());
                ResultSet rs = pst.executeQuery();

                ITSubCategorieResponse item;
                while (rs.next()) {

                    item = new ITSubCategorieResponse();
                    item.setCategorie(rs.getString("categorie"));
                    item.setSubCategorie(rs.getString("sub_categorie"));
                    itens.add(item);
                }

                con.close();
                pst.close();
            } catch (SQLException e) {
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


        return itens;
    }
}
