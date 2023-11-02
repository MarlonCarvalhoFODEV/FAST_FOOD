package com.hotburguer.acustom;

import com.hotburguer.itens.HotProduct;

public interface CartController {

    public Cart getCart();

    public void addItem(HotProduct item);

    public void removeItem(HotProduct item, boolean directClick);

    public void clearCart();

    public void updateCart();
}
