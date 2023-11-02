package com.c.hotburguerdelivery.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.acitivtys.cbuyer.BuyerActivity;
import com.comprazencasa.colaborador.itens.ITProduct;
import com.comprazencasa.colaborador.service_chat.UserMessage;
import com.comprazencasa.colaborador.zutils.PopImageManager;
import com.comprazencasa.colaborador.zutils.PopSaveAndRestore;
import com.comprazencasa.colaborador.zutils.webservice.RequestWs;
import com.comprazencasa.colaborador.zutils.webservice.WebResponseIT;
import com.comprazencasa.colaborador.zutils.webservice.WebResponses;
import com.comprazencasa.colaborador.zutils.webservice.controller.WebController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.Holder> implements
        WebResponseIT {

    private Context context ;
    //private List<Integer> positions ;
    //private Map<Integer, ITProduct> products ;
    //private Map<Integer, ITProduct> backupProduct ;
    List<ITProduct> products ;
    List<ITProduct> backup ;
    private Handler handler ;

    private String orderID ;
    private boolean initialized ;

    public AdapterProducts(Context context, String orderID){
        this.context = context ;
        //this.positions = new ArrayList<>();
        //this.products = new HashMap<>();
        //this.backupProduct = new HashMap<>();
        this.orderID = orderID ;
        this.products = new ArrayList<>();
        this.backup = new ArrayList<>();
        this.handler = new Handler(Looper.getMainLooper());

        String json = PopSaveAndRestore.getObject(context, BuyerActivity.BACKUP);

        try {
            JsonArray backArray = new Gson().fromJson(json, JsonArray.class);
            ITProduct i ;
            Gson gson = new Gson();
            for(JsonElement e1 : backArray){
                i = gson.fromJson(e1.toString(), ITProduct.class);
                backup.add(i);
                //backupProduct.put(i.getId(),i);
            }
        }
        catch(com.google.gson.JsonSyntaxException e){
            PopSaveAndRestore.saveObject(context,BuyerActivity.BACKUP,new Gson().toJson(new ArrayList<>()));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.it_product_order, parent, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //ITProduct item = products.get(positions.get(position));

        //holder.bindValues(item);

        holder.bindValues(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addProduct(final ITProduct product, int timeMills) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if(backupProduct.get(product.getId()) != null
                        && backupProduct.get(product.getId()).isSelected()){
                    product.setSelected(true);
                }

                product.setOrderID(orderID);
                products.put(product.getId(),product);
                positions.add(product.getId());
                notifyItemInserted(products.size()-1);*/
            }
        },timeMills);
    }

    public void addProduct2(final ITProduct product, int timeMills) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(ITProduct back : backup){
                    if(back.getId() == product.getId()
                            && back.isSelected()){
                        product.setSelected(true);
                    } else {
                        product.setSelected(false);
                    }
                }

                product.setOrderID(orderID);
                products.add(product);

                notifyItemInserted(products.size()-1);
            }
        },timeMills);
    }

    public boolean allProductsIsSelected(){
        /*for (ITProduct p : products.values()){
            if(p.isSelected() == false) {
                return false ;
            }
        }*/

        for (ITProduct p : products){
            if(p.isSelected() == false) {
                return false ;
            }
        }
        return true ;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public void webResponse(WebResponses response) {
        switch (response.getComunication()){
            case 0:{
                Toast.makeText(context, "Erro ao tentar excluir!", Toast.LENGTH_SHORT).show();
                break;
            }

            case 1:{
                Toast.makeText(context, "Excluido com sucesso!", Toast.LENGTH_SHORT).show();
                products.clear(); ;
                //backupProduct.clear();
                backup.clear();
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void receiverMessage(UserMessage msg) {

    }

    @Override
    public void interfaceController(WebResponses response) {

    }

    public void clear() {
        products.clear();
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener,
            AdapterView.OnLongClickListener,
            CompoundButton.OnCheckedChangeListener{

        private TextView tvTitle, tvPrice, tvCount ;
        private RelativeLayout base ;
        private CheckBox check ;
        private ImageView ivPhoto ;
        private View v ;
        private boolean clicked = false ;

        public Holder(View v) {
            super(v);
            this.v = v ;
            tvTitle = (TextView) v.findViewById(R.id.tv_it_name_product) ;
            tvPrice = (TextView) v.findViewById(R.id.tv_it_price_product) ;
            tvCount = (TextView) v.findViewById(R.id.tv_it_count_product) ;
            check = (CheckBox) v.findViewById(R.id.cb_check_product);
            ivPhoto = (ImageView) v.findViewById(R.id.iv_img_order_products);
            base = (RelativeLayout) v.findViewById(R.id.base_it_product_order);

            check.setOnCheckedChangeListener(this);
            this.v.setOnClickListener(this);
            this.v.setOnLongClickListener(this);
        }

        public void bindValues(ITProduct item){
            tvTitle.setText(item.getTitle());
            tvPrice.setText("R$ "+ String.valueOf(item.getPerPrice()).replace(".",","));
            tvCount.setText(String.valueOf(item.getCount()+(item.isWeight()?"g":"un")));

            PopImageManager.loadUrlImage(context, item.getPathImg(), ivPhoto);
            ivPhoto.setOnClickListener(new OpenImageDialog(context,item.getPathImg(), item.getTitle()));

            check.setChecked(item.isSelected());

            if(item.isFlavors()){
                StringBuilder sb = new StringBuilder();
                sb.append(item.getTitle());
                sb.append(" ( ");
                sb.append(item.getFlavor().getFlavor());
                sb.append(" )");

                tvTitle.setText(sb.toString());
                tvCount.setText(String.valueOf(item.getFlavor().getCount()+(item.isWeight()?"g":"un")));
            }

            if(item.isSelected()) {
                base.setBackgroundResource(R.color.colorAccent);
            }
            else base.setBackgroundResource(R.color.transparentePreto);

        }

        @Override
        public void onClick(View v) {
            if(initialized){

                check.setChecked(!check.isChecked());

                //ITProduct item = products.get(positions.get(getAdapterPosition()));
                ITProduct item = products.get(getAdapterPosition());
                item.setSelected(check.isChecked());

                //PopSaveAndRestore.saveObject(context, BuyerActivity.BACKUP,new Gson().toJson(new ArrayList<>(products.values())));
                PopSaveAndRestore.saveObject(context, BuyerActivity.BACKUP,new Gson().toJson(new ArrayList<>(products)));

            }
            else{
                check.setChecked(false);
                Toast.makeText(context, "Antes de selecionar, clique em INICIAR.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(initialized){
                //showDialogDeleteProduct(products.get(positions.get(getAdapterPosition())));
                showDialogDeleteProduct(products.get(getAdapterPosition()));
            }
            else {
                Toast.makeText(context, "Não pode excluir esse item, necessario iniciar produção", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(initialized){
                if(check.isChecked()){
                    base.setBackgroundResource(R.color.colorAccent);
                }
                else base.setBackgroundResource(R.color.transparentePreto);

                //ITProduct item = products.get(positions.get(getAdapterPosition()));
                ITProduct item = products.get(getAdapterPosition());
                item.setSelected(b);

                //PopSaveAndRestore.saveObject(context, BuyerActivity.BACKUP,new Gson().toJson(new ArrayList<>(products.values())));
                PopSaveAndRestore.saveObject(context, BuyerActivity.BACKUP,new Gson().toJson(new ArrayList<>(products)));
            } else{
                check.setChecked(false);
                Toast.makeText(context, "Antes de selecionar, clique em INICIAR.", Toast.LENGTH_SHORT).show();
            }
        }

        private void showDialogDeleteProduct(final ITProduct product){
            final AlertDialog dg =  new  AlertDialog.Builder(context).create();
            View view = LayoutInflater.from(context).inflate(R.layout.dg_del_product, null);
            ((TextView) view.findViewById(R.id.dg_del_tv_product_name)).setText(product.getTitle());

            ((Button)view.findViewById(R.id.dg_del_btn_not)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dg.dismiss();
                }
            });

            ((Button)view.findViewById(R.id.dg_del_btn_yes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestDeleteProduct(product);
                    dg.dismiss();
                }
            });

            dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dg.setView(view);
            dg.setCancelable(false);
            dg.show();
        }

        private void requestDeleteProduct(ITProduct product){

            StringBuilder method = new StringBuilder();
            method.append(WebController.Orders.CONTROLLER);
            method.append(WebController.Orders.Method.DELETE_PRODUCT_FROM_ORDER);

            WebResponses web = new WebResponses();
            web.setJsonObject(new Gson().toJson(product));

            new RequestWs(context, AdapterProducts.this)
                    .request(Request.Method.POST,method, web);
        }
    }

    private class OpenImageDialog extends AlertDialog implements View.OnClickListener {

        private String imgPath, title ;

        protected OpenImageDialog(@NonNull Context context, String imgPath, String title) {
            super(context);
            this.imgPath = imgPath ;
            this.title = title ;
        }

        public void open(String imgPath, String title){
            View view = LayoutInflater.from(context).inflate(R.layout.pop_open_image, null);

            ImageView img = view.findViewById(R.id.iv_open_img);
            TextView tv = view.findViewById(R.id.tv_title_open_img);

            tv.setText(title);
            PopImageManager.loadUrlImage(context, imgPath, img);

            setView(view);
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            show();
        }

        @Override
        public void onClick(View view) {
            open(imgPath, title);
        }
    }
}
