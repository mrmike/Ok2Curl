package com.moczul.ok2curl

import com.moczul.ok2curl.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response

class CurlInterceptor @JvmOverloads constructor(
    private val logger: Logger,
    configuration: Configuration = Configuration()
) : Interceptor {

    private val curlGenerator = CurlCommandGenerator(configuration)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val curl = curlGenerator.generate(request)
        logger.log(curl)

        return chain.proceed(request)
    }
}