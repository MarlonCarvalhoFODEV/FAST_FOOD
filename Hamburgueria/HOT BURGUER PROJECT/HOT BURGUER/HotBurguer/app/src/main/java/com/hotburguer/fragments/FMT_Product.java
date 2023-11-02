package com.hotburguer.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerFactory;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerInterface;
import com.hotburguer.acustom.Cart;
import com.hotburguer.acustom.CartShopping;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.adapters.AdapterFeedbacks;
import com.hotburguer.adapters.AdapterViewPager;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FMT_Product extends Fragment implements Interceptor {

    private Controller controller ;
    private HotProduct product;

    public FMT_Product(HotProduct product) {
        this.product = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CustomApplication.hideSystemUI(getActivity());

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(this);
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(
                getContext(),
                okHttpClientBuilder.build())
                .build();

        Fresco.initialize(getContext(),imagePipelineConfig);

        View view = inflater.inflate(R.layout.fmt_product_perform, null);

        controller = new Controller(product, view);

        return view;
    }

    public static FMT_Product newInstance(HotProduct hotProduct) {
        FMT_Product myFragment = new FMT_Product(hotProduct);

        Bundle args = new Bundle();
        args.putSerializable("product", hotProduct);
        myFragment.setArguments(args);

        return myFragment;
    }

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", getContext().getString(R.string.domain_connection_user),
                getContext().getString(R.string.domain_connection_authorization));
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

    public class Controller implements View.OnClickListener {

        private ControllerListener controllerListener ;
        private SimpleDraweeView draweeView ;
        private Cart cart;
        //private ImageView img;
        private final Button btnAnyless, btnMore, btnFeedBack;
        private final ToggleButton btnLike;
        private TextView tvCounter, tvTitle, tvPriceFirst, tvPriceDecimal, tvFeeDelivery, tvEngagements;
        //private RelativeLayout progress ;

        private int counter;

        public Controller(HotProduct product, View v) {

            cart = CustomApplication.getOrder().getCart();

            //img = v.findViewById(R.id.iv_img_product);
            draweeView = v.findViewById(R.id.sdvImage);

            btnAnyless = v.findViewById(R.id.btn_anyless_product);
            tvCounter = v.findViewById(R.id.tv_count_product);
            tvTitle = v.findViewById(R.id.tv_title_product);
            tvPriceFirst = v.findViewById(R.id.tv_firs_number_price_product);
            tvPriceDecimal = v.findViewById(R.id.tv_decimal_price_product);
            btnMore = v.findViewById(R.id.btn_more_product);
            btnLike = v.findViewById(R.id.btn_like_product);
            btnFeedBack = v.findViewById(R.id.btn_feedback_product);
            //progress = v.findViewById(R.id.rl_progress_product);
            tvFeeDelivery = v.findViewById(R.id.tv_fee_delivery);

            if(product != null){
                //setImageFromURL(img);
                setImage();
                counter = product.getCount();
                tvTitle.setText(product.getTitle().toUpperCase());
                tvPriceFirst.setText(getFirstPrice(product));
                tvPriceDecimal.setText(getDecimalPrice(product));
                tvCounter.setText(product.getCount()+"un");

                btnAnyless.setOnClickListener(this);
                btnMore.setOnClickListener(this);
                btnFeedBack.setOnClickListener(this);
                btnLike.setOnClickListener(this);
            }
            else {
                counter=product.getCount();
                tvTitle.setText("**********");
                tvPriceFirst.setText("**");
                tvPriceDecimal.setText(",**");
                tvCounter.setText(product.getCount()+"un");

            }
        }

        private final String generateAuthorization() {
            String creds = String.format("%s:%s", getString(R.string.domain_connection_user), getString(R.string.domain_connection_authorization));
            return "Basic " + Base64Utils.encode(creds.getBytes());
        }

        private void setImage(){
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.domain_connection));
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

        public synchronized void addProduct() {
            if (counter < 10) {
                counter += 1;

                product.setCount(counter);
                product.setSelected(true);

                tvCounter.setText(counter + "un");

                cart.addItem(product);
                CustomApplication.getCartShopping().updateCart();
            }
        }

        public void removeProduct() {

            if (counter >= 1) {
                counter -= 1;
                product.setCount(counter);
                product.setSelected(true);

                tvCounter.setText(product.getCount()+"un");

                cart.removeItem(product, false);
                CustomApplication.getCartShopping().updateCart();

                if (counter == 1) {
                    product.setSelected(false);
                    //product.setVisibility(View.GONE);
                }
            }
        }

        private String getFirstPrice(HotProduct p) {
            String price = String.valueOf(p.getPerPrice());
            return price.substring(0, price.indexOf(".")).replace(".", ",");
        }

        private String getDecimalPrice(HotProduct p) {
            String price = String.valueOf(p.getPerPrice());
            return price.substring(price.indexOf(".")).replace(".", ",");
        }

        private final void likedProduct(){
            HotUser user = CustomApplication.getUser();

            if (user != null) {

                try {
                    JSONObject send = new JSONObject();
                    send.put("id", user.getId());
                    send.put("id_dev", user.getDeviceID());
                    send.put("id_prod", product.getId());
                    send.put("status", btnLike.isChecked());

                    InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                    requestServer.setIdRequisition("likedProduct()");
                    requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                    requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                    requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                    requestServer.setDomain(getResources().getString(R.string.domain_connection));
                    requestServer.setController(WebController.Products.CONTROLLER);
                    requestServer.setFunction(WebController.Products.Functions.LIKED_PRODUCT);
                    requestServer.setObjectTransport(send);
                    requestServer.request(getContext(), new InternetRequisitionResponse() {
                        @Override
                        public void onLoading(boolean loading) {
                            if (getContext() != null)
                                ((HomeBurguerInterface) getContext()).loading(loading);
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

            if (user != null) {

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
                    requestServer.setDomain(getResources().getString(R.string.domain_connection));
                    requestServer.setController(WebController.Products.CONTROLLER);
                    requestServer.setFunction(WebController.Products.Functions.GET_FEEDBACK_PRODUCT);
                    requestServer.setObjectTransport(send);
                    requestServer.request(getContext(), new InternetRequisitionResponse() {
                        @Override
                        public void onLoading(boolean loading) {
                            if (getContext() != null)
                                progressBar.setVisibility(loading?View.VISIBLE:View.GONE);
                        }

                        @Override
                        public void onSucess(Object object) {
                            if (object != null) {

                                JsonArray jArray = new Gson().fromJson(object.toString(), JsonArray.class);

                                Gson gson = new Gson();
                                for (JsonElement e : jArray)
                                    adp.add(gson.fromJson(e, JSONObject.class));
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

            AdapterFeedbacks adp = new AdapterFeedbacks(getContext());

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            llm.setSmoothScrollbarEnabled(true);

            View view = LayoutInflater.from(getContext()).inflate(R.layout.dg_feedbacks, null);
            ProgressBar progress = view.findViewById(R.id.pb_loading_feedbacks);
            RecyclerView rcFeedbacks = view.findViewById(R.id.rc_feedbacks_product);
            rcFeedbacks.setAdapter(adp);
            rcFeedbacks.setHasFixedSize(true);
            rcFeedbacks.setLayoutManager(llm);

            requestFeedbacks(adp, progress);

            DialogInformative dialog = new DialogInformative(getContext());
            dialog.setTitle("Comentários");
            dialog.setMessage(null);
            dialog.setCustomView(view);
            dialog.setCancelable(true);
            dialog.setCancelableTouchOutside(true);
            dialog.show();
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
            }
        }
    }
}
