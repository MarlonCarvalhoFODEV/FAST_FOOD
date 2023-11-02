package br.com.hotbmanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.util.Base64Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.hotbmanager.R;
import br.com.hotbmanager.fragments.deliveryman.listener.DraweeListener;
import br.com.hotbmanager.utils.EndPointMapping;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterHoldingDeliveryman extends RecyclerView.Adapter<AdapterHoldingDeliveryman.Holder> {

    private Context context ;
    private ArrayList<JSONObject> itens ;

    public AdapterHoldingDeliveryman(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.it_holding_deliveryman, null);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            holder.bindValues(position);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("KDSKADKSAKDSA",e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void addItem(JSONObject object){
        itens.add(object);
        notifyItemInserted(itens.size()-1);
    }

    public int getLastItemID(){
        try {
            return itens.get(itens.size()-1).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0 ;
    }

    public void clear() {
        itens.clear();
        notifyDataSetChanged();
    }

    public boolean isHaveMore(){
        try {
            return itens.get(itens.size()-1).getBoolean("have_more");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false ;
    }

    public void setNoHaveMore(){
        try {
            itens.get(itens.size()-1).put("have_more", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class Holder extends RecyclerView.ViewHolder{

        private JSONObject item ;
        private CircleImageView civPhotoProfile ;
        private SimpleDraweeView draweeView ;
        private TextView tvName, tvOrdersIDS ;

        public Holder(@NonNull View v) {
            super(v);

            //civPhotoProfile = v.findViewById(R.id.civ_img_profile_it_deliveryman);
            draweeView = v.findViewById(R.id.sdv_it_photo_holding);
            tvName = v.findViewById(R.id.tv_name_it_deliveryman);
            tvOrdersIDS = v.findViewById(R.id.tv_orders_it_deliveryman);
        }

        public void bindValues(int position) throws JSONException {
            item = itens.get(position);

            int idDeliveryman = item.has("id")?item.getInt("id"):0;
            String name = item.has("name")?item.getString("name"):"(Sem nome)";
            String idOrders = item.has("id_orders")?item.getString("id_orders"):"(Sem ids de Pedidos)";

            setImageFromURL2(draweeView,idDeliveryman);
            tvName.setText(name);
            tvOrdersIDS.setText("COD: "+idOrders);
        }

        private void setImageFromURL(final CircleImageView imgView, int idDeliveryman) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.getString(R.string.domain_connection));
            sb.append(EndPointMapping.DeliveryMan.CONTROLLER);
            sb.append(EndPointMapping.DeliveryMan.Functions.GET_IMAGE_PROFILE);
            sb.append("/");
            sb.append(idDeliveryman);

            LazyHeaders.Builder builder = new LazyHeaders.Builder()
                    .addHeader("Authorization", generateAuthorization());

            GlideUrl glideUrl = new GlideUrl(sb.toString(),builder.build());

            Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_img_fail)
                    .timeout(6000)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(imgView);
        }

        private void setImageFromURL2(final DraweeView draweeView, int idDeliveryman) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.getString(R.string.domain_connection));
            sb.append(EndPointMapping.DeliveryMan.CONTROLLER);
            sb.append(EndPointMapping.DeliveryMan.Functions.GET_IMAGE_PROFILE);
            sb.append("/");
            sb.append(idDeliveryman);

            ControllerListener listener = new DraweeListener(sb.toString(),draweeView);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(sb.toString())
                    .setControllerListener(listener)
                    .setTapToRetryEnabled(true)
                    .setOldController(draweeView.getController())
                    .build();

            draweeView.setController(controller);
        }

        private final String generateAuthorization() {
            String creds = String.format("%s:%s", context.getString(R.string.domain_connection_user),  context.getString(R.string.domain_connection_authorization));
            return "Basic " + Base64Utils.encode(creds.getBytes());
        }
    }
}
