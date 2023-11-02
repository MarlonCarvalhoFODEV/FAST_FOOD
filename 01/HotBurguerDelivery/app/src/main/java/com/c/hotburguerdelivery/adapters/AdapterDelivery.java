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
import com.comprazencasa.colaborador.acitivtys.bdelivery.MapActivity;
import com.comprazencasa.colaborador.application.CustomApplication;
import com.comprazencasa.colaborador.fragments.FMT_Recycler_Swip_delivery;
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


public class AdapterDelivery extends RecyclerView.Adapter<AdapterDelivery.Holder> implements WebResponseIT {

    private Context context ;
    private List<GroupOrders> orders ;
    private Handler handler ;
    private FMT_Recycler_Swip_delivery fmt ;
    private int positionOrderSelected = 0;

    public AdapterDelivery(Context context, FMT_Recycler_Swip_delivery fmt){
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
         //   case GroupOrders.SINGLE_ORDER:{
                v = LayoutInflater.from(context).inflate(R.layout.it_delivery_single, parent, false);
        //        break;
        //    }
        //    case GroupOrders.DOUBLE_ORDER:{
         //       v = LayoutInflater.from(context).inflate(R.layout.it_delivery_single, parent, false);
        //        break;
        //    }
        //}
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

    public List<ITOrderBuyer> getAllOrders() {
        List<ITOrderBuyer> list = new ArrayList<>();
        for (GroupOrders g : orders){

            for(ITOrderBuyer i : g){
                list.add(i);
            }
        }
        return list;
    }

    public void update(){
        clear();
        fmt.requestOrders(null);
    }

    @Override
    public void webResponse(WebResponses response) {

        switch (response.getComunication()) {
            case 0: {
                update();
                Toast.makeText(context, "A Entrega já foi iniciada por outro COLABORADOR! ", Toast.LENGTH_LONG).show();
                break;
            }

            case 1: {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("group", new Gson().toJson(orders.get(positionOrderSelected)));
                intent.putExtra("position",positionOrderSelected);
                context.startActivity(intent);
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

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvCountDelivery, county, tvCity, tvNeighborhood, tvComplements, tvStreet, tvPrice;
        private TextView tvClientName2, tvHour2 ;

        public Holder(View v) {
            super(v);

            tvCountDelivery = (TextView) v.findViewById(R.id.tv_count_delivery_it_delivery);
            //tvCity = (TextView) v.findViewById(R.id.tv_city_it_delivery);
            //tvNeighborhood = (TextView) v.findViewById(R.id.tv_neighborhood_it_delivery);
            //tvStreet = (TextView) v.findViewById(R.id.tv_street_it_delivery);
            //tvComplements = (TextView) v.findViewById(R.id.tv_complement_it_delivery);
            tvNeighborhood = (TextView) v.findViewById(R.id.tv_neighborhood_it_delivery);

            int tag = ((int)v.getTag());
            tvCountDelivery.setTag(tag);

;           if(tag > 1){
                tvClientName2 = (TextView) v.findViewById(R.id.tv_client2_name_it_buyer);
                tvHour2 = (TextView) v.findViewById(R.id.tv_hour2_it_buyer);
            }

            v.setOnClickListener(this);
        }

        public void bindValues(GroupOrders group){
            tvCountDelivery.setText(group.size()+" Entrega(s)");
            tvNeighborhood.setText(group.get(getAdapterPosition()).getNeighborhood());
            switch ((int)tvCountDelivery.getTag()){
                case GroupOrders.SINGLE_ORDER:{

                    //tvCity.setText(group.get(SINGLE_ORDER-1).getCity());
                    //tvNeighborhood.setText(group.get(SINGLE_ORDER-1).getNeighborhood());
                    //tvStreet.setText(group.get(SINGLE_ORDER-1).getStreet());
                    //tvComplements.setText(group.get(SINGLE_ORDER-1).getComplements());

                    break;
                }
                //case DOUBLE_ORDER:{
                 //   tvCountDelivery.setText(group.get(SINGLE_ORDER-1).getClientName());
                    //tvNeighborhood.setText(group.get(SINGLE_ORDER-1).getDate());

                 //   ITOrderBuyer twoOrder = group.get(DOUBLE_ORDER-1);
                    //tvClientName2.setText(twoOrder.getClientName());
                 //   tvHour2.setText(twoOrder.getDate());
                //    break;
                //}
            }
        }

        @Override
        public void onClick(View view) {
            ITOrderBuyer order = orders.get(positionOrderSelected).get(0);
            if(!order.getStatus().equalsIgnoreCase(ITOrderBuyer.STATUS_FINALIZED)){
                positionOrderSelected = getAdapterPosition();

                StringBuilder method = new StringBuilder();
                method.append(WebController.Orders.CONTROLLER);
                method.append(WebController.Orders.Method.VERIFY_ORDER_AVAILABLE);

                order.setIdDeliveryMan(((CustomApplication)context.getApplicationContext()).getAccount(context).getId());

                new RequestWs(context, AdapterDelivery.this)
                        .request(Request.Method.POST, method, order);
            }
            else{
                Toast.makeText(context, "Entrega finalizada - ACESSO NEGADO!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
