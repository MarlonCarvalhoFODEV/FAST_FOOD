package com.c.hotburguerdelivery.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.acitivtys.corderProducts.OrderProductsActivity;
import com.comprazencasa.colaborador.adapters.AdapterProducts;
import com.comprazencasa.colaborador.itens.ITOrderBuyer;
import com.comprazencasa.colaborador.itens.ITProduct;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.CustomItemDecor;
import com.comprazencasa.colaborador.zutils.webservice.RequestWs;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

@SuppressLint("ValidFragment")
public class FMT_OrderProducts extends Fragment implements WebResponseIT, SwipeRefreshLayout.OnRefreshListener {

    private ITOrderBuyer order ;

    private AdapterProducts adp ;
    private SwipeRefreshLayout swip ;
    private TextView informative ;

    public FMT_OrderProducts(ITOrderBuyer order){
        this.order = order ;
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fmt_recycler_and_swip, null);

        adp = new AdapterProducts(getContext(), order.getIdOrder());
        informative = (TextView) v.findViewById(R.id.tv_message_informative_buyer);

        swip = v.findViewById(R.id.sp_recycler_and_swip);
        swip.setOnRefreshListener(this);
        swip.setColorSchemeColors(R.color.colorAccent, R.color.colorAguardandoAprovacao, R.color.colorPrimary);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.rc_recycler_and_swip);
        recycler.setLayoutManager(llm);
        recycler.addItemDecoration(new CustomItemDecor(5));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adp);

        return v ;
    }

    public boolean allProductsIsSelected(){
        return adp.allProductsIsSelected();
    }

    public void setInitialized(boolean initialized){
        adp.setInitialized(initialized);
    }

    private void showLoading(int listsize, String message, boolean showLoading){
        swip.setRefreshing(showLoading);

        if (showLoading == false && listsize == 0){
            informative.setVisibility(View.VISIBLE);
            informative.setText(message);
        } else {
            informative.setVisibility(View.GONE);
            informative.setText(message);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        requestMyOrders();
    }

    @Override
    public void webResponse(WebResponses response) {

        Gson gson = new Gson();
        switch (response.getComunication()){
            case 0:{
                showLoading(0,"Aparentemente não existe produtos nesse pedido, recarregue a lista novamente. Se o erro persistir entre em contato com o suporte",false);
                break;
            }
            case 1:{

                JsonArray array = gson.fromJson(response.getJsonObject(), JsonArray.class);

                ITProduct product ;
                for(JsonElement e : array){

                    product = gson.fromJson(e.toString(), ITProduct.class);

                    adp.addProduct2(product,100);
                }

                showLoading(array.size(),"",false);
                break;
            }
            case -2:{
                Toast.makeText(getContext(), "Essa operação já está em ANDAMENTO!", Toast.LENGTH_LONG).show();
                break;
            }
            case 2:{
                Toast.makeText(getContext(), "Operação realizada com sucesso!", Toast.LENGTH_LONG).show();
                break;
            }
            case -3:{
                Toast.makeText(getContext(), "Essa operação já está em ANDAMENTO!", Toast.LENGTH_LONG).show();
                break;
            }
            case 3:{
                Toast.makeText(getContext(), "Operação realizada com sucesso!", Toast.LENGTH_LONG).show();
                break;
            }
        }
        //((OrderProductsActivity) getContext()).hideProgress();

    }

    @Override
    public void receiverMessage(UserMessage msg) {

    }

    @Override
    public void interfaceController(WebResponses response) {
        ((OrderProductsActivity) getContext()).hideProgress();
        showLoading(0,"Não foi possivel carregar os produtos do pedido, recarregue lista novamente",false);
    }

    private void requestMyOrders(){
        showLoading(0,"", true);
        if(order.getStatus().equals(ITOrderBuyer.STATUS_IN_PRODUCTION)) adp.setInitialized(true);
        ((OrderProductsActivity) getContext()).showProgress();

        StringBuilder url = new StringBuilder();
        url.append(WebController.Orders.CONTROLLER);
        url.append(WebController.Orders.Method.GET_CART);

        WebResponses web = new WebResponses();
        web.setIdOrder(order.getIdOrder());
        web.setIdUser(order.getIdUser());

        new RequestWs(getContext(),this)
                .request(Request.Method.POST,url,web);
    }

    @Override
    public void onRefresh() {
        adp.clear();
        requestMyOrders();
    }
}
