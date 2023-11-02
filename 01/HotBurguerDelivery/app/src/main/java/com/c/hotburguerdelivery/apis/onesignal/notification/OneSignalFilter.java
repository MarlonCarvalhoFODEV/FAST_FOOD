package com.c.hotburguerdelivery.apis.onesignal.notification;

import org.json.JSONArray;
import org.json.JSONObject;

public class OneSignalFilter {

    private OneSignalNotification notification ;

    public OneSignalFilter (OneSignalNotification notification){
        this.notification = notification ;
    }

    public void setLocation(String... languages){
        JSONArray arrayFilters = new JSONArray();



        notification.put(Filter.Type.LOCATION, arrayFilters);
    }

    public void addFilter(String type, Filter... filters){
        JSONArray arrayFilters = new JSONArray();

        for (Filter filter : filters){

            JSONObject btn = new JSONObject();
            if (filter.getField() != null)
                btn.put(Filter.Params.FIELD, filter.getField());
            if (filter.getKey() != null)
                btn.put(Filter.Params.KEY, filter.getKey());
            if (filter.getRelation() != null)
                btn.put(Filter.Params.RELATION, filter.getRelation());
            if (filter.getValue() != null)
                btn.put(Filter.Params.VALUE, filter.getValue());
            if (filter.getHoursAgo() != null)
                btn.put(Filter.Params.HOURS_AGO, filter.getHoursAgo());
            if (filter.getHoursAgo() != null)
                btn.put(Filter.Params.EXISTS, filter.getExists());
            if (filter.getHoursAgo() != null)
                btn.put(Filter.Params.RADIUS, filter.getRadius());
            if (filter.getRadius() != null)
                btn.put(Filter.Params.LATITUDE, filter.getRadius());
            if (filter.getLongitude() != null)
                btn.put(Filter.Params.LONGITUDE, filter.getLongitude());
            if (filter.getLongitude() != null)
                btn.put(Filter.Params.LONGITUDE, filter.getLongitude());
            if (filter.getNotExists() != null)
                btn.put(Filter.Params.NOT_EXISTS, filter.getNotExists());
            if (filter.getTimeElapsedGT() != null)
                btn.put(Filter.Params.TIME_ELAPSED_GT, filter.getTimeElapsedGT());
            if (filter.getTimeElapsedLT() != null)
                btn.put(Filter.Params.TIME_ELAPSED_LT, filter.getTimeElapsedLT());

            arrayFilters.put(btn);
        }

        notification.put(type, arrayFilters);
    }

    public void setLastSessionAfter(float hours){
        notification.put(Filter.Type.LAST_SESSION, " > "+hours);
    }

    public void setLastSessionBefore(float hours){

    }

    public void setLastSessionDuring(float hours){

    }

    public static class Filter{
        private String field, key, relation, value, hoursAgo, exists, radius, latitude, longitude,
        notExists, timeElapsedGT, timeElapsedLT;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getHoursAgo() {
            return hoursAgo;
        }

        public void setHoursAgo(String hoursAgo) {
            this.hoursAgo = hoursAgo;
        }

        public String getExists() {
            return exists;
        }

        public void setExists(String exists) {
            this.exists = exists;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getNotExists() {
            return notExists;
        }

        public void setNotExists(String notExists) {
            this.notExists = notExists;
        }

        public String getTimeElapsedGT() {
            return timeElapsedGT;
        }

        public void setTimeElapsedGT(String timeElapsedGT) {
            this.timeElapsedGT = timeElapsedGT;
        }

        public String getTimeElapsedLT() {
            return timeElapsedLT;
        }

        public void setTimeElapsedLT(String timeElapsedLT) {
            this.timeElapsedLT = timeElapsedLT;
        }

        public class Type{
            public static final String FIRST_SESSION = "first_session";
            public static final String LAST_SESSION = "last_session";
            public static final String SESSION_COUNT = "session_count";
            public static final String USAGE_DURATION = "session_time";
            public static final String AMOUNT_SPENT = "amount_spent";
            public static final String PURCHASED_ITEM = "purchased_item";
            public static final String LANGUAGE = "language";
            public static final String APP_VERSION = "app_version";
            public static final String DEVICE_TYPE = "device_type";
            public static final String USER_TAG = "tag";
            public static final String LOCATION = "location";
            public static final String ROOTED = "rooted";
            public static final String SUBSCRIPTIONS = "subscriptions";
            public static final String EMAIL = "email";
            public static final String COUNTRY = "country";
            public static final String TEST_USERS = "test_users";
        }

        public class Params{
            public static final String FIELD = "field";
            public static final String KEY = "key";
            public static final String RELATION = "relation";
            public static final String VALUE = "value";
            public static final String HOURS_AGO = "hours_ago";
            public static final String EXISTS = "exists";
            public static final String RADIUS = "radius";
            public static final String LATITUDE = "lat";
            public static final String LONGITUDE = "long";
            public static final String NOT_EXISTS = "not_exists";
            public static final String TIME_ELAPSED_GT = "time_elapsed_gt";
            public static final String TIME_ELAPSED_LT = "time_elapsed_lt";
        }
    }
}
