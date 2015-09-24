package com.moczul.ok2curl;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

public class Ok2Curl {

    private static final String TAG = "Ok2Curl";

    public static void set(OkHttpClient client) {
        set(client, TAG, Log.DEBUG);
    }

    public static void set(OkHttpClient client, String tag, int logLevel) {
        final CurlInterceptor interceptor = new CurlInterceptor(tag, logLevel);
        client.interceptors().add(interceptor);
    }
}
