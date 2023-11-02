package com.c.hotburguerdelivery.apis.address;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver ;

    public FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent != null){
            String errorMessage = "" ;
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

            if(location == null){
                return;
            }

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null ;

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            } catch (IOException e) {
                errorMessage = e.getMessage();
            }

            if(addresses == null || addresses.isEmpty()){
                deliverResulToReceiver(Constants.FAILURE_RESULT,errorMessage);
            } else {

                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();

                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                    addressFragments.add(address.getAddressLine(i));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    deliverResulToReceiver(
                            Constants.SUCESS_RESULT,
                            TextUtils.join(
                                    Objects.requireNonNull(System.getProperty("line.separator")),
                                    addressFragments
                            )
                    );
                }
            }
        }
    }

    public class Constants{
        private static final String PACKAGE_NAME = "com.firtinnovationti.coordenadas";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
        public static final int SUCESS_RESULT = 432 ;
        public static final int FAILURE_RESULT = 478 ;
    }

    private void deliverResulToReceiver(int resultCode, String addressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode, bundle);
    }
}
