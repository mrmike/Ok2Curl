package com.moczul.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moczul.ok2curl.Configuration;
import com.moczul.ok2curl.CurlGenerator;
import com.moczul.ok2curl.modifier.HeaderModifier;
import com.moczul.sample.modifier.Base64Decoder;
import com.moczul.sample.modifier.BasicAuthorizationHeaderModifier;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView curlLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        curlLog = findViewById(R.id.curl_log);

        findViewById(R.id.get_request).setOnClickListener(this);
        findViewById(R.id.post_request).setOnClickListener(this);
        findViewById(R.id.get_request_modified).setOnClickListener(this);
    }

    private void sendRequest(String type) {
        final Intent intent = new Intent(this, RequestService.class);
        intent.putExtra(RequestService.REQUEST_TYPE, type);

        startService(intent);
    }

    private void displayCurlLog(String type) {
        final BasicAuthorizationHeaderModifier modifier = new BasicAuthorizationHeaderModifier(new Base64Decoder());
        final List<HeaderModifier> modifiers = Collections.singletonList(modifier);

        final CurlGenerator curlGenerator = new CurlGenerator(new Configuration(modifiers));
        final String curl = curlGenerator.generateCommand(RequestFactory.getRequest(type));
        curlLog.setText(curl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_request:
                sendRequest(RequestFactory.TYPE_GET);
                displayCurlLog(RequestFactory.TYPE_GET);
                break;
            case R.id.post_request:
                sendRequest(RequestFactory.TYPE_POST);
                displayCurlLog(RequestFactory.TYPE_POST);
                break;
            case R.id.get_request_modified:
                sendRequest(RequestFactory.TYPE_GET_MODIFIED);
                displayCurlLog(RequestFactory.TYPE_GET_MODIFIED);
                break;
            default:
                throw new IllegalArgumentException("Invalid view id");
        }
    }
}
