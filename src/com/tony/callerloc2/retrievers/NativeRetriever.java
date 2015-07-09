
package com.tony.callerloc2.retrievers;

public class NativeRetriever {

    public static native byte[] getLocationFromJni(String filename, String number);

    /** Load jni .so on initialization */
    static {
        System.loadLibrary("phone-number-jni");
    }

}
