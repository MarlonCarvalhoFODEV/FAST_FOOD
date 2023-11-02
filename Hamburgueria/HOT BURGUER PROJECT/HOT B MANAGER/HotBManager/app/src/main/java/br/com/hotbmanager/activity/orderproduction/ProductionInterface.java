package br.com.hotbmanager.activity.orderproduction;

import java.util.ArrayList;

import br.com.hotbmanager.itens.HotGroupOrders;
import br.com.hotbmanager.itens.HotOrderManager;

public interface ProductionInterface {

    public void showMessage(String message, boolean show);

    public void showLoading(boolean show);

    public void enableList(boolean enable);

    public ArrayList<HotOrderManager> getOrders();
}
