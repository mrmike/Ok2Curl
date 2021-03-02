package com.moczul.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.moczul.ok2curl.Configuration
import com.moczul.ok2curl.CurlGenerator
import com.moczul.sample.RequestFactory.getRequest
import com.moczul.sample.RequestService
import com.moczul.sample.modifier.Base64Decoder
import com.moczul.sample.modifier.BasicAuthorizationHeaderModifier

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var curlLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        curlLog = findViewById(R.id.curl_log)

        findViewById<View>(R.id.get_request).setOnClickListener(this)
        findViewById<View>(R.id.post_request).setOnClickListener(this)
        findViewById<View>(R.id.get_request_modified).setOnClickListener(this)
    }

    private fun sendRequest(type: String) {
        val intent = Intent(this, RequestService::class.java)
        intent.putExtra(RequestService.REQUEST_TYPE, type)
        startService(intent)
    }

    private fun displayCurlLog(type: String) {
        val modifier = BasicAuthorizationHeaderModifier(Base64Decoder())
        val curlGenerator = CurlGenerator(configuration = Configuration(listOf(modifier)))
        val curl = curlGenerator.generateCommand(getRequest(type))
        curlLog.text = curl
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.get_request -> {
                sendRequest(RequestFactory.TYPE_GET)
                displayCurlLog(RequestFactory.TYPE_GET)
            }
            R.id.post_request -> {
                sendRequest(RequestFactory.TYPE_POST)
                displayCurlLog(RequestFactory.TYPE_POST)
            }
            R.id.get_request_modified -> {
                sendRequest(RequestFactory.TYPE_GET_MODIFIED)
                displayCurlLog(RequestFactory.TYPE_GET_MODIFIED)
            }
            else -> throw IllegalArgumentException("Invalid view id")
        }
    }
}