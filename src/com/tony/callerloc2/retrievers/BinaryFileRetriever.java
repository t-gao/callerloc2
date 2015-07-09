
package com.tony.callerloc2.retrievers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.tony.callerloc2.CLConfig;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class BinaryFileRetriever {
    private final static String fileName = "AreaData.dat";
    private static String fileDir = null;
    private final static String TAG = "BinaryFileRetriever";

    /**
     * Gets the callerloc info
     *
     * @param incomingNumber
     * @param context
     * @return the location of the number
     */
    public static String getCallerInfo(String incomingNumber, Context context) {
        if (CLConfig.DEBUG) {
            Log.v(TAG, "incomingNumber:" + incomingNumber);
        }
        String result = "";
        String searchNum = getSearchNum(incomingNumber);
        if (CLConfig.DEBUG) {
            Log.v(TAG, "searchNum:" + searchNum);
        }
        if (!TextUtils.isEmpty(searchNum)) {
            result = getLocation(searchNum, context);
            if (CLConfig.DEBUG) {
                Log.v(TAG, "location:" + result);
            }
        }
        return result;
    }

    private static String getSearchNum(String phoneNumber) {
        String searchNum = "";
        searchNum = phoneNumber.replace("-", "");
        searchNum = searchNum.replace(" ", "");
        if (searchNum.startsWith("+86")) {
            searchNum = searchNum.replace("+86", "");
        }

        if (CLConfig.DEBUG) {
            Log.v(TAG, "num:" + searchNum);
        }

        if (searchNum.matches("^[0-9]*[1-9][0-9]*$")) {
            int len = searchNum.length();
            // telephone number start with '0' like 075582720818
            // mobile phone number not start with '0'
            if (searchNum.startsWith("0")) {
                if (len == 12 || len == 11 || len == 10) {
                    // {"10","20","21","22","23","24","25","27","28","29"}
                    if (Integer.parseInt(searchNum.substring(1, 3)) <= 29) {
                        searchNum = searchNum.substring(1, 3);
                    } else {
                        searchNum = searchNum.substring(1, 4);
                    }
                }
            } else {
                // the database only store number like '1367002' have 7 digit
                if (len == 11) {
                    searchNum = searchNum.substring(0, 7);
                }
            }
        } else {
            searchNum = "";
        }

        return searchNum;
    }

    /**
     * Gets location from data by the native retriever
     * 
     * @param number
     * @param context
     * @return
     */
    private static String getLocation(String number, Context context) {
        copyDatToFilesIfNecessary(context);
        byte[] result = NativeRetriever.getLocationFromJni(fileDir, number);
        String location = "";
        try {
            location = new String(result, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return location;
    }

    private static void copyDatToFilesIfNecessary(Context context) {
        if (!dataFileExists(context)) {
            try {
                InputStream inputStream = context.getAssets().open(fileName);
                int length = inputStream.available();
                byte[] buffer = new byte[length];
                inputStream.read(buffer);
                inputStream.close();
                FileOutputStream fileOutputStream = context
                        .openFileOutput(fileName, Context.MODE_PRIVATE);
                fileOutputStream.write(buffer);
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean dataFileExists(Context context) {
        if (TextUtils.isEmpty(fileDir)) {
            fileDir = context.getFilesDir().getPath() + File.separator + fileName;
        }
        return new File(fileDir).exists();
    }
}
