package br.com.comprazencasa.database;

import br.com.comprazencasa.itens.ITRecLocation;
import br.com.comprazencasa.itens.ITSound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoSpy {

    public boolean saveRecording(ITRecLocation recLocation){

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            String sql = "INSERT INTO spy (recording, file_name) VALUES (?,?);";

            try {
                pst = con.prepareStatement(sql);
                pst.setBytes(1,recLocation.getFile());
                pst.setString(2,recLocation.getNameFile());
                int linesAffecteds = pst.executeUpdate();

                if(linesAffecteds > 0){
                    con.close();
                    pst.close();

                    return true;
                }

                return false ;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
        return false ;
    }

    public ArrayList<ITSound> getRecordings(){
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        ArrayList<ITSound> itens = new ArrayList<>();
        if(con != null){

            String sql = "SELECT * FROM spy ";

            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                ITSound item ;
                while(rs.next()){
                    item = new ITSound();
                    item.setId(rs.getInt("id"));
                    item.setNameSound(rs.getString("file_name"));
                    item.setDateTime(rs.getString("date"));
                    itens.add(item);
                }

                con.close();
                pst.close();

                return itens ;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
        return null ;
    }

    public ITSound getRecording(String fileName){
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            String sql = "SELECT * FROM spy WHERE file_name=? ";

            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, fileName);
                ResultSet rs = pst.executeQuery();

                ITSound item = null;
                if (rs.next()){
                    item = new ITSound();
                    item.setRecording(rs.getBytes("recording"));

                    con.close();
                    pst.close();
                }

                con.close();
                pst.close();

                return item ;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
        return null ;
    }
}
