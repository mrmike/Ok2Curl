package com.moczul.ok2curl;

import com.moczul.ok2curl.logger.Loggable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class CurlInterceptor implements Interceptor {

    private static final String TAG = "Ok2Curl";

    private Loggable logger;

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
     * @param logger
     */
    public CurlInterceptor(Loggable logger) {
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
