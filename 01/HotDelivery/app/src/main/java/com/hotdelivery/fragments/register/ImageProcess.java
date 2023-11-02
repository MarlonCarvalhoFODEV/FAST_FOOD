package com.hotdelivery.fragments.register;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class ImageProcess extends AsyncTaskLoader<StepIdentity> {
    public ImageProcess(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public StepIdentity loadInBackground() {
        return null;
    }

    @Override
    public void deliverResult(@Nullable StepIdentity data) {
        super.deliverResult(data);
    }
}
