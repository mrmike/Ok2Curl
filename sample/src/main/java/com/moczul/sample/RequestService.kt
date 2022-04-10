package com.moczul.sample

import android.app.IntentService
import android.content.Intent
import com.moczul.ok2curl.Configuration
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.sample.modifier.Base64Decoder
import com.moczul.sample.modifier.BasicAuthorizationHeaderModifier
import okhttp3.OkHttpClient
import java.io.IOException

class RequestService : IntentService("RequestService") {

    override fun onHandleIntent(intent: Intent?) {
        val modifier = BasicAuthorizationHeaderModifier(Base64Decoder())
        val config = Configuration(headerModifiers = listOf(modifier))
        val curlInterceptor = CurlInterceptor(AndroidLogger(), config)

        val client = OkHttpClient.Builder()
            .addInterceptor(curlInterceptor)
            .build()
        val requestType = intent?.getStringExtra(REQUEST_TYPE)
        val request = RequestFactory.getRequest(requestType)

        try {
            // This call will be logged via logcat
            client.newCall(request).execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val REQUEST_TYPE = "request_type"
    }
}