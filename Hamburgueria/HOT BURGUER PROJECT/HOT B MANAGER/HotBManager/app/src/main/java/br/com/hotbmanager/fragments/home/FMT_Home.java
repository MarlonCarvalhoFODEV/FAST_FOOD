package br.com.hotbmanager.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.dashboard.DashboardInterface;
import br.com.hotbmanager.activity.orderproduction.OrderProductionActivity;
import br.com.hotbmanager.adapters.AdapterNeoOrders;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.itens.HotGroupOrders;
import br.com.hotbmanager.itens.HotOrderManager;
import br.com.hotbmanager.utils.EndPointMapping;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMT_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FMT_Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Controller controller ;

    public FMT_Home() {
        // Required empty public constructor
    }

    public static FMT_Home newInstance(String param1, String param2) {
        FMT_Home fragment = new FMT_Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fmt_home, container, false);
        controller = new Controller(view);
        ((DashboardInterface)getActivity()).setSubTitle("Novos Pedidos");
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == OrderProductionActivity.RESULT_ACTIVITY)
            controller.loadOrders(0);
    }

    public class Controller extends RecyclerView.OnScrollListener implements SwipeRefreshLayout.OnRefreshListener {

        private RecyclerView rcList ;
        private TextView tvMessage ;
        private SwipeRefreshLayout swipHome ;
        private AdapterNeoOrders adp ;

        private LinearLayoutManager llm ;
        private Handler handler ;

        public Controller(View v) {
            this.llm = new LinearLayoutManager(getContext());
            this.llm.setSmoothScrollbarEnabled(true);
            this.llm.setOrientation(LinearLayoutManager.VERTICAL);

            this.handler = new Handler(Looper.getMainLooper());
            this.adp = new AdapterNeoOrders(getContext());

            this.swipHome = v.findViewById(R.id.swip_home);

            this.rcList = v.findViewById(R.id.rc_home);
            this.rcList.setHasFixedSize(true);
            this.rcList.setLayoutManager(llm);
            this.rcList.setAdapter(adp);
            this.rcList.addOnScrollListener(this);

            this.tvMessage = v.findViewById(R.id.tv_message_home);

            this.swipHome.setOnRefreshListener(this);

            loadOrders(0);
        }

        private final synchronized void addGroupOrders(final HotGroupOrders groupOrders){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adp.add(groupOrders);
                }
            },500);
        }

        private void showLoading(boolean show){
            swipHome.setRefreshing(show);
        }

        public final void showMessage(String message, boolean show){
            tvMessage.setVisibility(show?View.VISIBLE: View.GONE);
            tvMessage.setText(message);
        }

        private void loadOrders(int idOrder){
            try {
                showMessage(null,false);
                adp.clear();

                final HotManagerAccount ac = CustomApplication.getAccount();

                JSONObject json = new JSONObject();
                json.put("id",ac.getId());
                json.put("id_dev",ac.getIdDevice());
                json.put("id_order",idOrder);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("loadOrders()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.Orders.CONTROLLER);
                requestServer.setFunction(EndPointMapping.Orders.Functions.GET_NEO_ORDERS);
                requestServer.setObjectTransport(json);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        showLoading(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            Gson gson = new Gson();
                            JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                            for(JsonElement e : array){
                                adp.add(gson.fromJson(e, HotGroupOrders.class));
                            }

                            if(array.size() == 0)
                                showMessage("Nenhum pedido\nrecente ainda...", true);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        showMessage("Erro: "+e.getMessage(), true);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                showMessage("Erro: "+e.getMessage(), true);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            View view = llm.findViewByPosition(adp.getItemCount() - 2);
            HotOrderManager lastProduct = adp.getLastItem();

            if (view != null && view.isShown() && lastProduct != null && lastProduct.isHaveMore()) {

                lastProduct.setHaveMore(false);

                if (adp.getItemCount() >= 1 ) {
                    recyclerView.smoothScrollToPosition(adp.getItemCount());
                    loadOrders(lastProduct.getIdOrder());
                }
            }
        }

        @Override
        public void onRefresh() {
            loadOrders(0);
        }
    }
}