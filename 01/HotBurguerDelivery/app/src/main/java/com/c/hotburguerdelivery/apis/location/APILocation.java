package com.c.hotburguerdelivery.apis.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.FMT_Product;
import com.hotburguer.itens.HotProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apis.address.AddressAPI;
import apis.address.AddressResultReceiver;
import apis.address.FetchAddressIntentService;
public class APILocation {

    private Activity activity ;
    private Context context ;

    public static final int REQUEST_CODE_SOURCE_SETTINGS = 1747 ;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1748 ;

    private AddressAPI address ;
    private Coordinates coordinates ;

    public APILocation(Activity activity){
        this.activity = activity ;
        this.context = activity.getApplicationContext();
        this.coordinates = new Coordinates();
    }

    public void askPermission(){
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentCoordinates();
        }
    }

    public AddressAPI getAddress() {
        return address;
    }

    public synchronized android.location.Address getAddressFromCoordinate(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context.getApplicationContext());

        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (addresses.size() > 1) {
                return addresses.get(Integer.valueOf((int)addresses.size()/2));
            } else if(addresses.size() == 1){
                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public android.location.Address getAddressActualPosition(){
        Coordinates coordinates = getCurrentCoordinatesObject();
        return getAddressFromCoordinate(coordinates.latitude, coordinates.longitude) ;
    }


    public android.location.Address getAddressFromCoordinate() {
       if(coordinates != null){
           Geocoder geocoder = new Geocoder(context.getApplicationContext());

           try {
               List<android.location.Address> addresses = geocoder.getFromLocation(coordinates.getLatitude(), coordinates.getLongitude(), 1);

               if (addresses.size() > 0) {
                   return addresses.get(0);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
        return null;
    }

    public void startActivityEnabledGPS(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, REQUEST_CODE_SOURCE_SETTINGS);
    }

    public boolean isEnabledGPS() {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public synchronized Coordinates getCurrentCoordinatesObject(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(activity)
                                .removeLocationUpdates(this);

                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size()-1;
                            coordinates.setLatitude(locationResult.getLocations().get(latestLocationIndex).getLatitude());
                            coordinates.setLongitude(locationResult.getLocations().get(latestLocationIndex).getLongitude());

                            Location location = new Location("providerNA");
                            location.setLatitude(coordinates.getLatitude());
                            location.setLongitude(coordinates.getLongitude());
                            //createAddressFromLngLat(location);
                        }
                    }
                }, Looper.getMainLooper());

        return coordinates ;
    }

    public void setOnRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentCoordinates();
            } else {
                Toast.makeText(context, "Permissao Negada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Coordinates{
        private double latitude ;
        private double longitude ;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public void onResult(AddressAPI addressReturned) {
        address = addressReturned ;
    }

    public void erroMessage(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void getCurrentCoordinates(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(activity)
                                .removeLocationUpdates(this);

                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size()-1;
                            coordinates.setLatitude(locationResult.getLocations().get(latestLocationIndex).getLatitude());
                            coordinates.setLongitude(locationResult.getLocations().get(latestLocationIndex).getLongitude());

                            Location location = new Location("providerNA");
                            location.setLatitude(coordinates.getLatitude());
                            location.setLongitude(coordinates.getLongitude());
                            createAddressFromLngLat(location);
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void createAddressFromLngLat(Location location){
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, new AddressResultReceiver(this,new Handler()));
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location);
        activity.startService(intent);
    }

    public static class Address{
        private String street ;
        private String neighborhood ;
        private String city ;
        private String state ;
        private String contry ;
        private String UF ;
        private String CEP ;
        private String complement ;
        private int number ;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getNeighborhood() {
            return neighborhood;
        }

        public void setNeighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getContry() {
            return contry;
        }

        public void setContry(String contry) {
            this.contry = contry;
        }

        public String getUF() {
            return UF;
        }

        public void setUF(String UF) {
            this.UF = UF;
        }

        public String getCEP() {
            return CEP;
        }

        public void setCEP(String CEP) {
            this.CEP = CEP;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getComplement() {
            return complement;
        }

        public void setComplement(String complement) {
            this.complement = complement;
        }

        public String getCompleteAddress(){
            StringBuilder add = new StringBuilder();
            add.append(street);
            add.append(", ");
            add.append(number);
            add.append(" - ");
            add.append(neighborhood);
            add.append(", ");
            add.append(city);
            add.append(" - ");
            add.append(UF);
            add.append(", ");
            add.append(CEP);
            add.append(", ");
            add.append(contry);
            return add.toString();
        }
    }
}
