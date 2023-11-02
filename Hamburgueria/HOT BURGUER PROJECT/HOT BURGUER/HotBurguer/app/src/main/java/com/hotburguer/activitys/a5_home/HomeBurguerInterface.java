package com.hotburguer.activitys.a5_home;

import com.hotburguer.fragments.payment.Payment;

public interface HomeBurguerInterface {

    public void loading(boolean loading);

    public void updateCartView();

    public void resultPayment(Payment payment);

    public void disableNextButton(boolean disable);

}
