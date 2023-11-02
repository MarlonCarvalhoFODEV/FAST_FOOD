package br.com.hotbmanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.orderproduction.OrderProductionActivity;
import br.com.hotbmanager.dialogs.DialogInformative;
import br.com.hotbmanager.itens.HotGroupOrders;
import br.com.hotbmanager.itens.HotOrderManager;
import br.com.hotbmanager.itens.HotProduct;

public class AdapterNeoOrders extends RecyclerView.Adapter<AdapterNeoOrders.Holder> {

    private Context context ;
    private ArrayList<HotGroupOrders> itens ;

    public AdapterNeoOrders(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.it_group_orders, null);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindValues(position);
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void clear(){
        itens.clear();
        notifyDataSetChanged();
    }

    public void add(HotGroupOrders groupOrders) {
        itens.add(groupOrders);
        notifyItemInserted(itens.size()-1);
    }

    public HotOrderManager getLastItem() {
        int size = itens.size();
        int lastPositionGroup = size>0?size-1:size;
        int lastPositionOrder = size>0?itens.get(lastPositionGroup).size()-1:size;

        if(size > 0 && itens.get(lastPositionGroup) != null && itens.get(lastPositionGroup).size() > 0 )
            return itens.get(lastPositionGroup).get(lastPositionOrder);
        else
            return null ;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private RelativeLayout base ;
        private TextView tvCount, tvClientName, tvCod, tvDistance;
        private HotGroupOrders group ;

        public Holder(@NonNull View v) {
            super(v);

            tvCount = v.findViewById(R.id.tv_count_it_order);
            tvClientName = v.findViewById(R.id.tv_client_name_it_group_order);
            tvCod = v.findViewById(R.id.tv_cod_it_group_order);
            tvDistance = v.findViewById(R.id.tv_distance_it_group_order);

            base  = v.findViewById(R.id.base_it_group_order);
            base.setOnLongClickListener(this);
        }

        public void bindValues(int itemPosition){
            group = itens.get(itemPosition);

            int countOrders = group.size();
            if(countOrders == 1){
                tvClientName.setVisibility(View.VISIBLE);
                tvCod.setVisibility(View.VISIBLE);

                tvClientName.setText(group.get(0).getClientName());
                tvCod.setText("COD."+group.get(0).getIdOrder());
                tvDistance.setText(""+group.get(0).getDistance()+"km");
            } else {
                tvClientName.setVisibility(View.GONE);
                tvCod.setVisibility(View.GONE);
            }

            tvDistance.setText(""+group.getTotalDistance()+"km");
            tvCount.setText(String.valueOf(group.size()));
        }

        private final void openOrders(){
            StringBuilder sb = new StringBuilder();

            for(HotOrderManager g : group) {
                sb.append("\nCliente - ");
                sb.append( g.getClientName());
                sb.append("\nCódigo do Pedido - ");
                sb.append(g.getIdOrder());

                for(HotProduct p : g.getProducts()) {
                    sb.append("\nProduto - ");
                    sb.append(p.getTitle());
                    sb.append(" ");
                    sb.append(p.getCount());
                    sb.append("x");
                }

                sb.append("\n---------------------------------------------------");
            }

            final DialogInformative dialog = new DialogInformative(context);
            dialog.setTitle("NOVO PEDIDO");
            dialog.setMessage(sb.toString());
            dialog.getTvMessage().setGravity(Gravity.LEFT);
            dialog.addButton("Iniciar Produção", Color.WHITE,
                    context.getResources().getDrawable(R.drawable.rounded_button_confirm),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.setCancelable(false);
                            dialog.setCancelableTouchOutside(false);
                        }
                    });
            dialog.show();
        }

        @Override
        public boolean onLongClick(View view) {
            Intent intent = new Intent(context, OrderProductionActivity.class);
            intent.putExtra("neo_orders",group);
            ((Activity)context).startActivityForResult(intent, OrderProductionActivity.REQUEST_ACTIVITY);
            return false;
        }
    }
}
