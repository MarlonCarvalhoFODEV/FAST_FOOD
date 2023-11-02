package com.c.hotburguerdelivery.zutils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.comprazencasa.colaborador.Collaborator.CollaboratorAccount;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class PopImageManager {

    private static StringBuilder url = new StringBuilder();

    public static synchronized Bitmap readBitmap(Context context, Uri uri, int resizeFactor){
        Bitmap bitmap = null ;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = resizeFactor ;
        AssetFileDescriptor fileDescriptor = null ;

        try{

            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri,"r");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        } finally {
            try{
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(),null,options);

                fileDescriptor.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return diminuirImagem(bitmap);
    }

    public static synchronized void loadUrlImage(Context c, String pathImage, final ImageView iv){

        iv.setImageBitmap(null);
        iv.setBackgroundDrawable(null);

        url.append(c.getResources().getString(R.string.connectionWs));
        url.append(WebController.Image.CONTROLLER);
        url.append(WebController.Image.Methods.GET_IMAGE);
        url.append(pathImage);

        ImageLoader.getInstance().displayImage(url.toString(), iv);
        url.replace(0,url.length(),"");
        //return bitmap[0];
    }

    public static synchronized String createUrlGet(Context c, String nameFile){
        StringBuilder sb = new StringBuilder();
        sb.append(c.getString(R.string.connectionWs));
        sb.append(WebController.Image.CONTROLLER);
        sb.append(WebController.Image.Methods.GET_IMAGE);
        sb.append(nameFile);
        return sb.toString();
    }

    public static Bitmap getBitmapFromUrl(Context context, String nameFile){
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.connectionWs));
        sb.append(WebController.Image.CONTROLLER);
        sb.append(WebController.Image.Methods.GET_IMAGE);
        sb.append(nameFile);

        try {
            URL url = new URL(sb.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);

            return BitmapFactory.decodeStream(con.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static synchronized byte[] generateBAFromImageName(Context c, String nameFile){

        return  getImageBAFromUrl(createUrlGet(c,nameFile));
    }

    public static synchronized String generateNameImg(CollaboratorAccount conta){
        Random ran = new Random();
        return conta.getId()+"_"+ran.nextInt(999999999)+".png";
    }

    public static synchronized String generateNameImg(Map<String, String> map, CollaboratorAccount conta){
        Random ran = new Random();
        return conta.getId()+"_"+(map.size()+1)+ran.nextInt(999999999)+".png";
    }

    public static synchronized byte[] cvBitmapToBA(final Bitmap bitmap){
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bout);
        return bout.toByteArray();
    }

    public static synchronized Bitmap cvBAToBitmap(byte[] image){
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }

    public static synchronized Bitmap getBitmapFromUrl(String url_image){

        HttpURLConnection con = null;

        try {
            URL url = new URL(url_image);
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            return BitmapFactory.decodeStream(con.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static synchronized byte[] getImageBAFromUrl(String url_image){

        try {
            URL url = new URL(url_image);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            BitmapFactory.decodeStream(con.getInputStream())
            .compress(Bitmap.CompressFormat.PNG, 100,bout);

            bout.close();

            return bout.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    private static synchronized Bitmap diminuirImagem(Bitmap bitmap){

        int imgWidth = bitmap.getWidth() ;
        int height = bitmap.getHeight() ;

        if(imgWidth >= 1000 && imgWidth <= 1500  || height <= 1500 && height >= 1000 ){
            height = (int)(height / 4) ;
            imgWidth = (int)(imgWidth / 4) ;
        }
        else if(imgWidth >= 650 | height >= 650 && imgWidth <= 1000 | height <= 1000 ){
            height = (int)(height / 3) ;
            imgWidth = (int)(imgWidth / 3) ;
        }
        else if(imgWidth <= 650 | height <= 650){
            //Toast.makeText(this, "Image", Toast.LENGTH_SHORT).show();
        }
        else{
            return null ;
        }

        Log.i("DIMEN: ", "LARGURA: "+imgWidth+" ALTURA: "+height);
        return Bitmap.createScaledBitmap(bitmap,imgWidth, height, false);
    }

    public static String generateImageName(String cruptoPass){
        Random ran1 = new Random();
        return new StringBuilder(CryptoUtil.encryptMD5(cruptoPass.getBytes())+ran1.nextInt(999999999)).toString()+".png";
    }
}
