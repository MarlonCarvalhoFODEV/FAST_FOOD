package com.c.hotburguerdelivery.apis.onesignal;

import android.util.Log;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import apis.onesignal.notification.OneSignalFilter;
import apis.onesignal.notification.OneSignalNotification;
import apis.onesignal.notification.OneSignalSegment;

public class APIOneSignal {

    public void setExternalID(String externalUserID){

// Setting External User Id with Callback Available in SDK Version 3.13.0+
        OneSignal.setExternalUserId(externalUserID, new OneSignal.OSExternalUserIdUpdateCompletionHandler() {
            @Override
            public void onComplete(JSONObject results) {
                // The results will contain push and email success statuses
                OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id done with results: " + results.toString());

                // Push can be expected in almost every situation with a success status, but
                // as a pre-caution its good to verify it exists
                try {
                    if (results.has("push") && results.getJSONObject("push").has("success")) {
                        boolean isPushSuccess = results.getJSONObject("push").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for push status: " + isPushSuccess);
                    }

                    // Verify the email is set or check that the results have an email success status
                    if (results.has("email") && results.getJSONObject("email").has("success")) {
                        boolean isEmailSuccess = results.getJSONObject("email").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Sets external user id for email status: " + isEmailSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendNotification(String title, String message){
        try {
            OneSignalNotification not = new OneSignalNotification();
            not.setLanguage(OneSignalNotification.Languages.ENGLISH);
            not.setTitle("SEU PEDIDO");
            not.setSubTitle("sub titulo");
            not.setMessage("Está a caminho...");
            not.getSegments().setSegmentBoth(OneSignalSegment.Type.INCLUDED_SEGMENT);
            not.getFilters().addFilter(OneSignalFilter.Filter.Type.);
            not.getAppearances().setAndroidSmallIcon("ic_notification_full");
            not.getAppearances().setAndroidLedColor("FA730E");
            not.getAppearances().setAndroidAccentColor("FA730E");
            //not.getButtons().addButton(OneSignalButton.Type.BUTTONS, new OneSignalButton.Button("1455","RESPONDER"));

            /*OneSignalFilter.Filter filter = new OneSignalFilter.Filter();
            filter.setField(OneSignalFilter.Filter.Type.SESSION_COUNT);

            not.getFilters().addFilter(OneSignalFilter.Filter.Type.USAGE_DURATION, filter);*/

            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Test Message'}, 'include_player_ids': ['" + "userId" + "']}"),
                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("OneSignalExample", "postNotification Success: " + response.toString());
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
