package com.hotdelivery.fragments.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hotdelivery.R;
import com.hotdelivery.activitys.a2_register.RegisterInterface;

public class FMT_03_QUEUE extends Fragment {

    private RegisterInterface regInterface ;

    public FMT_03_QUEUE(RegisterInterface exRegInterface) {
        regInterface = exRegInterface ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.from(getContext()).inflate(R.layout.fmt_queue, null);

        return view;
    }
}
