package com.c.hotburguerdelivery.acitivtys.bdelivery;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.c.hotburguerdelivery.R;
import com.c.hotburguerdelivery.itens.GroupOrders;
import com.c.hotburguerdelivery.itens.HotOrder;
import com.c.hotburguerdelivery.itens.ITOrderBuyer;
import com.c.hotburguerdelivery.service_chat.UserMessage;
import com.c.hotburguerdelivery.zutils.webservice.controller.WebController;
import com.c.hotburguerdelivery.acitivtys.dhandcrank.HandCrankActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.matching.v5.MapboxMapMatching;
import com.mapbox.api.matching.v5.models.MapMatchingMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

// classes needed to add the location component
// classes needed to add a marker
// classes to calculate a route
// classes needed to launch navigation UI


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener
, LocationEngineCallback {
    private MapView mapView;
    // variables for adding location layer
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Location originLocation;

    private LatLng originCoord;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private GroupOrders group;

    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;

    private boolean granted = false ;
    private boolean routeCreated = false ;
    private boolean progress = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        String groupJson = (String) getIntent().getExtras().get("group");
        group = new Gson().fromJson(groupJson, GroupOrders.class);

        Toolbar tbClientName = (Toolbar) findViewById(R.id.tb_map);
        tbClientName.setSubtitle(group.size() + " Entrega(s)");

        setSupportActionBar(tbClientName);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }


    private void showProgress(String message){
        progress = true ;
        ((LinearLayout) findViewById(R.id.ll_progress_map)).setVisibility(View.VISIBLE);

        if(message!=null && message.length() > 0 && !message.isEmpty())
            ((TextView) findViewById(R.id.tv_message_informative_delivery)).setText(message);
    }

    private void hideProgress(){
        progress = false ;
        ((LinearLayout) findViewById(R.id.ll_progress_map)).setVisibility(View.GONE);

        String message = ((TextView) findViewById(R.id.tv_message_informative_delivery)).getText().toString();
        if(message!=null && message.length() > 0 && !message.isEmpty())
            ((TextView) findViewById(R.id.tv_message_informative_delivery)).setText(null);
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        MapActivity.this.mapboxMap = mapboxMap;
                        enableLocationComponent();

                        if(originLocation != null) {
                            originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());

                            //showProgress("Traçando rotas....");

                            if(group.size() > 1){
                                //createRoutesPRO(originCoord);
                                createRoutes(Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude()));
                            } else {
                                createRoutes(Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude()));
                            }
                        } else granted = true ;
                    }
                }
        );
    }

    public void startNavigation(View view) {
        view.setEnabled(false);
        if (currentRoute != null) {

            UserMessage msg = new UserMessage();
            msg.setIdDestinys(group.getIdUsers());
            msg.setIdOrder(group.getIdOrders());
            msg.setTypeCommunication(UserMessage.COMMUNICATION);
            msg.setMessage(ITOrderBuyer.STATUS_DELIVERY_ON_MY_WAY);

            // atualiza o status do pedido
            requestUpdateStatusOrder(ITOrderBuyer.STATUS_DELIVERY_ON_MY_WAY);

            // Envia a notificação que está a caminho
        } else {
            ((FloatingActionButton) findViewById(R.id.fab_start_navigation)).setEnabled(true);
            Toast.makeText(this, "Não conseguimos calcular a rota!", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestUpdateStatusOrder(String status) {
        StringBuilder url = new StringBuilder();
        url.append(WebController.Orders.CONTROLLER);
        url.append(WebController.Orders.Functions.UPDATE_ALL_ORDER);

        for (HotOrder order : group) {
            //order.setCompanyFunction(account.getFunction().getCompanyFunction());
            order.setStatus(status);


        }

    }

    private void createRoutes(Point origin) {

        Point destino = Point.fromLngLat(group.get(group.size() - 1).getLongitude(), group.get(group.size() - 1).getLatitude());

        NavigationRoute.Builder builder = NavigationRoute.builder(this);
        builder.accessToken(Mapbox.getAccessToken());
        builder.origin(origin);
        builder.destination(destino);
        builder.voiceUnits(DirectionsCriteria.METRIC);
        builder.profile(DirectionsCriteria.PROFILE_DRIVING);

        //group.remove(group.get(group.size()-1));

        for (HotOrder o : group) {
            mapboxMap.addMarker(new MarkerOptions()
                    .setPosition(new LatLng(o.getLatitude(), o.getLongitude()))
                    .setTitle(o.getTitle())
                    //.setSnippet("Cobrar - " + o.getPurchaseValue())
                    .setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_client)));
            builder.addWaypoint(Point.fromLngLat(o.getLongitude(), o.getLatitude()));
        }

        NavigationRoute nav = builder.build();

        nav.getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.e(TAG, "No routes found");
                    return;
                }

                currentRoute = response.body().routes().get(0);
// Draw the route on the map
                if (navigationMapRoute != null) {
                    navigationMapRoute.removeRoute();
                } else {
                    if (mapboxMap != null && mapView != null) {
                        navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                    }
                }

                navigationMapRoute.addRoute(currentRoute);
                hideProgress();
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
            }
        });

    }

    private synchronized void createRoutesPRO(LatLng origin) {

        List<Point> coordinates = new ArrayList<>();
        //coordinates.add(Point.fromLngLat(-43.331655,-22.852849));
        coordinates.add(Point.fromLngLat(origin.getLongitude(),origin.getLatitude()));

        for(HotOrder o : group){
            coordinates.add(Point.fromLngLat(o.getLongitude(),o.getLatitude()));

            MarkerOptions maker = new MarkerOptions();
            maker.setTitle(o.getTitle());
            maker.setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_client));
            maker.setPosition(new LatLng(o.getLatitude(), o.getLongitude()));

            //if(o.getPaymentLocal() == 1)maker.setSnippet("Cobrar - " + o.getPurchaseValue());

            mapboxMap.addMarker(maker);
        }


        if (coordinates.size() > 1) {
            // Split Out CoOrdinates In To

            MapboxMapMatching.Builder mapMatchingBuilder = MapboxMapMatching.builder()
                    .accessToken(Mapbox.getAccessToken())
                    .coordinates(coordinates)
                    .steps(true)
                    .tidy(true)
                    .voiceInstructions(true)
                    .bannerInstructions(true)
                    .language("pt-BR")
                    .voiceUnits(DirectionsCriteria.METRIC)
                    .profile(DirectionsCriteria.PROFILE_DRIVING);

                mapMatchingBuilder.build().enqueueCall(new Callback<MapMatchingResponse>() {
                    @Override
                    public void onResponse(Call<MapMatchingResponse> call, Response<MapMatchingResponse> response) {
                        Log.i(TAG, response.message());
                        Log.i(TAG, ""+response.isSuccessful());
                        Log.i(TAG, ""+(response.body().tracepoints() != null?"Tracepoint Nulo":"Trace Point OK"));
                        Log.i(TAG, ""+(response.body().matchings()!=null?"matchings OK":"matchings NULO"));

                        for(MapMatchingMatching mpa : response.body().matchings()){
                            Log.i(TAG,""+mpa.toDirectionRoute());
                        }
                        if (response.isSuccessful()) {
                            Log.i(TAG, "SIZE: "+(response.body().matchings().size()));
                            if (response.body().matchings().size() > 0) {

                                currentRoute = (response.body().matchings().get(0).toDirectionRoute());

                                for(MapMatchingMatching mpa : response.body().matchings()){
                                    Log.i("DASDADSADA",""+mpa.toDirectionRoute());
                                }

                                if (navigationMapRoute != null) {
                                    navigationMapRoute.removeRoute();
                                } else {
                                    if (mapboxMap != null && mapView != null) {
                                        navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                                    }
                                }

                                navigationMapRoute.addRoute(currentRoute);
                                hideProgress();
                                routeCreated = true ;
                            } else {
                                Timber.tag(TAG).e("No Matching Available");
                            }
                        } else {
                            Timber.tag(TAG).e(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<MapMatchingResponse> call, Throwable t) {
                        Timber.tag(TAG).e(t);
                    }
                });
        } else {
            Timber.tag(TAG).e("No Coordinates Passed Into Navigation.");

        }
    }

    private void initializeLocationEngine(){
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 300){
            finish();
        }
    }

    private void setCameraPosition(Location loc, int cameraZoom){
       if(loc != null){
           mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                   (new LatLng(loc.getLatitude(),loc.getLongitude()),cameraZoom));

           if(granted == true) {
               originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
               createRoutesPRO(originCoord);
           }
       } else {
           String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
           //Se vier null ou length == 0 é por que o GPS esta desabilitado.
           // Para abrir a tela do menu pode fazer assim:
           Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
           startActivityForResult(intent, 100);
       }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent() {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(this, mapboxMap.getStyle());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

            originLocation = locationComponent.getLastKnownLocation();

            setCameraPosition(originLocation,15);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add("Entrega manual");
        item.setIcon(getResources().getDrawable(R.drawable.ic_hand_crank, null));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: finish(); break;

            case 0:{
                if(routeCreated == false && progress == false){
                    Intent intent = new Intent(this, HandCrankActivity.class);
                    intent.putExtra("group_orders", new Gson().toJson(group));
                    startActivityForResult(intent,0);
                } else {
                    Toast.makeText(this, "Não há nescessidade - ENTREGA MANUAL", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent();
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates(this);
        }

        if(locationLayerPlugin != null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates(this);
        }

        if(locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine != null){
            //locationEngine.deactivate();
        }
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void webResponse() {
        switch (5){
            case 0:{
                Toast.makeText(this, "Esse pedido já está acaminho!", Toast.LENGTH_LONG).show();
                finish();
                break;
            }
            case 2:{
                Intent intent = new Intent(this, TurnByTurnActivity.class);
                intent.putExtra("group_orders",new Gson().toJson(group));
                intent.putExtra("current_route", currentRoute);
                intent.putExtra("origin_position",originCoord);
                startActivityForResult(intent,1);
                break;
            }
        }
    }

    @Override
    public void onSuccess(Object result) {

    }

    @Override
    public void onFailure(@NonNull Exception exception) {

    }
}