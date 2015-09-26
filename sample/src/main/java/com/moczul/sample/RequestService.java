package com.moczul.sample;

import android.app.IntentService;
import android.content.Intent;

import com.moczul.ok2curl.Ok2Curl;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

public class RequestService extends IntentService {

    public RequestService() {
        super("RequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        OkHttpClient client = new OkHttpClient();

        /**
         * Alternatively you can specify tag and log level
         * Ok2Curl.set(client, "MyTag", Log.DEBUG);
         */
        Ok2Curl.set(client);

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
