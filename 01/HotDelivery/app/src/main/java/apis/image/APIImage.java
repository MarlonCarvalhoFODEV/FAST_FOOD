package apis.image;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;

public class APIImage {

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

        return Bitmap.createScaledBitmap(bitmap,imgWidth, height, false);
    }

}
