package br.com.hotbmanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.hotbmanager.R;
import br.com.hotbmanager.itens.HotGroupOrders;
import br.com.hotbmanager.itens.HotOrderManager;

public class AdapterProduction extends RecyclerView.Adapter<AdapterProduction.Holder> {

    private Context context;
    private ArrayList<HotOrderManager> itens;

    public AdapterProduction(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.it_order_production, null);

        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindValues(position);
    }

    public void addAllOrders(ArrayList<HotOrderManager> orders) {
        if (orders != null) {
            itens.addAll(orders);
            notifyItemInserted(itens.size() - 1);
        }
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private HotOrderManager order;

        private ImageView ivIconExpandable;
        private TextView tvClientName, tvCodOrder;

        private ConstraintLayout baseAction, expandableLayout;
        private ListView lvProducts;

        private AdapterProducts adpProducts;

        public Holder(@NonNull View v) {
            super(v);
            baseAction = v.findViewById(R.id.cl_base_action_it_opening_hour);

            ivIconExpandable = v.findViewById(R.id.iv_icon_it_production);
            tvClientName = v.findViewById(R.id.tv_client_name_it_production);
            tvCodOrder = v.findViewById(R.id.tv_id_order_it_production);

            expandableLayout = v.findViewById(R.id.cl_expandable_it_production);
            lvProducts = v.findViewById(R.id.lv_products_it_production);

        }

        public void bindValues(int position) {
            order = itens.get(position);

            adpProducts = new AdapterProducts(context);
            adpProducts.addAllProduct(order.getProducts());
            int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            int height = (60 * 2) * adpProducts.getCount();

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, height);
            lvProducts.setAdapter(adpProducts);
            lvProducts.setLayoutParams(params);

            ivIconExpandable.setImageResource(order.isExpandable() ? R.drawable.ic_collapsible : R.drawable.ic_expandable);
            tvClientName.setText(order.getClientName());
            tvCodOrder.setText("COD." + order.getIdOrder());

            baseAction.setOnClickListener(this);
            expandableLayout.setVisibility(order.isExpandable() ? View.VISIBLE : View.GONE);

        }

        @Override
        public void onClick(View view) {
            boolean expandable = itens.get(getLayoutPosition()).isExpandable();
            order.setExpandable(!expandable);

            notifyItemChanged(getLayoutPosition());

        }
    }
}
