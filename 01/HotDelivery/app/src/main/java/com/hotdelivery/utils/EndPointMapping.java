package com.hotdelivery.utils;

import android.content.Context;

import com.hotdelivery.R;

public class EndPointMapping {

    public static String getDomain(Context context){
        return context.getString(R.string.domain_connection);
    }

    public static class DeliveryMan{
        public static final String CONTROLLER = "hot-deliveryman/";

        public static class Functions{
            public static final String NEW_REGISTER = "new-register";
            public static final String NEW_EMAIL = "new-email";
            public static final String REGISTER_PHOTO = "register-photo";
        }
    }

    public static class Checkout {
        public static final String CONTROLLER = "hot-payment/";

        public static class Functions{
            public static final String GET_PUBLIC_KEY = "get-public-key";
        }
    }
}
