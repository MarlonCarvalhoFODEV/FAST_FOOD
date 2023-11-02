package br.com.hotbdelivery.fragments.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.internet.WebJson;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.adapters.AdapterHistoricDelivery;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.itens.HotHistoricRegister;
import br.com.hotbdelivery.utils.EndPointMapping;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMT_HistoricDelivery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FMT_HistoricDelivery extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Controller controller ;

    public FMT_HistoricDelivery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FMT_HistoricDelivery.
     */
    // TODO: Rename and change types and number of parameters
    public static FMT_HistoricDelivery newInstance(String param1, String param2) {
        FMT_HistoricDelivery fragment = new FMT_HistoricDelivery();
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
        View view = inflater.inflate(R.layout.fmt_dash_historic_delivery, container, false);

        controller = new Controller(view);
        controller.onRefresh();

        return view ;
    }

    public class Controller extends RecyclerView.OnScrollListener implements SwipeRefreshLayout.OnRefreshListener {

        private TextView tvMessageError ;
        private SwipeRefreshLayout swip ;
        private RecyclerView listHistoric ;
        private AdapterHistoricDelivery adp ;
        private LinearLayoutManager llm ;

        public Controller(View v) {
            adp = new AdapterHistoricDelivery(getContext());

            llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            llm.setSmoothScrollbarEnabled(true);

            this.tvMessageError = v.findViewById(R.id.tv_error_message_fmt_historic);
            this.swip = v.findViewById(R.id.swip_historic_delivery);
            this.swip.setOnRefreshListener(this);

            this.listHistoric = v.findViewById(R.id.rv_historic_delivery);
            this.listHistoric.setHasFixedSize(true);
            this.listHistoric.setLayoutManager(llm);
            this.listHistoric.setAdapter(adp);
            this.listHistoric.addOnScrollListener(this);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            View view = llm.findViewByPosition(adp.getItemCount() - 2);
            HotHistoricRegister lastProduct = adp.getLastItem();

            if (view != null && view.isShown() && lastProduct.isHaveMore()) {

                lastProduct.setHaveMore(false);

                if (adp.getItemCount() >= 1 ) {
                    swip.setRefreshing(true);
                    recyclerView.smoothScrollToPosition(adp.getItemCount());
                    requestHistoricDelivery(lastProduct.getId());
                }
            }
        }

        private final void reloadHistoricDelivery(){
            adp.clear();
            requestHistoricDelivery(0);
        }


        private final void requestHistoricDelivery(int lastItemId){

            tvMessageError.setVisibility(View.GONE);

            HotDeliveryMan account = CustomApplication.getAccount();

            JSONObject json = new JSONObject();
            try {
                json.put("id", account.getId());
                json.put("id_dev", account.getIdDevice());
                json.put("id_recipient", account.getIdRecipient());
                json.put("id_last_item", lastItemId);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("requestHistoricDelivery()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.GET_HISTORIC_DELIVERY);
                requestServer.setObjectTransport(json);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        swip.setRefreshing(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            Gson gson = new Gson();
                            JsonArray array = gson.fromJson(object.toString(),JsonArray.class);


                            for (JsonElement e : array){
                                adp.addItem(gson.fromJson(e, HotHistoricRegister.class));
                            }

                            if(array != null && array.size() == 0) {
                                tvMessageError.setText("Sem registros no\nseu historico!");
                                tvMessageError.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        tvMessageError.setText("Sem resposta do Servidor!!");
                        tvMessageError.setVisibility(View.VISIBLE);
                        swip.setRefreshing(false);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                swip.setRefreshing(false);
                tvMessageError.setText("Erro na Requisição");
                tvMessageError.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onRefresh() {
            reloadHistoricDelivery();
        }
    }

}