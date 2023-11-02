package com.c.hotburguerdelivery.apis.onesignal.notification;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OneSignalNotification extends JSONObject {

    private String language ;

    private final OneSignalAppearance appearances ;
    private final OneSignalFilter filters ;
    private final OneSignalButton buttons ;
    private final OneSignalEmail emails ;
    private final OneSignalSegment segments ;

    public OneSignalNotification() {
        this.appearances = new OneSignalAppearance(this);
        this.filters = new OneSignalFilter(this);
        this.buttons = new OneSignalButton(this);
        this.emails = new OneSignalEmail(this);
        this.segments = new OneSignalSegment(this);
    }

    public void setTitle(String title){
        JSONObject tt = new JSONObject();
        tt.put(language, title);
        put(Params.HEADING_OBJECT, tt);
    }

    public void setSubTitle(String subTitle){
        JSONObject tt = new JSONObject();
        tt.put(language, subTitle);
        put(Params.SUBTITLE_OBJECT, tt);
    }

    public void setMessage(String message){
        JSONObject tt = new JSONObject();
        tt.put(language, message);
        put(Params.CONTENT_OBJECT, tt);
    }

    public void setObject(Object object){
        put(Params.DATA, object);
    }

    public void setContentAvailable(boolean isAvailable){
        put(Params.CONTENT_AVAILABLE_BOOLEAN, isAvailable);
    }

    public void setMutable(boolean mutable){
        put(Params.MUTABLE_BOOLEAN, mutable);
    }

    public void setTemplate(String id){
        put(Params.TEMPLATE_ID_STRING, id);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public OneSignalAppearance getAppearances() {
        return appearances;
    }

    public OneSignalFilter getFilters() {
        return filters;
    }

    public OneSignalButton getButtons() {
        return buttons;
    }

    public OneSignalEmail getEmails() {

        return emails;
    }

    public OneSignalSegment getSegments() {
        return segments;
    }

    public class GroupingCollapsing{
        public static final String ANDROID_GROUP = "android_group";
        public static final String ANDROID_GROUP_MESSAGE = "android_group_message";
        public static final String ADM_GROUP = "adm_group";
        public static final String ADM_GROUP_MESSAGE = "adm_group_message";
        public static final String THREAD_ID = "thread_id";
        public static final String SUMMARY_ARG = "summary_arg";
        public static final String SUMMARY_ARG_COUNT = "summary_arg_count";

    }

    public class PlatformDelivery{
        public static final String IS_IOS = "isIos";
        public static final String IS_ANDROID = "isAndroid";
        public static final String IS_HUAWEI = "isHuawei";
        public static final String IS_ANY_WEB = "isAnyWeb";
        public static final String IS_CHROME_WEB = "isChromeWeb";
        public static final String IS_FIREFOX = "isFirefox";
        public static final String IS_SAFARI = "isSafari";
        public static final String IS_WP_WNS = "isWP_WNS";
        public static final String IS_ADM = "isAdm";
        public static final String IS_CHROME = "isChrome";
        public static final String CHANNEL_FOR_EXTERNAL_USER_ID = "channel_for_external_user_ids";

    }

    public class Delivery{
        public static final String SEND_AFTER = "send_after";
        public static final String DELAYED_OPTION = "delayed_option";
        public static final String DELIVERY_TIME_OF_DELAY = "delivery_time_of_day";
        public static final String TTL = "ttl";
        public static final String PRIORITY = "priority";
        public static final String APNS_PUSH_TYPE_OVERRIDE = "apns_push_type_override";


    }

    public class Params{
        public static final String SEGMENTS = "included_segments" ;
        public static final String FILTERS = "filters" ;
        public static final String DATA = "data" ;
        public static final String CONTENT_OBJECT = "contents" ;
        public static final String HEADING_OBJECT = "headings" ;
        public static final String SUBTITLE_OBJECT = "subtitle" ;
        public static final String TEMPLATE_ID_STRING = "template_id" ;
        public static final String CONTENT_AVAILABLE_BOOLEAN = "content_available" ;
        public static final String MUTABLE_BOOLEAN = "mutable_content" ;
    }

    public class Languages{
        public static final String ENGLISH = "en" ;
        public static final String ARABIC = "ar" ;
        public static final String CATALAN = "ca" ;
        public static final String CHINESE_SIMPLIFIED = "zh-Hans" ;
        public static final String CHINESE_TRADITIONAL = "zh-Hant" ;
        public static final String CROATIAN = "hr" ;
        public static final String CZECH = "cs" ;
        public static final String DANISH = "da" ;
        public static final String DUTCH = "nl" ;
        public static final String ESTONIAN = "et" ;
        public static final String FINNISH = "fi" ;
        public static final String FRENCH = "fr" ;
        public static final String GEORGIAN = "ka" ;
        public static final String BULGARIAN = "bg" ;
        public static final String GERMAN = "de" ;
        public static final String GREEK = "el" ;
        public static final String HINDI = "hi" ;
        public static final String HEBREW = "he" ;
        public static final String HUNGARIAN = "hu" ;
        public static final String INDONESIAN = "id" ;
        public static final String ITALIAN = "it" ;
        public static final String JAPANESE = "ja" ;
        public static final String KOREAN = "ko" ;
        public static final String LATVIAN = "lv" ;
        public static final String LITHUANIAN = "lt" ;
        public static final String MALAY = "ms" ;
        public static final String NORWEGIAN = "nb" ;
        public static final String PERSIAN = "fa" ;
        public static final String POLISH = "pl" ;
        public static final String PORTUGUESE = "pt" ;
        public static final String ROMANIAN = "ro" ;
        public static final String RUSSIAN = "ru" ;
        public static final String SERBIAN = "sr" ;
        public static final String SLOVAK = "sk" ;
        public static final String SPANISH = "es" ;
        public static final String SWEDISH = "sv" ;
        public static final String THAI = "th" ;
        public static final String TURKISH = "tr" ;
        public static final String UKRAINIAN = "uk" ;
        public static final String VIETNAMESE = "vi" ;

    }


    public class Attachments{
        public static final String DATA = "data";
        public static final String HUAWEI_MSG_TYPE = "huawei_msg_type";
        public static final String URL = "url";
        public static final String WEB_URL = "web_url";
        public static final String APP_URL = "app_url";
        public static final String IOS_ATTACHMENT = "ios_attachments";
        public static final String BIG_PICTURE = "big_picture";
        public static final String HUAWEI_BIG_PICTURE = "huawei_big_picture";
        public static final String CHROME_WEB_IMG = "chrome_web_image";
        public static final String ADM_BIG_PICTURE = "adm_big_picture";
        public static final String CHROME_BIG_PICTURE = "chrome_big_picture";

    }

}
