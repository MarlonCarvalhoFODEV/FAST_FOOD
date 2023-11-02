package com.hotdelivery.activitys.a2_register;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.hotdelivery.R;
import com.hotdelivery.fragments.register.FMT_01_REG_EMAIL;
import com.hotdelivery.fragments.register.FMT_CompleteRegister;
import com.hotdelivery.fragments.register.FMT_03_QUEUE;

import java.util.ArrayList;

import com.hotdelivery.aplication.CustomApplication;
import com.hotdelivery.itens.deliveryman.HotDeliveryMan;
import com.hotdelivery.utils.UIController;

import apis.cache.CacheAPI;

public class RegisterActivity extends AppCompatActivity implements RegisterInterface {

    private FrameLayout progress, baseFragments ;

    private FMT_01_REG_EMAIL regEmail ;
    private FMT_CompleteRegister regComplete;
    private FMT_03_QUEUE queue ;

    private ArrayList<Fragment> steps ;

    private int currentStep = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.tb_register);
        setSupportActionBar(toolbar);

        UIController.hideSystemUI(this);

        progress = findViewById(R.id.progress);

        baseFragments = findViewById(R.id.fl_base_fragments);
        //fabBackStep = findViewById(R.id.fab_back_step);
        //fabNextStep = findViewById(R.id.fab_next_step);

        regEmail = new FMT_01_REG_EMAIL(this);
        regComplete = new FMT_CompleteRegister(this);
        queue = new FMT_03_QUEUE(this);

        steps = new ArrayList<>();
        steps.add(regEmail);
        steps.add(regComplete);
        steps.add(queue);

        init();
    }

    private final void init(){
        String jsonAccount = CacheAPI.getObjectJson(this, HotDeliveryMan.class.getSimpleName(), true);
        HotDeliveryMan ac = new Gson().fromJson(jsonAccount, HotDeliveryMan.class);

        Bundle bundle = getIntent().getExtras();
        Object obj = (bundle!=null?bundle.get("step"):null);
        int step = (int) (obj!=null?obj:0);

        if(ac == null || ac.getId() == 0 || step == 1){
            exchangeFragment(regEmail);
        }
        else if(ac.getId() > 0 && ac.getIdRecipient() == null || step == 2){
            exchangeFragment(regComplete);
        }
        else if(ac.getId() > 0 && ac.getIdRecipient() != null || step == 3){
            exchangeFragment(queue);
        }
        else if(ac.getId() > 0 && ac.getIdRecipient() != null && ac.getStatus().equals(HotDeliveryMan.Status.ACTIVE) || step == 4){
           /*
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
            */
        } else{

        }
    }

    private void exchangeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(baseFragments.getId(), fragment)
                .commit();
    }

    @Override
    public void showLoading(boolean show) {
        progress.setVisibility(show?View.VISIBLE:View.GONE);
    }

    @Override
    public void nextStep() {
        if(currentStep < steps.size()){
            currentStep++;
            exchangeFragment(steps.get(currentStep));
        }
    }

    @Override
    public void nextStep(int step) {
        currentStep++;
        switch (step){
            case 1: exchangeFragment(regEmail); break;
            case 2: exchangeFragment(regComplete); break;
            case 3: exchangeFragment(queue); break;
            //case 4: exchangeFragment(regEmail); break;
        }
    }

    @Override
    public void backStep() {
        if(currentStep > 0 && currentStep <= steps.size()){
            currentStep--;
            exchangeFragment(steps.get(currentStep));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CustomApplication.getSocialLogin().getSignInGoogle().onActivityResult(requestCode, resultCode, data);
        CustomApplication.getSocialLogin().getSignInFacebook().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UIController.hideSystemUI(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIController.hideSystemUI(this);
    }
}