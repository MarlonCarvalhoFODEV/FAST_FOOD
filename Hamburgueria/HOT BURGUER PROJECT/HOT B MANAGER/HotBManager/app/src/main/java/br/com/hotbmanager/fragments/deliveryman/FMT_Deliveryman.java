package br.com.hotbmanager.fragments.deliveryman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.dashboard.DashboardInterface;
import br.com.hotbmanager.adapters.AdapterViewPager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMT_Deliveryman#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FMT_Deliveryman extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Controller controller ;

    public FMT_Deliveryman() {
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
    public static FMT_Deliveryman newInstance(String param1, String param2) {
        FMT_Deliveryman fragment = new FMT_Deliveryman();
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

        View view = inflater.inflate(R.layout.fmt_deliveryman, container, false);
        controller = new Controller(view);

        return view;
    }


    public class Controller extends RecyclerView.OnScrollListener implements ViewPager.OnPageChangeListener {

        private TabLayout tabLayout ;
        private ViewPager viewPager ;
        private AdapterViewPager adp ;

        private FMT_HoldingDeliveryman waitingDeliveryman ;
        private FMT_ActiveDeliveryman activeDeliveryman ;
        private FMT_QueueDeliveryman queueDeliveryman ;

        public Controller(View v) {
            this.tabLayout = v.findViewById(R.id.tb_deliveryman);
            this.viewPager = v.findViewById(R.id.vp_deliveryman);
            this.adp = new AdapterViewPager(getActivity().getSupportFragmentManager());

            this.waitingDeliveryman = new FMT_HoldingDeliveryman();
            this.activeDeliveryman = new FMT_ActiveDeliveryman();
            this.queueDeliveryman = new FMT_QueueDeliveryman();

            this.adp.addFragment(waitingDeliveryman);
            this.adp.addFragment(activeDeliveryman);
            this.adp.addFragment(queueDeliveryman);

            viewPager.setAdapter(adp);
            viewPager.addOnPageChangeListener(this);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setupWithViewPager(viewPager);
        }

        public void addFragment(Fragment fragment){
            adp.addFragment(fragment);
        }

        public void removeFragment(int position){
            adp.remove(position);
        }

        private final void setSubTitle(String subTitle){
            ((DashboardInterface)getActivity()).setSubTitle(subTitle);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0: setSubTitle("Entregadores Aguardando");break;
                case 1: setSubTitle("Entregadores Ativos");break;
                case 2: setSubTitle("Entregadores - Fila de Espera");break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}