package com.hotburguer.activitys.a5_home;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hotburguer.R;
import com.hotburguer.activitys.a6_trackorder.TrackOrderActivity;
import com.hotburguer.acustom.Cart;
import com.hotburguer.acustom.CartShopping;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.acustom.HotConfigurations;
import com.hotburguer.fragments.FMT_ProductsList;
import com.hotburguer.fragments.FMT_ServerNotFound;
import com.hotburguer.fragments.address.FMT_Address;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.fragments.payment.FMT_Payment;
import com.hotburguer.fragments.payment.Payment;
import com.hotburguer.fragments.payment.listeners.TrackOrderListener;
import com.hotburguer.itens.HotOrder;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.utils.CacheApp;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;

import java.util.ArrayList;

import static com.hotburguer.acustom.CustomApplication.getOrder;
import static com.hotburguer.utils.Utils.PaymentResult.decodeBackgroundColor;
import static com.hotburguer.utils.Utils.PaymentResult.decodeBackgroundDrawable;
import static com.hotburguer.utils.Utils.PaymentResult.decodeSubTitle;
import static com.hotburguer.utils.Utils.PaymentResult.decodeTitle;

public class HomeBurguerActivity extends AppCompatActivity implements HomeBurguerInterface, View.OnClickListener {

    private CartShopping cartShopping;

    private String[] descriptionData = {"Lanches", "Bebidas", "Endereço", "Finalizar"};
    private StateProgressBar stateProgressBar;

    private RelativeLayout progress;
    private FrameLayout fragmentSuport;
    private RelativeLayout paymentResult;

    private ImageView ivIconNext;
    private TextView tvFeeDelivery;
    private Button btnNextStep, btnBackStep;

    private int stepPosition = 0;
    private ArrayList<Fragment> steps;

    private boolean doubleBackToExitPressedOnce = false;

    private static boolean ONLY_DRINKS = false;
    public static boolean VISIBLE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Fresco.initialize(this);
            setContentView(R.layout.activity_home_burguer);
            Toolbar toolbar = findViewById(R.id.tb_home_burguer);
            setSupportActionBar(toolbar);

            CustomApplication.hideSystemUI(this);

            tvFeeDelivery = findViewById(R.id.tv_fee_delivery);

            stateProgressBar = findViewById(R.id.state_progress);
            stateProgressBar.setStateDescriptionData(descriptionData);
            stateProgressBar.enableAnimationToCurrentState(true);
            stateProgressBar.setOnStateItemClickListener((stateProgressBar, stateItem, stateNumber, isCurrentState) -> {
                if (cartShopping.getCart().size() > 0) {
                    stateProgressBar.setCurrentStateNumber(convertPositionToState(stateNumber - 1));
                    loadFragment(steps.get(stateNumber - 1));
                } else {
                    Toast.makeText(HomeBurguerActivity.this, "Seu Carrinho Está Vazio", Toast.LENGTH_SHORT).show();
                }
            });

            progress = findViewById(R.id.rl_base_progress);
            paymentResult = findViewById(R.id.rl_base_result_payment);
            fragmentSuport = findViewById(R.id.rl_fragments);

            steps = new ArrayList<>();
            steps.add(new FMT_ProductsList("h_request", HotProduct.Categories.SNACKS));
            steps.add(new FMT_ProductsList("d_request", HotProduct.Categories.DRINKS));
            steps.add(new FMT_Address(this));
            steps.add(new FMT_Payment(this));

            ivIconNext = findViewById(R.id.iv_icon_next);

            btnNextStep = findViewById(R.id.btn_next_step);
            btnBackStep = findViewById(R.id.btn_back_step);

            if(CustomApplication.TEST_MODE){
                loading(false);
                steps.clear();
                showServerNotFound();
            }
            else {
                btnNextStep.setOnClickListener(this);
                btnBackStep.setOnClickListener(this);

                loadFragment(steps.get(stepPosition));
            }

            checkContainsOnlyDrinks();

