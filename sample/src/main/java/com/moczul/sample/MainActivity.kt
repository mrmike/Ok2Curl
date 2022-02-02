package com.moczul.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.moczul.ok2curl.Configuration
import com.moczul.ok2curl.CurlCommandGenerator
import com.moczul.ok2curl.modifier.HeaderModifier
import com.moczul.sample.modifier.Base64Decoder
import com.moczul.sample.modifier.BasicAuthorizationHeaderModifier

class MainActivity : AppCompatActivity() {

    private lateinit var curlLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        curlLog = findViewById(R.id.curl_log)

        findViewById<View>(R.id.get_request).setOnClickListener {
            performRequest(RequestFactory.TYPE_GET)
        }

        findViewById<View>(R.id.post_request).setOnClickListener {
            performRequest(RequestFactory.TYPE_POST)
        }

        findViewById<View>(R.id.get_request_modified).setOnClickListener {
            performRequest(RequestFactory.TYPE_GET_MODIFIED)
        }
    }

    private fun performRequest(type: String) {
        sendRequest(type)
        displayCurlLog(type)
    }

    private fun sendRequest(type: String) {
        val intent = Intent(this, RequestService::class.java)
        intent.putExtra(RequestService.REQUEST_TYPE, type)
        startService(intent)
    }

    private fun displayCurlLog(type: String) {
        val modifier = BasicAuthorizationHeaderModifier(Base64Decoder())
        val modifiers: List<HeaderModifier> = listOf(modifier)
        val commandGenerator = CurlCommandGenerator(Configuration(modifiers))
        val curl: String = commandGenerator.generate(RequestFactory.getRequest(type))
        curlLog.text = curl
    }

}