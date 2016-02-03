package com.moczul.sample;

import android.util.Log;

import com.moczul.ok2curl.logger.Loggable;

public class AndroidLogger implements Loggable {

    public static final String TAG = "Ok2Curl";

    @Override
    public void log(String message) {
        Log.v(TAG, message);
    }
}
