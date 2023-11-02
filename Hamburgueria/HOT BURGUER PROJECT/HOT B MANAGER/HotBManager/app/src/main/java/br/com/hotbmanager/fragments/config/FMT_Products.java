package br.com.hotbmanager.fragments.config;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
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
import br.com.hotbmanager.R;
import br.com.hotbmanager.adapters.AdapterHours;
import br.com.hotbmanager.adapters.AdapterProducts;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.itens.HotProduct;
import br.com.hotbmanager.utils.CustomItemDecor;
import br.com.hotbmanager.utils.EndPointMapping;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMT_Products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FMT_Products extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Controller controller ;

    public FMT_Products() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FMT_Products.
     */
    // TODO: Rename and change types and number of parameters
    public static FMT_Products newInstance(String param1, String param2) {
        FMT_Products fragment = new FMT_Products();
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fmt_holding_deliveryman,  container, false);
        controller = new Controller(view);
        // Inflate the layout for this fragment
        return view ;
    }

    public class Controller extends RecyclerView.OnScrollListener implements SwipeRefreshLayout.OnRefreshListener{

        private RecyclerView rcList ;
        private TextView tvMessage ;
        private SwipeRefreshLayout swipHolding ;
        private AdapterProducts adp ;

        private LinearLayoutManager llm ;

        public Controller(View v) {
            this.llm = new LinearLayoutManager(getContext());
            this.llm.setSmoothScrollbarEnabled(true);
            this.llm.setOrientation(LinearLayoutManager.VERTICAL);

            //this.handler = new Handler(Looper.getMainLooper());
            this.adp = new AdapterProducts(getContext());

            this.swipHolding = v.findViewById(R.id.swip_holding_deliveryman);

            this.rcList = v.findViewById(R.id.rc_holding_deliveryman);
            this.rcList.setHasFixedSize(true);
            this.rcList.setLayoutManager(llm);
            this.rcList.addItemDecoration(new CustomItemDecor(8));
            this.rcList.setAdapter(adp);
            this.rcList.addOnScrollListener(this);

            this.tvMessage = v.findViewById(R.id.tv_message_holding_deliveryman);

            this.swipHolding.setOnRefreshListener(this);

            loadProducts();
        }

        private void showLoading(boolean show){
            swipHolding.setRefreshing(show);
        }

        public final void showMessage(String message, boolean show){
            tvMessage.setVisibility(show?View.VISIBLE: View.GONE);
            tvMessage.setText(message);
        }


        private void loadProducts(){
            try {
                showMessage(null,false);
                //adp.clear();

                final HotManagerAccount ac = CustomApplication.getAccount();

                JSONObject json = new JSONObject();
                json.put("id",ac.getId());
                json.put("idv",ac.getIdDevice());
                //json.put("id_order",idOrder);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("loadProducts()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.Manager.CONTROLLER);
                requestServer.setFunction(EndPointMapping.Manager.Functions.GET_PRODUCTS);
                requestServer.setObjectTransport(json);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        showLoading(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            JsonArray jArray = new Gson().fromJson(object.toString(), JsonArray.class);

                            Gson gson = new Gson();

                            for (JsonElement e : jArray) {
                                adp.addProduct(gson.fromJson(e, HotProduct.class));
                            }

                            if(jArray.size() == 0)
                                showMessage("Nenhum produto cadastrado", true);

                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        showLoading(false);
                        showMessage("Erro: "+e.getMessage(), true);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                showLoading(false);
                showMessage("Erro: "+e.getMessage(), true);
            }
        }

        @Override
        public void onRefresh() {
            loadProducts();
        }
    }
}