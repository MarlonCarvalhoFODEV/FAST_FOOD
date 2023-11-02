package com.hotburguer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerInterface;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.adapters.AdapterViewPager;
import com.hotburguer.connection.WebController;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;

public class FMT_Products extends Fragment {

    private FragmentManager fragmentManager;
    private TabLayout indicator;
    private ViewPager viewPager;
    private AdapterViewPager adpProducts;

    private String id;
    private String categorie;

    public FMT_Products(String id, String categorie, FragmentManager fragmentManager) {
        this.id = id;
        this.categorie = categorie;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fmt_products, null);

        adpProducts = new AdapterViewPager(fragmentManager);

        viewPager = (ViewPager) view.findViewById(R.id.vp_slider);
        viewPager.setOffscreenPageLimit(8);
        viewPager.setAdapter(adpProducts);

        indicator = (TabLayout) view.findViewById(R.id.tab_dots);
        indicator.setupWithViewPager(viewPager, true);
        getProducts(categorie);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        CustomApplication.getApiRequisition().onDestroyConnections(getContext());
        super.onDestroy();
    }

    private synchronized void getProducts(String categorie) {
        ((HomeBurguerInterface) getContext()).loading(true);

        HotUser user = CustomApplication.getUser();

        if (adpProducts.getCount() == 0 && user != null) {

            try {
                JSONObject send = new JSONObject();
                send.put("id", user.getId());
                send.put("id_dev", user.getDeviceID());
                send.put("categorie", categorie);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition(id);
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getResources().getString(R.string.domain_connection));
                requestServer.setController(WebController.Products.CONTROLLER);
                requestServer.setFunction(WebController.Products.Functions.GET_ALL);
                requestServer.setObjectTransport(send);
                requestServer.request(getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {
                        if (getContext() != null)
                            ((HomeBurguerInterface) getContext()).loading(loading);
                    }

                    @Override
                    public void onSucess(Object array) {
                        if (array != null) {
                            adpProducts.clear();
                            if (getActivity() != null)
                                adpProducts = new AdapterViewPager(getActivity().getSupportFragmentManager());

                            JsonArray jArray = new Gson().fromJson(array.toString(), JsonArray.class);

                            ArrayList<HotProduct> products = new ArrayList();

                            Gson gson = new Gson();
                            for (JsonElement e : jArray)
                                products.add(gson.fromJson(e, HotProduct.class));

                            adpProducts.addAllSliders(products);

                            viewPager.setAdapter(adpProducts);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
