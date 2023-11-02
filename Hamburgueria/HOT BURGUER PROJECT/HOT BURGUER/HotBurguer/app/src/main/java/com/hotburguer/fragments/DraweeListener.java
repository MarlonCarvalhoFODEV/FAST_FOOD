package com.hotburguer.fragments;

import android.graphics.drawable.Animatable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;

public class DraweeListener implements ControllerListener {

    private DraweeController controller;

    public DraweeListener(String url, DraweeView draweeView) {
        this.controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setControllerListener(this)
                .setTapToRetryEnabled(true)
                .setOldController(draweeView.getController())
                .build();
    }

    public DraweeController getController() {
        return controller;
    }

    @Override
    public void onSubmit(String id, Object callerContext) {

    }

    @Override
    public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {

    }

    @Override
    public void onIntermediateImageSet(String id, Object imageInfo) {

    }

    @Override
    public void onIntermediateImageFailed(String id, Throwable throwable) {

    }

    @Override
    public void onFailure(String id, Throwable throwable) {

    }

    @Override
    public void onRelease(String id) {

    }
}
