package br.com.hotbdelivery.activitys.a3_dashboard;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onesignal.OneSignal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import apis.encrypt.AES;
import apis.internet.InternetRequisition;
import apis.location.APILocation;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.activitys.a1_splash.SplashActivity;
import br.com.hotbdelivery.activitys.a3_dashboard.customsnack.CustomSnackbarDeliveryFinalized;
import br.com.hotbdelivery.activitys.a3_dashboard.customsnack.CustomSnackbarDeliveryOption;
import br.com.hotbdelivery.activitys.a3_dashboard.interfaces.IDashboard;
import br.com.hotbdelivery.activitys.a3_dashboard.listeners.SwithCheckListener;
import br.com.hotbdelivery.activitys.a4_running.MapActivity;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.aplication.CustomNotificationReceiverHandler;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.itens.HotOrdersDelivery;
import br.com.hotbdelivery.itens.HotRunning;
import br.com.hotbdelivery.utils.CacheApp;
import br.com.hotbdelivery.utils.EndPointMapping;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.navigation.Navigation.findNavController;
import static br.com.hotbdelivery.activitys.a3_dashboard.customsnack.CustomSnackbarDeliveryOption.REQUEST_CODE_ACCEPT_RUNNING;

public class DashboardActivity extends AppCompatActivity implements IDashboard,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar ;
    private CircleImageView civPhoto;
    private TextView tvName;
    private Switch aSwitch ;

    private BottomNavigationView navBottom;
    private NavController navController;

    private APILocation apiLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.tb_dashboard);
        setSupportActionBar(toolbar);

        civPhoto = findViewById(R.id.civ_photo_deliveryman);
        tvName = findViewById(R.id.tv_name_deliveryman);

        aSwitch = findViewById(R.id.sw_enable_operation);

        navBottom = findViewById(R.id.nav_bottom);
        navController = findNavController(findViewById(R.id.fragment));

        NavigationUI.setupWithNavController(navBottom, navController);

        init();
    }

    private final void init() {
        setDataDeliveryMan();

        OneSignal.getCurrentOrNewInitBuilder()
                .setNotificationReceivedHandler(new CustomNotificationReceiverHandler(this));
        HotDeliveryMan account = CustomApplication.getAccount();

        apiLocation = new APILocation(this);
        if (!apiLocation.isEnabledGPS())
            apiLocation.startActivityEnabledGPS();
        else
            apiLocation.askPermission();

        aSwitch.setChecked(account.isStatusOperation());
        aSwitch.setText(account.isStatusOperation()?"Online":"Offline");
        aSwitch.setOnCheckedChangeListener(new SwithCheckListener(this));

        JSONObject data = CustomApplication.getDataNotification();
        if(data != null){
            notifyNewRunnig(data);
            CustomApplication.setDataNotification(null);
        }

        HotRunning running = CacheApp.getRunning(this);
        if(running != null && running.getDeliverys() != null && running.getDeliverys().size() > 0){
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("data_running", running);
            startActivityForResult(intent, REQUEST_CODE_ACCEPT_RUNNING);
        }
    }

    private final void setDataDeliveryMan(){
        HotDeliveryMan account = CustomApplication.getAccount();

        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.domain_connection));
        sb.append(EndPointMapping.DeliveryMan.CONTROLLER);
        sb.append(EndPointMapping.DeliveryMan.Functions.GET_IMAGE_PROFILE);
        sb.append("/");
        sb.append(account.getId());

        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("Authorization", generateAuthorization());

        GlideUrl glideUrl = new GlideUrl(sb.toString(),builder.build());

        Glide.with(this)
                .asBitmap()
                .load(glideUrl)
                .placeholder(R.drawable.ic_img_fail)
                .error(R.drawable.ic_img_fail)
                .timeout(6000)
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(civPhoto);

        tvName.setText(account.getName());
    }

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", getString(R.string.domain_connection_user), getString(R.string.domain_connection_authorization));
        return "Basic " + Base64Utils.encode(creds.getBytes());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        apiLocation.setOnRequestPermissionResult(requestCode, permissions, grantResults);

        if (requestCode == apiLocation.REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                apiLocation.askPermission();
            }
        }
        else  if (requestCode == apiLocation.REQUEST_CODE_SOURCE_SETTINGS) {
            if (!apiLocation.isEnabledGPS())
                apiLocation.startActivityEnabledGPS();
            else
                apiLocation.askPermission();
        }
    }

    @Override
    public void notifyNewRunnig(JSONObject data) {
        CoordinatorLayout
                clPositionSnack = findViewById(R.id.cl_position_snackbar);
        clPositionSnack.bringToFront();

        CustomSnackbarDeliveryOption newRunning = new CustomSnackbarDeliveryOption(this, clPositionSnack , data);
        newRunning.showSnackbar();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.FMT_Home: {
                Toast.makeText(this, "HOME", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.FMT_HistoricDelivery: {
                Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.FMT_Profile: {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ACCEPT_RUNNING && resultCode == MapActivity.RESULT_DELIVERY){
            CustomSnackbarDeliveryFinalized newRunning = new CustomSnackbarDeliveryFinalized(this, getWindow().getDecorView() , data);
            newRunning.showSnackbar();
        }
    }
}