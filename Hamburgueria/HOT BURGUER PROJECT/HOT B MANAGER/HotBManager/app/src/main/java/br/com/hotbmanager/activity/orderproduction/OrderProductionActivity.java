package br.com.hotbmanager.activity.orderproduction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.orderproduction.listeners.ProductionListener;
import br.com.hotbmanager.adapters.AdapterNeoOrders;
import br.com.hotbmanager.adapters.AdapterProduction;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;
import br.com.hotbmanager.itens.HotGroupOrders;
import br.com.hotbmanager.itens.HotOrderManager;
import br.com.hotbmanager.utils.CacheApp;
import br.com.hotbmanager.utils.CustomItemDecor;
import br.com.hotbmanager.utils.EndPointMapping;

public class OrderProductionActivity extends AppCompatActivity implements ProductionInterface {

    private RecyclerView rcList;
    private Button btnUpdateStatus;

    private AdapterProduction adp ;
    private ArrayList<HotOrderManager> orders;
    private RelativeLayout progress;
    private TextView tvMessage;
    private View locked ;

    public static final int RESULT_ACTIVITY = 145 ;
    public static final int REQUEST_ACTIVITY = 144 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_production);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        rcList = findViewById(R.id.rc_order_production);
        btnUpdateStatus = findViewById(R.id.btn_update_status);
        progress = findViewById(R.id.fl_progress);
        tvMessage = findViewById(R.id.tv_message_order_production);
        locked = findViewById(R.id.blocked_order_production);

        init();
    }

    private final void init() {
        orders = (ArrayList) getIntent().getExtras().get("neo_orders");

        adp = new AdapterProduction(this);
        adp.addAllOrders(orders);

        rcList.addItemDecoration(new CustomItemDecor(8));
        rcList.setAdapter(adp);
        rcList.setEnabled(false);

        btnUpdateStatus.setOnLongClickListener(new ProductionListener(this,btnUpdateStatus));
    }

    @Override
    public void showLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void enableList(boolean enable) {
        rcList.setEnabled(enable);
        locked.setVisibility(enable?View.GONE:View.VISIBLE);
    }

    @Override
    public ArrayList<HotOrderManager> getOrders() {
        return orders;
    }

    @Override
    public void showMessage(String message, boolean show) {
        tvMessage.setText(message);
        tvMessage.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}