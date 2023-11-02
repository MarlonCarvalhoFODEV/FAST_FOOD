package com.c.hotburguerdelivery.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.acitivtys.corderProducts.OrderProductsActivity;
import com.comprazencasa.colaborador.application.CustomApplication;
import com.comprazencasa.colaborador.fragments.FMT_Recycler_Swip;
import com.comprazencasa.colaborador.itens.GroupOrders;
import com.comprazencasa.colaborador.itens.ITOrderBuyer;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.webservice.RequestWs;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.comprazencasa.colaborador.itens.GroupOrders.SINGLE_ORDER;


public class AdapterBuyer extends RecyclerView.Adapter<AdapterBuyer.Holder> implements WebResponseIT {

    private Context context ;
    private List<GroupOrders> orders ;
    private Handler handler ;

    private FMT_Recycler_Swip fmt ;
    private int positionOrderSelected = -1 ;
    private boolean clicked = false ;

    public AdapterBuyer(Context context){
        this.context = context ;
        this.orders = new ArrayList<>();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public AdapterBuyer(Context context, FMT_Recycler_Swip fmt){
        this.context = context ;
        this.orders = new ArrayList<>();
        this.handler = new Handler(Looper.getMainLooper());
        this.fmt = fmt ;
    }

    @Override
    public int getItemViewType(int position) {
        return orders.get(position).getTypeGroup();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null ;
        //switch (viewType){
         //   case SINGLE_ORDER:{
                v = LayoutInflater.from(context).inflate(R.layout.it_buyer_single, parent, false);
        //        break;
      //      }
        //    case GroupOrders.DOUBLE_ORDER:{
        //        v = LayoutInflater.from(context).inflate(R.layout.it_buyer_double, parent, false);
         //       break;
         //   }
       // }
        v.setTag(viewType);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        GroupOrders group = orders.get(position);
        holder.bindValues(group);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public synchronized void addOrder(final GroupOrders group, int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                orders.add(group);
                notifyItemInserted(orders.size()-1);
            }
        },delay);
    }

    public synchronized void delOrder(final int position, int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                orders.remove(position);
                notifyItemRemoved(position);
            }
        },delay);
    }

    public int size(){
        return orders.size();
    }

    public GroupOrders getLastItem() {
        if(orders.size() > 0){
            return orders.get(orders.size()-1);
        }else{
            return null ;
        }
    }

    public void clear() {
        orders.clear();
        notifyDataSetChanged();
    }

    public void update(){
        clear();
        fmt.loadOrders();
    }

    public List<ITOrderBuyer> getAllOrders() {
        List<ITOrderBuyer> list = new ArrayList<>();
        for (GroupOrders g : orders){

            for(ITOrderBuyer i : g){
                list.add(i);
            }
        }
        return list;
    }

    @Override
    public void webResponse(WebResponses response) {

        Intent intent = new Intent(context, OrderProductsActivity.class);
        intent.putExtra("group", new Gson().toJson(orders.get(positionOrderSelected)));

        clicked = false ;
        boolean openAvtivity = false;
        String status = fmt.getStatus();
        switch (status){
            case ITOrderBuyer.STATUS_WAIT:{
                if(response.getComunication() == 1){
                    openAvtivity = true ;
                    intent.putExtra(ITOrderBuyer.STATUS_IN_PRODUCTION, true);
                    intent.putExtra(ITOrderBuyer.STATUS_FOR_DELIVERY, true);
                }
                else {
                    update();
                }
                break;
            }

            case ITOrderBuyer.STATUS_IN_PRODUCTION:{

                if(response.getComunication() == 1){
                    openAvtivity = true ;
                    intent.putExtra(ITOrderBuyer.STATUS_IN_PRODUCTION, false);
                    intent.putExtra(ITOrderBuyer.STATUS_FOR_DELIVERY, true);
                }
                else{
                    update();
                    Toast.makeText(context, "Esse pedido já está aguardando p/entrega!", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case ITOrderBuyer.STATUS_FOR_DELIVERY:{
                if(response.getComunication() == 1){

                    Toast.makeText(context, "Você já alterou para entrega, o pedido não pode ser acessado!", Toast.LENGTH_LONG).show();
                }
                else{
                    update();
                    Toast.makeText(context, "Esse pedido já foi finalizado!\nLista atualizada.", Toast.LENGTH_LONG).show();
                }
                intent.putExtra(ITOrderBuyer.STATUS_IN_PRODUCTION, false);
                intent.putExtra(ITOrderBuyer.STATUS_FOR_DELIVERY, false);
                break;
            }
        }

        if(openAvtivity)context.startActivity(intent);

    }

    @Override
    public void receiverMessage(UserMessage msg) {

    }

    @Override
    public void interfaceController(WebResponses response) {

    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvClientName, tvHour ;
        private TextView tvClientName2, tvHour2 ;


        public Holder(View v) {
            super(v);

            tvClientName = (TextView) v.findViewById(R.id.tv_client_name_it_buyer);
            tvHour = (TextView) v.findViewById(R.id.tv_hour_it_buyer);

            int tag = ((int)v.getTag());
            tvClientName.setTag(tag);

;           if(tag > 1){
                tvClientName2 = (TextView) v.findViewById(R.id.tv_client2_name_it_buyer);
                tvHour2 = (TextView) v.findViewById(R.id.tv_hour2_it_buyer);

            }

            v.setOnClickListener(this);
        }

        public void bindValues(GroupOrders group){

            tvClientName.setText(group.size()+" Pedidos");
            tvHour.setText(group.get(SINGLE_ORDER-1).getDate());

            /*switch ((int)tvClientName.getTag()){
                case SINGLE_ORDER:{
                    tvClientName.setText(group.get(SINGLE_ORDER-1).getClientName());
                    tvHour.setText(group.get(SINGLE_ORDER-1).getDate());
                    break;
                }
                case DOUBLE_ORDER:{
                    tvClientName.setText(group.get(SINGLE_ORDER-1).getClientName());
                    tvHour.setText(group.get(SINGLE_ORDER-1).getDate());

                    ITOrderBuyer twoOrder = group.get(DOUBLE_ORDER-1);
                    tvClientName2.setText(twoOrder.getClientName());
                    tvHour2.setText(twoOrder.getDate());
                    break;
                }
            }*/
        }

        @Override
        public void onClick(View view) {
            if(!clicked){
                positionOrderSelected = getAdapterPosition();

                fmt.hideInformative();
                StringBuilder method = new StringBuilder();
                method.append(WebController.Orders.CONTROLLER);
                method.append(WebController.Orders.Method.VERIFY_ORDER_AVAILABLE);

                orders.get(positionOrderSelected).get(0).setIdBuyer(getIdMyAccount());
                new RequestWs(context, AdapterBuyer.this)
                        .request(Request.Method.POST,method, orders.get(positionOrderSelected).get(0));

                clicked = true ;
            } else {
                Toast.makeText(context, "Aguarde...\nVerificando se disponivel!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    private int getIdMyAccount(){
        return ((CustomApplication)context.getApplicationContext()).getAccount(context).getId();
    }
}
