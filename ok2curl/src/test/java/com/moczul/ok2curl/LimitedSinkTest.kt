package com.moczul.ok2curl

import okio.Buffer
import okio.ByteString.Companion.encodeUtf8
import okio.buffer
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.Charset

class LimitedSinkTest {

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when limit is 0`() {
        LimitedSink(Buffer(), 0L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when limit is negative`() {
        LimitedSink(Buffer(), -1L)
    }

    @Test(expected = NullPointerException::class)
    fun `should throw exception when buffer is null`() {
        LimitedSink(null, 10L)
    }

    @Test
    fun `should write to buffer only limited amount of bytes`() {
        // given
        val limited = Buffer()
        val source = LimitedSink(limited, 10L).buffer()

        // when
        source.write("0123456789012345678901234567890123456789".encodeUtf8()) // 40B
        source.flush()

        // then
        assertEquals("0123456789", limited.readString(Charset.forName("UTF-8")))
    }

    @Test
    fun `should write to buffer only limited amount of bytes when written in parts`() {
        // given
        val limited = Buffer()
        val source = LimitedSink(limited, 10L).buffer()

        // when
        source.write("01234".encodeUtf8()) // 5B
        source.flush()
        source.write("5678901234".encodeUtf8()) // 10B
        source.flush()
        source.write("5678901234567890123456789".encodeUtf8()) // 25B
        source.flush()

        // then
        assertEquals("0123456789", limited.readString(Charset.forName("UTF-8")))
    }

    @Test
    fun `should write all bytes when limit is bigger than input`() {
        // given
        val limited = Buffer()
        val source = LimitedSink(limited, 10L).buffer()

        // when
        source.write("01234".encodeUtf8()) // 5B
        source.flush()

        // then
        assertEquals("01234", limited.readString(Charset.forName("UTF-8")))
    }
}