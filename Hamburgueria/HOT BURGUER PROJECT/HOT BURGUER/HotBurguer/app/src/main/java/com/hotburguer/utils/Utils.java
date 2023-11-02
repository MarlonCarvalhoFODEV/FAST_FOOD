package com.hotburguer.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.hotburguer.R;
import com.hotburguer.fragments.payment.Payment;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;

public class Utils {

    public static class FieldText{

        public static void setMaskInEditText(EditText et, String mask){
            SimpleMaskFormatter smf = new SimpleMaskFormatter(mask);
            MaskTextWatcher mtw = new MaskTextWatcher(et, smf);
            et.addTextChangedListener(mtw);
        }
    }

    public static class Image{

    }

    public static class PaymentResult {
        public static int decodeBackgroundColor(int paymentResultCode){;
            switch (paymentResultCode){
                case Payment.PAID: return R.color.checkoutSuccess;
                case Payment.PENDING_REFUND: return R.color.checkoutPendingRefund;
                case Payment.CHARGEBACK: return R.color.checkoutChargeback;
                case Payment.PROCESSING: return R.color.checkoutProcessing;
                case Payment.REFUSED: return R.color.checkoutRefused;
                case Payment.AUTHORIZED: return R.color.checkoutAuthorized;
                case Payment.REFUNDED: return R.color.checkoutRefunded;
                default: return R.color.checkoutRefused ;
            }
        }

        public static int decodeBackgroundDrawable(int paymentResultCode){;
            switch (paymentResultCode){
                case Payment.PAID: return R.drawable.ic_buy_success;
                case Payment.PENDING_REFUND: return R.drawable.ic_buy_pending;
                case Payment.CHARGEBACK: return R.drawable.ic_buy_chargeback;
                case Payment.PROCESSING: return R.drawable.ic_buy_pending;
                case Payment.REFUSED: return R.drawable.ic_buy_refused;
                case Payment.AUTHORIZED: return R.drawable.ic_buy_success;
                case Payment.REFUNDED: return R.drawable.ic_buy_refused;
                default: return R.color.checkoutRefused ;
            }
        }

        public static String decodeTitle(int paymentResultCode){
            switch (paymentResultCode){
                case Payment.PAID:  return "Obrigado";
                case Payment.PENDING_REFUND: return "Pendente";
                case Payment.CHARGEBACK: return "Estornado";
                case Payment.PROCESSING: return "Em Processamento" ;
                case Payment.REFUSED: return "Recusado";
                case Payment.AUTHORIZED: return "Autorizado";
                case Payment.REFUNDED: return "Devolvido" ;
                default: return "ERRO INESPERADO!" ;
            }
        }


        public static String decodeSubTitle(int paymentResultCode){
            switch (paymentResultCode){
                case Payment.PAID:  return "Pagamento realizado com sucesso";
                case Payment.PENDING_REFUND: return "Pagamento não concluído";
                case Payment.CHARGEBACK: return "Transação sofreu chargeback";
                case Payment.PROCESSING: return "Em Processo de autorização" ;
                case Payment.REFUSED: return "Pagamento não aprovado!";
                case Payment.AUTHORIZED: return "Pagamento autorizado";
                case Payment.REFUNDED: return "O valor foi devolvido" ;
                default: return "Fique tranquilo(a), não foi debitado do seu cartão" ;
            }
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
    }

    public static class Network {

        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
            return false;
        }
    }
}
