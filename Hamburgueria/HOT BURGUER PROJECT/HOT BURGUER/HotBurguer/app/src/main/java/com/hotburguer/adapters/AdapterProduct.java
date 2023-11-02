package com.hotburguer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerInterface;
import com.hotburguer.acustom.Cart;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.DraweeListener;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;
import com.hotburguer.utils.CacheApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.internet.WebJson;
import apis.share.Share;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.Holder> implements Interceptor {

    private Context context ;
    private ArrayList<HotProduct> itens ;
    private final FrameLayout.LayoutParams params ;

    private Cart cart;
    private Cart savedCart ;

    private PipelineDraweeControllerBuilder pipeline;

    public AdapterProduct(Context context) {
        this.context = context;
        this.itens = new ArrayList<>();
        this.cart = CustomApplication.getOrder().getCart();

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point(); display.getSize(size);

        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, size.y-(35*4+(55*2)+(37*2)));

        savedCart = CacheApp.getCart(context);
        pipeline = Fresco.newDraweeControllerBuilder();
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(this);

        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(
                context,
                okHttpClientBuilder.build())
                .build();

        Fresco.initialize(context,imagePipelineConfig);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.it_product_perform, null);
        View base = view.findViewById(R.id.base_it_product);
        base.setLayoutParams(params);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindValues(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void clear(){
        itens.clear();
        notifyDataSetChanged();
    }

    public synchronized void addProduct(HotProduct product){
        HotProduct sP = savedCart.get(product.getId());

        if(sP != null) {
            product.setCount(sP.getCount());
        }

        itens.add(product);
        notifyItemInserted(itens.size()-1);
    }

    public HotProduct getProductFromID(int sharedProductID) {
        for(HotProduct p : itens)
            if(p.getId() == sharedProductID)
                return p;
        return null;
    }

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", context.getString(R.string.domain_connection_user),
                context.getString(R.string.domain_connection_authorization));
        return "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        newRequest = request.newBuilder()
                .addHeader("Authorization", generateAuthorization())
                //.addHeader(HeadersContract.HEADER_X_CLIENT_ID, CLIENT_ID)
                .build();
        return chain.proceed(newRequest);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle,tvCounter, tvFirst, tvDecimal, tvEngagements, tvFeeDelivery;
        private Button btnFeedbacks, btnAddProduct, btnRemoveProduct, btnShare ;
        private ToggleButton btnLike ;
        private SimpleDraweeView draweeView ;

        private int counter;

        public Holder(@NonNull View v) {
            super(v);
            draweeView = v.findViewById(R.id.sdvImage);

            btnRemoveProduct = v.findViewById(R.id.btn_anyless_product);
            tvCounter = v.findViewById(R.id.tv_count_product);
            tvTitle = v.findViewById(R.id.tv_title_product);
            tvFirst = v.findViewById(R.id.tv_firs_number_price_product);
            tvDecimal = v.findViewById(R.id.tv_decimal_price_product);
            btnAddProduct = v.findViewById(R.id.btn_more_product);
            btnLike = v.findViewById(R.id.btn_like_product);
            btnFeedbacks = v.findViewById(R.id.btn_feedback_product);
            btnShare = v.findViewById(R.id.btn_share_product);
            //progress = v.findViewById(R.id.rl_progress_product);
            tvFeeDelivery = v.findViewById(R.id.tv_fee_delivery);

            tvEngagements = v.findViewById(R.id.tv_engagement);

            btnRemoveProduct.setOnClickListener(this);
            btnAddProduct.setOnClickListener(this);
            btnFeedbacks.setOnClickListener(this);
            btnLike.setOnClickListener(this);
            btnShare.setOnClickListener(this);


        }

        public void bindValues(HotProduct product) {
            if(product != null){
                product.setPosition(getAdapterPosition());

                setImage();
                counter = product.getCount();

                tvTitle.setText(product.getTitle().toUpperCase());
                tvFirst.setText(getFirstPrice(product));
                tvDecimal.setText(getDecimalPrice(product));
                tvCounter.setText(product.getCount()+"un");

                StringBuilder sb = new StringBuilder();
                sb.append(product.getCountLikes());
                sb.append(" curtidas e ");
                sb.append(product.getCountFeedbacks());
                sb.append(" comentários");

                tvEngagements.setText(sb.toString());
                btnLike.setChecked(product.isLiked());

                if(savedCart.size() > 0 && product.getCount() > 0){
                    cart.addItem(product);
                    CustomApplication.getCartShopping().updateCart();
                }

            }
            else {
                counter=product.getCount();
                tvTitle.setText("**********");
                tvFirst.setText("**");
                tvDecimal.setText(",**");
                tvCounter.setText(product.getCount()+"un");
            }
        }

        private void setImage(){
            StringBuilder sb = new StringBuilder();
            sb.append(context.getString(R.string.domain_connection));
            sb.append(WebController.Products.CONTROLLER);
            sb.append(WebController.Products.Functions.GET_IMAGE);
            sb.append("/");
            sb.append(itens.get(getAdapterPosition()).getId());

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(sb.toString())
                    //.setControllerListener(listener)
                    .setTapToRetryEnabled(true)
                    .setOldController(draweeView.getController())
                    .build();

            draweeView.setController(controller);
        }

        private String getDecimalPrice(HotProduct p) {
            String price = String.valueOf(p.getPerPrice());
            return price.substring(price.indexOf(".")).replace(".", ",");
        }

        private String getFirstPrice(HotProduct p) {
            String price = String.valueOf(p.getPerPrice());
            return price.substring(0, price.indexOf(".")).replace(".", ",");
        }

        private final void likedProduct(){
            HotProduct prod = itens.get(getAdapterPosition());
            prod.setLiked(btnLike.isChecked());
            prod.setCountLikes(btnLike.isChecked()?prod.getCountLikes()+1:prod.getCountLikes()-1);

            StringBuilder sb = new StringBuilder();
            sb.append(prod.getCountLikes());
            sb.append(" curtidas e ");
            sb.append(prod.getCountFeedbacks());
            sb.append(" comentários");
            tvEngagements.setText(sb.toString());
            HotUser user = CustomApplication.getUser();

            if (user != null) {

                try {
                    JSONObject send = new JSONObject();
                    send.put("id", user.getId());
                    send.put("id_dev", user.getDeviceID());
                    send.put("id_prod", itens.get(getAdapterPosition()).getId());
                    send.put("status", btnLike.isChecked());

                    InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                    requestServer.setIdRequisition("likedProduct()");
                    requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                    requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                    requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                    requestServer.setDomain(context.getResources().getString(R.string.domain_connection));
                    requestServer.setController(WebController.Products.CONTROLLER);
                    requestServer.setFunction(WebController.Products.Functions.LIKED_PRODUCT);
                    requestServer.setObjectTransport(send);
                    requestServer.request(context, new InternetRequisitionResponse() {
                        @Override
                        public void onLoading(boolean loading) {
                        }

                        @Override
                        public void onSucess(Object object) {
                            if (object != null) {

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

        private final void requestFeedbacks(AdapterFeedbacks adp, ProgressBar progressBar){
            HotUser user = CustomApplication.getUser();
            HotProduct product = itens.get(getAdapterPosition());

            if (user != null && product.getCountFeedbacks() > 0) {

                try {
                    JSONObject send = new JSONObject();
                    send.put("id", user.getId());
                    send.put("id_dev", user.getDeviceID());
                    send.put("id_prod", product.getId());

                    InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                    requestServer.setIdRequisition("getFeedbacks()");
                    requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                    requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                    requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                    requestServer.setDomain(context.getResources().getString(R.string.domain_connection));
                    requestServer.setController(WebController.Products.CONTROLLER);
                    requestServer.setFunction(WebController.Products.Functions.GET_FEEDBACK_PRODUCT);
                    requestServer.setObjectTransport(send);
                    requestServer.request(context, new InternetRequisitionResponse() {
                        @Override
                        public void onLoading(boolean loading) {
                            if (context != null)
                                progressBar.setVisibility(loading?View.VISIBLE:View.GONE);
                        }

                        @Override
                        public void onSucess(Object object) {
                            if (object != null) {

                                JSONObject ob = null;
                                try {
                                    ob = new JSONObject(object.toString());
                                    JSONArray array = ob.getJSONArray("myArrayList");
                                    for (int i = 0; i < array.length(); i++) {
                                        adp.add(array.getJSONObject(i).getJSONObject("map"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

        private final void showFeedbacks(){

            if(itens.get(getAdapterPosition()).getCountFeedbacks() > 0){
                LinearLayoutManager llm = new LinearLayoutManager(context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                llm.setSmoothScrollbarEnabled(true);

                View view = LayoutInflater.from(context).inflate(R.layout.dg_feedbacks, null);

                AdapterFeedbacks adp = new AdapterFeedbacks(context);

                ProgressBar progress = view.findViewById(R.id.pb_loading_feedbacks);
                RecyclerView rcFeedbacks = view.findViewById(R.id.rc_feedbacks_product);
                rcFeedbacks.setAdapter(adp);
                rcFeedbacks.setHasFixedSize(true);
                rcFeedbacks.setLayoutManager(llm);

                requestFeedbacks(adp, progress);

                DialogInformative dialog = new DialogInformative(context);
                dialog.setTitle(null);
                dialog.setMessage(null);
                dialog.setCustomView(view);
                dialog.setCancelable(true);
                dialog.setCancelableTouchOutside(true);
                dialog.setPadding(16*2,true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        CustomApplication.hideSystemUI((Activity)context);
                    }
                });
                dialog.show();
            }
        }

        public synchronized void addProduct() {
            if (counter < 10) {
                counter += 1;

                HotProduct product = itens.get(getAdapterPosition());

                product.setCount(counter);
                product.setSelected(true);

                tvCounter.setText(counter + "un");

                cart.addItem(product);
                CacheApp.saveCart(context, cart);
                CustomApplication.getCartShopping().updateCart();

            }
        }

        public void removeProduct() {

            if (counter >= 1) {
                counter -= 1;

                HotProduct product = itens.get(getAdapterPosition());

                product.setCount(counter);
                product.setSelected(true);

                tvCounter.setText(product.getCount()+"un");

                cart.removeItem(product, false);
                CustomApplication.getCartShopping().updateCart();

                CacheApp.saveCart(context, cart);

                if (counter == 1) {
                    product.setSelected(false);
                    //product.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_anyless_product: {
                    removeProduct();
                    break;
                }
                case R.id.btn_more_product: {
                    addProduct();
                    break;
                }
                case R.id.btn_like_product: {
                    likedProduct();
                    break;
                }
                case R.id.btn_feedback_product: {
                    showFeedbacks();
                    break;
                }
                case R.id.btn_share_product: {

                    HotProduct pro = itens.get(getAdapterPosition());

                    int idProduct = pro.getId();
                    String title = pro.getTitle();
                    String descrition = pro.getDescription();

                    String link = "https://www.instagram.com/hotburguer.delivery?id="+idProduct;
                    String dynamicLinkDomain = "hotburguer.page.link";
                    Share.DynamicLinkShare.shareProduct(
                            (Activity)context,
                            link,
                            dynamicLinkDomain,
                            idProduct,
                            title,
                            descrition,
                            generateCopy(pro.getCategorie(), pro.isCombo()));
                    break;
                }
            }
        }
        private String generateCopy(String categorie, boolean isCombo){
            if(categorie.equals(HotProduct.Categories.SNACKS) && isCombo){

                StringBuilder copy = new StringBuilder();
                copy.append("AÍ!! Você que gosta de fast food, se liga só nesse combo!!");
                copy.append("\nTá praticamente de graça e um monte de gente ta comprando");
                copy.append("\nSe tu tava pensando em lanche HÁA, ta aí uma boa opção");
                copy.append("\n#ficaligado tem muita promoção nesse app.");

                return copy.toString();
            } else if(categorie.equals(HotProduct.Categories.SNACKS) && !isCombo){
                StringBuilder copy = new StringBuilder();
                copy.append("AÍ!! Você que gosta de fast food, se liga só!");
                copy.append("\nTá praticamente de graça, tem muita gente comprando");
                copy.append("\nSe tu tava pensando em lanche HÁA, ta aí o salvador da pátria KK ");
                copy.append("\n#ficaligado tem muita promoção nesse app.");

                return copy.toString();
            }
            else if(categorie.equals(HotProduct.Categories.DRINKS)){
                StringBuilder copy = new StringBuilder();
                copy.append("HII! Geladim\uD83E\uDD17 geladim\uD83E\uDD17 pra acompanhar o lanche ");
                copy.append("\n\uD83E\uDD14pensa muito não, já ouviu falar? Quem pensa de mais, nada faz?");
                copy.append("\nSem duvida você merece, trabalhou o mês todo né não? ");
                copy.append("\nHáaa\uD83D\uDE06 pega logo a geladinha, fica timido não \uD83D\uDE09");

                return copy.toString();
            }

            return null ;
        }


    }
}
