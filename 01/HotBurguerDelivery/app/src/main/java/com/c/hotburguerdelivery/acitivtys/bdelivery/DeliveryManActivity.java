package com.c.hotburguerdelivery.acitivtys.bdelivery;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.comprazencasa.colaborador.Collaborator.CollaboratorAccount;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.application.CustomApplication;
import com.comprazencasa.colaborador.fragments.FMT_Recycler_Swip_delivery;
import com.comprazencasa.colaborador.itens.ITOrderBuyer;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.customapplication.ResultReceiverCZC;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeliveryManActivity extends AppCompatActivity implements ServiceConnection, WebResponseIT {

    private CollaboratorAccount account ;
    private FMT_Recycler_Swip_delivery fmtForDelivery = new FMT_Recycler_Swip_delivery(ITOrderBuyer.STATUS_FOR_DELIVERY);
    private Fragment fmtOnTheWay = new FMT_Recycler_Swip_delivery(ITOrderBuyer.STATUS_DELIVERY_ON_MY_WAY);
    private Fragment fmtFinalized = new FMT_Recycler_Swip_delivery(ITOrderBuyer.STATUS_FINALIZED);

    private int fmtPosition =  0 ;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fmtPosition = 0 ;
                    exchangeFragment(fmtForDelivery);
                    return true;
                case R.id.navigation_dashboard:
                    fmtPosition = 1 ;
                    exchangeFragment(fmtOnTheWay);
                    return true;
                case R.id.navigation_notifications:
                    fmtPosition = 2 ;
                    exchangeFragment(fmtFinalized);
                    return true;
            }
            return false;
        }
    };

    private ResultReceiverCZC resultReceiverCZC ;
    private Intent intentService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nb_buyer);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        account = ((CustomApplication)getApplicationContext()).getAccount(this);
        applyInfsAccount(account);
        exchangeFragment(fmtForDelivery);

        resultReceiverCZC = ((CustomApplication)getApplicationContext()).getResultReceiver();
        resultReceiverCZC.setReceiverMessage(this);

        intentService = ((CustomApplication) getApplicationContext()).getIntent();
        bindService(intentService, this, Context.BIND_AUTO_CREATE);
    }

    private void applyInfsAccount(CollaboratorAccount account){
        StringBuilder url = new StringBuilder();
        url.append(getResources().getString(R.string.connectionWs));
        url.append(WebController.Collaborator.CONTROLLER);
        url.append(WebController.Collaborator.Method.GET_IMAGE);
        url.append(account.getImgPath());

        CircleImageView civ = (CircleImageView) findViewById(R.id.civ_photo_delivery);
        ImageLoader.getInstance().displayImage(url.toString(), civ);

        TextView tvName = (TextView) findViewById(R.id.tv_name_buyer);
        TextView tvFunction = (TextView) findViewById(R.id.tv_function_buyer);

        String lastName = account.getLastName();

        tvName.setText(account.getName()+" "+lastName!=null?lastName:"");
        tvFunction.setText("Colaborador(a) - "+account.getFunction().getCompanyFunction());
    }

    private void exchangeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.rl_base_buyer, fragment)
                .commit();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public void webResponse(WebResponses response) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N_MR1)
            unbindService(this);
    }

    @Override
    public void receiverMessage(UserMessage msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (fmtPosition){
                    case 0:{
                        fmtForDelivery.requestOrders(null);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void interfaceController(WebResponses response) {

    }
}
