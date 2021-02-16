package com.moczul.sample;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.moczul.ok2curl.Configuration;
import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.sample.modifier.Base64Decoder;
import com.moczul.sample.modifier.BasicAuthorizationHeaderModifier;

import java.io.IOException;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RequestService extends JobIntentService {

    public static final String REQUEST_TYPE = "request_type";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final BasicAuthorizationHeaderModifier modifier = new BasicAuthorizationHeaderModifier(new Base64Decoder());
        final Configuration config = new Configuration(Collections.singletonList(modifier));

        final CurlInterceptor curlInterceptor = new CurlInterceptor(new AndroidLogger(), config);

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(curlInterceptor)
                .build();

        final String requestType = intent.getStringExtra(REQUEST_TYPE);
        final Request request = RequestFactory.getRequest(requestType);

        try {
            // This call will be logged via logcat
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
