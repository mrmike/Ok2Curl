package com.moczul.sample;

import android.app.IntentService;
import android.content.Intent;

import com.moczul.ok2curl.CurlInterceptor;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RequestService extends IntentService {

    public static final String REQUEST_TYPE = "request_type";

    public RequestService() {
        super("RequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /**
         * Alternatively you can specify tag and log level
         * CurlInterceptor curlInterceptor = new CurlInterceptor("MyTag", Log.DEBUG);
         */
        final CurlInterceptor curlInterceptor = new CurlInterceptor();

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(curlInterceptor)
                .build();

        final String requestType = intent.getStringExtra(REQUEST_TYPE);
        final Request request = RequestFactory.getRequest(requestType);

        try {
            /**
             * This call will be logged via logcat
             */
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
