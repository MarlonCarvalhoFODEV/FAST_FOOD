package com.c.hotburguerdelivery.zutils;

import android.content.Context;
import android.content.Intent;

public class Share {


    public static void shareApp(Context context, String title, String uri){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Adiciona os dados na intent
        intent.putExtra(Intent.EXTRA_TEXT,uri);
        context.startActivity(Intent.createChooser(intent,title));
    }
}
