package apis.onesignal.notification;

import org.json.JSONArray;
import org.json.JSONObject;

public class OneSignalButton {

    private OneSignalNotification notification;

    public OneSignalButton(OneSignalNotification notification) {
        this.notification = notification;
    }

    public void addButton(String type, Button... buttons) {
        JSONArray arrayButtons = new JSONArray();

        for (Button button : buttons){

            JSONObject btn = new JSONObject();
            if (button.getId() != null)
                btn.put(Params.ID, button.getId());
            if (button.getText() != null)
                btn.put(Params.TEXT, button.getText());
            if (button.getIcon() != null)
                btn.put(Params.ICON, button.getIcon());
            if (button.getUrl() != null)
                btn.put(Params.URL, button.getUrl());

            arrayButtons.put(btn);
        }

        notification.put(type, arrayButtons);
    }

    public static class Button{
        private String id, text, icon, url ;

        public Button() {}

        public Button(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public Button(String id, String text, String icon) {
            this.id = id;
            this.text = text;
            this.icon = icon;
        }

        public Button(String id, String text, String icon, String url) {
            this.id = id;
            this.text = text;
            this.icon = icon;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class Type {

        public static final String BUTTONS = "buttons";
        public static final String WEB_BUTTONS = "web_buttons";
        public static final String IOS_CATEGORIE = "ios_category";

    }

    private class Params {

        public static final String ID = "id";
        public static final String TEXT = "text";
        public static final String ICON = "icon";
        public static final String URL = "url";

    }
}
