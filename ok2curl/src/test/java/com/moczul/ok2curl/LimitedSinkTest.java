package com.moczul.ok2curl;

import org.junit.Test;

import java.nio.charset.Charset;

import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;

import static org.junit.Assert.*;

public class LimitedSinkTest {

    @Test(expected = IllegalArgumentException.class)
    public void testWhenLimitIs0_throwIllegalArgumentException() throws Exception {
        new LimitedSink(new Buffer(), 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenLimitIsNegative_throwIllegalArgumentException() throws Exception {
        new LimitedSink(new Buffer(), -1L);
    }

    @Test(expected = NullPointerException.class)
    public void testWhenBufferIsNull_throwNullPointerException() throws Exception {
        new LimitedSink(null, 10L);
    }

    @Test
    public void testWhenLongResult_writeOnlyLimitedBytes() throws Exception {
        final Buffer limited = new Buffer();
        final BufferedSink source = Okio.buffer(new LimitedSink(limited, 10L));

        source
                .write(ByteString.encodeUtf8("0123456789012345678901234567890123456789")); // 40B
        source.flush();

        assertEquals("0123456789", limited.readString(Charset.forName("UTF-8")));
    }

    @Test
    public void testWhenLongResultWrittenInParts_writeOnlyLimitedBytes() throws Exception {
        final Buffer limited = new Buffer();
        final BufferedSink source = Okio.buffer(new LimitedSink(limited, 10L));

        source
                .write(ByteString.encodeUtf8("01234")); // 5B
        source.flush();
        source
                .write(ByteString.encodeUtf8("5678901234")); // 10B
        source.flush();
        source
                .write(ByteString.encodeUtf8("5678901234567890123456789")); // 25B
        source.flush();

        assertEquals("0123456789", limited.readString(Charset.forName("UTF-8")));
    }

    @Test
    public void testWhenShorterResult_writeOnlyThatData() throws Exception {
        final Buffer limited = new Buffer();
        final BufferedSink source = Okio.buffer(new LimitedSink(limited, 10L));

        source
                .write(ByteString.encodeUtf8("01234")); // 5B
        source.flush();

        assertEquals("01234", limited.readString(Charset.forName("UTF-8")));
    }
}
