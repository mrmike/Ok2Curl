package com.moczul.ok2curl;

import com.moczul.ok2curl.logger.Loggable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class CurlInterceptor implements Interceptor {

    private static final String TAG = "Ok2Curl";
    private static final long DEFAULT_LIMIT = 1024L * 1024L;

    private final Loggable logger;
    private final long limit;

    /**
     * Interceptor responsible for printing curl logs
     *
     * Logs are pushed to stdout with 1MB limit
     */
    public CurlInterceptor() {
        this(new Loggable() {
            @Override
            public void log(String message) {
                System.out.println(TAG + " " + message);
            }
        });
    }

    /**
     * Interceptor responsible for printing curl logs
     *
     * Logs are pushed to stdout with 1MB limit
     *
     * @param logger output of logging
     */
    public CurlInterceptor(Loggable logger) {
        this(logger, DEFAULT_LIMIT);
    }

    /**
     * Interceptor responsible for printing curl logs
     *
     * @param logger output of logging
     * @param limit limit maximal bytes logged, if negative - non limited
     */
    public CurlInterceptor(Loggable logger, long limit) {
        this.logger = logger;
        this.limit = limit;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();

        final Request copy = request.newBuilder().build();
        final String curl = new CurlBuilder(copy, limit).build();

        logger.log(curl);

        return chain.proceed(request);
    }
}
