package com.hotburguer.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerInterface;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.adapters.AdapterProduct;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;
import com.hotburguer.utils.CustomItemDecor;
import com.hotburguer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.share.Share;

public class FMT_ServerNotFound extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fmt_server_not_found, null);

        return view;
    }
}
