/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import apis.encrypt.Cryptograph;
import br.com.comprazencasa.itens.ITFavorite;
import br.com.comprazencasa.itens.ITStore;
import br.com.comprazencasa.user.ContaUsuario;
import br.com.comprazencasa.user.UserLogin;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class DaoUser {

    private static final int LIMIT_LOAD_STORES = 16;

    public DaoUser(){}

    public ContaUsuario userLogin(UserLogin login) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM user_login ");
                sql.append("WHERE email=? AND pass=?; ");

                pst = con.prepareStatement(sql.toString());
                pst.setString(1, login.getUser());
                pst.setString(2, login.getPassword());
                ResultSet rs = pst.executeQuery();

                ContaUsuario contaUsuario = null ;
                if(rs.next()){
                    contaUsuario = getUserFromID_EMAIL(rs.getInt("id"), rs.getString("email"));
                }

                con.close();
                pst.close();

                return contaUsuario ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);

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
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return null ;
    }

    public ContaUsuario selectFromID(int idUser) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "SELECT * FROM accounts WHERE id=? ";

                pst = con.prepareStatement(sql);
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();

                ContaUsuario account = null ;
                if(rs.next()){
                    account = new ContaUsuario();
                    account.setId(rs.getInt("id"));
                    account.setName(rs.getString("name"));
                    account.setEmail(rs.getString("email"));
                    account.getContato().setEmail(rs.getString("email"));
                    account.setCpf(rs.getString("cpf"));
                    account.setDate(rs.getString("date"));
                    account.getContato().setCel(rs.getString("cel"));
                    account.getEndereco().setCep(rs.getString("cep"));
                    account.getEndereco().setBairro(rs.getString("neighborhood"));
                    account.getEndereco().setEstado(rs.getString("state"));
                    account.getEndereco().setCidade(rs.getString("city"));
                    account.getEndereco().setRuaLogradouro(rs.getString("street"));
                    account.getEndereco().setNumeroResidencia(rs.getInt("residence_number"));
                    account.getEndereco().setComplemento(rs.getString("complement"));
                    account.getEndereco().setUf(rs.getString("uf"));
                    account.setPathImg(rs.getString("img_path"));
                    account.getEndereco().setLatitude(rs.getDouble("latitude"));
                    account.getEndereco().setLongitude(rs.getDouble("longitude"));
                    account.setDataNascimento(rs.getString("date_of_birth"));
                    account.setSexy(rs.getString("sexy").charAt(0));

                }

                con.close();
                pst.close();
                return account ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return null ;
    }

    private ContaUsuario getUserFromID_EMAIL(int id, String email){
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "SELECT * FROM accounts WHERE id=? AND email=? ";

                pst = con.prepareStatement(sql);
                pst.setInt(1, id);
                pst.setString(2, email);
                ResultSet rs = pst.executeQuery();

                ContaUsuario account = null ;
                if(rs.next()){
                    account = new ContaUsuario();
                    account.setId(rs.getInt("id"));
                    account.setIdCustomer(rs.getInt("id_customer"));
                    account.setName(rs.getString("name"));
                    account.setEmail(rs.getString("email"));
                    account.getContato().setEmail(rs.getString("email"));
                    account.setCpf(rs.getString("cpf"));
                    account.setDate(rs.getString("date"));
                    account.getContato().setCel(rs.getString("cel"));
                    account.getEndereco().setCep(rs.getString("cep"));
                    account.getEndereco().setBairro(rs.getString("neighborhood"));
                    account.getEndereco().setEstado(rs.getString("state"));
                    account.getEndereco().setCidade(rs.getString("city"));
                    account.getEndereco().setRuaLogradouro(rs.getString("street"));
                    account.getEndereco().setNumeroResidencia(rs.getInt("residence_number"));
                    account.getEndereco().setComplemento(rs.getString("complement"));
                    account.getEndereco().setUf(rs.getString("uf"));
                    account.setPathImg(rs.getString("img_path"));
                    account.getEndereco().setLatitude(rs.getDouble("latitude"));
                    account.getEndereco().setLongitude(rs.getDouble("longitude"));
                    account.setDataNascimento(rs.getString("date_of_birth"));
                    account.setSexy(rs.getString("sexy").charAt(0));
                    account.setBanned(rs.getBoolean("banned"));

                }

                con.close();
                pst.close();

                if(account.isBanned()){
                    account = new ContaUsuario();
                    account.setBanned(true);
                }


                return account ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return null ;
    }
    
    private boolean registerLogin(int id, String email, String password){
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = " INSERT INTO user_login (id, email, pass)"
                        + " VALUES (?,?,?) " ;

                pst = con.prepareStatement(sql);
                pst.setInt(1, id);
                pst.setString(2, email);
                pst.setString(3, password);
                pst.execute();

                pst.close();
                con.close();

                return true ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);

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
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return false ;
    }
    
    public WebResponses userRegister(ContaUsuario register) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        WebResponses response = new WebResponses();

        if(con != null){
            try {
                String sql = "INSERT INTO accounts "
                        + "(name,cpf,email,cel, cep,state,neighborhood,street, city, complement, term_politics, img_path,residence_number, uf, sexy, date_of_birth, latitude, longitude) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, register.getName());
                pst.setString(2, register.getCpf());
                pst.setString(3, register.getEmail());
                pst.setString(4, register.getContato().getCel());
                pst.setString(5, register.getEndereco().getCep());
                pst.setString(6, register.getEndereco().getEstado());
                pst.setString(7, register.getEndereco().getBairro());
                pst.setString(8, register.getEndereco().getRuaLogradouro());
                pst.setString(9, register.getEndereco().getCidade());
                pst.setString(10, register.getEndereco().getComplemento());
                pst.setBoolean(11, register.isTermsOfUseAndPolicyPrivacity());
                pst.setString(12, register.getPathImg());
                pst.setInt(13, register.getEndereco().getNumeroResidencia());
                pst.setString(14, register.getEndereco().getUf());
                pst.setString(15, String.valueOf(register.getSexy()));
                pst.setString(16, register.getDataNascimento());
                pst.setDouble(17, register.getEndereco().getLatitude());
                pst.setDouble(18, register.getEndereco().getLongitude());

                try{
                    pst.executeUpdate();
                    ResultSet rs = pst.getGeneratedKeys();


                    if(rs.next()){
                        int id = rs.getInt(1);

                        boolean registerdLogin = registerLogin(id, register.getEmail(), register.getSenha());
                        if(registerdLogin){
                            if(getCountRegisters() <= 700 && register.getCpf() != null){
                                new Thread(){
                                    @Override
                                    public void run() {
                                        new DaoCoupons().insert3CouponsForUsersRegisters(id);
                                    }
                                };
                            }
                        }

                        response.setIdUser(id);
                        response.setComunication(1);

                        con.close();
                        pst.close();


                        return response ;
                    }
                }catch(SQLIntegrityConstraintViolationException e){
                    con.close();
                    pst.close();
                    response.setComunication(0);
                    return response ;
                }
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);
                    con.close();
                    pst.close();
                    response.setComunication(-1);
                    return response ;
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        response.setComunication(-1);
        return response ;
    }

    public boolean userUpdate(ContaUsuario registerUpdate) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {

                if(registerUpdate.getId() == 0) {
                    registerUpdate.setId(getUserIDFromEmail(registerUpdate.getEmail()));
                }

                String sql = "UPDATE accounts SET name=?, cpf=?, email=?, cel=?, cep=?,"
                        + "state=?, neighborhood=?, street=?, complement=?, city=?, img_path=?, "
                        + "sexy=?, id_customer=? "
                        + "WHERE id=? " ;

                pst = con.prepareStatement(sql);
                pst.setString(1, registerUpdate.getName());
                pst.setString(2, registerUpdate.getCpf());
                pst.setString(3, registerUpdate.getEmail());
                pst.setString(4, registerUpdate.getContato().getCel());
                pst.setString(5, registerUpdate.getEndereco().getCep());
                pst.setString(6, registerUpdate.getEndereco().getEstado());
                pst.setString(7, registerUpdate.getEndereco().getBairro());
                pst.setString(8, registerUpdate.getEndereco().getRuaLogradouro());
                pst.setString(9, registerUpdate.getEndereco().getComplemento());
                pst.setString(10, registerUpdate.getEndereco().getCidade());
                pst.setString(11, registerUpdate.getPathImg());
                pst.setString(12, String.valueOf(registerUpdate.getSexy()));
                pst.setInt(13, registerUpdate.getId());
                pst.setInt(14, registerUpdate.getIdCustomer());

                pst.executeUpdate();

                con.close();
                pst.close();

                return true ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return false ;
    }
    
    private int getUserIDFromEmail(String email){
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;
        
        String sql = "SELECT id FROM accounts "
                + "WHERE email=? " ;
        
        if(con != null){
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, email);
                ResultSet rs = pst.executeQuery();

                if(rs.next()){
                    return rs.getInt("id");
                }
                con.close();
                pst.close();

                return 0 ;
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return 0 ;
    }

    public int getCountRegisters() {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            String sql = "SELECT count(*) c FROM accounts ;";
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                if(rs.next()) return rs.getInt("c");
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

        return 0;
    }

    public int getIdCustomerPagarme(int idUser) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            String sql = "SELECT id_customer FROM accounts WHERE id=?;";
            try {

                pst = con.prepareStatement(sql);
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();
                if(rs.next()) return rs.getInt("id_customer");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    if(pst!=null)pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    public boolean ban(int idUSer, String bannedReason) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            String sql = "UPDATE accounts SET banned=1, banned_reason=? WHERE id=?; ";
            try {

                pst = con.prepareStatement(sql);
                pst.setString(1, bannedReason);
                pst.setInt(2, idUSer);

                int linesAffectds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffectds > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    if(pst!=null)pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false ;
    }

    public ArrayList<ITStore> getFavorites(ITFavorite favorite) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        ArrayList<ITStore> itens = new ArrayList<>();

        if(con != null){
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT store.id, store.type, store.name, categb.b_categorie, store_config.time_min_delivery, ");
            sql.append("store_config.time_max_delivery, store_config.max_installments_without_fee, store_config.fee_delivery, store_config.max_installments, ");
            sql.append("store_config.fee_installments, store_config.minimum_purchase_installments ,address.country, address.state, address.city, address.neighborhood, address.latitude, address.longitude, ");
            sql.append("Geo(acc.longitude,acc.latitude,address.longitude,address.latitude) distance_in_km, ");
            sql.append("AverageRating(eval.s1, eval.s2, eval.s3, eval.s4, eval.s5) avg_rating, ");
            sql.append("(SELECT favorited FROM stores_favorited WHERE id_account=acc.id AND id_store=store.id) favorited ");
            sql.append("FROM stores_favorited favorite ");
            sql.append("LEFT JOIN stores store ");
            sql.append("ON store.id=favorite.id_store ");
            sql.append("LEFT JOIN stores_configuration store_config ");
            sql.append("ON store_config.id_store = store.id ");
            sql.append("LEFT JOIN stores_address address ");
            sql.append("ON address.id_store = store.id ");
            sql.append("LEFT JOIN stores_evaluations eval ");
            sql.append("ON eval.id_store = store.id ");
            sql.append("LEFT JOIN app_categories_business categb ");
            sql.append("ON categb.id=store.id_categorie ");
            sql.append("LEFT JOIN accounts acc ");
            sql.append("ON acc.id=favorite.id_account ");
            sql.append("WHERE favorite.id_account=? ");
            try {

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, favorite.getIdUser());

                ResultSet rs = pst.executeQuery();

                ITStore s;

                int counter = 0;
                while (rs.next()) {
                    s = new ITStore();
                    s.setId(rs.getInt("store.id"));
                    s.setType(rs.getInt("store.type"));
                    s.setStoreName(rs.getString("store.name"));
                    s.setCategorie(rs.getString("categb.b_categorie"));
                    s.getStoreConfiguration().setAverageRating(rs.getDouble("avg_rating"));
                    s.getStoreConfiguration().setTimeMinDelivery(rs.getInt("store_config.time_min_delivery"));
                    s.getStoreConfiguration().setTimeMaxDelivery(rs.getInt("store_config.time_max_delivery"));
                    s.getStoreConfiguration().setFeeDelivery(rs.getDouble("store_config.fee_delivery"));
                    s.setFavorited(rs.getBoolean("favorited"));
                    s.getStoreConfiguration().getPaymentConfiguration().setMaxInstallments(rs.getInt("store_config.max_installments"));
                    s.getStoreConfiguration().getPaymentConfiguration().setMaxWithoutFEE(rs.getInt("store_config.max_installments_without_fee"));
                    s.getStoreConfiguration().getPaymentConfiguration().setFeeInstallments(rs.getDouble("store_config.fee_installments"));
                    s.getStoreConfiguration().getPaymentConfiguration().setMinimumPurchaseInstallments(rs.getInt("minimum_purchase_installments"));

                    counter++;

                    if (counter == LIMIT_LOAD_STORES) {
                        itens.get(LIMIT_LOAD_STORES - 1).setHaveMore(true);
                    } else {
                        itens.add(s);
                    }
                }

                con.close();
                pst.close();

                return itens ;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    if(pst!=null)pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return itens;
    }
}
