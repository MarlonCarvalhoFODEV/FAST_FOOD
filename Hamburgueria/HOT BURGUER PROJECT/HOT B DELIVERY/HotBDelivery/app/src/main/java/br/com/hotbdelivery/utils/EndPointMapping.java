package br.com.hotbdelivery.utils;

import android.content.Context;

import br.com.hotbdelivery.R;

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
            public static final String CHECK_STATUS = "check-status";
            public static final String GET_PERFORMANCES = "get-performances";
            public static final String GET_IMAGE_PROFILE= "get-photo-profile";
            public static final String GET_HISTORIC_DELIVERY= "get-historic-delivery";
            public static final String UPDATE_STATUS_OPERATION = "update-status-operation";

            public static final String ACCEPT_RUNNIG = "accept-runnig";
            public static final String REJECT_RUNNIG = "reject-runnig";

            public static final String I_ARRIVED_AT_THE_PLACE = "arrived-place-client";
            public static final String COMPLETE_DELIVERY = "complete-delivery";
            public static final String ARRIVED_COLLECT_POINT = "arrived-collect-point";
            public static final String IM_ON_MY_WAY = "on-my-way";

            public static final String GET_ACHIEVEMENTS = "get-achievements";
            public static final String TRANSFER_BALANCE = "transfer-balance";

        }
    }

    public static class Checkout {
        public static final String CONTROLLER = "hot-payment/";

        public static class Functions{
            public static final String GET_PUBLIC_KEY = "get-public-key";
        }
    }

    public static class System{
        public static final String CONTROLLER = "system" ;

        public static class Functions{
            public static final String CHECK_UPDATE = "/has-update-deliveryman" ;
        }
    }

}
