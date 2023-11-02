package com.c.hotburguerdelivery.zutils.progress;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comprazencasa.colaborador.R;


public class CustomProgress {

    private Context context ;
    private TextView tvMessage ;
    private ProgressBar progressBar ;
    private LinearLayout baseL ;
    private String[] messages ;
    private AlertDialog dialog ;

    public CustomProgress(Context context){
        this.context = context ;
        View v = LayoutInflater.from(context).inflate(R.layout.dg_progress, null);

        this.progressBar = v.findViewById(R.id.pbCustomLoading);
        this.tvMessage = v.findViewById(R.id.tvMessageCustomLoading);
        this.baseL = v.findViewById(R.id.baseCustomLoading);

        dialog = new AlertDialog.Builder(context).create();
        dialog.setView(v);

    }

    public void setMessages(String preExecution, String inExecution, String postExecution){
        this.messages = new String[]{preExecution, inExecution, postExecution};
    }

    private void preExecute() {
        tvMessage.setText(messages[0]);
    }

    private void inExecution() {
        progressUpdate(messages[1]);
    }

    private void postExecute() {
        progressUpdate(messages[2]);
    }

    public void execute(){
        preExecute();
        inExecution();
        postExecute();
    }

    public void progressUpdate(final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                tvMessage.setText(message);
            }
        });
    }

    public void progressUpdate(final String message, boolean handler) {
        if(handler == true){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    tvMessage.setText(message);
                }
            });
        }else{
            tvMessage.setText(message);
        }
    }

    public void setOutClickFinish(boolean value){
        dialog.setCancelable(value);
    }

    public void show(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void dismiss(final int timeMilis){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(timeMilis);
                dialog.dismiss();
            }
        }).start();
    }
}
