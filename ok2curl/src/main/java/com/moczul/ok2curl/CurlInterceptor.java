package com.moczul.ok2curl;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/* package */ class CurlInterceptor implements Interceptor {

    private final String tag;
    private final int logLevel;

    /**
     * Interceptor responsible for printing curl logs
     * @param tag
     * @param logLevel
     */
    public CurlInterceptor(String tag, int logLevel) {
        this.tag = tag;
        this.logLevel = logLevel;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();

        final Request copy = request.newBuilder().build();
        final String curl = new CurlBuilder(copy).build();
        Log.println(logLevel, tag, curl);

        return chain.proceed(request);
    }
}
