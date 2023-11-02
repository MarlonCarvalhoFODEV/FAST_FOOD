package com.hotburguer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotburguer.R;

import java.io.Serializable;
import java.util.ArrayList;

import apis.card.CardManager;
import apis.userbuyer.BCard;

public class AdapterSavedCards extends BaseAdapter {

    private Context context ;
    private final ArrayList<BCard> itens ;
    private BCard cardSelected ;

    public AdapterSavedCards(Context c, ArrayList<BCard> cards){
        this.context = c ;
        this.itens = cards ;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public BCard getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -1 ;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null ;
        if(view ==  null){
            view = LayoutInflater.from(context).inflate(R.layout.it_card_saved, null);
            holder = new Holder(itens.get(i),view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.bindValues();

        return view;
    }

    public BCard getCardSelected() {
        return cardSelected;
    }

    public class Holder implements Serializable, View.OnClickListener {
        private ImageView icon ;
        private TextView cardName ;
        private BCard card ;

        public Holder(BCard card, View v){
            this.card = card ;
            icon = v.findViewById(R.id.iv_icon_card);
            cardName = v.findViewById(R.id.tv_card_name);
        }

        public void bindValues(){
            CardManager.BrandCard brandCard = CardManager.detectIcon(card.getBrand());
            icon.setImageResource(brandCard.getIdIcon());
            cardName.setText(brandCard.getBrand()+" • *** "+card.getLastDigits());
        }

        @Override
        public void onClick(View view) {
            cardSelected = card ;
        }
    }
}
