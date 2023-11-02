package br.com.hotbdelivery.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import static apis.image.APIImage.readBitmap;

public class Utils {

    public static class FieldText{

        public static void setMaskInEditText(EditText et, String mask){
            SimpleMaskFormatter smf = new SimpleMaskFormatter(mask);
            MaskTextWatcher mtw = new MaskTextWatcher(et, smf);
            et.addTextChangedListener(mtw);
        }
    }

    public static class Image{

        public static synchronized Bitmap readBitmap(Context context, Uri uri, int resizeFactor){
            Bitmap bitmap = null ;
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = resizeFactor ;
            AssetFileDescriptor fileDescriptor = null ;

            try{

                fileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri,"r");
            }catch(FileNotFoundException e){
                e.printStackTrace();
            } finally {
                try{
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(),null,options);

                    fileDescriptor.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            int imgWidth = bitmap.getWidth() ;
            int height = bitmap.getHeight() ;

            if(imgWidth >= 1000 && imgWidth <= 1500  || height <= 1500 && height >= 1000 ){
                height = (int)(height / 4) ;
                imgWidth = (int)(imgWidth / 4) ;
            }
            else if(imgWidth >= 650 | height >= 650 && imgWidth <= 1000 | height <= 1000 ){
                height = (int)(height / 3) ;
                imgWidth = (int)(imgWidth / 3) ;
            }
            else if(imgWidth <= 650 | height <= 650){
                //Toast.makeText(this, "Image", Toast.LENGTH_SHORT).show();
            }
            else{
                return null ;
            }

            return Bitmap.createScaledBitmap(bitmap,imgWidth, height, false);
        }

        public byte[] convertToByteArray(Bitmap bitmap, int quality){
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bout);

            return (bout.toByteArray());
        }
    }

    public static class SystemUI{


        /**
         * Based on the following Stackoverflow answer:
         * http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
         */
        @SuppressWarnings("WeakerAccess")
        public static class KeyboardUtils implements ViewTreeObserver.OnGlobalLayoutListener
        {
            private final static int MAGIC_NUMBER = 200;

            private SoftKeyboardToggleListener mCallback;
            private View mRootView;
            private Boolean prevValue = null;
            private float mScreenDensity;
            private static HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();

            public interface SoftKeyboardToggleListener
            {
                void onToggleSoftKeyboard(boolean isVisible);
            }


            @Override
            public void onGlobalLayout()
            {
                Rect r = new Rect();
                mRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
                float dp = heightDiff/ mScreenDensity;
                boolean isVisible = dp > MAGIC_NUMBER;

                if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
                    prevValue = isVisible;
                    mCallback.onToggleSoftKeyboard(isVisible);
                }
            }

            /**
             * Add a new keyboard listener
             * @param act calling activity
             * @param listener callback
             */
            public static void addKeyboardToggleListener(Activity act, SoftKeyboardToggleListener listener)
            {
                removeKeyboardToggleListener(listener);

                sListenerMap.put(listener, new KeyboardUtils(act, listener));
            }

            /**
             * Remove a registered listener
             * @param listener {@link SoftKeyboardToggleListener}
             */
            public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener)
            {
                if(sListenerMap.containsKey(listener))
                {
                    KeyboardUtils k = sListenerMap.get(listener);
                    k.removeListener();

                    sListenerMap.remove(listener);
                }
            }

            /**
             * Remove all registered keyboard listeners
             */
            public static void removeAllKeyboardToggleListeners()
            {
                for(SoftKeyboardToggleListener l : sListenerMap.keySet())
                    sListenerMap.get(l).removeListener();

                sListenerMap.clear();
            }

            /**
             * Manually toggle soft keyboard visibility
             * @param context calling context
             */
            public static void toggleKeyboardVisibility(Context context)
            {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager != null)
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

            /**
             * Force closes the soft keyboard
             * @param activeView the view with the keyboard focus
             */
            public static void forceCloseKeyboard(View activeView)
            {
                InputMethodManager inputMethodManager = (InputMethodManager) activeView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager != null)
                    inputMethodManager.hideSoftInputFromWindow(activeView.getWindowToken(), 0);
            }

            private void removeListener()
            {
                mCallback = null;

                mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }

            public KeyboardUtils(Activity act, SoftKeyboardToggleListener listener)
            {
                mCallback = listener;

                mRootView = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
                mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

                mScreenDensity = act.getResources().getDisplayMetrics().density;
            }

        }

        public static void hideSystemUI(Activity activity) {
            // Set the IMMERSIVE flag.
            // Set the content to appear under the system bars so that the content
            // doesn't resize when the system bars hide and show.
            if(activity != null){
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }


        }

        // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
        public static void showSystemUI(Activity activity) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        public static final void hideKeyboard(Context context, View view){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        public static final boolean keyboardShow(Activity activity){
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            return imm.isAcceptingText();
        }
    }
}
