package com.moczul.ok2curl;

import com.moczul.ok2curl.util.FakeLogger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CookieHandlerTest {

    private FakeLogger logger;
    private MockWebServer server;
    private String url;

    @Before
    public void setUpOkHttp() throws IOException {
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

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CurlInterceptor(logger))
                .cookieJar(getCookieJar())
                .build();

        okHttpClient.newCall(request).execute();

        final String expectedCurl = "curl -X GET " + url;
        verify(logger).log(expectedCurl);
    }

    @Test
    public void testNetworkInterceptor() throws IOException {
        final Request request = new Request.Builder().url(url).build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new CurlInterceptor(logger))
                .cookieJar(getCookieJar())
                .build();

        okHttpClient.newCall(request).execute();

        verify(logger).log(contains("-H \"Cookie:foo=bar; banana=rama\""));
    }

    private CookieJar getCookieJar() {
        return new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                Cookie foo = new Cookie.Builder()
                        .name("foo")
                        .value("bar")
                        .domain(url.host())
                        .build();
                Cookie banana = new Cookie.Builder()
                        .name("banana")
                        .value("rama")
                        .domain(url.host())
                        .build();
                return Arrays.asList(foo, banana);
            }
        };
    }
}
