package apis.onesignal.notification;

public class OneSignalAppearance {

    private OneSignalNotification notification ;

    public OneSignalAppearance(OneSignalNotification notification) {
        this.notification = notification;
    }

    public void setTitleColor(String colorARGB){
        notification.put(Params.HEADING_COLOR, colorARGB);
    }

    public void setContentColor(String colorARGB){
        notification.put(Params.CONTENTS_COLOR, colorARGB);
    }

    public void setAndroidSmallIcon(String androidIconID){
        notification.put(Params.SMALL_ICON, androidIconID);
    }

    public void setAndroidLargeIcon(String androidIconID){
        notification.put(Params.LARGE_ICON, androidIconID);
    }

    public void setImage(String image){
        notification.put(Params.IMAGE, image);
    }

    public void setAndroidLedColor(String colorARGB){
        colorARGB = colorARGB.replace("#","");
        notification.put(Params.ANDROID_LED_COLOR, colorARGB);
    }

    public void setAndroidAccentColor(String colorARGB){
        colorARGB = colorARGB.replace("#","");
        notification.put(Params.ANDROID_ACCENT_COLOR, colorARGB);
    }

    public void setAndroidBackgroundLayout(String colorARGB){
        colorARGB = colorARGB.replace("#","");
        notification.put(Params.ANDROID_BACKGROUND_LAYOUT, colorARGB);
    }

    public void setAndroidChannelId(String channelId){
        notification.put(Params.ANDROID_CHANNEL_ID, channelId);
    }

    public void setAndroidExistingChannelId(String channelId){
        notification.put(Params.EXISTING_ANDROID_CHANNEL_ID, channelId);
    }

    public void setAndroidSound(String soundId){
        notification.put(Params.ANDROID_SOUND, soundId);
    }

    @Deprecated
    public void setAndroidVisibility(String channelId){
        notification.put(Params.ANDROID_VISIBILITY, channelId);
    }

    public void setHuaweiLedColor(String colorARGB){
        colorARGB = colorARGB.replace("#","");
        notification.put(Params.HUAWEI_LED_COLOR, colorARGB);
    }

    public void setHuaweiAccentColor(String colorARGB){
        colorARGB = colorARGB.replace("#","");
        notification.put(Params.HUAWEI_ACCENT_COLOR, colorARGB);
    }

    public void setHuaweiSmallIcon(String huaweiIconId){
        notification.put(Params.HUAWEI_SMALL_ICON, huaweiIconId);
    }

    public void setHuaweiLargeIcon(String huaweiIconId){
        notification.put(Params.HUAWEI_LARGE_ICON, huaweiIconId);
    }

    public void setHuaweiChannelId(String channelId){
        notification.put(Params.HUAWEI_CHANNEL_ID, channelId);
    }

    public void setHuaweiExistingChannelId(String channelId){
        notification.put(Params.HUAWEI_EXISTING_CHANNEL_ID, channelId);
    }

    public void setHuaweiSound(String soundId){
        notification.put(Params.HUAWEI_SOUND, soundId);
    }

    @Deprecated
    public void setHuaweiVisibility(String colorARGB){
        colorARGB = colorARGB.replace("#","");
        notification.put(Params.HUAWEI_VISIBILITY, colorARGB);
    }

    public void setAdmSmallIcon(String iconId){
        notification.put(Params.ADM_SMALL_ICON, iconId);
    }

    public void setAdmLargeIcon(String iconId){
        notification.put(Params.ADM_LARGE_ICON, iconId);
    }

    public void setAdmSound(String sound){
        notification.put(Params.ADM_SOUND, sound);
    }

    public void setIosSound(String sound){
        notification.put(Params.IOS_SOUND, sound);
    }

    public void setIosBadgeType(String badgeType){
        notification.put(Params.IOS_BADGE_TYPE, badgeType);
    }

    public void setIosBadgeCount(String badgeCount){
        notification.put(Params.IOS_BADGE_COUNT, badgeCount);
    }

    public void setChromeIcon(String chromeIcon){
        notification.put(Params.CHROME_ICON, chromeIcon);
    }

    public void setChromeWebIcon(String chromeWebIcon){
        notification.put(Params.CHROME_WEB_ICON, chromeWebIcon);
    }

    public void setChromeWebImage(String chromeWebImage){
        notification.put(Params.CHROME_WEB_IMAGE, chromeWebImage);
    }

    public void setChromeWebBadge(String chromeWebBadge){
        notification.put(Params.CHROME_WEB_BADGE, chromeWebBadge);
    }

    public void setFirefoxIcon(String firefoxIcon){
        notification.put(Params.FIREFOX_ICON, firefoxIcon);
    }

    public void setWpWnsSound(String sound){
        notification.put(Params.WP_WNS_SOUND, sound);
    }

    public void setCollapseId(String id){
        notification.put(Params.COLLAPSE_ID, id);
    }

    public void setApnsAlert(String alert){
        notification.put(Params.APNS_ALERT, alert);
    }


    public class Params{
        public static final String ANDROID_CHANNEL_ID = "android_channel_id";
        public static final String HUAWEI_CHANNEL_ID = "huawei_channel_id";
        public static final String EXISTING_ANDROID_CHANNEL_ID = "existing_android_channel_id";
        public static final String HUAWEI_EXISTING_CHANNEL_ID = "huawei_existing_channel_id";
        public static final String ANDROID_BACKGROUND_LAYOUT = "android_background_layout";
        public static final String SMALL_ICON = "small_icon";
        public static final String HUAWEI_SMALL_ICON = "huawei_small_icon";
        public static final String LARGE_ICON = "large_icon";
        public static final String HUAWEI_LARGE_ICON = "huawei_large_icon";
        public static final String ADM_SMALL_ICON = "adm_small_icon";
        public static final String ADM_LARGE_ICON = "adm_large_icon";
        public static final String CHROME_WEB_ICON = "chrome_web_icon";
        public static final String CHROME_WEB_IMAGE = "chrome_web_image";
        public static final String CHROME_WEB_BADGE = "chrome_web_badge";
        public static final String FIREFOX_ICON = "firefox_icon";
        public static final String CHROME_ICON = "chrome_icon";
        public static final String IOS_SOUND = "ios_sound";
        public static final String ANDROID_SOUND = "android_sound";
        public static final String HUAWEI_SOUND = "huawei_sound";
        public static final String ADM_SOUND = "adm_sound";
        public static final String WP_WNS_SOUND = "wp_wns_sound";
        public static final String ANDROID_LED_COLOR = "android_led_color";
        public static final String HUAWEI_LED_COLOR = "huawei_led_color";
        public static final String ANDROID_ACCENT_COLOR = "android_accent_color";
        public static final String HUAWEI_ACCENT_COLOR = "huawei_accent_color";
        public static final String ANDROID_VISIBILITY = "android_visibility";
        public static final String HUAWEI_VISIBILITY = "huawei_visibility";
        public static final String IOS_BADGE_TYPE = "ios_badgeType";
        public static final String IOS_BADGE_COUNT = "ios_badgeCount";
        public static final String COLLAPSE_ID = "collapse_id";
        public static final String APNS_ALERT = "apns_alert";
        public static final String IMAGE = "image";
        public static final String HEADING_COLOR = "headings_color";
        public static final String CONTENTS_COLOR = "contents_color";
    }
}
