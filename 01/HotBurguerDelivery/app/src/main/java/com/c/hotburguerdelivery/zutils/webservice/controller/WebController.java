package com.c.hotburguerdelivery.zutils.webservice.controller;

public class WebController {

    public static class Collaborator{
        public static final String CONTROLLER = "Collaborator/";

        public static class Functions{
            public static final String LOGIN = "login/";
            public static final String ADD_PRODUCTION = "add-production/";
            public static final String GET_IMAGE = "get-photo/";
        }
    }

    public static class Image{

        public static final String CONTROLLER = "Image/";

        public class Methods{

            public static final String GET_IMAGE = "get-image/" ;

        }

    }

    public static class Products{
        public static final String CONTROLLER = "Czc/";

        public static class Method{
            public static final String GET_PRODUCTS = "get-products-coll/";
            public static final String SEARCH_PRODUCTS = "search-products-inactive/";
            public static final String UPDATE_ACTIVE = "update-active/";
        }
    }

    public static class Orders{
        public static final String CONTROLLER = "Order/";

        public static class Functions{
            public static final String GET_ORDERS = "get-order-day/";
            public static final String GET_STATUS_ORDERS = "get-status-order/";
            public static final String GET_GROUP_ORDERS = "get-group-order-day/";
            public static final String GET_CART = "get-cart-origin/";
            public static final String UPDATE_ALL_ORDER = "update-all-orders/";
            public static final String UPDATE_ORDER = "update-order/";
            public static final String VERIFY_ORDER_AVAILABLE = "verify-order-available/";
            public static final String DELETE_PRODUCT_FROM_ORDER = "delete-product-from-order/";
        }
    }
}
