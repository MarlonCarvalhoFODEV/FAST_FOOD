package br.com.hotbmanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.orderproduction.OrderProductionActivity;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.dialogs.DialogInformative;
import br.com.hotbmanager.itens.HotGroupOrders;
import br.com.hotbmanager.itens.HotOrderManager;
import br.com.hotbmanager.itens.HotProduct;
import br.com.hotbmanager.utils.EndPointMapping;

public class AdapterPromptDelivery extends RecyclerView.Adapter<AdapterPromptDelivery.Holder> {

    private Context context;
    private ArrayList<HotGroupOrders> itens;

    public AdapterPromptDelivery(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.it_order_prompt_delivery, null);

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

    public void clear() {
        itens.clear();
        notifyDataSetChanged();
    }

    public void add(HotGroupOrders groupOrders) {
        itens.add(groupOrders);
        notifyItemInserted(itens.size() - 1);
    }

    public HotOrderManager getLastItem() {
        int size = itens.size();
        int lastPositionGroup = size > 0 ? size - 1 : size;
        int lastPositionOrder = size > 0 ? itens.get(lastPositionGroup).size() - 1 : size;

        if (size > 0 && itens.get(lastPositionGroup) != null && itens.get(lastPositionGroup).size() > 0)
            return itens.get(lastPositionGroup).get(lastPositionOrder);
        else
            return null;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private View base;
        private TextView tvCount, tvDeliveryManName, tvCodOrders;
        private Button btnNotification;
        private HotGroupOrders group;
        private RelativeLayout progress ;

        public Holder(@NonNull View v) {
            super(v);

            tvCount = v.findViewById(R.id.tv_count_it_order_prompt_delivery);
            tvDeliveryManName = v.findViewById(R.id.tv_name_it_order_prompt_delivery);
            tvCodOrders = v.findViewById(R.id.tv_ids_orders_it_prompt_delivery);

            base = v;

            progress = v.findViewById(R.id.rl_progress_it_prompt_delivery);
            btnNotification = v.findViewById(R.id.btn_notification_it_order_prompt_delivery);
            btnNotification.setOnLongClickListener(this);
        }

        public void bindValues(int itemPosition) {
            group = itens.get(itemPosition);

            tvDeliveryManName.setText(group.getNameDeliveryman());
            tvCodOrders.setText("Pedidos( "+group.getCodeOrders()+" )");
            tvCount.setText(String.valueOf(group.size()));
        }

        private final void renotificationDeliveryman() {
            try {

                final HotManagerAccount ac = CustomApplication.getAccount();

                JSONObject json = new JSONObject();
                json.put("id",ac.getId());
                json.put("id_dev",ac.getIdDevice());
                json.put("id_del", group.getIdDeliveryman());
                json.put("id_orders", group.getIdOrders());

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("loadOrdersPackaged()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(context.getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.NOTIFY_DELIVERYMAN);
                requestServer.setObjectTransport(json);
                requestServer.request(context, new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        progress.setVisibility(loading?View.VISIBLE:View.GONE);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            try {
                                JSONObject response = new JSONObject(object.toString());
                                response = response.getJSONObject("map");

                                boolean result = response.getBoolean("result");
                                if(result)
                                    Toast.makeText(context, "Entregador Notificado", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        //showMessage("Erro: "+e.getMessage(), true);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                //showMessage("Erro: "+e.getMessage(), true);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            renotificationDeliveryman();
            return false;
        }
    }
}
