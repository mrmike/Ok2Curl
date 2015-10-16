package com.moczul.ok2curl;

import com.moczul.ok2curl.util.FakeLogger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CookieHandlerTest {

    private OkHttpClient okHttpClient;
    private FakeLogger logger;
    private MockWebServer server;
    private String url;

    @Before
    public void setUpOkHttp() throws IOException {
        okHttpClient = new OkHttpClient();
        okHttpClient.setCookieHandler(getCookieHandler());
        logger = mock(FakeLogger.class);

        server = new MockWebServer();
        server.start();
        server.enqueue(new MockResponse().setBody("ok"));
        url = server.url("/test").toString();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testApplicationInterceptor() throws IOException {
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.interceptors().add(new CurlInterceptor(logger));

        okHttpClient.newCall(request).execute();

        final String expectedCurl = "curl -X GET " + url;
        verify(logger).log(expectedCurl);
    }

    @Test
    public void testNetworkInterceptor() throws IOException {
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.networkInterceptors().add(new CurlInterceptor(logger));

        okHttpClient.newCall(request).execute();

        verify(logger).log(contains("-H \"Cookie:foo=bar; banana=rama;\""));
    }

    private CookieHandler getCookieHandler() {
        return new CookieHandler() {
            @Override
            public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
                final Map<String, List<String>> result = new HashMap<>();
                result.put("Cookie", Collections.singletonList("foo=bar; banana=rama;"));
                return result;
            }

            @Override
            public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {

            }
        };
    }
}
