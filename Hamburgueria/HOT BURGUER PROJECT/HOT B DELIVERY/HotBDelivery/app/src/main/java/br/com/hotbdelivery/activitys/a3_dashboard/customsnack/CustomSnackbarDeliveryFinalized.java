package br.com.hotbdelivery.activitys.a3_dashboard.customsnack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import apis.location.LAddress;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.itens.HotOrdersDelivery;
import br.com.hotbdelivery.utils.CacheApp;
//import br.com.hotbdelivery.activitys.a4_running.RunningActivity;


public class CustomSnackbarDeliveryFinalized {

    private Context context ;
    private Snackbar snackbar ;
    private Snackbar.SnackbarLayout snackbarLayout ;
    private Controller controller ;

    public CustomSnackbarDeliveryFinalized(Context context, View viewRoot, Intent data){
        this.context = context ;
        this.snackbar = Snackbar.make(viewRoot, CustomSnackbarDeliveryFinalized.class
                .getSimpleName(), Snackbar.LENGTH_INDEFINITE);
        this.snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        View customSanckbar = LayoutInflater.from(context).inflate(R.layout.custom_snack_finish_running, null);

        controller = new Controller(customSanckbar, data.getExtras().getDouble("amount"));

        snackbarLayout.setPadding(0,0,0,0);
        snackbarLayout.addView(customSanckbar);

    }

    public class Controller implements View.OnClickListener {

        private Button btnClose ;
        private TextView tvAmount, tvCountDelivery;

        public Controller(View view, double amount){
            tvAmount = view.findViewById(R.id.tv_snack_total_achievement);
            //tvCountDelivery = view.findViewById(R.id.tv_count_delivery);
            btnClose = view.findViewById(R.id.btn_closed_snack_finish);
            tvAmount.setText("+R$ "+amount);

            btnClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            snackbar.dismiss();
        }
    }

    public void showSnackbar(){
        snackbar.show();

        /*YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .repeat(0)
                .playOn(snackbar.getView());*/
    }

    public void hideSnackbar(){
        YoYo.with(Techniques.SlideOutUp)
                .duration(500)
                .repeat(0)
                .playOn(snackbar.getView());
        snackbar.dismiss();
    }

    public boolean isShowing(){
        return snackbar.isShown();
    }

    private final ArrayList<LAddress> convertToAddress(JsonArray array){

        ArrayList<LAddress> addresses = new ArrayList<>();

        LAddress address ;
        Gson gson = new Gson();
        for(JsonElement e : array){
            address = gson.fromJson(e, LAddress.class);
            addresses.add(address);
        }

        return addresses ;
    }

}