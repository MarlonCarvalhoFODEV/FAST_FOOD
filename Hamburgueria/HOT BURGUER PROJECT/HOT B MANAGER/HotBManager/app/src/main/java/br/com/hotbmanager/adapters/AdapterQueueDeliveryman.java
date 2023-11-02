package br.com.hotbmanager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.dialogs.DialogInformative;
import br.com.hotbmanager.fragments.deliveryman.FMT_QueueDeliveryman;
import br.com.hotbmanager.fragments.deliveryman.listener.DraweeListener;
import br.com.hotbmanager.utils.CacheApp;
import br.com.hotbmanager.utils.EndPointMapping;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterQueueDeliveryman extends RecyclerView.Adapter<AdapterQueueDeliveryman.Holder> {

    private FMT_QueueDeliveryman fragment ;
    private ArrayList<JSONObject> itens ;

    public AdapterQueueDeliveryman(FMT_QueueDeliveryman fragment) {
        this.fragment = fragment;
        this.itens = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.it_queue_deliveryman, null);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            holder.bindValues(position);
        } catch (JSONException e) {
            e.printStackTrace();
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

    private final void removeItem(int position){
        itens.remove(position);
        notifyItemRemoved(position);
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

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private JSONObject item ;
        //private CircleImageView civPhotoProfile ;
        private Button btnActive ;
        private TextView tvName ;
        private SimpleDraweeView draweeView ;

        private int idDeliveryman ;
        private String name ;
        private String email ;
        private String cel ;

        public Holder(@NonNull View v) {
            super(v);

            //civPhotoProfile = v.findViewById(R.id.civ_img_profile_it_deliveryman);
            draweeView = v.findViewById(R.id.sdv_it_product_order);
            tvName = v.findViewById(R.id.tv_name_it_deliveryman);
            btnActive = v.findViewById(R.id.btn_active_it_deliveryman_active);
            btnActive.setOnLongClickListener(this);
            v.setOnClickListener(this);
        }

        public void bindValues(int position) throws JSONException {
            item = itens.get(position);

            idDeliveryman = item.has("id")?item.getInt("id"):0;
            name = item.has("name")?item.getString("name"):"(Sem nome)";
            email = item.has("email")?item.getString("email"):"(Sem email)";
            cel = item.has("cel")?item.getString("cel"):"(Sem número)";
           // String idOrders = item.has("id_orders")?item.getString("id_orders"):"(Sem ids de Pedidos)";

            setImageFromURL2(draweeView, idDeliveryman);
            tvName.setText(name);
            //tvOrdersIDS.setText("COD: "+idOrders);
        }

        private void setImageFromURL2(final DraweeView draweeView, int idDeliveryman) {
            StringBuilder sb = new StringBuilder();
            sb.append(fragment.getContext().getString(R.string.domain_connection));
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

        private final void showMessage(String message, boolean show){
            fragment.getController().showMessage(message, show);
        }

        private void activeDeliveryman(int idDeliveryman){

            btnActive.setEnabled(false);
            try {
                showMessage(null,false);

                final HotManagerAccount ac = CustomApplication.getAccount();

                JSONObject json = new JSONObject();
                json.put("id",ac.getId());
                json.put("id_dev",ac.getIdDevice());
                json.put("id_del",idDeliveryman);

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("activeDeliveryman()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(fragment.getContext().getString(R.string.domain_connection));
                requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
                requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.ACTIVE_DELIVERYMAN);
                requestServer.setObjectTransport(json);
                requestServer.request(fragment.getContext(), new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {

                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {
                            try {

                                JSONObject ob = new JSONObject(object.toString());
                                ob = ob.getJSONObject("map");

                                boolean result = ob.getBoolean("result");

                                if(result) {
                                    Toast.makeText(fragment.getContext(), "Entregador Ativado", Toast.LENGTH_SHORT).show();
                                    removeItem(getAdapterPosition());
                                } else{
                                    Toast.makeText(fragment.getContext(), "Erro ao ativar\nentregador", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                fragment.getController().showLoading(false);
                                showMessage(e.getMessage(),true);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        fragment.getController().showLoading(false);
                        showMessage("Erro: "+e.getMessage(), true);
                        btnActive.setEnabled(true);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                fragment.getController().showLoading(false);
                btnActive.setEnabled(true);
                showMessage("Erro: "+e.getMessage(), true);
            }
        }

        private boolean checkSecurity(){
            Map<Integer, String> security = CacheApp.getSecurity(tvName.getContext());
            return security.size()>0;
        }

        @Override
        public boolean onLongClick(View view) {
            final MediaPlayer player = MediaPlayer.create(tvName.getContext(), R.raw.alarm);
            final AdminAccess admin = new AdminAccess(tvName.getContext());
            admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(CustomApplication.getPassAdmin().equals(admin.getPass())){
                        admin.dismiss();
                        activeDeliveryman(idDeliveryman);
                        player.stop();
                    } else {
                        Toast.makeText(tvName.getContext(), "Senha Inválida", Toast.LENGTH_SHORT).show();
                        CacheApp.saveSecurity(tvName.getContext(), idDeliveryman, name);
                        player.start();
                    }
                }
            });
            admin.show();

            return false;
        }

        @Override
        public void onClick(View view) {
            final MediaPlayer player = MediaPlayer.create(tvName.getContext(), R.raw.alarm);
            if(!checkSecurity()){
                StringBuilder sb = new StringBuilder();
                sb.append("EMAIL: ");
                sb.append(email);
                sb.append("\nCel: ");
                sb.append(cel);

                final DialogInformative inf = new DialogInformative(tvName.getContext());
                inf.setTitle(tvName.getText().toString());
                inf.setMessage(sb.toString());
                inf.setCancelableTouchOutside(true);
                inf.setCancelable(true);

                final AdminAccess admin = new AdminAccess(tvName.getContext());
                admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(CustomApplication.getPassAdmin().equals(admin.getPass())){
                            admin.dismiss();
                            inf.show();
                            player.stop();
                        } else {
                            Toast.makeText(tvName.getContext(), "Senha Inválida", Toast.LENGTH_SHORT).show();
                            CacheApp.saveSecurity(tvName.getContext(), idDeliveryman, name);
                            player.start();
                        }
                    }
                });
                admin.show();

            } else {
                StringBuilder sb = new StringBuilder();

                Map<Integer, String> breakSecurity = CacheApp.getSecurity(tvName.getContext());
                for(String name : breakSecurity.values()) {
                    sb.append("Entregador: ");
                    sb.append(name);
                    sb.append("\n");
                }

                final DialogInformative inf = new DialogInformative(tvName.getContext());
                inf.setTitle("Acessos Não Autorizado");
                inf.setMessage(sb.toString());
                inf.setCancelableTouchOutside(true);
                inf.setCancelable(true);
                inf.addButton("Limpar Não Autorizados", Color.WHITE,
                        tvName.getContext().getResources().getDrawable(R.drawable.rounded_button_confirm),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                final AdminAccess admin = new AdminAccess(tvName.getContext());
                                admin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(CustomApplication.getPassAdmin().equals(admin.getPass())){
                                            admin.dismiss();
                                            inf.dismiss();

                                            CacheApp.eraseSecurity(tvName.getContext());
                                            player.stop();
                                        } else {
                                            Toast.makeText(tvName.getContext(), "Senha Inválida", Toast.LENGTH_SHORT).show();
                                            CacheApp.saveSecurity(tvName.getContext(), idDeliveryman, name);
                                            player.start();
                                        }
                                    }
                                });
                                admin.show();
                            }
                        });
                inf.setPadding(16*2, true);
                inf.show();
            }


        }
    }

    public class AdminAccess{

        private Context context ;
        private EditText etPass ;
        private DialogInformative dialog ;

        public AdminAccess(Context context) {
            this.context = context ;

            etPass = new EditText(context);
            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            dialog = new DialogInformative(context);
            dialog.setTitle("Administrador");
            dialog.setMessage("Senha para acessar informações do entregador");
            dialog.setCustomView(etPass);
            dialog.setPadding(16*2, true);
            dialog.setCancelableTouchOutside(false);
            dialog.setCancelable(false);

        }

        public void setOnClickListener(View.OnClickListener onClickListener){
            dialog.addButton("Confirmar", Color.WHITE,
                    context.getResources().getDrawable(R.drawable.rounded_button_confirm),
                    onClickListener);
        }

        public String getPass(){
            return etPass.getText().toString();
        }

        public void dismiss(){
            dialog.dismiss();
        }

        public void show(){
            dialog.show();
        }
    }
}
