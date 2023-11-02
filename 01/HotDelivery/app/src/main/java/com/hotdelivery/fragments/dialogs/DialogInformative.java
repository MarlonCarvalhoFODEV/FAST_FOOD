package com.hotdelivery.fragments.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.hotdelivery.R;


public class DialogInformative {

    private Context context;
    private TextView tvTitle, tvMessage;
    private LinearLayout baseButtons;
    private FrameLayout flCustomView ;
    private AlertDialog dialog;

    public DialogInformative(Context context) {
        this.context = context;

        View v = LayoutInflater.from(context).inflate(R.layout.dg_informative, null);
        this.tvTitle = (TextView) v.findViewById(R.id.tv_title_pop_informative);
        this.tvMessage = (TextView) v.findViewById(R.id.tv_pop_informative);
        this.flCustomView = (FrameLayout) v.findViewById(R.id.fl_custom_view_pop_informative);
        this.baseButtons = (LinearLayout) v.findViewById(R.id.ll_panel_buttons_pop_informative);

        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setView(v);
    }

    public void setTitle(String title) {
        this.tvTitle.setText(title);
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }

    public void setMessage(String message) {
        this.tvMessage.setText(message);
    }

    public String getMessage() {
        return tvMessage.getText().toString();
    }

    public void setCustomView(View view){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER ;

        view.setLayoutParams(params);

        flCustomView.addView(view);
    }

    public void addButton(String text, View.OnClickListener onclick){
        Button btn = new Button(context);
        btn.setText(text);
        btn.setTextSize(16);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 0.0000001f;

        btn.setLayoutParams(params);
        btn.setOnClickListener(onclick);

        baseButtons.addView(btn);
    }

    public void addButton(String text, int backgroundColor, View.OnClickListener onclick){
        Button btn = new Button(context);
        btn.setText(text);
        btn.setTextSize(16);
        btn.setBackgroundColor(backgroundColor);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 0.0000001f;

        btn.setLayoutParams(params);
        btn.setOnClickListener(onclick);

        baseButtons.addView(btn);
    }

    public void addButton(String text, int textColor, int backgroundColor, View.OnClickListener onclick){
        Button btn = new Button(context);
        btn.setText(text);
        btn.setTextSize(16);
        btn.setBackgroundColor(backgroundColor);
        btn.setTextColor(textColor);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 0.0000001f;

        btn.setLayoutParams(params);
        btn.setOnClickListener(onclick);

        baseButtons.addView(btn);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

}
