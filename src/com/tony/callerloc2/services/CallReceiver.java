
package com.tony.callerloc2.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tony.callerloc2.CLConfig;
import com.tony.callerloc2.ui.BaseActivity;

/**
 * @author Tony Gao
 */
public class CallReceiver extends BroadcastReceiver {

    private static final String TAG = "CallReceiver";

    // private static final long INCOMING_CALL_DELAY = 1600;// milliseconds

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent == null) {
            return;
        }

        SharedPreferences prefs = context.getSharedPreferences(BaseActivity.PREFERENCES_NAME,
                Context.MODE_PRIVATE);

        if (prefs == null) {
            if (CLConfig.DEBUG) {
                Log.d(TAG, "SharedPreferences not prepared, exit");
            }
            return;
        }

        boolean inEnabled = prefs.getBoolean(BaseActivity.PREFERENCES_KEY_INCOMING_ENABLED, false);
        boolean outEnabled = prefs.getBoolean(BaseActivity.PREFERENCES_KEY_OUTGOING_ENABLED, false);

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (CLConfig.DEBUG) {
            Log.d(TAG,
                    "onreceive, action: " + intent.getAction() + "; callState: "
                            + tm.getCallState());
        }

        // Note: An outgoing call fires this receiver twice for two actions:
        // once for action "android.intent.action.NEW_OUTGOING_CALL" with call state 0,
        // and then once for action "android.intent.action.PHONE_STATE" with call state 2;
        // AND: There won't be any thing happening that can be caught here when this
        // outgoing call is answered!
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            if (!outEnabled) {
                return;
            }
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if (CLConfig.DEBUG) {
                Log.d(TAG, "outgoing call broadcast received, number:" + phoneNumber
                        + "; callState: " + tm.getCallState());
            }

            Intent i = new Intent(context, CallAnswerService.class);
            i.putExtra(Intent.EXTRA_PHONE_NUMBER, phoneNumber);
            i.putExtra(CallAnswerService.EXTRA_CALL_STATE,
                    CallAnswerService.CUSTOM_CALL_STATE_CALLING);
            context.startService(i);
        } else {
            int callState = tm.getCallState();

            if (CLConfig.DEBUG) {
                Log.d(TAG, "broadcast received, call state: " + callState);
            }

            Intent i = new Intent(context, CallAnswerService.class);
            i.putExtra(CallAnswerService.EXTRA_CALL_STATE, callState);
            switch (callState) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (!inEnabled) {
                        return;
                    }
                    String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    i.putExtra(Intent.EXTRA_PHONE_NUMBER, number);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (!inEnabled && !outEnabled) {
                        return;
                    } else {
                        // do nothing
                    }
                    break;
                default:
                    return; // return to not start the service
            }
            context.startService(i);
        }
    }
}
