package com.hotburguer.fragments.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.adapters.AdapterSavedCards;
import com.hotburguer.fragments.payment.FMT_Payment;

import java.util.ArrayList;

import apis.userbuyer.BCard;

public class DialogCardsSaved extends DialogFragment {

    private ArrayList<BCard> cards ;
    private FMT_Payment payment ;
    private Controller controller ;

    public DialogCardsSaved(ArrayList<BCard> cards, FMT_Payment payment){
        this.cards = cards ;
        this.payment = payment ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.dg_cards_saveds, null);

        controller = new Controller(cards, view);

        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.CustomDialog);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDestroy() {
        CustomApplication.hideSystemUI(getActivity());
        super.onDestroy();
    }

    public Controller getController() {
        return controller;
    }

    public class Controller implements AdapterView.OnItemClickListener, View.OnClickListener {

        private AdapterSavedCards adp ;
        private ListView lvCards ;
        private BCard cardSelected ;
        private Button btnCloseDialog ;

        public Controller(ArrayList<BCard> cards, View v){
            adp = new AdapterSavedCards(getContext(), cards);

            lvCards = v.findViewById(R.id.lv_cards_saved_dg);
            lvCards.setAdapter(adp);
            lvCards.setOnItemClickListener(this);
            btnCloseDialog = v.findViewById(R.id.btn_close_dg_cards_saved);
            btnCloseDialog.setOnClickListener(this);
        }

        public AdapterSavedCards getAdapter() {
            return adp;
        }

        public ListView getLvCards() {
            return lvCards;
        }

        public BCard getCardSelected(){
            return adp.getCardSelected();
        }


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BCard cardSelected = adp.getItem(i);

            if (cardSelected != null) {
                dismiss();
                CustomApplication.hideSystemUI(getActivity());
                payment.showSelectedCard(cardSelected, true);
            }
        }

        @Override
        public void onClick(View view) {
            dismiss();
            CustomApplication.hideSystemUI(getActivity());
        }
    }
}
