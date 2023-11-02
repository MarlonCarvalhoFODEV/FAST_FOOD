package br.com.hotbdelivery.activitys.a2_register;

import android.content.Context;

import androidx.fragment.app.Fragment;

public interface RegisterInterface {

    public void showLoading(boolean show);

    public void nextStep();

    public void nextStep(int step);

    public void backStep();
}
