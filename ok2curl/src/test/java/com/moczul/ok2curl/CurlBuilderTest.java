package com.moczul.ok2curl;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class CurlBuilderTest {

    @Test
    public void getRequest_hasCorrectCommand() {
        final Request request = new Request.Builder().url("http://example.com/").build();
        final String command = new CurlBuilder(request).build();

        assertEquals("curl -X GET http://example.com/", command);
    }

    @Test
    public void getRequest_hasCorrectHeader() {
        final Request request = new Request.Builder()
                .url("http://example.com/")
                .header("Accept", "application/json")
                .build();
        final String command = new CurlBuilder(request).build();

        assertEquals("curl -X GET -H \"Accept:application/json\" http://example.com/", command);
    }

    @Test
    public void getRequest_hasCorrectCacheHeader() {
        final CacheControl cache  = new CacheControl.Builder()
                .maxAge(1, TimeUnit.DAYS)
                .onlyIfCached()
                .build();

        final Request request = new Request.Builder()
                .url("http://example.com/")
                .cacheControl(cache)
                .build();

        final String command = new CurlBuilder(request).build();

        assertEquals("curl -X GET -H \"Cache-Control:max-age=86400, only-if-cached\" http://example.com/", command);
    }

    @Test
    public void postRequest_hasCorrectPostData() {
        final Request request = new Request.Builder().url("http://example.com/").post(body()).build();
        final String command = new CurlBuilder(request).build();

        final String expected = "curl -X POST -H \"Content-Type:application/x-www-form-urlencoded\" -d \"key1=value1\" http://example.com/";
        assertEquals(expected, command);
    }

    private RequestBody body() {
        return new FormEncodingBuilder().add("key1", "value1").build();
    }
}