package com.moczul.ok2curl;

import com.moczul.ok2curl.modifier.HeaderModifier;

import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static org.junit.Assert.assertEquals;

public class CurlBuilderTest {

    @Test
    public void getRequestHasCorrectCommand() {
        final Request request = new Request.Builder().url("http://example.com/").build();

        final String command = new CurlBuilder(request).build();

        assertEquals("curl -X GET \"http://example.com/\"", command);
    }

    @Test
    public void getRequestHasCorrectHeader() {
        final Request request = new Request.Builder()
                .url("http://example.com/")
                .header("Accept", "application/json")
                .build();

        final String command = new CurlBuilder(request).build();

        assertEquals("curl -X GET -H \"Accept:application/json\" \"http://example.com/\"", command);
    }

    @Test
    public void getRequestHasCorrectCacheHeader() {
        final CacheControl cache  = new CacheControl.Builder()
                .maxAge(1, TimeUnit.DAYS)
                .onlyIfCached()
                .build();

        final Request request = new Request.Builder()
                .url("http://example.com/")
                .cacheControl(cache)
                .build();

        final String command = new CurlBuilder(request).build();

        assertEquals("curl -X GET -H \"Cache-Control:max-age=86400, only-if-cached\" \"http://example.com/\"", command);
    }

    @Test
    public void postRequestHasCorrectPostData() {
        final Request request = new Request.Builder().url("http://example.com/").post(body()).build();

        final String command = new CurlBuilder(request).build();

        final String expected = "curl -X POST -H \"Content-Type:application/x-www-form-urlencoded\" -d 'key1=value1' \"http://example.com/\"";
        assertEquals(expected, command);
    }

    @Test
    public void postRequestBodyWithNullMediaType() {
        final RequestBody body = RequestBody.create(null, "StringBody");
        final Request request = new Request.Builder().url("http://example.com/").post(body).build();

        final String command = new CurlBuilder(request).build();

        final String expected = "curl -X POST -d 'StringBody' \"http://example.com/\"";
        assertEquals(expected, command);
    }

    @Test
    public void multipleHeadersWithTheSameNameShouldBeAddedToCurlCommand() {
        final Request request = new Request.Builder()
                .url("http://example.com/")
                .addHeader("Cookie", "FIRST=foo")
                .addHeader("Cookie", "SECOND=bar")
                .build();

        final String command = new CurlBuilder(request).build();

        assertEquals("curl -X GET -H \"Cookie:FIRST=foo\" -H \"Cookie:SECOND=bar\" \"http://example.com/\"", command);
    }

    @Test
    public void getRequestContainsInsecureOption() {
        final Request request = new Request.Builder().url("http://example.com/").build();
        final Options options = Options.builder().insecure().build();

        final String command = new CurlBuilder(request, 1024, Collections.<HeaderModifier>emptyList(), options).build();

        assertEquals("curl --insecure -X GET \"http://example.com/\"", command);
    }

    @Test
    public void getRequestContainsConnectTimeoutOptions() {
        final Request request = new Request.Builder().url("http://example.com/").build();
        final Options options = Options.builder().connectTimeout(120).build();

        final String command = new CurlBuilder(request, 1024, Collections.<HeaderModifier>emptyList(), options).build();

        assertEquals("curl --connect-timeout 120 -X GET \"http://example.com/\"", command);
    }

    private RequestBody body() {
        return new FormBody.Builder().add("key1", "value1").build();
    }
}
