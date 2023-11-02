package br.com.hotbdelivery.fragments.register;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class StepIdentity implements Serializable {

    private Bitmap frontPhoto, versePhoto ;
    private byte[] frontPhotoBytes, versePhotoBytes ;

    public Bitmap getFrontPhoto() {
        return frontPhoto;
    }

    public void setFrontPhoto(Bitmap frontPhoto) {
        this.frontPhoto = frontPhoto;
        setFrontPhotoBytes(frontPhoto, 70);
    }

    public Bitmap getVersePhoto() {
        return versePhoto;
    }

    public void setVersePhoto(Bitmap versePhoto) {
        this.versePhoto = versePhoto;
        setVersePhotoBytes(versePhoto, 70);
    }

    public byte[] getFrontPhotoBytes() {
        return frontPhotoBytes;
    }

    public void setFrontPhotoBytes(final Bitmap bitmap, final int quality) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bout);

                frontPhotoBytes = bout.toByteArray();
            }
        }).start();
    }

    public byte[] getVersePhotoBytes() {
        return versePhotoBytes;
    }

    public void setVersePhotoBytes(final Bitmap bitmap, final int quality) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bout);

                versePhotoBytes = bout.toByteArray();
            }
        }).start();
    }

    public boolean isOK(){
        return (frontPhoto != null && versePhoto != null) ;
    }


}
