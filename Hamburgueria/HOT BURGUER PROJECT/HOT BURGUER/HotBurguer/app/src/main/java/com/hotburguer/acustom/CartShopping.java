package com.hotburguer.acustom;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotburguer.acustom.listener.OpenCartShoppingListener;
import com.hotburguer.itens.HotProduct;

public class CartShopping implements CartController, View.OnClickListener {

    private Context context ;
    private FrameLayout fmBase ;
    private ImageView ivIcon ;
    private TextView tvCounterProducts, tvSubtotal ;

    public CartShopping(Context c){
        context = c ;
    }

    public CartShopping(Context contextExt, FrameLayout base, ImageView icon, TextView counter, TextView subtotal){
        context = contextExt ;
        fmBase = base ;
        ivIcon = icon ;
        tvCounterProducts = counter ;
        tvSubtotal = subtotal ;

        fmBase.setOnClickListener(this);
    }

    public void setFmBase(FrameLayout fmBase) {
        this.fmBase = fmBase;
        this.fmBase.setOnClickListener(new OpenCartShoppingListener(context));
    }

    public void setIvIcon(ImageView ivIcon) {
        this.ivIcon = ivIcon;
    }

    public void setTvCounterProducts(TextView tvCounterProducts) {
        this.tvCounterProducts = tvCounterProducts;
    }

    public void setTvSubtotal(TextView tvSubtotal) {
        this.tvSubtotal = tvSubtotal;
    }

    public void setIconColor(int color) {
        if(ivIcon!=null) {
            ivIcon.setColorFilter(color);
            ivIcon.requestLayout();
        }
    }

    private String formatPrice(String price) {
        return "R$ " + price.replace(".", ",");
    }

    @Override
    public Cart getCart() {
        return CustomApplication.getOrder().getCart();
    }

    @Override
    public void addItem(HotProduct item) {
        CustomApplication.getOrder().getCart().addItem(item);
        updateCart();
    }

    @Override
    public void removeItem(HotProduct item, boolean directClick) {
        if(getCart().getSize() > 0)
            getCart().removeItem(item, directClick);
        updateCart();
    }

    @Override
    public void clearCart() {
        getCart().clearCart();
    }

    @Override
    public void updateCart() {
        if(tvCounterProducts!=null)
            tvCounterProducts.setText("" + getCart().getSize());

        if(tvSubtotal!=null)
            tvSubtotal.setText(formatPrice(String.valueOf(getCart().getPurchaseValue())));

    }

    @Override
    public void onClick(View view) {

    }
}
