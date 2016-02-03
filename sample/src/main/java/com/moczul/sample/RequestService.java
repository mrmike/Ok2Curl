package com.moczul.sample;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Loggable;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

public class RequestService extends IntentService {

    public RequestService() {
        super("RequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        CurlInterceptor curlInterceptor = new CurlInterceptor(new Loggable() {
            @Override
            public void log(String message) {
                Log.v("MyTag", message);
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(curlInterceptor)
                .build();

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/vmg/redcarpet/issues?state=closed")
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();

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
