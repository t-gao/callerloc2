
package com.tony.callerloc2.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tony.callerloc2.R;

/**
 * @author Tony Gao
 */
public class BaseActivity extends Activity {

    public static final String PREFERENCES_NAME = "com.tony.callerloc2";
    public static final String PREFERENCES_KEY_INCOMING_ENABLED = "key_in";
    public static final String PREFERENCES_KEY_OUTGOING_ENABLED = "key_out";
    public static final String PREFERENCES_KEY_UPDATE_CALL_LOG_ENABLED = "key_update_call_log_enabled";
    public static final String PREFERENCES_KEY_TEXT_COLOR_POS = "key_txt_color";

    public static final int DEFAULT_COLOR_POS = 9;// deep blue

    protected SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
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
}
