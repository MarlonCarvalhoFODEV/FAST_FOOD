/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import br.com.comprazencasa.util.SendImage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marlon
 */
public class DaoImage {

    private UserImage userImage ;
    private ProductImage productImage ;
    private OtherImage otherImage ;

    public DaoImage(){
        this.userImage = new UserImage();
        this.productImage = new ProductImage();
        this.otherImage = new OtherImage();
    }

    public ProductImage getProductImage() {
        return productImage;
    }

    public UserImage getUserImage() {
        return userImage;
    }

    public OtherImage getOtherImage() {
        return otherImage;
    }

    public synchronized boolean updateImage(SendImage image) {
        if(!image.getImageName().contains(".png")||!image.getImageName().contains(".jpg")) image.setImageName(image.getImageName()+".jpg");

        if(image.getImageByte() != null){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "UPDATE images SET img=? "+
                    "WHERE id=? " ;
            try {
                pst = con.prepareStatement(sql);
                pst.setBytes(1, image.getImageByte());
                pst.setInt(2,image.getIdProduct());

                if(pst.executeUpdate() == 1){
                    con.close();
                    pst.close();

                    return true ;
                } else {
                    con.close();
                    pst.close();
                    return false ;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
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

        return false ;
    }

    public boolean deleteImage(SendImage image) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        String sql = "DELETE FROM images WHERE id=?" ;
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1,image.getIdProduct());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                } catch (SQLException de) {
                    de.printStackTrace();
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

    /*
    public void qualityImage(){
        try {

            Thumbnails.Builder tb = Thumbnails.of(new File("C:\\COMPRAZEM_CASA_RESOURCES\\Carrefour\\").listFiles());
            tb.size(202, 202);
            tb.outputFormat("jpg");
            tb.outputQuality(0.88);
            renamefiles("C:\\COMPRAZEM_CASA_RESOURCES\\Carrefour\\");

            try{
                tb.toFiles(Rename.NO_CHANGE);

            } catch (javax.imageio.IIOException e){

                renamefiles("C:\\COMPRAZEM_CASA_RESOURCES\\Carrefour\\");
            }

            renamefiles("C:\\COMPRAZEM_CASA_RESOURCES\\Carrefour\\");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    public void renamefiles(String path){
        //deleteAll(path);

        File[] files = new File(path).listFiles();
        for(File f : files){

            if(f.getName().contains(".png") && f.getName().contains(".jpg")){

                f.renameTo(new File(path+f.getName().replaceAll(".png","")));
            }
        }
    }

    public void deleteAll(String path){
        File[] files = new File(path).listFiles();
        for(File f : files){

            if(f.getName().contains(".png") && f.getName().contains(".jpg") || f.getName().contains(".jpg")){

                f.delete();
            }
        }
    }

    public void selectNoImage(String path){
        File[] files = new File(path).listFiles();
        for(File f : files){

            if(!f.getName().contains(".png") && !f.getName().contains(".jpg")){

                f.delete();
            }
        }
    }

    // CLASSES

    public class UserImage{
        public final int insertImageDB(SendImage image){

            if(!verifyImageExist(image)){
                Connection con = ConnectionDB_APP.connect();
                PreparedStatement pst = null;

                String sql = "INSERT INTO accounts_imgs (id_user, image_path, img) " +
                        "VALUES (?,?,?) ";

                try {

                    pst = con.prepareStatement(sql);
                    pst.setInt(1,  image.getIdUser());
                    pst.setString(2, image.getImageName());
                    pst.setBytes(3, image.getImageByte());
                    try {
                        pst.execute();

                    } catch (SQLException e){

                            try {
                                con.close();
                                pst.close();
                            } catch (SQLException es) {
                                es.printStackTrace();
                            }
                        return 3 ;
                    } finally {
                        try {
                            con.close();
                            pst.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    pst.close();
                    con.close();
                    return 3 ;
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        con.close();
                        pst.close();
                        return  - 1;
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
            } else {
                return updateImageDB(image);
            }

            return 4 ;
        }

        public int updateImageDB(SendImage image){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null;

            String sql = "UPDATE accounts_imgs SET img=? " +
                    "WHERE id_user=?  ";

            try {
                pst = con.prepareStatement(sql);
                // pst.setInt(1,image.getIdUser());
                //pst.setString(1, image.getImageName());
                pst.setInt(2, image.getIdUser());
                pst.setBytes(1,image.getImageByte());
                //pst.setString(3, image.getImageName());
                pst.executeUpdate();

                con.close();
                pst.close();
                return 3 ;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                    return  - 1;
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
            return 4 ;
        }

        public int deleteImageDB(SendImage image){
            Connection con = ConnectionDB_Collaborators.connect();
            PreparedStatement pst = null;

            String sql = "DELETE FROM coll_image WHERE id_user=? ";

            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1,image.getIdUser());
                //pst.setString(2, image.getImageName());
                //pst.setBytes(3, image.getImageByte());
                pst.execute();

                pst.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                    return  - 1;
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
            return 3 ;
        }

        public byte[] selectImageDB(String imagePath){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT img FROM accounts_imgs WHERE image_path=? ";

            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, imagePath);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){

                    byte[] img = rs.getBytes("img");
                    con.close();
                    pst.close();

                    return img ;
                }

                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
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

            return null ;
        }

        public byte[] selectImageDB(int idUser){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT img FROM accounts_imgs WHERE id_user=? ";

            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){

                    byte[] img = rs.getBytes("img");
                    con.close();
                    pst.close();

                    return img ;
                }

                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
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

            return null ;
        }

        public boolean verifyImageExist(SendImage image){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT img FROM accounts_imgs WHERE id_user=? ";

            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, image.getIdUser());
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    return true ;
                }

                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
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
            return false ;
        }
    }

    public class ProductImage{
        public byte[] getImageDB(String pathImage){

            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT img FROM images WHERE path=? ";

            if(con != null){
                try {
                    pst = con.prepareStatement(sql);
                    pst.setString(1, pathImage);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){

                        byte[] img = rs.getBytes("img");
                        con.close();
                        pst.close();

                        return img ;
                    }

                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        pst.close();
                        con.close();
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

            return null ;
        }

        public byte[] getImagePromotionDB(String pathImage){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT img FROM images_promotional WHERE path=? ";

            if(con != null){
                try {
                    pst = con.prepareStatement(sql);
                    pst.setString(1, pathImage);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){

                        byte[] img = rs.getBytes("img");
                        con.close();
                        pst.close();

                        return img ;
                    }

                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        pst.close();
                        con.close();
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

            return null ;
        }

        public Map<Integer,SendImage> getImageDBFromCount(int idProduct, int countImg){

            Map<Integer,SendImage> images = new HashMap<>();

            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT * FROM images WHERE id>? ";

            if(con != null){
                try {
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, idProduct);
                    ResultSet rs = pst.executeQuery();
                    SendImage img ;

                    int imgLoaded = 0 ;
                    while(rs.next()){

                        img = new SendImage();
                        img.setIdProduct(rs.getInt("id"));
                        img.setSubPath(rs.getString("path"));
                        img.setImageByte(rs.getBytes("img"));

                        if(img != null){

                            imgLoaded++;
                            images.put(img.getIdProduct(), img);
                        }

                        if(imgLoaded == countImg) break;
                    }

                    con.close();
                    pst.close();

                    return images ;
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        pst.close();
                        con.close();
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

            return null ;
        }

        public int insertImageDB(SendImage image){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null;

            String sql = "INSERT INTO images (id, path, img) " +
                    "VALUE (?,?,?) ";

            if(con != null){
                try {
                    pst = con.prepareStatement(sql);
                    pst.setInt(1,image.getIdProduct());
                    pst.setString(2, image.getImageName());
                    pst.setBytes(3, image.getImageByte());
                    pst.executeUpdate();

                    pst.close();
                    con.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        con.close();
                        pst.close();
                        return  - 1;
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

            return 3 ;
        }

        public synchronized boolean insertImageProduct(int idProduct, String pathImg, byte[] imgBytes){
            if (!existImg(idProduct,pathImg)){
                Connection con = ConnectionDB_APP.connect();
                PreparedStatement pst = null;

                String sql = "INSERT INTO images (id, path, img) " +
                        "VALUE (?,?,?) ";

                if(con != null){
                    try {
                        pst = con.prepareStatement(sql);
                        pst.setInt(1,idProduct);
                        pst.setString(2, pathImg);
                        pst.setBytes(3, imgBytes);

                        try{
                            pst.execute();
                        } catch (SQLIntegrityConstraintViolationException e){
                            pst.close();
                            con.close();
                        }


                        pst.close();
                        con.close();

                        return true ;
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
            }

            return false ;
        }

        public boolean existImg(int id, String pathImg){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            if(con != null){
                try {
                    String sql = "SELECT id FROM images " +
                            "WHERE id=? AND path=? " ;

                    pst = con.prepareStatement(sql);
                    pst.setInt(1, id);
                    pst.setString(2,pathImg);

                    ResultSet rs = pst.executeQuery();
                    boolean sucess = rs.next();

                    con.close();
                    pst.close();
                    return sucess ;
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        pst.close();con.close();
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

            return false ;
        }

        public boolean updateImage(SendImage sendImage){
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null;

            String sql = "UPDATE images SET img=? WHERE id=? " ;

            if(con != null){
                try {
                    pst = con.prepareStatement(sql);
                    pst.setBytes(1,sendImage.getImageByte());
                    pst.setInt(2, sendImage.getIdProduct());
                    pst.executeUpdate();

                    pst.close();
                    con.close();

                    return true ;
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

            return false ;
        }

        public SendImage getSendImage(int id) {
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT * FROM images WHERE id=? ";

            if(con != null){
                try {
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, id);

                    ResultSet rs = pst.executeQuery();

                    SendImage img = null ;

                    while(rs.next()){
                        img = new SendImage();
                        img.setIdProduct(rs.getInt("id"));
                        img.setSubPath(rs.getString("path"));
                        img.setImageByte(rs.getBytes("img"));
                    }

                    con.close();
                    pst.close();

                    return img ;
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        pst.close();
                        con.close();
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

            return null ;
        }
    }

    public class OtherImage{
        public byte[] getImageDB(int id){

            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT img FROM other_images WHERE id=? ";

            if(con != null){
                try {
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, id);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){

                        byte[] img = rs.getBytes("img");
                        con.close();
                        pst.close();

                        return img ;
                    }

                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        pst.close();
                        con.close();
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

            return null ;
        }
    }

    public class CollaboratorsImage{
        public final int insertImageDB(SendImage image){

            Connection con = ConnectionDB_Collaborators.connect();
            PreparedStatement pst = null;

            String sql = "INSERT INTO coll_image (path, img) " +
                    "VALUES (?,?) ";

            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, image.getImageName());
                pst.setBytes(2, image.getImageByte());
                try {
                    pst.execute();

                } catch (SQLException e){
                    return 3 ;
                }
                pst.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                    return  - 1;
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
            return 3 ;
        }

        public int updateImageDB(SendImage image){
            Connection con = ConnectionDB_Collaborators.connect();
            PreparedStatement pst = null;

            String sql = "UPDATE coll_image SET img=? " +
                    "WHERE path=? ";

            try {
                pst = con.prepareStatement(sql);
                // pst.setInt(1,image.getIdUser());
                pst.setString(1, image.getImageName());
                pst.setBytes(2, image.getImageByte());
                pst.executeUpdate();

                pst.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                    return  - 1;
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
            return 3 ;
        }

        public int deleteImageDB(SendImage image){
            Connection con = ConnectionDB_Collaborators.connect();
            PreparedStatement pst = null;

            String sql = "DELETE FROM coll_image WHERE path=? ";

            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1,image.getIdUser());
                //pst.setString(2, image.getImageName());
                //pst.setBytes(3, image.getImageByte());
                pst.execute();

                pst.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                    return  - 1;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            return 3 ;
        }

        public byte[] selectImageDB(String imagePath){
            Connection con = ConnectionDB_Collaborators.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT img FROM coll_image WHERE path=? ";

            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, imagePath);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){

                    byte[] img = rs.getBytes("img");
                    con.close();
                    pst.close();

                    return img ;
                }

                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
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

            return null ;
        }
    }
}
