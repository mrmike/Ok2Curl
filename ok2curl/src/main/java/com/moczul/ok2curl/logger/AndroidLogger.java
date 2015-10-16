package com.moczul.ok2curl.logger;

import android.util.Log;

public class AndroidLogger implements Loggable {

    private final int logLevel;
    private final String tag;

    public AndroidLogger(int logLevel, String tag) {
        this.logLevel = logLevel;
        this.tag = tag;
    }

    @Override
    public void log(String message) {
        Log.println(logLevel, tag, message);
    }
}
