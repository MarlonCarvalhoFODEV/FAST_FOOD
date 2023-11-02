package com.c.hotburguerdelivery.zutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comprazencasa.colaborador.R;


public class TabCustom {

    private int countMsg;
    private LinearLayout rlIcon;
    ;
    private TextView tvTitle, tvSubTitle;
    private boolean clicked;

    private View view;

    public TabCustom(Context context, int idLayout) {
        this.view = LayoutInflater.from(context).inflate(idLayout, null);
        this.tvTitle = (TextView) view.findViewById(R.id.tvTitleTabInfAnnounce);
        this.tvSubTitle = (TextView) view.findViewById(R.id.tvSubTitleTabInfAnnounce);
    }

    public View getView() {
        return view;
    }

    public void showNotify(int countMsg) {
        rlIcon.setVisibility(View.VISIBLE);
       // tvCountMsg.setText(String.valueOf(countMsg));
    }

    public void hideNotify() {
        rlIcon.setVisibility(View.GONE);
       // tvCountMsg.setText(String.valueOf(0));
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public TextView getTvSubTitle() {
        return tvSubTitle;
    }

    public void setTvSubTitle(String subTitle) {
        this.tvSubTitle.setText(subTitle);
    }

    public int getCountMsg() {
        return countMsg;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}