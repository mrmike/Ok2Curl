package com.moczul.ok2curl;

import com.moczul.ok2curl.logger.Loggable;
import com.moczul.ok2curl.modifier.HeaderModifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CurlInterceptor implements Interceptor {

    private static final long DEFAULT_LIMIT = 1024L * 1024L;
    private static final String DEFAULT_DELIMITER = " ";

    protected final Loggable logger;
    protected final long limit;
    protected final List<HeaderModifier> headerModifiers = new ArrayList<>();
    protected final Options options;
    protected final String delimiter;

    /**
     * Interceptor responsible for printing curl logs
     *
     * Logs are pushed to stdout with 1MB limit
     *
     * @param logger output of logging
     */
    public CurlInterceptor(Loggable logger) {
        this(logger, DEFAULT_LIMIT, Collections.emptyList(), Options.EMPTY, DEFAULT_DELIMITER);
    }

    /**
     * Interceptor responsible for printing curl logs
     *
     * Logs are pushed to stdout with 1MB limit
     *
     * @param logger output of logging
     * @param options list of curl options
     */
    public CurlInterceptor(Loggable logger, Options options) {
        this(logger, DEFAULT_LIMIT, Collections.emptyList(), options, DEFAULT_DELIMITER);
    }

    /**
     * Interceptor responsible for printing curl logs
     *
     * @param logger output of logging
     * @param headerModifiers list of header modifiers
     */
    public CurlInterceptor(Loggable logger, List<HeaderModifier> headerModifiers) {
        this(logger, DEFAULT_LIMIT, headerModifiers, Options.EMPTY, DEFAULT_DELIMITER);
    }

    /**
     * Interceptor responsible for printing curl logs
     *
     * @param logger output of logging
     * @param limit limit maximal bytes logged, if negative - non limited
     */
    public CurlInterceptor(Loggable logger, long limit) {
        this(logger, limit, Collections.emptyList(), Options.EMPTY, DEFAULT_DELIMITER);
    }

    /**
     *  Interceptor responsible for printing curl logs
     * @param logger output of logging
     * @param limit limit maximal bytes logged, if negative - non limited
     * @param headerModifiers list of header modifiers
     * @param options list of curl options
     */
    public CurlInterceptor(Loggable logger, long limit, List<HeaderModifier> headerModifiers, Options options) {
        this(logger, limit, headerModifiers, options, DEFAULT_DELIMITER);
    }

    /**
     *  Interceptor responsible for printing curl logs
     * @param logger output of logging
     * @param limit limit maximal bytes logged, if negative - non limited
     * @param headerModifiers list of header modifiers
     * @param options list of curl options
     * @param delimiter string delimiter
     */
    public CurlInterceptor(Loggable logger, long limit, List<HeaderModifier> headerModifiers, Options options, String delimiter) {
        this.logger = logger;
        this.limit = limit;
        this.headerModifiers.addAll(headerModifiers);
        this.options = options;
        this.delimiter = delimiter;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();

        if (logger.isEnable()) {
            final Request copy = request.newBuilder().build();
            final String curl = getCurlBuilder(copy).build();

            logger.log(curl);
        }

        return chain.proceed(request);
    }

    protected CurlBuilder getCurlBuilder(Request copy) {
        return new CurlBuilder(copy, limit, headerModifiers, options, delimiter);
    }
}
