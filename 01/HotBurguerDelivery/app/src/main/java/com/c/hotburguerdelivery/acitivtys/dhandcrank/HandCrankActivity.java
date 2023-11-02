package com.c.hotburguerdelivery.acitivtys.dhandcrank;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.comprazencasa.colaborador.Collaborator.CollaboratorAccount;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.adapters.AdapterViewPage;
import com.comprazencasa.colaborador.application.CustomApplication;
import com.comprazencasa.colaborador.fragments.FMT_InfosClient;
import com.comprazencasa.colaborador.itens.GroupOrders;
import com.comprazencasa.colaborador.itens.ITOrderBuyer;
import com.comprazencasa.colaborador.mservices.MessageManager;
import com.comprazencasa.colaborador.mservices.ServiceConnectionCZC;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.TabCustom;
import com.comprazencasa.colaborador.zutils.customapplication.ResultReceiverCZC;
import com.comprazencasa.colaborador.zutils.webservice.RequestWs;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HandCrankActivity extends AppCompatActivity implements WebResponseIT,
        ServiceConnection {

    private ViewPager vpInfos ;
    private AdapterViewPage adp ;

    private GroupOrders group ;

    private ResultReceiverCZC resultReceiverCZC ;
    private MessageManager msgManager ;
    private Intent intentService ;

    private TabLayout mTabLayout ;
    private List<TabCustom> tabs ;

    private boolean initialized = false ;
    private boolean finalized = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_crank);

        Toolbar tb = (Toolbar) findViewById(R.id.tb_hand_crank);
        setSupportActionBar(tb);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        resultReceiverCZC = ((CustomApplication) getApplicationContext()).getResultReceiver();
        resultReceiverCZC.setReceiverMessage(this);

        intentService = ((CustomApplication) getApplicationContext()).getIntent();
        bindService(intentService, this, Context.BIND_AUTO_CREATE);

        tabs = new ArrayList<>();
        mTabLayout = (TabLayout) findViewById(R.id.tab_hand_crank);
        mTabLayout.setSelectedTabIndicatorColor(Color.BLUE);

        String gson = (String) getIntent().getExtras().get("group_orders");
        group = new Gson().fromJson(gson, GroupOrders.class);

        adp = new AdapterViewPage(getSupportFragmentManager());

        vpInfos = (ViewPager) findViewById(R.id.vp_hand_crank);
        vpInfos.setAdapter(adp);

        for(ITOrderBuyer o : group){
            addTabCustom(new FMT_InfosClient(o),o.getClientName(),vpInfos);
        }
    }

    private void addTabCustom(Fragment frag, String title, ViewPager mViewPager){

        ((AdapterViewPage) mViewPager.getAdapter()).addFrag(frag);

        TabCustom tab = new TabCustom(this, R.layout.tab_info_announce);
        tab.setTitle(title);
        tabs.add(tab);
        mTabLayout.setupWithViewPager(mViewPager);

        for(int i=0; i < tabs.size(); i++){

            mTabLayout.getTabAt(i).setCustomView(tabs.get(i).getView());
        }
    }

    public void initializeOrFinalize(View view){
        verifyOrderFree();
    }

    private void verifyOrderFree(){
        ITOrderBuyer order = group.get(vpInfos.getCurrentItem());
        if(!order.getStatus().equalsIgnoreCase(ITOrderBuyer.STATUS_FINALIZED)){

            StringBuilder method = new StringBuilder();
            method.append(WebController.Orders.CONTROLLER);
            method.append(WebController.Orders.Method.VERIFY_ORDER_AVAILABLE);

            order.setIdDeliveryMan(((CustomApplication) getApplicationContext()).getAccount(this).getId());

            new RequestWs(this, HandCrankActivity.this)
                    .request(Request.Method.POST, method, order);
        }
        else{
            Toast.makeText(this, "Entrega finalizada - ACESSO NEGADO!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeProcessDelivery(){
        UserMessage msg = new UserMessage();
        msg.setIdDestinys(group.getIdUsers());
        msg.setIdOrder(group.getIdOrders());
        msg.setTypeCommunication(UserMessage.COMMUNICATION);
        msg.setMessage(ITOrderBuyer.STATUS_DELIVERY_ON_MY_WAY);

        requestUpdateStatusOrder(ITOrderBuyer.STATUS_DELIVERY_ON_MY_WAY);
        msgManager.sendMessage(new Gson().toJson(msg));

        initialized = true ;
        ((FloatingActionButton)findViewById(R.id.fab_hand_crank)).setImageResource(R.drawable.ic_ok);
        Toast.makeText(this, "Status das Entregas - A CAMINHO!", Toast.LENGTH_LONG).show();
    }

    private void finalizeProcessDelivery(){
        UserMessage msg = new UserMessage();
        msg.setIdDestinys(group.getIdUsers());
        msg.setIdOrder(group.getIdOrders());
        msg.setTypeCommunication(UserMessage.COMMUNICATION);
        msg.setMessage(ITOrderBuyer.STATUS_FINALIZED);

        requestUpdateStatusOrder(group.get(vpInfos.getCurrentItem()), ITOrderBuyer.STATUS_FINALIZED);
        saveProductionDeliveryman(group.get(vpInfos.getCurrentItem()));
        msgManager.sendMessage(new Gson().toJson(msg));

        Toast.makeText(this, "Entrega Finalizada com Sucesso!", Toast.LENGTH_SHORT).show();

        if(vpInfos.getCurrentItem() != group.size()-1)
            vpInfos.setCurrentItem(vpInfos.getCurrentItem()+1);
    }

    private void saveProductionDeliveryman(ITOrderBuyer order){
        StringBuilder method = new StringBuilder();
        method.append(WebController.Collaborator.CONTROLLER);
        method.append(WebController.Collaborator.Method.ADD_PRODUCTION);

        CollaboratorAccount account = ((CustomApplication)getApplicationContext()).getAccount(this);
        order.setIdBuyer(0);
        order.setCompanyFunction(account.getFunction().getCompanyFunction());
        order.setIdDeliveryMan(account.getId());

        WebResponses sendRequest = new WebResponses();
        sendRequest.setJsonObject(new Gson().toJson(new GroupOrders(order)));
        new RequestWs(this, this)
                .request(Request.Method.POST,method,sendRequest);
    }

    private void requestUpdateStatusOrder(ITOrderBuyer order, String status){
        StringBuilder url = new StringBuilder();
        url.append(WebController.Orders.CONTROLLER);
        url.append(WebController.Orders.Method.UPDATE_ORDER);

        CollaboratorAccount account = ((CustomApplication)getApplicationContext()).getAccount(this);

        order.setCompanyFunction(account.getFunction().getCompanyFunction());
        order.setStatus(status);

        order.setIdDeliveryMan(account.getId());

        new RequestWs(this,this)
                .request(Request.Method.POST, url, order);
    }

    private void requestUpdateStatusOrder(String status) {
        StringBuilder url = new StringBuilder();
        url.append(WebController.Orders.CONTROLLER);
        url.append(WebController.Orders.Method.UPDATE_ALL_ORDER);

        CollaboratorAccount account = ((CustomApplication) getApplicationContext()).getAccount(this);

        for (ITOrderBuyer order : group) {
            order.setCompanyFunction(account.getFunction().getCompanyFunction());
            order.setStatus(status);

            order.setIdDeliveryMan(account.getId());
        }

        WebResponses resp = new WebResponses();
        resp.setJsonObject(new Gson().toJson(group));

        new RequestWs(this, this)
                .request(Request.Method.POST, url, resp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void webResponse(WebResponses response) {
        switch (response.getComunication()){
            case 0: {
                Toast.makeText(this, "A Entrega já foi iniciada por outro COLABORADOR! ", Toast.LENGTH_LONG).show();
                break;
            }
            case 1: {

                if(!initialized && finalized == false){
                    initializeProcessDelivery();
                }
                else if(finalized == false && response.getIdOrder() == null){
                    finalizeProcessDelivery();
                }
                else if(response.getIdOrder() != null && vpInfos.getCurrentItem() == group.size()-1
                        && group.get(vpInfos.getCurrentItem()).getStatus().equals(ITOrderBuyer.STATUS_FINALIZED)){

                    setResult(300);
                    finish();
                }
            }
            case 2: {

                break;
            }
        }
    }

    @Override
    public void receiverMessage(UserMessage msg) {

    }

    @Override
    public void interfaceController(WebResponses response) {

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ServiceConnectionCZC.InfoController infC = (ServiceConnectionCZC.InfoController) iBinder;
        msgManager = infC.getMessageManager();
        if (getIntent().hasExtra("CLICKED")) {
            //msgManager.clearMessageFromID(chat.getIdProduct());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
