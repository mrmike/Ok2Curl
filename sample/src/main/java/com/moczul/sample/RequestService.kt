package com.moczul.sample

import android.content.Intent
import androidx.core.app.JobIntentService
import com.moczul.ok2curl.Configuration
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.sample.RequestFactory.getRequest
import com.moczul.sample.modifier.Base64Decoder
import com.moczul.sample.modifier.BasicAuthorizationHeaderModifier
import okhttp3.OkHttpClient
import java.io.IOException

class RequestService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val modifier = BasicAuthorizationHeaderModifier(Base64Decoder())
        val config = Configuration(headerModifiers = listOf(modifier))
        val curlInterceptor = CurlInterceptor(AndroidLogger(), config)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(curlInterceptor)
            .build()

        val requestType = intent.getStringExtra(REQUEST_TYPE)
        val request = getRequest(requestType)
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