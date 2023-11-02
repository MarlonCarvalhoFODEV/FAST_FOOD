package apis.onesignal.notification;

import org.json.JSONArray;

public class OneSignalSegment {

    private OneSignalNotification notification;

    public OneSignalSegment(OneSignalNotification notification) {
        this.notification = notification;
    }

    public void setSegment(String segmentType, boolean activeUsers, boolean inactiveUsers) {
        JSONArray segs = new JSONArray();

        if (activeUsers)
            segs.put(Values.USE_ACTIVE);

        if (inactiveUsers)
            segs.put(Values.USE_INACTIVE);

        notification.put(segmentType, segs);
    }

    public void setSegmentBoth(String segmentType) {
        JSONArray segs = new JSONArray();
        segs.put(Values.USE_ACTIVE);
        segs.put(Values.USE_INACTIVE);

        notification.put(segmentType, segs);
    }

    public class Type {
        public static final String INCLUDED_SEGMENT = "included_segments";
        public static final String EXCLUDED_SEGMENT = "excluded_segments";
    }

    public class Values {
        public static final String USE_ACTIVE = "Active Users";
        public static final String USE_INACTIVE = "Inactive Users";
        public static final String ALL = "All";
    }
}
