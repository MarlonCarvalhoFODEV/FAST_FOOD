package com.hotburguer.acustom.listener;

import android.content.Context;
import android.view.View;

import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.itens.HotOrder;

public class OpenCartShoppingListener implements View.OnClickListener {

    private Context context ;
    public static final int REQUEST_CODE = 100;

    public OpenCartShoppingListener(Context contextExt){
        context = contextExt ;
    }

    @Override
    public void onClick(View view) {
        openCart(context);
    }

    private HotOrder getOrder(){
        return CustomApplication.getOrder();
    }

    private void openCart(Context context) {
        /*ITOrder cart = getCart(context);
        Activity activity = ((Activity)context);

        if (((CustomApplication) context.getApplicationContext()).isUserLogged() && cart.getTamanhoDoCarrinho() > 0) {
            Intent intent = new Intent(context, CarrinhoComprasActivity.class);
            activity.startActivityForResult(intent, REQUEST_CODE);
        } else if (!((CustomApplication) context.getApplicationContext()).isUserLogged()) {
            CustomSnackbar snackbar = new CustomSnackbar(context, ((RelativeLayout) activity.findViewById(R.id.rl_base_store_inside)), "InsideStoreAcitivity");
            snackbar.showSnackbar();
        } else if (cart.getTamanhoDoCarrinho() == 0) {
            Toast.makeText(context, "Seu carrinho está vazio!", Toast.LENGTH_SHORT).show();
        }*/
    }
}
