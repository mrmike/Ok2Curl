package com.moczul.ok2curl

import com.moczul.ok2curl.logger.Loggable
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.BufferedSink
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.contains
import org.mockito.Mockito
import org.mockito.Mockito.verify
import java.io.IOException

class CookieHandlerTest {

    private val logger: Loggable = Mockito.mock(Loggable::class.java)
    private lateinit var server: MockWebServer
    private lateinit var url: String

    @Before
    fun setUpOkHttp() {
        server = MockWebServer()
        server.start()
        server.enqueue(MockResponse().setBody("ok"))
        url = server.url("/test").toString()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `should intercept failed request and log error information`() {
        // given
        val request: Request = Request.Builder().url(url).patch(object : RequestBody() {
            override fun contentType(): MediaType? {
                return "application/json".toMediaTypeOrNull()
            }

            override fun writeTo(sink: BufferedSink) {
                throw IOException("exception")
            }
        }).build()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(CurlInterceptor(logger))
            .cookieJar(createCookieJar())
            .build()

        // when
        try {
            okHttpClient.newCall(request).execute()
        } catch (ignore: IOException) {
        }

        // then
        verify(logger).log(contains("Error"))
    }

    @Test
    fun `should intercept request with application interceptor`() {
        // given
        val request: Request = Request.Builder().url(url).build()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(CurlInterceptor(logger))
            .cookieJar(createCookieJar())
            .build()

        // when
        okHttpClient.newCall(request).execute()

        // then
        val expectedCurl = String.format("curl -X GET \"%s\"", url)
        verify(logger).log(expectedCurl)
    }

    @Test
    fun `should intercept request with network interceptor`() {
        // given
        val request: Request = Request.Builder().url(url).build()
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(CurlInterceptor(logger))
            .cookieJar(createCookieJar())
            .build()

        // when
        okHttpClient.newCall(request).execute()

        // then
        verify(logger).log(contains("-H \"Cookie:foo=bar; banana=rama\""))
    }

    private fun createCookieJar(): CookieJar {
        return object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val foo: Cookie = Cookie.Builder()
                    .name("foo")
                    .value("bar")
                    .domain(url.host)
                    .build()
                val banana: Cookie = Cookie.Builder()
                    .name("banana")
                    .value("rama")
                    .domain(url.host)
                    .build()
                return listOf(foo, banana)
            }
        }
    }
}