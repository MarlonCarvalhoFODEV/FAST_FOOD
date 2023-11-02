/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

import br.com.comprazencasa.database.DaoImage;
import br.com.comprazencasa.itens.ITProduct;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class ImagemUtil {
    
    private static String pathPast = null ;
    
    private static byte[] convertToByteArray(ImageIcon icon){
        if(icon != null){

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
                try {
                    ImageIO.write(imageToBufferedImage(icon.getImage()), "PNG", ios);
                    // Set a flag to indicate that the write was successful
                } finally {
                    ios.close();
                }
                return baos.toByteArray();
            } catch (IOException ex) {
            }
        }
        return null ;
    }
    
    public synchronized static String prepareForUrl(String nameImage){
        nameImage = nameImage.replaceAll(" ", "_").toLowerCase();
        nameImage = nameImage.replaceAll("á", "a");
        nameImage = nameImage.replaceAll("é", "e");
        nameImage = nameImage.replaceAll("ê", "e");
        nameImage = nameImage.replaceAll("ó", "o");
        nameImage = nameImage.replaceAll("í", "i");
        nameImage = nameImage.replaceAll("ã", "a");
        nameImage = nameImage.replaceAll("õ", "o");
        nameImage = nameImage.replaceAll("-", "_");
        nameImage = nameImage.replaceAll(",", "_");
        nameImage = nameImage.replaceAll("°", "");
        nameImage = nameImage.replaceAll("/", "");
        //nameImage = nameImage.replaceAll("'", "");
        return nameImage.replaceAll("ú", "u");
    }
    
    public static synchronized void saveImage(String baseImg,String productName, String urlImage){
        new Thread(){
            @Override
            public void run() {
                System.out.println("IMG_PATH: "+baseImg);
                                System.out.println("IMG_NAME: "+productName);
                                System.out.println("IMG_URL: "+urlImage);
                if( baseImg != null && !baseImg.isEmpty() &&
                        productName!= null && !productName.isEmpty() &&
                                urlImage != null && !urlImage.isEmpty()){
                    try {
                        File dir = new File(baseImg);
                        
                        if(!dir.exists()) dir.mkdirs();
                        
                        if(!urlImage.isEmpty()){
                            URL url = new URL(urlImage);

                            try{
                                BufferedImage image = ImageIO.read(url);
                                ImageIO.write(image, "PNG", new File(baseImg+productName));
                            }
                            catch(IIOException e){
                                e.printStackTrace();
                                //downloadImageFromUrl(url, baseImg+productName);
                            }
                        }
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ImagemUtil.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ImagemUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    
    public static void saveImage2(ITProduct p){
        new Thread(){
            @Override
            public void run() {
                if (p.getTitle() != null && !p.getTitle().isEmpty() &&
                                p.getUrl() != null && !p.getUrl() .isEmpty()) {

                    if (!p.getUrl().isEmpty()) {

                        try {
                            URL url = new URL(p.getUrl());
                            downloadImageFromUrl(url,p);

                        } catch (MalformedURLException ex) {
                            Logger.getLogger(ImagemUtil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }.start();
    }
    
    private synchronized static ImageIcon redmimImage(ImageIcon img, int widthImg){
        
        int width = img.getIconWidth();
        int height = img.getIconHeight();
        
        if(widthImg != 0){
            
            int newHeight = reduzirFromPercentual(widthImg, height);
            int newWidth = reduzirFromPercentual(newHeight, width);

            return new ImageIcon(img.getImage().getScaledInstance(newWidth, newHeight, 100));
        }
        else{
            
            return new ImageIcon(img.getImage().getScaledInstance(width, height, 100));
        }
    }

    private synchronized static BufferedImage redmimGalery(BufferedImage img, int widthImg){

        int width = img.getWidth();
        int height = img.getHeight();

        if(widthImg != 0){

            int newHeight = reduzirFromPercentual(widthImg, height);
            int newWidth = reduzirFromPercentual(newHeight, width);

            return (BufferedImage) img.getScaledInstance(newWidth, newHeight, 100);
        }
        else{

            return (BufferedImage) img.getScaledInstance(width, height, 100);
        }
    }

    private synchronized static int reduzirFromPercentual(int sizeForm, int sizeImage){
        double percentual = (100 - ((sizeForm * 100) / sizeImage));
        return (int) (sizeImage - ((percentual/100) * sizeImage))-5;
    }
    
    public synchronized static BufferedImage imageToBufferedImage(Image im) {
     BufferedImage bi = new BufferedImage
        (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
     Graphics bg = bi.getGraphics();
     bg.drawImage(im, 0, 0, null);
     bg.dispose();
     return bi;
  }
    
    public static void downloadImageFromUrl(URL url, ITProduct p){
        new Thread(){
            @Override
            public void run() {
                ByteArrayOutputStream bout = null ;
                InputStream inputStream = null ;

                try {
                    int read = 0;
                    byte[] bytes = new byte[500];

                    HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
                    httpConnection.addRequestProperty("User-Agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                    httpConnection.setRequestMethod("GET");
                    inputStream = httpConnection.getInputStream();

                    //File file = new File(pathFile);
                    //if(!file.exists()) file = new File(pathFile);

                    bout = new ByteArrayOutputStream();
                    while ((read = inputStream.read(bytes)) != -1) {
                        bout.write(bytes, 0, read);
                        bout.flush();
                    }

                    httpConnection.disconnect();
                    inputStream.close();
                    bout.close();

                    new DaoImage().getProductImage().insertImageProduct(p.getId(),p.getPathImg(), bout.toByteArray());
                } catch (FileNotFoundException ex) {
                    ex.getMessage();
                } catch (IOException ex) {
                    ex.getMessage();
                } finally {
                    try {
                        if (bout != null) {
                            bout.close();
                            inputStream.close();
                        }
                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                }
            }
        }.start();
    }

    public static synchronized byte[] downloadAndResizeImage(String urlStr, int resize){
        ByteArrayOutputStream bout = null ;

        try {
            int read = 0;
            byte[] bytes = new byte[200];

            URL url = new URL(urlStr);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
            httpConnection.setRequestProperty("Accept-Charset", "UTF-8");

            InputStream inputStream = httpConnection.getInputStream();

            bout = new ByteArrayOutputStream();
            while ((read = inputStream.read(bytes)) != -1) {
                bout.write(bytes, 0, read);
                bout.flush();
            }
            httpConnection.disconnect();
            inputStream.close();
            bout.close();

            ImageIcon imgb = new ImageIcon(bout.toByteArray());
            imgb = redmimImage(imgb,resize);

            int w = imgb.getImage().getWidth(null);
            int h = imgb.getImage().getHeight(null);

            BufferedImage bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            bimg.createGraphics().drawImage(imgb.getImage(), 0, 0,Color.WHITE, null);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ImageIO.write(bimg,"jpg",out);

            //out.toByteArray() ;    // THIS IS WHERE I  GET BYTES FROM PICTURE

            return out.toByteArray();
        } catch (FileNotFoundException ex) {
            ex.getMessage();
        } catch (IOException ex) {
            ex.getMessage();
        } finally {
            try {
                if (bout != null) {
                    bout.close();
                }
            } catch (IOException ex) {
                ex.getMessage();
            }
        }
        return null ;
    }

    public static synchronized byte[] optimizeImage(InputStream input){
        try {

            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            BufferedImage image = ImageIO.read(input);
            if(image != null){
                BufferedImage result = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
                ImageIO.write(result, "jpg", bout);

            }
            return bout.toByteArray();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public synchronized static BufferedImage convertByteArrayToBufferdImage(byte[] imageBytes){
        try {
            return ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException ex) {
            Logger.getLogger(ImagemUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null ;
    }

    public synchronized static void setPathPast(String pathPast) {
        ImagemUtil.pathPast = pathPast;
    }

    public static String getPathPast() {
        return pathPast;
    }
    
    
}
