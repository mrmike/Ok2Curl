package com.moczul.ok2curl;

import com.moczul.ok2curl.modifier.HeaderModifier;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.moczul.ok2curl.StringUtil.join;

public class ConfigurableCurlBuilder extends CurlBuilder {

    public static final int PART_CURL = 0;
    public static final int PART_OPTIONS = 1;
    public static final int PART_METHOD = 2;
    public static final int PART_HEADERS = 3;
    public static final int PART_CONTENT_TYPE = 4;
    public static final int PART_BODY = 5;
    public static final int PART_URL = 6;
    protected static final int[] DEFAULT_PARTS_ORDER = new int[]{PART_CURL, PART_OPTIONS, PART_METHOD, PART_HEADERS, PART_CONTENT_TYPE, PART_BODY, PART_URL};

    protected final int[] partsOrder;

    public ConfigurableCurlBuilder(Request request, long limit, List<HeaderModifier> headerModifiers, Options options, String delimiter) {
        this(request, limit, headerModifiers, options, delimiter, DEFAULT_PARTS_ORDER);
    }

    public ConfigurableCurlBuilder(Request request, long limit, List<HeaderModifier> headerModifiers, Options options, String delimiter, int[] partsOrder) {
        super(request, limit, headerModifiers, options, delimiter);
        this.partsOrder = partsOrder;
    }

    @Override
    public String build() {
        List<String> parts = new ArrayList<>();
        for (int part : partsOrder) {
            switch (part) {
                case PART_CURL: addCurl(parts); break;
                case PART_OPTIONS: addOptions(parts); break;
                case PART_METHOD: addMethod(parts); break;
                case PART_HEADERS: addHeaders(parts); break;
                case PART_CONTENT_TYPE: addContentType(parts); break;
                case PART_BODY: addBody(parts); break;
                case PART_URL: addUrl(parts); break;
            }
        }

        return join(delimiter, parts);
    }

    private void addCurl(List<String> parts) {
        parts.add("curl");
    }

    private void addOptions(List<String> parts) {
        parts.addAll(options);
    }

    private void addMethod(List<String> parts) {
        parts.add(String.format(FORMAT_METHOD, method.toUpperCase()));
    }

    private void addHeaders(List<String> parts) {
        for (Header header : headers) {
            final String headerPart = String.format(FORMAT_HEADER, header.name(), header.value());
            parts.add(headerPart);
        }
    }

    private void addContentType(List<String> parts) {
        if (contentType != null && !containsName(CONTENT_TYPE, headers)) {
            parts.add(String.format(FORMAT_HEADER, CONTENT_TYPE, contentType));
        }
    }

    private void addBody(List<String> parts) {
        if (body != null) {
            parts.add(String.format(FORMAT_BODY, body));
        }
    }

    private void addUrl(List<String> parts) {
        parts.add(String.format(FORMAT_URL, url));
    }
}
