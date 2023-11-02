package br.com.hotbdelivery.fragments.dashboard.profile.listeners;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import apis.userbuyer.BAccountBank;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.utils.Utils;

public class BankTypeListener implements Spinner.OnItemSelectedListener {

    private final HotDeliveryMan account ;

    public BankTypeListener() {
        this.account = CustomApplication.getAccount();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String typeBank = (String) adapterView.getSelectedItem();
        switch (typeBank) {
            case "Corrente": {
                account.getBankAccount().setType(BAccountBank.Type.CURRENT);
                break;
            }
            case "Corrente Conjunta": {
                account.getBankAccount().setType(BAccountBank.Type.CURRENT_JOINT);
                break;
            }
            case "Poupança": {
                account.getBankAccount().setType(BAccountBank.Type.SAVINGS);
                break;
            }
            case "Poupança Conjunta": {
                account.getBankAccount().setType(BAccountBank.Type.SAVINGS_JOINT);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
