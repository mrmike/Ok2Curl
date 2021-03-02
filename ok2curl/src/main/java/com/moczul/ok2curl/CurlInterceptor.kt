package com.moczul.ok2curl

import com.moczul.ok2curl.logger.Loggable
import okhttp3.Interceptor
import okhttp3.Response

class CurlInterceptor @JvmOverloads constructor(
    private val logger: Loggable,
    configuration: Configuration = Configuration()
) : Interceptor {

    private val curlGenerator = CurlGenerator(configuration)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val curl = curlGenerator.generateCommand(request)

        logger.log(curl)

        return chain.proceed(request)
    }
}