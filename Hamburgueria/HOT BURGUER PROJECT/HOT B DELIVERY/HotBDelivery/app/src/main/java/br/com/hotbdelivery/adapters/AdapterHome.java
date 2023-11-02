package br.com.hotbdelivery.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.activitys.a1_splash.SplashActivity;
import br.com.hotbdelivery.activitys.a2_register.RegisterActivity;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.itens.HotPanelHome;
import br.com.hotbdelivery.utils.EndPointMapping;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.Holder> {

    private Context context;
    private ArrayList<HotPanelHome> itens;

    public AdapterHome(Context c) {
        this.context = c;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.it_panel, null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindValues(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    @Override
    public int getItemViewType(int position) {
        HotPanelHome panel = itens.get(position);
        return panel!=null?panel.getTypeView():0;
    }

    public void addPanel(HotPanelHome panel) {
        itens.add(panel);
        notifyItemInserted(itens.size() - 1);
    }

    public void removePanel(int position) {
        itens.remove(position);
        notifyItemRemoved(itens.size() - 1);
    }

    public void disablePanel(int position) {
        itens.remove(position);
    }

    public void clear() {
        itens.clear();
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        private TextView tvAmount, tvDeliverds, tvAccepts, tvRejected, tvName;

        public Holder(@NonNull View v) {
            super(v);

            cardView = v.findViewById(R.id.it_panel_base);
            tvName = v.findViewById(R.id.tv_performance_day);
            tvAmount = v.findViewById(R.id.tv_amount);
            tvDeliverds = v.findViewById(R.id.tv_count_delivered);
            tvAccepts = v.findViewById(R.id.tv_offer_accepteds);
            tvRejected = v.findViewById(R.id.tv_offer_rejecteds);

            cardView.setOnClickListener(this);
        }

        public void bindValues(HotPanelHome item) {
            if(item != null){
                tvName.setText("Desempenho - "+item.getTitle());
                tvAmount.setText("R$ " + item.getAmount());
                tvDeliverds.setText(String.valueOf(item.getDelivered()));
                tvAccepts.setText(String.valueOf(item.getOfferAccepteds()));
                tvRejected.setText(String.valueOf(item.getOfferRejecteds()));

                cardView.setEnabled(item.isEnabled());
                tvDeliverds.setEnabled(item.isEnabled());
                tvAccepts.setEnabled(item.isEnabled());
                tvRejected.setEnabled(item.isEnabled());
            }
        }



        @Override
        public void onClick(View view) {

        }
    }
}
