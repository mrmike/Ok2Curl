package com.moczul.ok2curl;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okio.Buffer;

import static com.moczul.ok2curl.StringUtil.join;

/* package */ class CurlBuilder {

    private static final String FORMAT_HEADER = "-H \"%1$s:%2$s\"";
    private static final String FORMAT_METHOD = "-X %1$s";
    private static final String FORMAT_BODY = "-d \"%1$s\"";

    private final String url;
    private String method;
    private String contentType;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public CurlBuilder(Request request) {
        this.url = request.urlString();
        this.method = request.method();
        final RequestBody body = request.body();
        if (body != null) {
            this.contentType = getContentType(body);
            this.body = getBodyAsString(body);
        }

        final Headers headers = request.headers();
        for (int i = 0; i < headers.size(); i++) {
            this.headers.put(headers.name(i), headers.value(i));
        }
    }

    private String getContentType(RequestBody body) {
        final MediaType mediaType = body.contentType();
        if (mediaType != null) {
            return mediaType.toString();
        }

        return null;
    }

    private String getBodyAsString(RequestBody body) {
        try {
            final Buffer sink = new Buffer();
            final MediaType mediaType = body.contentType();
            final Charset charset = getCharset(mediaType);
            body.writeTo(sink);
            return sink.readString(charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Charset getCharset(MediaType mediaType) {
        if (mediaType != null) {
            return mediaType.charset(Charset.defaultCharset());
        }

        return Charset.defaultCharset();
    }

    public String build() {
        List<String> parts = new ArrayList<>();
        parts.add("curl");
        parts.add(String.format(FORMAT_METHOD, method.toUpperCase()));

        for (String key : headers.keySet()) {
            final String headerPart = String.format(FORMAT_HEADER, key, headers.get(key));
            parts.add(headerPart);
        }

        if (contentType != null) {
            parts.add(String.format(FORMAT_HEADER, "Content-Type", contentType));
        }

        if (body != null) {
            parts.add(String.format(FORMAT_BODY, body));
        }

        parts.add(url);

        return join(" ", parts);
    }
}
