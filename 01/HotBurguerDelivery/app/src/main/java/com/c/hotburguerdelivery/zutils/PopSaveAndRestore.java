package com.c.hotburguerdelivery.zutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.comprazencasa.colaborador.R;

import static android.content.Context.MODE_PRIVATE;

public class PopSaveAndRestore {

    public static String jsons ;

    public static  void saveId(Context context, String identifyName, int id){
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_db),MODE_PRIVATE);
        sp.edit().putInt(identifyName, id).apply();
    }

    public static int getId(Context context, String identifyName){
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_db),MODE_PRIVATE);
        return sp.getInt(identifyName,0);
    }

    public static  void saveObject(Context context, String identifyName, String json){
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_db),MODE_PRIVATE);
        sp.edit().putString(identifyName, json).apply();
    }

    public static String getObject(Context context, String identifyName){
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_db),MODE_PRIVATE);
        return sp.getString(identifyName,identifyName);
    }
}
