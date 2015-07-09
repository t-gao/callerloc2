
package com.tony.callerloc2;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tony.callerloc2.ui.BaseActivity;

/**
 * @author Tony Gao
 */
public class CallerlocApp extends Application {

    private static final String TAG = "CallerlocApp";

    private int mTextColorId;
    private int mCurrentCallState = -1;
    private long mRingStartTime = 0l;

    @Override
    public void onCreate() {
        if (CLConfig.DEBUG) {
            Log.d(TAG, "onCreate entered");
        }
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences(BaseActivity.PREFERENCES_NAME, MODE_PRIVATE);

        // Enable show floating window for incoming calls
        prefs.edit().putBoolean(BaseActivity.PREFERENCES_KEY_INCOMING_ENABLED, true).commit();

        int colorPos = prefs.getInt(BaseActivity.PREFERENCES_KEY_TEXT_COLOR_POS,
                BaseActivity.DEFAULT_COLOR_POS);
        if (colorPos > 11) {
            colorPos = BaseActivity.DEFAULT_COLOR_POS;
        }

        synchronized (this) {
            mTextColorId = getResources().getColor(getColorIdByPosition(colorPos));
        }

        TelephonyManager t = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (t != null) {
            synchronized (this) {
                mCurrentCallState = t.getCallState();
            }
        }

        // new DatabaseInitializer(getApplicationContext()).initSpecialNums();
    }

    int getColorIdByPosition(int position) {
        switch (position) {
            case 0:// white
                return R.color.white;
            case 1:// black
                return R.color.black;
            case 2:// red
                return R.color.red;
            case 3:// grey
                return R.color.grey;
            case 4:// sky blue
                return R.color.sky_blue;
            case 5:// grass green
                return R.color.grass_green;
            case 6:// orange
                return R.color.orange;
            case 7:// pink
                return R.color.pink;
            case 8:// brown
                return R.color.brown;
            case 9:// deep blue
                return R.color.deep_blue;
            case 10:// bright green
                return R.color.bright_green;
            case 11:// bright yellow
                return R.color.bright_yellow;
            default:
                break;
        }
        return -1;
    }

    public synchronized int getTextColorId() {
        return mTextColorId;
    }

    public synchronized int getCurrentCallState() {
        return mCurrentCallState;
    }

    public synchronized void setCurrentCallState(int currentCallState) {
        mCurrentCallState = currentCallState;
    }

    public synchronized long getRingStartTime() {
        return mRingStartTime;
    }

    public synchronized void setRingStartTime(long ringStartTime) {
        mRingStartTime = ringStartTime;
    }
}