            //init();
        }catch(Fragment.InstantiationException w){
            Intent intent = new Intent(this, HomeBurguerActivity.class);
            startActivity(intent);
            finish();
        }
    }

  /*  private final void init(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setSmoothScrollbarEnabled(true);

        adp = new AdapterProduct(this);
        rcProducts.setLayoutManager(llm);
        rcProducts.setHasFixedSize(true);
        rcProducts.setAdapter(adp);
    }*/

    private void showServerNotFound(){


        View v = LayoutInflater.from(this).inflate(R.layout.fmt_server_not_found,null);

        fragmentSuport.addView(v);
    }

    private void openTermsAndPolitics() {
        WebView webView = new WebView(this);
        webView.loadUrl("file:///android_asset/terms_and_politic.html");

        DialogInformative dialog = new DialogInformative(this);
        dialog.setTitle("Termos e\nPolitica de Privacidade");
        dialog.setMessage("");
        dialog.setCustomView(webView);
        dialog.addButton("OK", view -> dialog.dismiss());
        dialog.show();
    }

    private synchronized void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(fragmentSuport.getId(), fragment)
                .commit();
    }

    private StateProgressBar.StateNumber convertPositionToState(int position) {
        switch (position) {
            case 0:
                return StateProgressBar.StateNumber.ONE;
            case 1:
                return StateProgressBar.StateNumber.TWO;
            case 2:
                return StateProgressBar.StateNumber.THREE;
            case 3:
                return StateProgressBar.StateNumber.FOUR;
            case 4:
                return StateProgressBar.StateNumber.FIVE;
            default:
                return StateProgressBar.StateNumber.ONE;
        }
    }

    private void checkContainsOnlyDrinks() {
        HotOrder order = getOrder();
        Cart cart = order.getCart();

        Integer drinks = cart.getCountProductFromCategorie().get(HotProduct.Categories.DRINKS);
        Integer snacks = cart.getCountProductFromCategorie().get(HotProduct.Categories.SNACKS);
        HotConfigurations hotConfig = CustomApplication.getHotconfig();
        HotConfigurations.SystemConfig sysConfig = hotConfig!=null?hotConfig.getSystemConfig():null;

        if (cart != null) {
            if (hotConfig != null && sysConfig != null && sysConfig.getFeeDeliveryFree() &&
                    drinks != null && drinks > 0 && (snacks != null && snacks == 0 || snacks == null)
            || sysConfig != null && !sysConfig.getFeeDeliveryFree()) {

                double feeDelivery = CustomApplication.getHotconfig().getDeliveryMan().getFeeDelivery();
                order.setFeeDelivery(feeDelivery);

                tvFeeDelivery.setVisibility(View.VISIBLE);
                tvFeeDelivery.setText("+R$ " + order.getFeeDelivery() + "\nentrega");
                ONLY_DRINKS = true;
            }
            else {
                tvFeeDelivery.setVisibility(View.GONE);
                tvFeeDelivery.setText("+R$ " + order.getFeeDelivery() + "\nentrega");
                ONLY_DRINKS = false;
            }
        }
    }

    private final void nextStep() {
        if (cartShopping.getCart().size() > 0 || stepPosition + 1 <= 1 && cartShopping.getCart().size() == 0) {

            checkContainsOnlyDrinks();

            if (stepPosition == 2 && getOrder().getAddress().getStreet() == null) {
                Toast.makeText(this, "Endereço Obrigatório!!", Toast.LENGTH_LONG).show();
            } else {
                if (stepPosition < steps.size() - 1) {
                    loading(false);
                    stepPosition += 1;
                    stateProgressBar.setCurrentStateNumber(convertPositionToState(stepPosition));

                    loadFragment(steps.get(stepPosition));

                    if (stepPosition == steps.size() - 1) {
                        btnNextStep.setBackground(getResources().getDrawable(R.drawable.back_btn_payment));
                        ivIconNext.setImageResource(0);
                    } else {
                        btnNextStep.setBackground(getResources().getDrawable(R.drawable.back_btn_next));
                        ivIconNext.setImageResource(R.drawable.ic_next);
                    }
                } else if (stepPosition == steps.size() - 1) {
                    CustomApplication.hideSystemUI(this);
                    FMT_Payment fmtPayment = (FMT_Payment) steps.get(steps.size() - 1);
                    fmtPayment.checkIsDeliveryOrder();
                }
            }
        } else {
            Toast.makeText(this, "Escolha um item primeiro!!", Toast.LENGTH_LONG).show();
        }
    }

    private final void backStep() {
        if (stepPosition > 0) {
            loading(false);
            stepPosition -= 1;
            loadFragment(steps.get(stepPosition));
            stateProgressBar.setCurrentStateNumber(convertPositionToState(stepPosition));

            if (stepPosition == steps.size() - 2) {
                btnNextStep.setBackground(getResources().getDrawable(R.drawable.back_btn_next));
                ivIconNext.setImageResource(R.drawable.ic_next);
            }
        } else {
            closeApp();
        }
    }

    private final void closeApp() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Aperte novamente para sair!", Toast.LENGTH_SHORT).show();

        CustomApplication.hideSystemUI(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart_shopping, menu);

        MenuItem itemCart = menu.findItem(R.id.action_cart);
        FrameLayout rlCart = (FrameLayout) itemCart.getActionView();
        TextView counter = rlCart.findViewById(R.id.tv_counter_product_inside_store);
        ImageView ivIcon = rlCart.findViewById(R.id.iv_menu_icon_cart_inside_store);
        TextView tvTotalValue = findViewById(R.id.tv_total_value);

        cartShopping = new CartShopping(this, rlCart, ivIcon, counter, tvTotalValue);
        CustomApplication.setCartShopping(cartShopping);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int menuID = item.getItemId();

        switch (menuID){
            case R.id.action_cart:{
                Toast.makeText(this, "Meu Carrinho", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_more:{
                openTermsAndPolitics();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        VISIBLE = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomApplication.hideSystemUI(this);
    }

    @Override
    protected void onDestroy() {
        VISIBLE = false;
        super.onDestroy();
    }


    private final void showPaymentResult(Payment payment) {
        btnNextStep.setEnabled(true);

        paymentResult.setVisibility(View.VISIBLE);

        ImageView icon = paymentResult.findViewById(R.id.iv_icon_pay_result);
        TextView title = paymentResult.findViewById(R.id.tv_title_pay_result);
        TextView description = paymentResult.findViewById(R.id.tv_description_pay_result);

        Button trackOrder = paymentResult.findViewById(R.id.btn_track_order_pay_result);
        if (payment.getStatus() == Payment.PAID || payment.getStatus() == Payment.PROCESSING
                || payment.getStatus() == Payment.AUTHORIZED) {
            trackOrder.setEnabled(true);
            trackOrder.setOnClickListener(onclick -> {
                Intent intent = new Intent(this, TrackOrderActivity.class);
                startActivity(intent);
                paymentResult.setVisibility(View.GONE);

            });
        }else
            trackOrder.setEnabled(false);


        Button btnClose = paymentResult.findViewById(R.id.btn_close_pay_result);
        btnClose.setOnClickListener(onclick -> { paymentResult.setVisibility(View.GONE); });

        if (payment != null) {

            int color = 0;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                color = getResources().getColor(decodeBackgroundColor(payment.getStatus()), null);
            } else {
                color = getResources().getColor(decodeBackgroundColor(payment.getStatus()));
            }

            if (!payment.isBlocked()) {

                icon.setImageResource(decodeBackgroundDrawable(payment.getStatus()));
                title.setText(decodeTitle(payment.getStatus()));
                description.setText(decodeSubTitle(payment.getStatus()));
                paymentResult.setBackgroundColor(color);
            } else {
                icon.setImageResource(decodeBackgroundDrawable(payment.getStatus()));
                title.setText(decodeTitle(payment.getStatus()));
                description.setText(decodeSubTitle(payment.getStatus()));
                paymentResult.setBackgroundColor(color);
                trackOrder.setOnClickListener(new TrackOrderListener());

                icon.setImageResource(decodeBackgroundDrawable(payment.getStatus()));
                title.setText(payment.getMsgAlert());
                description.setText(payment.getMsgComplement());
                paymentResult.setBackgroundColor(color);
                trackOrder.setVisibility(View.GONE);
            }


        } else {
            title.setText("PAGAMENTO FALHOU!!");
            description.setText("Ocorreu erro inesperado");

            int color = 0;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                color = getResources().getColor(decodeBackgroundColor(Payment.REFUSED), null);
            } else {
                color = getResources().getColor(decodeBackgroundColor(Payment.REFUSED));
            }
            icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_buy_rejected));
            paymentResult.setBackgroundColor(color);
            trackOrder.setVisibility(View.GONE);
            //checkoutResultViews.getBtnCheckOrderResult().setOnClickListener(new ListenerButton(payment));
        }
    }

    @Override
    public void loading(boolean loading) {
        runOnUiThread(() -> {
            if (loading)
                progress.setVisibility(View.VISIBLE);
            else
                progress.setVisibility(View.GONE);
        });
    }

    @Override
    public void updateCartView() {
        cartShopping.updateCart();
    }

    @Override
    public void resultPayment(Payment payment) {
        showPaymentResult(payment);
    }

    @Override
    public void disableNextButton(boolean disable) {
        btnNextStep.setEnabled(!disable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_step: {
                nextStep();
                break;
            }
            case R.id.btn_back_step: {
                backStep();
                break;
            }
        }
    }
}