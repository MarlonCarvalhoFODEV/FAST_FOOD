package com.c.hotburguerdelivery.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.acitivtys.cbuyer.DaoRegions;
import com.comprazencasa.colaborador.adapters.AdapterBuyer;
import com.comprazencasa.colaborador.itens.GroupOrders;
import com.comprazencasa.colaborador.itens.ITOrderBuyer;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.CustomItemDecor;
import com.comprazencasa.colaborador.zutils.webservice.RequestWs;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
@SuppressLint("ValidFragment")
public class FMT_Recycler_Swip extends Fragment implements WebResponseIT, SwipeRefreshLayout.OnRefreshListener{

    private LinearLayoutManager llm ;
    private SwipeRefreshLayout swip ;
    private AdapterBuyer adp ;

    private TextView tvInformative ;

    private String status ;

    @SuppressLint("ValidFragment")
    public FMT_Recycler_Swip(String status) {
        this.status = status ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fmt_recycler_and_swip, container, false);

        adp = new AdapterBuyer(getContext(), this);

        tvInformative = (TextView) view.findViewById(R.id.tv_message_informative_buyer);

        swip = (SwipeRefreshLayout) view.findViewById(R.id.sp_recycler_and_swip);
        swip.setOnRefreshListener(this);
        swip.setColorSchemeResources(R.color.colorAccent,R.color.colorAprovado,R.color.colorAguardandoAprovacao);

        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setSmoothScrollbarEnabled(true);

        RecyclerView recyler = (RecyclerView) view.findViewById(R.id.rc_recycler_and_swip);
        recyler.addItemDecoration(new CustomItemDecor(10));
        recyler.setHasFixedSize(true);
        recyler.setLayoutManager(llm);
        recyler.setAdapter(adp);
        recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                loadMoreOrders();
            }
        });

        return view ;
    }

    public void loadOrders(){
        showSwip();
        adp.clear();
        requestOrders(null);
    }

    private void requestOrders(String date){
        StringBuilder method = new StringBuilder();
        method.append(WebController.Orders.CONTROLLER);
        method.append(WebController.Orders.Method.GET_ORDERS);

        ITOrderBuyer order = new ITOrderBuyer();
        order.setStatus(status);
        order.setDate(date);

        new RequestWs(getContext(), this)
                .request(Request.Method.POST,method, order);
    }

    private synchronized void addOrder2(String json){
        Gson gson = new Gson();
        JsonArray array = gson.fromJson(json, JsonArray.class);
        List<ITOrderBuyer> orders = new ArrayList<>();

        ITOrderBuyer o ;
        for(JsonElement e : array){
            o = gson.fromJson(e,ITOrderBuyer.class);
            orders.add(o);
        }

        GroupOrders group ;

        int ordersSize = array.size();

        for (int i = 0; i <= ordersSize; i++) {
            // Grupo criado
            group = createGroupOrders(orders);

            if(group != null){
                adp.addOrder(group,100);

                orders.removeAll(group);

                i=0;
                ordersSize = orders.size();

            } else break ;
        }
    }

    private GroupOrders createGroupOrders(List<ITOrderBuyer> orders){
        GroupOrders group = null ;
        if(orders.size() > 0){
            // Pega o primeiro pedido mais proximo da base
            ITOrderBuyer first = orders.get(0);
            // Remove da lista para comparação
            orders.remove(first);

            // Cria o primeiro membro de um Grupo de Pedidos
            group = new GroupOrders(first);

            int ordersSize = orders.size();
            for (int i = 0; i < ordersSize; i++) {
                if(DaoRegions.calculateDistanceKM(first.getLatitude(), first.getLongitude(),
                        orders.get(i).getLatitude(), orders.get(i).getLongitude()) < 3 ){
                    // Adiciona um novo membro P/Grupo
                    group.add(orders.get(i));
                } else break ;
            }
        }
        return group ;
    }

    private synchronized void loadMoreOrders(){
        GroupOrders product = adp.getLastItem() ;

        if((adp.getItemCount()-2) == llm.findLastVisibleItemPosition()-3 &&
                product!= null /*&& product.isHaveMore() == true*/){

            if(adp.getItemCount() >= 1 ){

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        //requestOrders(adp.getLastItem().getDate());
                    }
                }).start();
            }
           /*product.setHaveMore(false);*/
        }
    }

    @Override
    public void webResponse(WebResponses response) {
        switch (response.getComunication()){
            case 0:{
                hideSwip("Nenhum pedido feito no momento, logo logo teremos mais!");
                break;
            }
            case 1:{
                hideSwip("");
                addOrder2(response.getJsonObject());

                break;
            }
        }

    }

    @Override
    public void receiverMessage(UserMessage msg) {
        hideSwip("Não foi possivel atualizar a lista, deslize pra baixo para recarregar");
    }

    @Override
    public void interfaceController(WebResponses response) {
        adp.setClicked(false);
        hideSwip("Nenhum ressultado retornado pelo servidor, atualize a lista!");
    }

    private void showSwip(){
        swip.setRefreshing(true);
        hideInformative();
    }

    private void hideSwip(String message){
        swip.setRefreshing(false);
        showInformative(message);
    }

    private void showInformative(String message){
        if(adp.size() == 0) {
            tvInformative.setVisibility(View.VISIBLE);
            tvInformative.setText(message);
        }else{
            tvInformative.setVisibility(View.GONE);
        }
    }

    public void hideInformative(){
        tvInformative.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        loadOrders();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadOrders();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getStatus() {
        return status;
    }

    public AdapterBuyer getAdapter() {
        return adp;
    }
}
