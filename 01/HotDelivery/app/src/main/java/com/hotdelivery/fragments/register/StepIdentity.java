package com.hotdelivery.fragments.register;

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

    public void setFrontPhotoBytes(Bitmap bitmap, int quality) {
        new Thread(()->{
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bout);

            this.frontPhotoBytes = bout.toByteArray();
        }).start();
    }

    public byte[] getVersePhotoBytes() {
        return versePhotoBytes;
    }

    public void setVersePhotoBytes(Bitmap bitmap, int quality) {
        new Thread(()->{
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bout);

            this.versePhotoBytes = bout.toByteArray();
        }).start();
    }

    public boolean isOK(){
        return (frontPhoto != null && versePhoto != null) ;
    }


}
