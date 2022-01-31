package com.moczul.sample;

import android.util.Log;

import androidx.annotation.NonNull;

import com.moczul.ok2curl.logger.Logger;

public class AndroidLogger implements Logger {

    public static final String TAG = "Ok2Curl";

    @Override
    public void log(@NonNull String message) {
        Log.v(TAG, message);
    }
}
