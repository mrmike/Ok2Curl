package com.moczul.ok2curl;

import android.util.Log;

import com.moczul.ok2curl.logger.AndroidLogger;
import com.moczul.ok2curl.logger.Loggable;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class CurlInterceptor implements Interceptor {

    private static final String TAG = "Ok2Curl";

    private Loggable logger;

    public CurlInterceptor() {
        this(TAG, Log.DEBUG);
    }

    /**
     * Interceptor responsible for printing curl logs
     * @param tag
     * @param logLevel
     */
    public CurlInterceptor(String tag, int logLevel) {
        this.logger = new AndroidLogger(logLevel, tag);
    }

    /* package */ CurlInterceptor(Loggable logger) {
        this.logger = logger;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();

        final Request copy = request.newBuilder().build();
        final String curl = new CurlBuilder(copy).build();

        logger.log(curl);

        return chain.proceed(request);
    }
}
