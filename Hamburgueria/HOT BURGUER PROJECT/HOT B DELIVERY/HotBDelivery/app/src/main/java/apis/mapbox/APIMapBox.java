package apis.mapbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
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

import java.util.ArrayList;
import java.util.List;

import br.com.hotbdelivery.R;

public class APIMapBox {

    private Context context;
    private MapView mapView;
    private MapboxMap mapboxMaps;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    private LatLng originCoord;
    private Location originLocation;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private PermissionsManager permissionsManager;

    private boolean granted = false;
    private boolean routeCreated = false;
    private boolean progress = false;

    public APIMapBox(Context c) {
        context = c;
        Mapbox.getInstance(c, c.getString(R.string.mapbox_access_token));
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView, Bundle savedInstanceState) {
        this.mapView = mapView;
        this.mapView.onCreate(savedInstanceState);
    }

    public void setPermissionsManager(PermissionsManager permissionsManager) {
        this.permissionsManager = permissionsManager;
    }


}
