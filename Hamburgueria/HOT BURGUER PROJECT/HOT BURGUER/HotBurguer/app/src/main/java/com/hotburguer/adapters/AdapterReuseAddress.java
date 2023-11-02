package com.hotburguer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.address.FMT_Address;
import com.hotburguer.fragments.dialogs.DialogAddress;
import com.hotburguer.fragments.dialogs.DialogResuseAddress;
import com.hotburguer.itens.HotReuseAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.location.LAddress;
import apis.userbuyer.BCard;

public class AdapterReuseAddress extends BaseAdapter {

    private Context context ;
    private ArrayList<HotReuseAddress> itens ;
    private DialogResuseAddress dialog ;

    public AdapterReuseAddress(Context c, DialogResuseAddress dg, ArrayList<HotReuseAddress> addresses){
        context = c ;
        dialog = dg ;
        itens = addresses ;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public HotReuseAddress getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public LAddress getAddress(int i) {
        return itens.get(i).getAddress();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder ;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.it_reuse_address, null);
            holder = new Holder(view);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.bindValues(itens.get(i));

        return view;
    }

    public class Holder implements View.OnClickListener {

        private TextView tvAddress ;
        private Button btnDisableAddress ;
        private HotReuseAddress a ;

        public Holder(View v){
            tvAddress = v.findViewById(R.id.tv_reuse_address);
            btnDisableAddress = v.findViewById(R.id.btn_reuse_address);
        }

        public void bindValues(HotReuseAddress reuse){

            a = reuse;


            tvAddress.setText(a.getAddress().getfull());
            btnDisableAddress.setOnClickListener(this);
        }

        private final void disableResuseAddress(){

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("getAddressFromCEP()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(context.getString(R.string.domain_connection));
            requestServer.setController(WebController.Address.CONTROLLER);
            requestServer.setFunction(WebController.Address.Functions.DISABLE_ADDRESS);
            requestServer.setObjectTransport(a);
            requestServer.request(context, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                    dialog.showLoading(loading);
                }

                @Override
                public void onSucess(Object array) {
                    if (array != null) {

                         dialog.dismiss();

                        JSONObject ob = new JSONObject();
                        try {
                            boolean result = ob.getBoolean("result_request");
                            Toast.makeText(context, result?"Excluido com sucesso":"Erro na Exclusão", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            itens.remove(a.getPosition());
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View view) {
            disableResuseAddress();
        }


    }
}
