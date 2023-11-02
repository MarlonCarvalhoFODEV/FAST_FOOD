package com.c.hotburguerdelivery.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.acitivtys.cbuyer.addproduct.AddProductActivity;
import com.comprazencasa.colaborador.itens.ITFlavor;
import com.comprazencasa.colaborador.itens.ITProduct;
import com.comprazencasa.colaborador.mservices.MessageManager;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.customapplication.ResultReceiverCZC;
import com.comprazencasa.colaborador.zutils.webservice.RequestWs;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AdapterADDProducr extends BaseAdapter implements WebResponseIT {

    private Context context ;
    private List<ITProduct> itens ;

    private ResultReceiverCZC resultReceiverCZC ;
    private MessageManager msgManager ;
    private Intent intentService ;

    public AdapterADDProducr(Context context){
        this.context = context ;
        this.itens = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public ITProduct getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ITProduct it = itens.get(i);

        Hoder holder ;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.it_add_product, null);
            holder = new Hoder(view);
            view.setTag(holder);
        }
        else {
            holder = (Hoder) view.getTag();
        }

        holder.setTitle(it.getTitle());
        holder.tvPrice.setText("R$ "+it.getPerPrice());
        holder.tvCategorie.setText(categorieDecode(it.getCategorie()));

        StringBuilder url = new StringBuilder(context.getResources().getString(R.string.connectionWs));
        url.append(WebController.Image.CONTROLLER);
        url.append(WebController.Image.Methods.GET_IMAGE);
        url.append(it.getPathImg());

        holder.ivImg.setImageDrawable(context.getResources().getDrawable(R.drawable.loading_img));
        ImageLoader.getInstance().displayImage(url.toString(), holder.ivImg);

        holder.getBtnAdd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(it);
            }
        });

        return view;
    }

    public ITProduct getLastItem() {
        if(itens.size()>0){
            return itens.get(itens.size()-1);
        }

        return null ;
    }

    private void showDialog(final ITProduct item){
        item.clearFlavors();
        View view = LayoutInflater.from(context).inflate(R.layout.dg_add_product, null);

        final TextView tvTitle = (TextView) view.findViewById(R.id.tv_title_add_p) ;
        final EditText etFlavors = ((EditText)view.findViewById(R.id.et_flavor_add_p));
        final EditText etPrice = ((EditText)view.findViewById(R.id.et_price_add_p));
        final Button btnFinish = ((Button) view.findViewById(R.id.btn_save_product));

        final AlertDialog dg =  new  AlertDialog.Builder(context).create();
        dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dg.setView(view);
        dg.show();

        final CheckBox checkBox = ((CheckBox)view.findViewById(R.id.cb_check_activate));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tvTitle.setText("Ativar produto");
                    btnFinish.setText("ATIVAR E ATUALIZAR");
                } else {
                    tvTitle.setText("Desativar produto");
                    btnFinish.setText("DESATIVAR E ATUALIZAR");
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPrice.getText() != null && !etPrice.getText().toString().isEmpty()){
                    if(etFlavors.getText().toString().split(",").length >= 1 && etFlavors.getText().toString().length() >= 2){
                        for (String flav : etFlavors.getText().toString().split(",")){
                            ITFlavor newFlav = new ITFlavor();
                            newFlav.setFlavor(flav);
                            newFlav.setId_product(item.getId());

                            item.getFlavors().put(item.getFlavors().size(), newFlav);
                        }
                    }

                    ((AddProductActivity) context).showProgress("Aguarde atualizando...");
                    item.setPerPrice(Double.valueOf(etPrice.getText().toString().replaceAll(",",".")));
                    dg.dismiss();

                    StringBuilder method = new StringBuilder();
                    method.append(WebController.Products.CONTROLLER);
                    method.append(WebController.Products.Method.UPDATE_ACTIVE);

                    WebResponses web = new WebResponses();
                    web.setJsonObject(new Gson().toJson(item));
                    web.setMessage(checkBox.isChecked()?"active":"inactive");

                    new RequestWs(context,AdapterADDProducr.this)
                            .request(Request.Method.POST, method, web);
                } else {
                    Toast.makeText(context, "Faltou digitar o valor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addProduct(final ITProduct item) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itens.add(item);
                notifyDataSetChanged();
            }
        });
    }

    public void clear() {
        itens.clear();
        notifyDataSetChanged();
    }

    @Override
    public void webResponse(WebResponses response) {
        switch (response.getComunication()){
            case 0:{
                Toast.makeText(context, "Outro colaborador já atualizou!", Toast.LENGTH_SHORT).show();
                break;
            }
            case 1:{

                Toast.makeText(context, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        ((AddProductActivity) context).hideProgress();
        ((AddProductActivity) context).updateList();
    }

    @Override
    public void receiverMessage(UserMessage msg) {

    }

    @Override
    public void interfaceController(WebResponses response) {

    }

    public class Hoder{
        private TextView tvTitle, tvPrice, tvCategorie;
        private Button btnAdd ;
        private ImageView ivImg ;

        public Hoder(View view) {
            this.ivImg = (ImageView) view.findViewById(R.id.iv_img_add_p) ;
            this.tvTitle = (TextView) view.findViewById(R.id.tv_title_add_p);
            this.tvPrice = (TextView) view.findViewById(R.id.tv_price_add_p);
            this.tvCategorie = (TextView) view.findViewById(R.id.tv_categorie_add_p);
            this.btnAdd = (Button) view.findViewById(R.id.btn_add_product_it);
        }

        public ImageView getIvImg() {
            return ivImg;
        }

        public void setImage(int idResource) {
            this.ivImg.setImageResource(idResource);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public void setTitle(String title) {
            this.tvTitle.setText(title);
        }

        public Button getBtnAdd() {
            return btnAdd;
        }
    }

    private String categorieDecode(String categoria){
        switch (categoria){
            case "Morning": return "Matinais" ;
            case "Cookies & Sugarplum": return "Biscoitos e Bombons" ;
            case "Cleaning Perfumery": return "Limpeza e Perfumaria" ;
            case "Butchery": return "Açougue" ;
            case "Drinks": return "Bebidas" ;
            case "Baby": return "Bebês" ;
            case "Cold & Dairy": return "Frio e Laticínios" ;
            case "Basic Food": return "Alimentos Básicos" ;
            case "Hortifruti": return "Frutas, Legumes e Verduras";
            case "Salty": return "Salgados" ;
            default: return "ERROR!";
        }
    }
}
