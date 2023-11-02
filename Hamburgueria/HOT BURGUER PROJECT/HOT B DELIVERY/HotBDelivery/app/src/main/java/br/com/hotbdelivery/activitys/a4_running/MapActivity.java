package br.com.hotbdelivery.activitys.a4_running;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apis.location.APILocation;
import apis.location.LAddress;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.itens.HotDelivery;
import br.com.hotbdelivery.itens.HotOrdersDelivery;
import br.com.hotbdelivery.itens.HotRunning;
import br.com.hotbdelivery.utils.CacheApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

// classes needed to add the location component
// classes needed to add a marker
// classes to calculate a route
// classes needed to launch navigation UI


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineCallback {

    private MapView mapView;
    // variables for adding location layer
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Location originLocation;

    private LatLng originCoord;
    private DirectionsRoute currentRoute;

    private String TAG = "createRoutes";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 214;
    public static final int REQUEST_RUNNING = 764;
    public static final int RESULT_DELIVERY = 765;

    private NavigationMapRoute navigationMapRoute;
    //private HotOrdersDelivery delivery;
    private HotRunning running;
    /*private ArrayList<Integer> idOrders ;
    private ArrayList<LAddress> addresses;*/

    private LocationEngine locationEngine;

    private boolean granted = false;
    private boolean routeCreated = false;
    private boolean progress = false;

    private APILocation apiLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);

        init();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onBackPressed() {}

    private void init() {
        apiLocation = new APILocation(this);

        if (!apiLocation.isEnabledGPS())
            apiLocation.startActivityEnabledGPS();
        else
            apiLocation.askPermission();

        Location loc = apiLocation.getLastBestLocation();

        originLocation = new Location("providerNA");
        originLocation.setLatitude(loc.getLatitude());
        originLocation.setLongitude(loc.getLongitude());

        //delivery = (HotOrdersDelivery) getIntent().getExtras().get("data_running");
        running = CacheApp.getRunning(this);
        //addresses = getAddressesResponse();

    }

    private void showProgress(String message) {
        progress = true;
        ((LinearLayout) findViewById(R.id.ll_progress_map)).setVisibility(View.VISIBLE);

        if (message != null && message.length() > 0 && !message.isEmpty())
            ((TextView) findViewById(R.id.tv_message_informative_delivery)).setText(message);
    }

    private void hideProgress() {
        progress = false;
        ((LinearLayout) findViewById(R.id.ll_progress_map)).setVisibility(View.GONE);

        String message = ((TextView) findViewById(R.id.tv_message_informative_delivery)).getText().toString();
        if (message != null && message.length() > 0 && !message.isEmpty())
            ((TextView) findViewById(R.id.tv_message_informative_delivery)).setText(null);
    }

    public void startTurnByTurn(View view) {

        /*Intent intent = new Intent(this, RunningActivity.class);
        intent.putExtra("data_running", delivery);
        startActivityForResult(intent, REQUEST_RUNNING);*/
        Intent intent = new Intent(this, RunningActivity.class);
        intent.putExtra("route",currentRoute);
        startActivityForResult(intent, REQUEST_RUNNING);
    }

    private final ArrayList<LAddress> getAddressesResponse() {
        JsonArray dataRunning = new Gson().fromJson(getIntent().getExtras().get("data_running").toString(), JsonArray.class);
        ArrayList<LAddress> addresses = new ArrayList<>();

        Gson gson = new Gson();

        LAddress address;
        for (JsonElement e : dataRunning) {
            address = gson.fromJson(e, LAddress.class);
            addresses.add(address);

        }

        return addresses;
    }

    private final ArrayList<Point> createPoints(ArrayList<LAddress> addresses) {
        ArrayList<Point> points = new ArrayList<>();
        for (LAddress address : addresses)
            points.add(Point.fromLngLat(address.getLongitude(), address.getLatitude()));

        return points;
    }

    private void initializeLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        MapActivity.this.mapboxMap = mapboxMap;

                        enableLocationComponent();

                        if (originLocation != null) {
                            originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());

                            /*LAddress addressFinal = delivery.getAddress().get(delivery.getAddress().size() - 1);
                            delivery.setPointOrigin(Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude()));
                            delivery.setPointFinal(Point.fromLngLat(addressFinal.getLongitude(), addressFinal.getLatitude()));
                            showProgress("Traçando rotas....");

                            //createRoutesPRO(originCoord);
                            createRoutes(originCoord, delivery.getAddress());*/

                            showProgress("Traçando rotas....");

                            //createRoutesPRO(originCoord);
                            createRoutes2(originCoord, running);

                        } else {
                            granted = true;
                            //enableLocationComponent();
                        }
                    }
                }
        );
    }

    /*private void createRoutes(LatLng origin, ArrayList<LAddress> waypoints) {
        if (waypoints.size() > 0) {
            LAddress address = waypoints.get(waypoints.size() - 1);

            Point pOrigin = Point.fromLngLat(origin.getLongitude(), origin.getLatitude());
            Point pDestino = Point.fromLngLat(address.getLongitude(), address.getLatitude());

            //Point pDestino = Point.fromLngLat( -43.323690, -22.849509);

            NavigationRoute.Builder builder = NavigationRoute.builder(this);
            builder.accessToken(Mapbox.getAccessToken());
            builder.origin(pOrigin);
            builder.destination(pDestino);
            builder.voiceUnits(DirectionsCriteria.METRIC);
            builder.profile(DirectionsCriteria.PROFILE_DRIVING);

            //waypoints.remove(waypoints.size() - 1);
            //waypoints.remove(waypoints.get(waypoints.size() - 1));

            LAddress lastAddress = waypoints.get(waypoints.size() - 1);
            int position = 0 ;
            for (LAddress a : waypoints) {
                if (position == 0) {

                    mapboxMap.addMarker(new MarkerOptions()
                            .setPosition(new LatLng(a.getLatitude(), a.getLongitude()))
                            .setTitle(a.getStreet())
                            .setSnippet(a.getComplement())
                            .setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_hot_burguer))
                    );
                    builder.addWaypoint(Point.fromLngLat(a.getLongitude(), a.getLatitude()));
                    position++;
                } else {

                    mapboxMap.addMarker(new MarkerOptions()
                            .setPosition(new LatLng(a.getLatitude(), a.getLongitude()))
                            .setTitle(a.getStreet())
                            .setSnippet(a.getComplement())
                            .setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_person_pin48x64))
                    );
                    builder.addWaypoint(Point.fromLngLat(a.getLongitude(), a.getLatitude()));
                }
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
                    delivery.setRoute(currentRoute);
                    hideProgress();
                }

                @Override
                public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                    Log.e(TAG, "Error: " + throwable.getMessage());
                }
            });

        }
    }*/

    private void createRoutes2(LatLng origin, HotRunning running) {

        ArrayList<HotDelivery> waypoints = new ArrayList<>(running.getDeliverys().values());
        Collections.sort(waypoints);

        if (waypoints.size() > 0) {
            LAddress address = waypoints.get(waypoints.size() - 1).getAddressDelivery();

            Point pOrigin = Point.fromLngLat(origin.getLongitude(), origin.getLatitude());
            Point pDestino = Point.fromLngLat(address.getLongitude(), address.getLatitude());

            running.setOriginLat(origin.getLatitude());
            running.setOriginLng(origin.getLongitude());
            running.setFinalLat(pDestino.latitude());
            running.setFinalLng(pDestino.longitude());

            CacheApp.saveRunning(MapActivity.this, running);

            NavigationRoute.Builder builder = NavigationRoute.builder(this);
            builder.accessToken(Mapbox.getAccessToken());
            builder.origin(pOrigin);
            builder.destination(pDestino);
            builder.voiceUnits(DirectionsCriteria.METRIC);
            builder.profile(DirectionsCriteria.PROFILE_DRIVING);

            //waypoints.remove(waypoints.size() - 1);
            //waypoints.remove(waypoints.get(waypoints.size() - 1));


            for (HotDelivery del : waypoints) {
                if (del.getAddressDelivery().getId() == 1) {

                    LAddress a = del.getAddressDelivery();
                    mapboxMap.addMarker(new MarkerOptions()
                            .setPosition(new LatLng(a.getLatitude(), a.getLongitude()))
                            .setTitle(a.getStreet())
                            .setSnippet(a.getComplement())
                            .setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_hot_burguer))
                    );
                    builder.addWaypoint(Point.fromLngLat(a.getLongitude(), a.getLatitude()));

                } else {
                    LAddress a = del.getAddressDelivery();
                    mapboxMap.addMarker(new MarkerOptions()
                            .setPosition(new LatLng(a.getLatitude(), a.getLongitude()))
                            .setTitle(a.getStreet())
                            .setSnippet(a.getComplement())
                            .setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_person_pin48x64))
                    );
                    builder.addWaypoint(Point.fromLngLat(a.getLongitude(), a.getLatitude()));
                }
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
                    //delivery.setRoute(currentRoute);
                    hideProgress();
                }

                @Override
                public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                    Log.e(TAG, "Error: " + throwable.getMessage());
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 300) {
            finish();
        }
        else if(requestCode == REQUEST_RUNNING && resultCode == RunningActivity.RESULT){
            setResult(RESULT_DELIVERY, data);
            finish();
        }
    }

    private void setCameraPosition(Location loc, int cameraZoom) {
        if (loc != null) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoom));

            //createRoutes(latLng, addresses);
        } else if (!apiLocation.isEnabledGPS()) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            //Se vier null ou length == 0 é por que o GPS esta desabilitado.
            // Para abrir a tela do menu pode fazer assim:
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 100);
        } else {
            APILocation.Coordinates cl = apiLocation.getCurrentCoordinatesObject();
            Location ol = apiLocation.getLastBestLocation();

            double latitude = cl != null ? cl.getLatitude() : ol != null ? ol.getLatitude() : 0;
            double longitude = cl != null ? cl.getLongitude() : ol != null ? ol.getLongitude() : 0;

            if (latitude != 0 && longitude != 0) {
                originLocation = new Location("providerNA");
                originLocation.setLongitude(longitude);
                originLocation.setLatitude(latitude);

                originCoord = new LatLng(latitude, longitude);

                //createRoutes(originCoord, addresses);
            } else {

            }
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private synchronized void enableLocationComponent() {

        if (granted) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, mapboxMap.getStyle());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

            originLocation = locationComponent.getLastKnownLocation();
            if (originLocation == null) {
                APILocation.Coordinates cl = apiLocation.getCurrentCoordinatesObject();
                Location ol = apiLocation.getLastBestLocation();

                double latitude = cl != null ? cl.getLatitude() : ol != null ? ol.getLatitude() : 0;
                double longitude = cl != null ? cl.getLongitude() : ol != null ? ol.getLongitude() : 0;

                if (latitude != 0 && longitude != 0) {
                    originLocation = new Location("providerNA");
                    originLocation.setLongitude(longitude);
                    originLocation.setLatitude(latitude);

                    originCoord = new LatLng(latitude, longitude);

                    // createRoutes(originCoord, addresses);
                }
            } else
                originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());

            setCameraPosition(originLocation, 15);
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

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            /*case 0:{
                if(routeCreated == false && progress == false){
                    Intent intent = new Intent(this, HandCrankActivity.class);
                    intent.putExtra("group_orders", new Gson().toJson(group));
                    startActivityForResult(intent,0);
                } else {
                    Toast.makeText(this, "Não há nescessidade - ENTREGA MANUAL", Toast.LENGTH_SHORT).show();
                }
                break;
            }*/
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public synchronized void onPermissionResult(boolean granted) {
        if (granted) {
            this.granted = granted;
            enableLocationComponent();
        } else {
            Toast.makeText(this, "Permissão Negada", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            this.granted = true;
            enableLocationComponent();
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
        }



        if (mapView != null)
            mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
        }

        if (mapView != null)
            mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            //locationEngine.deactivate();
        }

        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onSuccess(Object result) {

    }

    @Override
    public void onFailure(@NonNull Exception exception) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}