package com.moczul.ok2curl.util

import com.moczul.ok2curl.Configuration
import com.moczul.ok2curl.CurlGenerator
import com.moczul.ok2curl.Header
import com.moczul.ok2curl.modifier.HeaderModifier
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test

class HeaderModifierTest {
    private val cookieHeaderModifier: HeaderModifier = object : HeaderModifier {
        override fun matches(header: Header): Boolean {
            return "Cookie" == header.name
        }

        override fun modify(header: Header): Header {
            return Header(header.name, "modifiedCookieValue")
        }
    }
    private val nullHeaderModifier: HeaderModifier = object : HeaderModifier {
        override fun matches(header: Header): Boolean {
            return true
        }

        override fun modify(header: Header): Header? {
            return null
        }
    }

    @Test
    fun `should generate GET command with modified header`() {
        // given
        val config = Configuration(headerModifiers = listOf(cookieHeaderModifier))
        val curlGenerator = CurlGenerator(configuration = config)
        val request: Request = Request.Builder()
            .url("http://example.com/")
            .header("Cookie", "FIRST=foo")
            .build()

        // when
        val command = curlGenerator.generateCommand(request)

        // then
        assertEquals(
            "curl -X GET -H \"Cookie:modifiedCookieValue\" \"http://example.com/\"",
            command
        )
    }

    @Test
    fun `should generate GET command with unmodified header`() {
        // given
        val config = Configuration(headerModifiers = listOf(cookieHeaderModifier))
        val curlGenerator = CurlGenerator(configuration = config)
        val request: Request = Request.Builder()
            .url("http://example.com/")
            .header("Accept", "application/json")
            .build()

        // when
        val command = curlGenerator.generateCommand(request)

        // then
        assertEquals(
            "curl -X GET -H \"Accept:application/json\" \"http://example.com/\"",
            command
        )
    }

    @Test
    fun `should generate GET command without any headers`() {
        // given
        val config = Configuration(headerModifiers = listOf(nullHeaderModifier))
        val curlGenerator = CurlGenerator(configuration = config)
        val request: Request = Request.Builder()
            .url("http://example.com/")
            .header("Cookie", "FIRST=foo")
            .header("Accept", "application/json")
            .build()

        // when
        val command = curlGenerator.generateCommand(request)

        // then
        assertEquals(command, "curl -X GET \"http://example.com/\"", command)
    }
}