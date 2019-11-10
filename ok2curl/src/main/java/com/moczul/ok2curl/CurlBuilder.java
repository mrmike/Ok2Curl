package com.moczul.ok2curl;

import com.moczul.ok2curl.modifier.HeaderModifier;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;

import static com.moczul.ok2curl.StringUtil.join;

public class CurlBuilder {

    protected static final String FORMAT_HEADER = "-H \"%1$s:%2$s\"";
    protected static final String FORMAT_METHOD = "-X %1$s";
    protected static final String FORMAT_BODY = "-d '%1$s'";
    protected static final String FORMAT_URL = "\"%1$s\"";
    protected static final String CONTENT_TYPE = "Content-Type";

    protected final String url;
    protected final String method;
    protected final String contentType;
    protected final String body;
    protected final List<String> options;
    protected final List<Header> headers;
    protected final String delimiter;

    public CurlBuilder(Request request) {
        this(request, -1L, Collections.emptyList(), Options.EMPTY);
    }

    public CurlBuilder(Request request, long limit, List<HeaderModifier> headerModifiers, Options options) {
        this(request, limit, headerModifiers, options, " ");
    }

    public CurlBuilder(Request request, long limit, List<HeaderModifier> headerModifiers, Options options, String delimiter) {
        this.url = request.url().toString();
        this.method = request.method();
        this.options = Collections.unmodifiableList(options.list());
        this.delimiter = delimiter;
        final RequestBody body = request.body();
        if (body != null) {
            this.contentType = getContentType(body);
            this.body = getBodyAsString(body, limit);
        } else {
            this.contentType = null;
            this.body = null;
        }

        final Headers headers = request.headers();
        final List<Header> modifiableHeaders = new LinkedList<>();
        for (int i = 0; i < headers.size(); i++) {
            final Header header = new Header(headers.name(i), headers.value(i));
            final Header modifiedHeader = modifyHeader(header, headerModifiers);
            if (modifiedHeader != null) {
                modifiableHeaders.add(modifiedHeader);
            }
        }
        this.headers = Collections.unmodifiableList(modifiableHeaders);
    }

    private Header modifyHeader(Header header, List<HeaderModifier> headerModifiers) {
        for (HeaderModifier modifier : headerModifiers) {
            if (modifier.matches(header)) {
                return modifier.modify(header);
            }
        }

        return header;
    }

    private String getContentType(RequestBody body) {
        final MediaType mediaType = body.contentType();
        if (mediaType != null) {
            return mediaType.toString();
        }

        return null;
    }

    private String getBodyAsString(RequestBody body, long limit) {
        try {
            final Buffer sink = new Buffer();

            final MediaType mediaType = body.contentType();
            final Charset charset = getCharset(mediaType);

            if (limit > 0) {
                final BufferedSink buffer = Okio.buffer(new LimitedSink(sink, limit));
                body.writeTo(buffer);
                buffer.flush();
            } else {
                body.writeTo(sink);
            }

            return sink.readString(charset);
        } catch (IOException e) {
            return "Error while reading body: " + e.toString();
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
        parts.addAll(options);
        parts.add(String.format(FORMAT_METHOD, method.toUpperCase()));

        for (Header header : headers) {
            final String headerPart = String.format(FORMAT_HEADER, header.name(), header.value());
            parts.add(headerPart);
        }

        if (contentType != null && !containsName(CONTENT_TYPE, headers)) {
            parts.add(String.format(FORMAT_HEADER, CONTENT_TYPE, contentType));
        }

        if (body != null) {
            parts.add(String.format(FORMAT_BODY, body));
        }

        parts.add(String.format(FORMAT_URL, url));

        return join(delimiter, parts);
    }

    protected boolean containsName(String name, List<Header> headers) {
        for (Header header : headers) {
            if (header.name().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
