package com.hotburguer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.util.Base64Utils;
import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.DraweeListener;
import com.hotburguer.itens.HotProduct;

import java.util.ArrayList;
import java.util.Collection;

import apis.finance.FinanceAPI;

public class AdapterProductOrder extends BaseAdapter {

    private Context context ;
    private ArrayList<HotProduct> itens ;

    public AdapterProductOrder(Context context){
        this.context = context ;
        this.itens = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public HotProduct getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itens.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null ;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.it_product_order, null);
            holder = new Holder(view);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.bindValues(itens.get(i));

        return view;
    }

    public synchronized void addProduct(HotProduct product){
        itens.add(product);
        notifyDataSetChanged();
    }

    public void addAll(Collection<HotProduct> values) {
        for(HotProduct p : values){
            addProduct(p);
        }
    }

    public double getTotalValue(){

        double value = 0 ;
        for(HotProduct p : itens)
            value += p.getPerPriceMultiply();

        return FinanceAPI.format(value, 2);
    }

    public class Holder{

        private final SimpleDraweeView draweeView ;
        private final TextView tvName, tvPrice, tvCount, tvSubTotal ;

        public Holder(View v){
            draweeView = v.findViewById(R.id.sdv_it_product_order);
            tvName = v.findViewById(R.id.tv_title_product_order);
            tvPrice = v.findViewById(R.id.tv_price_product_order);
            tvCount = v.findViewById(R.id.tv_qt_product_order);
            tvSubTotal = v.findViewById(R.id.tv_total_value_product_order);
        }

        public void bindValues(HotProduct product){
            setImage(product);

            tvName.setText(product.getTitle());
            tvPrice.setText("R$ "+product.getPerPrice());
            tvCount.setText("Quantidade - "+product.getCount()+"x");
            tvSubTotal.setText("R$ "+product.getPerPriceMultiply()+"\nTOTAL");
        }

        private void setImage(HotProduct product){
            StringBuilder sb = new StringBuilder();
            sb.append(context.getString(R.string.domain_connection));
            sb.append(WebController.Products.CONTROLLER);
            sb.append(WebController.Products.Functions.GET_IMAGE);
            sb.append("/");
            sb.append(product.getId());

            ControllerListener listener = new DraweeListener(sb.toString(),draweeView);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(sb.toString())
                    .setControllerListener(listener)
                    .setTapToRetryEnabled(true)
                    .setOldController(draweeView.getController())
                    .build();

            draweeView.setController(controller);
        }

        /*private void setImageFromURL(final ImageView imgView, HotProduct product) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.getString(R.string.domain_connection));
            sb.append(WebController.Products.CONTROLLER);
            sb.append(WebController.Products.Functions.GET_IMAGE);
            sb.append("/");
            sb.append(product.getId());

            LazyHeaders.Builder builder = new LazyHeaders.Builder()
                    .addHeader("Authorization", generateAuthorization());

            GlideUrl glideUrl = new GlideUrl(sb.toString(),builder.build());

            Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .placeholder(R.drawable.ic_other_img)
                    .error(R.drawable.ic_img_fail)
                    .timeout(6000)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(imgView);
        }*/

        private final String generateAuthorization() {
            String creds = String.format("%s:%s", context.getString(R.string.domain_connection_user),  context.getString(R.string.domain_connection_authorization));
            return "Basic " + Base64Utils.encode(creds.getBytes());
        }
    }
}
