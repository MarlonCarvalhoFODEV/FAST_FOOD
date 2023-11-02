package br.com.hotbmanager.fragments.deliveryman;

import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.android.gms.common.util.Base64Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.dashboard.DashboardInterface;
import br.com.hotbmanager.adapters.AdapterHoldingDeliveryman;
import br.com.hotbmanager.adapters.AdapterQueueDeliveryman;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.utils.EndPointMapping;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMT_QueueDeliveryman#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FMT_QueueDeliveryman extends Fragment implements Interceptor{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Controller controller;

    public FMT_QueueDeliveryman() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FMT_HoldingDeliveryman.
     */
    // TODO: Rename and change types and number of parameters
    public static FMT_QueueDeliveryman newInstance(String param1, String param2) {
        FMT_QueueDeliveryman fragment = new FMT_QueueDeliveryman();
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

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(this);

        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(
                getContext(),
                okHttpClientBuilder.build())
                .build();

        Fresco.initialize(getContext(),imagePipelineConfig);

        View view = inflater.inflate(R.layout.fmt_holding_deliveryman, container, false);
        controller = new Controller(view);

        return view;
    }

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", getContext().getString(R.string.domain_connection_user),
                getContext().getString(R.string.domain_connection_authorization));
        return "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        newRequest = request.newBuilder()
                .addHeader("Authorization", generateAuthorization())
                //.addHeader(HeadersContract.HEADER_X_CLIENT_ID, CLIENT_ID)
                .build();
        return chain.proceed(newRequest);
    }

    public class Controller extends RecyclerView.OnScrollListener implements SwipeRefreshLayout.OnRefreshListener {

        private RecyclerView rcList;
        private TextView tvMessage;
        private SwipeRefreshLayout swipHolding;
        private AdapterQueueDeliveryman adp;

        private LinearLayoutManager llm;

        public Controller(View v) {
            this.llm = new LinearLayoutManager(getContext());
            this.llm.setSmoothScrollbarEnabled(true);
            this.llm.setOrientation(LinearLayoutManager.VERTICAL);

            //this.handler = new Handler(Looper.getMainLooper());
            this.adp = new AdapterQueueDeliveryman(FMT_QueueDeliveryman.this);

            this.swipHolding = v.findViewById(R.id.swip_holding_deliveryman);

            this.rcList = v.findViewById(R.id.rc_holding_deliveryman);
            this.rcList.setHasFixedSize(true);
            this.rcList.setLayoutManager(llm);
            this.rcList.setAdapter(adp);
            this.rcList.addOnScrollListener(this);

            this.tvMessage = v.findViewById(R.id.tv_message_holding_deliveryman);

            this.swipHolding.setOnRefreshListener(this);

            loadQueueDeliverymans(0);
        }

        public final void addItem(JSONObject object) {
            adp.addItem(object);
        }

        public final void clear() {
            adp.clear();
        }

        public void showLoading(boolean show) {
            swipHolding.setRefreshing(show);
        }

        public final void showMessage(String message, boolean show) {
            tvMessage.setVisibility(show ? View.VISIBLE : View.GONE);
            tvMessage.setText(message);
        }

        public final void reloadQueueDeliverymans(){
            loadQueueDeliverymans(0);
        }


        private void loadQueueDeliverymans(int idLastDeliveryman) {
            try {
                showMessage(null, false);

                if (idLastDeliveryman == 0)
                    adp.clear();

                final HotManagerAccount ac = CustomApplication.getAccount();

                JSONObject json = new JSONObject();
                json.put("id", ac.getId());
                json.put("id_dev", ac.getIdDevice());
                json.put("id_del", idLastDeliveryman);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("loadOrders()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.GET_QUEUE_DELIVERYMAN);
                requestServer.setObjectTransport(json);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        showLoading(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {
                            try {

                                JSONObject ob = new JSONObject(object.toString());
                                JSONArray array = ob.getJSONArray("myArrayList");
                                for (int i = 0; i < array.length(); i++) {
                                    adp.addItem(array.getJSONObject(i).getJSONObject("map"));
                                }

                                if (array.length() == 0)
                                    showMessage("Sem entregador na\nfila de espera...", true);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                showLoading(false);
                                showMessage(e.getMessage(), true);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        showLoading(false);
                        showMessage("Erro: " + e.getMessage(), true);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                showLoading(false);
                showMessage("Erro: " + e.getMessage(), true);
            }
        }


        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            View view = llm.findViewByPosition(adp.getItemCount() - 2);

            if (view != null && view.isShown() && adp.isHaveMore()) {

                adp.setNoHaveMore();

                if (adp.getItemCount() >= 1) {
                    recyclerView.smoothScrollToPosition(adp.getItemCount());
                    loadQueueDeliverymans(adp.getLastItemID());
                }
            }
        }

        @Override
        public void onRefresh() {
            loadQueueDeliverymans(0);
        }
    }
}