package br.com.hotbdelivery.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.hotbdelivery.R;
import br.com.hotbdelivery.itens.HotHistoricRegister;
import br.com.hotbdelivery.itens.HotPanelHome;

public class AdapterHistoricDelivery extends RecyclerView.Adapter<AdapterHistoricDelivery.Holder> {

    private Context context;
    private ArrayList<HotHistoricRegister> itens;
    private HotHistoricRegister beforeItem;

    public AdapterHistoricDelivery(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.it_historic_register, null);
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindValues(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void addItem(HotHistoricRegister item) {
        itens.add(item);
        notifyItemInserted(itens.size() - 1);
    }

    public HotHistoricRegister getLastItem() {
        if (itens.size() >= 1)
            return itens.get(itens.size() - 1);
        else
            return null;
    }

    public void clear() {
        itens.clear();
        beforeItem = null ;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout consBase;
        private TextView tvFirstLetter, tvDayName, tvCountDelivery, tvAmount, tvMonthName;
        private int viewType ;

        public Holder(@NonNull View v, int viewType) {
            super(v);

            this.viewType = viewType ;
            tvFirstLetter = v.findViewById(R.id.tv_first_letter_day_historic);
            tvDayName = v.findViewById(R.id.tv_day_name_historic);
            tvCountDelivery = v.findViewById(R.id.tv_count_delivered_historic);
            tvAmount = v.findViewById(R.id.tv_amount_historic_delivery);
            tvMonthName = v.findViewById(R.id.tv_month_it_historic_register);
            consBase = v.findViewById(R.id.cons_base);
            consBase.setOnClickListener(this);

        }

        public void bindValues(HotHistoricRegister register) {

          /*  if(beforeItem == null) {
                beforeItem = register;
                tvMonthName.setVisibility(View.VISIBLE);
            }else if(!beforeItem.getMonth().equals(register.getMonth())) {
                beforeItem = register;
            } else
                tvMonthName.setVisibility(View.GONE);*/

            tvMonthName.setText(register.getMonth());
            tvFirstLetter.setText(register.getDayName().substring(0, 1));
            tvDayName.setText(register.getDayName());
            tvCountDelivery.setText(register.getCountDelivery() + " Entregas");
            tvAmount.setText("R$ " +String.valueOf(register.getAmount()).replace(".",","));
        }

        @Override
        public void onClick(View view) {

        }
    }
}
