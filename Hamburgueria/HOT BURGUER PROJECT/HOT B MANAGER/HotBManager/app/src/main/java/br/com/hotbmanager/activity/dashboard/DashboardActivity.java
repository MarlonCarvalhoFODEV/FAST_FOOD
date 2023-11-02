package br.com.hotbmanager.activity.dashboard;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import android.view.View;

import br.com.hotbmanager.R;
import br.com.hotbmanager.activity.orderproduction.OrderProductionActivity;
import br.com.hotbmanager.utils.CustomItemDecor;

import static androidx.navigation.Navigation.findNavController;

public class DashboardActivity extends AppCompatActivity implements DashboardInterface {

    private BottomNavigationView navBottom ;
    private NavController navController ;
    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.tb_dashboard);
        setSupportActionBar(toolbar);

        navBottom = findViewById(R.id.nav_bottom);
        navController = findNavController(findViewById(R.id.fragment));

        NavigationUI.setupWithNavController(navBottom, navController);

    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setSubTitle(String subtitle) {
        toolbar.setSubtitle(subtitle);
    }

}