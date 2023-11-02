package apis.onesignal;

import android.content.Context;
import android.util.Log;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class APIOneSignal {

    public static void setExternalID(String externalUserId) {
        OneSignal.setExternalUserId(externalUserId, new OneSignal.OSExternalUserIdUpdateCompletionHandler() {
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

    public static void sendNotification(String userId, String title, String message) {

        String json = "{\"android_led_color\":\"FA730E\",\"contents\":{\"en\":\"" + title + "\"},'include_player_ids': ['" + userId + "']}, \"android_accent_color\":\"FA730E\",\"subtitle\":{\"en\":\"" + message + "\"},\"headings\":{\"en\":\"" + message + "\"},\"included_segments\":[\"Active Users\",\"Inactive Users\"],\"small_icon\":\"ic_notification_full\",\"app_id\":\"98c46fd1-2bd2-4226-bd83-480ec194b796\"}";

        try {
            OneSignal.postNotification(new JSONObject(json),
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
