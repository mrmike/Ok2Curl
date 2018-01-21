package com.moczul.ok2curl.util;

import com.moczul.ok2curl.CurlBuilder;
import com.moczul.ok2curl.Header;
import com.moczul.ok2curl.Options;
import com.moczul.ok2curl.modifier.HeaderModifier;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import okhttp3.Request;

import static junit.framework.Assert.assertEquals;

public class HeaderModifierTest {

    private final HeaderModifier cookieHeaderModifier = new HeaderModifier() {

        @Override
        public boolean matches(Header header) {
            return "Cookie".equals(header.name());
        }

        @Override
        public Header modify(Header header) {
            return new Header(header.name(), "modifiedCookieValue");
        }
    };

    private final HeaderModifier nullHeaderModifier = new HeaderModifier() {

        @Override
        public boolean matches(Header header) {
            return true;
        }

        @Override
        public Header modify(Header header) {
            return null;
        }
    };

    @Test
    public void curlCommand_shouldContains_modifiedHeader() {
        final Request request = new Request.Builder()
                .url("http://example.com/")
                .header("Cookie", "FIRST=foo")
                .build();

        final List<HeaderModifier> modifiers = Collections.singletonList(cookieHeaderModifier);
        final String command = new CurlBuilder(request, -1L, modifiers, Options.EMPTY).build();

        assertEquals("curl -X GET -H \"Cookie:modifiedCookieValue\" http://example.com/", command);
    }

    @Test
    public void curlCommand_shouldNotBeModified_ifDoesNotContainMatchingHeader() {
        final Request request = new Request.Builder()
                .url("http://example.com/")
                .header("Accept", "application/json")
                .build();
        final List<HeaderModifier> modifiers = Collections.singletonList(cookieHeaderModifier);

        final String command = new CurlBuilder(request, -1L, modifiers, Options.EMPTY).build();

        assertEquals("curl -X GET -H \"Accept:application/json\" http://example.com/", command);
    }

    @Test
    public void curlCommand_shouldNotContainsAnyHeaders_forNullHeaderModifier() {
        final Request request = new Request.Builder()
                .url("http://example.com/")
                .header("Cookie", "FIRST=foo")
                .header("Accept", "application/json")
                .build();

        final List<HeaderModifier> modifiers = Collections.singletonList(nullHeaderModifier);
        final String command = new CurlBuilder(request, -1L, modifiers, Options.EMPTY).build();

        assertEquals(command, "curl -X GET http://example.com/", command);
    }
}
