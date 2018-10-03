package com.moczul.sample;

import android.app.IntentService;
import android.content.Intent;

import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.modifier.HeaderModifier;
import com.moczul.sample.modifier.Base64Decoder;
import com.moczul.sample.modifier.BasicAuthorizationHeaderModifier;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RequestService extends IntentService {

    public static final String REQUEST_TYPE = "request_type";

    public RequestService() {
        super("RequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final BasicAuthorizationHeaderModifier modifier = new BasicAuthorizationHeaderModifier(new Base64Decoder());
        final List<HeaderModifier> modifiers = Collections.<HeaderModifier>singletonList(modifier);

        final CurlInterceptor curlInterceptor = new CurlInterceptor(new AndroidLogger(), modifiers);

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
