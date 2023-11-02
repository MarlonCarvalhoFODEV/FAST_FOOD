package br.com.hotbdelivery.fragments.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.adapters.AdapterHome;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.itens.HotPanelHome;
import br.com.hotbdelivery.utils.CustomItemDecor;
import br.com.hotbdelivery.utils.EndPointMapping;

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

    private Controller controller ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FMT_Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FMT_Home.
     */
    // TODO: Rename and change types and number of parameters
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
        View view  = inflater.inflate(R.layout.fmt_dash_home, container, false);

        controller = new Controller(view);

        return view ;
    }

    public class Controller implements SwipeRefreshLayout.OnRefreshListener {

        private SwipeRefreshLayout swip ;
        private RecyclerView list ;
        private AdapterHome adp ;
        private TextView tvErrorMessage ;

        public Controller(View view){
            adp = new AdapterHome(getContext());

            swip = view.findViewById(R.id.swip_home);
            swip.setOnRefreshListener(this);

            tvErrorMessage = view.findViewById(R.id.tv_error_message_fmt_home);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            llm.setSmoothScrollbarEnabled(true);

            list = view.findViewById(R.id.rv_home);
            list.addItemDecoration(new CustomItemDecor(16));
            list.setHasFixedSize(true);
            list.setLayoutManager(llm);
            list.setAdapter(adp);

            reload();
        }

        public SwipeRefreshLayout getSwip() {
            return swip;
        }

        public RecyclerView getList() {
            return list;
        }

        public AdapterHome getAdp() {
            return adp;
        }

        private final void reload(){
            HotDeliveryMan account = CustomApplication.getAccount();
            adp.clear();
            loadData(account);
        }

        private void showMessage(String message, boolean show){
            if(show){
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(message);
            } else {
                tvErrorMessage.setVisibility(View.GONE);
                tvErrorMessage.setText(message);
            }
        }

        private void loadData(HotDeliveryMan item) {
            showMessage(null, false);
            try {
                JSONObject json = new JSONObject();
                json.put("id", item.getId());
                json.put("id_dev", item.getIdDevice());
                json.put("id_recipient", item.getIdRecipient());

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("loadData()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.GET_PERFORMANCES);
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

                            JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                            for(JsonElement e : array){
                                adp.addPanel(gson.fromJson(e, HotPanelHome.class));
                            }

                            if(array.size() == 0)
                                showMessage("Nenhuma Atividade\nRecente...", true);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        showMessage("Sem resposta do Servidor", true);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRefresh() {
            reload();
        }
    }
}