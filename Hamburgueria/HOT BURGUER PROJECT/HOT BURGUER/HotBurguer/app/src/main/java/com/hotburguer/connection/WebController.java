package com.hotburguer.connection;

import android.content.Context;

import com.hotburguer.R;

public class WebController {

    public static String getDomain(Context context){
        return context.getString(R.string.domain_connection);
    }

    public static class Products{
        public static final String CONTROLLER = "hot-products" ;

        public static class Functions{
            public static final String GET_ALL = "/get-all" ;
            public static final String GET_IMAGE = "/get-image" ;
            public static final String EVALUATION_PRODUCT = "/evaluation" ;
            public static final String LIKED_PRODUCT = "/like" ;
            public static final String GET_FEEDBACK_PRODUCT = "/get-feedbacks" ;
        }
    }

    public static class Address{
        public static final String CONTROLLER = "hot-address" ;

        public static class Functions{
            public static final String GET_ADDRESS_FROM_CEP = "/get-address-from-cep" ;
            public static final String GET_ADDRESS_FROM_COORDINATES = "/get-address-from-coordinates" ;
            public static final String GET_ADDRESS_FROM_USER = "/get-address-from-user" ;
            public static final String DISABLE_ADDRESS = "/disability-address" ;
        }
    }

    public static class Checkout{
        public static final String CONTROLLER = "hot-payment" ;

        public static class Functions{
            public static final String GET_PUBLIC_KEY = "/get-public-key" ;
            public static final String SAVE_CARD = "/save-card" ;
            public static final String GET_CARDS_SAVED = "/get-cards-saved" ;
            public static final String CREATE_PAYMENT = "/create-payment" ;
        }
    }

    public static class User{
        public static final String CONTROLLER = "hot-users" ;

        public static class Functions{
            public static final String NEW_USER = "/new-user" ;
            public static final String UPDATE_USER = "/update-user" ;
            public static final String UPDATE_USER_PERSONAL_DATA = "/update-user-personal-data" ;
            public static final String UPDATE_USER_ADDRESS = "/update-user-personal-data" ;
            public static final String GET_IMG_PROFILE = "/get-img-profile" ;
        }
    }

    public static class Order{
        public static final String CONTROLLER = "hot-order" ;

        public static class Functions{
            public static final String MAKE_ORDER = "/new-order" ;
            public static final String CHECK_IS_DELIVERY_LOCAL = "/check-is-delivery-local" ;
            public static final String CHECK_PRODUCTS_AVAILABLE = "/check-products-available" ;
            public static final String GET_STATUS_ORDER = "/get-status-order" ;
            public static final String CONFIRM_DELIVERY = "/confirm-delivery" ;
            public static final String GET_FEE_DELIVERY = "/get-fee-delivery" ;
        }
    }

    public static class System{
        public static final String CONTROLLER = "system" ;

        public static class Functions{
            public static final String CHECK_UPDATE = "/check-update" ;
        }
    }

}
