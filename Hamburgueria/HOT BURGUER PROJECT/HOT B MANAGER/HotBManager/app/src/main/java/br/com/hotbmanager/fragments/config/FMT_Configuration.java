package br.com.hotbmanager.fragments.config;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.dashboard.DashboardInterface;
import br.com.hotbmanager.adapters.AdapterActivesDeliveryman;
import br.com.hotbmanager.adapters.AdapterViewPager;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.fragments.deliveryman.FMT_ActiveDeliveryman;
import br.com.hotbmanager.fragments.deliveryman.FMT_HoldingDeliveryman;
import br.com.hotbmanager.fragments.deliveryman.FMT_QueueDeliveryman;
import br.com.hotbmanager.utils.EndPointMapping;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMT_Configuration#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FMT_Configuration extends Fragment implements Interceptor {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Controller controller ;
    private String mParam2;

    public FMT_Configuration() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FMT_Configuration.
     */
    // TODO: Rename and change types and number of parameters
    public static FMT_Configuration newInstance(String param1, String param2) {
        FMT_Configuration fragment = new FMT_Configuration();
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
        // Inflate the layout for this fragment
        ((DashboardInterface)getActivity()).setSubTitle("Configurações");


        // Inflate the layout for this fragment
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(this);
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(
                getContext(),
                okHttpClientBuilder.build())
                .build();
        Fresco.initialize(getContext(),imagePipelineConfig);

        View view = inflater.inflate(R.layout.fmt_configuration, container, false);
        controller = new Controller(view);

        return view;
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

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", getContext().getString(R.string.domain_connection_user),
                getContext().getString(R.string.domain_connection_authorization));
        return "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
    }

    private class Controller extends RecyclerView.OnScrollListener implements ViewPager.OnPageChangeListener{

        private TabLayout tabLayout ;
        private ViewPager viewPager ;
        private AdapterViewPager adp ;

        private FMT_OpeningHours fmtOpeningHours ;
        private FMT_Products fmtProducts ;
        private FMT_Performance fmtPerformance ;

        public Controller(View v) {
            this.tabLayout = v.findViewById(R.id.tb_configuration);
            this.viewPager = v.findViewById(R.id.vp_configuration);
            this.adp = new AdapterViewPager(getActivity().getSupportFragmentManager(), new String[]{"Horários F.","Produtos","Relatório"});

            this.fmtOpeningHours = new FMT_OpeningHours();
            this.fmtProducts = new FMT_Products();
            this.fmtPerformance = new FMT_Performance();

            this.adp.addFragment(fmtOpeningHours);
            this.adp.addFragment(fmtProducts);
            this.adp.addFragment(fmtPerformance);

            viewPager.setAdapter(adp);
            viewPager.addOnPageChangeListener(this);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setupWithViewPager(viewPager);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}