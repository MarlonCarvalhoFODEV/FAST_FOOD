package com.c.hotburguerdelivery.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.itens.ITOrderBuyer;

@SuppressLint("ValidFragment")
public class FMT_InfosClient extends Fragment {

    private ITOrderBuyer order ;

    public FMT_InfosClient(ITOrderBuyer order){
        this.order = order ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fmt_infos_client, null);


        TextView tvCel = (TextView) view.findViewById(R.id.tv_cel_infos_client);
        TextView tvCollection = (TextView) view.findViewById(R.id.tv_value_infos_client);
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_address_infos_client);

        StringBuilder address = new StringBuilder();
        address.append(order.getStreet());
        address.append("\n");
        address.append(order.getNeighborhood());
        address.append("\n");
        address.append(order.getComplements());

        tvCel.setText(order.getCel());
        tvCollection.setText(order.getPurchaseValue());
        tvAddress.setText(address.toString());

        return view ;
    }
}
