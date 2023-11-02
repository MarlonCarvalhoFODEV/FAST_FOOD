package com.hotburguer.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerInterface;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.adapters.AdapterProduct;
import com.hotburguer.adapters.AdapterViewPager;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;
import com.hotburguer.utils.CacheApp;
import com.hotburguer.utils.CustomItemDecor;
import com.hotburguer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.share.Share;

public class FMT_ProductsList extends Fragment {

    private RecyclerView rcProducts;
    private AdapterProduct adpProducts;

    private String id;
    private String categorie;

    public FMT_ProductsList(String id, String categorie) {
        this.id = id;
        this.categorie = categorie;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fmt_products_vertical_list, null);

        adpProducts = new AdapterProduct(getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setSmoothScrollbarEnabled(true);

        rcProducts = view.findViewById(R.id.rc_products);
        rcProducts.setLayoutManager(llm);
        rcProducts.setHasFixedSize(true);
        rcProducts.addItemDecoration(new CustomItemDecor(16));
        rcProducts.setAdapter(adpProducts);

        getProducts(categorie);
        return view;
    }

    private final void selectPrdoductFromID(AdapterProduct adp) {
        if (adp.getItemCount() > 0 && getActivity() != null) {
            Share.DynamicLinkShare.getProductShared(getActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                @Override
                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();

                        String params = deepLink.toString().split("=")[1];
                        int idProduct = Integer.valueOf(params);

                        HotProduct product = adp.getProductFromID(idProduct);

                        if (product != null) {
                            rcProducts.smoothScrollToPosition(product.getPosition());
                            //rcProducts.setS
                        }
                    }
                }
            });
        }
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
        if(Utils.Network.isNetworkAvailable(getContext())){
            ((HomeBurguerInterface) getContext()).loading(true);

            HotUser user = CustomApplication.getUser();

            if (adpProducts.getItemCount() == 0 && user != null) {

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

                                JsonArray jArray = new Gson().fromJson(array.toString(), JsonArray.class);

                                Gson gson = new Gson();

                                for (JsonElement e : jArray) {
                                    adpProducts.addProduct(gson.fromJson(e, HotProduct.class));
                                }

                                selectPrdoductFromID(adpProducts);
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
        else{
            DialogInformative informative = new DialogInformative(getContext());
            informative.setTitle("Sem conexão");
            informative.setMessage("Dispositivo sem conexão com a internet");
            informative.show();
        }
    }
}
