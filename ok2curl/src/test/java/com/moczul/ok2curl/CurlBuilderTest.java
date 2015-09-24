package com.moczul.ok2curl;

import com.squareup.okhttp.Request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CurlBuilderTest {

    @Test
    public void getRequest_hasCorrectCommand() throws Exception {
        final Request request = new Request.Builder().url("http://example.com/").build();
        final String command = new CurlBuilder(request).build();
        assertEquals("curl -X GET http://example.com/", command);
    }
}