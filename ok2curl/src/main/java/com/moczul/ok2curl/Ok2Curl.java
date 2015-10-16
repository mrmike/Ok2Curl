package com.moczul.ok2curl;

import com.squareup.okhttp.OkHttpClient;

public class Ok2Curl {

    public static void set(OkHttpClient client) {
        final CurlInterceptor interceptor = new CurlInterceptor();
        client.interceptors().add(interceptor);
    }

    public static void set(OkHttpClient client, String tag, int logLevel) {
        final CurlInterceptor interceptor = new CurlInterceptor(tag, logLevel);
        client.interceptors().add(interceptor);
    }
}
