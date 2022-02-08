package com.moczul.ok2curl

import okhttp3.CacheControl
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit


class CurlCommandGeneratorTest {

    private val configuration = Configuration()

    @Test
    fun `should generate simple GET command`() {
        // given
        val curlGenerator = CurlCommandGenerator(configuration)
        val request: Request = Request.Builder().url("http://example.com/").build()

        // when
        val command = curlGenerator.generate(request)

        // then
        assertEquals("curl -X GET \"http://example.com/\"", command)
    }

    @Test
    fun `should generate get command with headers`() {
        // given
        val curlGenerator = CurlCommandGenerator(configuration)
        val request: Request = Request.Builder()
            .url("http://example.com/")
            .header("Accept", "application/json")
            .build()

        // when
        val command = curlGenerator.generate(request)

        // then
        assertEquals("curl -X GET -H \"Accept:application/json\" \"http://example.com/\"", command)
    }

    @Test
    fun `should generate GET command with cache-control header`() {
        // given
        val curlGenerator = CurlCommandGenerator(configuration)
        val request: Request = Request.Builder()
            .url("http://example.com/")
            .cacheControl(oneDayCache())
            .build()

        // when
        val command = curlGenerator.generate(request)

        // then
        assertEquals(
            "curl -X GET -H \"Cache-Control:max-age=86400, only-if-cached\" \"http://example.com/\"",
            command
        )
    }

    @Test
    fun `should generate POST command with body`() {
        // given
        val curlGenerator = CurlCommandGenerator(configuration)
        val request: Request = Request.Builder().url("http://example.com/").post(body()).build()

        // when
        val command = curlGenerator.generate(request)

        // then
        val expected =
            "curl -X POST -H \"Content-Type:application/x-www-form-urlencoded\" -d 'key1=value1' \"http://example.com/\""
        assertEquals(expected, command)
    }

    @Test
    fun postRequestBodyWithNullMediaType() {
        // given
        val curlGenerator = CurlCommandGenerator(configuration)
        val body = "StringBody".toRequestBody(contentType = null)
        val request: Request = Request.Builder().url("http://example.com/").post(body).build()

        // when
        val command = curlGenerator.generate(request)

        // then
        val expected = "curl -X POST -d 'StringBody' \"http://example.com/\""
        assertEquals(expected, command)
    }

    @Test
    fun `should generate GET command with mutli-value header`() {
        // given
        val curlGenerator = CurlCommandGenerator(configuration)
        val request: Request = Request.Builder()
            .url("http://example.com/")
            .addHeader("Cookie", "FIRST=foo")
            .addHeader("Cookie", "SECOND=bar")
            .build()

        // when
        val command = curlGenerator.generate(request)

        // when
        assertEquals(
            "curl -X GET -H \"Cookie:FIRST=foo\" -H \"Cookie:SECOND=bar\" \"http://example.com/\"",
            command
        )
    }

    @Test
    fun `should generate GET command with insecure flag`() {
        // given
        val insecureConfig = Configuration(flags = Flags.builder().insecure().build())
        val curlGenerator = CurlCommandGenerator(insecureConfig)
        val request: Request = Request.Builder().url("http://example.com/").build()

        // when
        val command = curlGenerator.generate(request)

        // then
        assertEquals("curl --insecure -X GET \"http://example.com/\"", command)
    }

    @Test
    fun `should generate GET command with connect-timeout flag`() {
        // given
        val timeoutConfig = Configuration(flags = Flags.builder().connectTimeout(120).build())
        val curlGenerator = CurlCommandGenerator(timeoutConfig)
        val request: Request = Request.Builder().url("http://example.com/").build()

        // when
        val command = curlGenerator.generate(request)

        // †hen
        assertEquals("curl --connect-timeout 120 -X GET \"http://example.com/\"", command)
    }

    @Test
    fun `should generate GET command using specific delimiter`() {
        // given
        val delimiterConfig = Configuration(delimiter = " \\\n")
        val curlGenerator = CurlCommandGenerator(delimiterConfig)
        val request: Request = Request.Builder().url("http://example.com/").build()

        // when
        val command = curlGenerator.generate(request)

        // then
        assertEquals("curl \\\n-X GET \\\n\"http://example.com/\"", command)
    }

    @Test
    fun `should generate POST command with specified components`() {
        // given
        val simpleConfig = Configuration(
            components = listOf(
                CommandComponent.Curl,
                CommandComponent.Method,
                CommandComponent.Url
            )
        )
        val curlGenerator = CurlCommandGenerator(simpleConfig)
        val request: Request = Request.Builder()
            .url("https://github.com")
            .cacheControl(oneDayCache())
            .post(body())
            .build()

        // when
        val command = curlGenerator.generate(request)

        // then
        assertEquals("curl -X POST \"https://github.com/\"", command)
    }

    private companion object {
        fun body() = FormBody.Builder().add("key1", "value1").build()

        fun oneDayCache() =
            CacheControl.Builder().maxAge(1, TimeUnit.DAYS).onlyIfCached().build()
    }
}