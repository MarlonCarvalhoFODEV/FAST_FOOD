package apis.address;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import apis.location.APILocation;


public class AddressResultReceiver extends ResultReceiver {
 
    private APILocation APILocation;

    public AddressResultReceiver(APILocation APILocation, Handler handler) {
        super(handler);
        this.APILocation = APILocation;
    }

    private AddressAPI getAddress(String address){
        String[] addressSplited = address.split(",");
        AddressAPI add = new AddressAPI();

        try {
            add.setStreet(addressSplited[0]);
            add.setNumber(Integer.valueOf(addressSplited[1].split("-")[0].replace(" ", "")));
            add.setNeighborhood(addressSplited[1].split("-")[1].replace(" ", ""));
            add.setCity(addressSplited[2].split("-")[0]);
            add.setUF(addressSplited[2].split("-")[1]);
            add.setCEP(addressSplited[3]);
            add.setContry(addressSplited[4]);
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return null ;
        }

        return add ;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if(resultCode == FetchAddressIntentService.Constants.SUCESS_RESULT){
            AddressAPI addressReturned = getAddress(resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY));
            APILocation.onResult(addressReturned);
            Log.i("FDSFDSAFDAFDAFD",addressReturned.getCompleteAddress());
        } else {
            APILocation.erroMessage(resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY));
        }
    }
}