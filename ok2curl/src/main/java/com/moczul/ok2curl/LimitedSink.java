package com.moczul.ok2curl;

import java.io.IOException;

import okio.Buffer;
import okio.Sink;
import okio.Timeout;

public class LimitedSink implements Sink {

    private final Buffer limited;
    private long total;

    public LimitedSink(Buffer limited, long limit) {
        if (limited == null) throw new NullPointerException("limited can not be null");
        if (limit <= 0) throw new IllegalArgumentException("limit has to be grater than 0");
        this.limited = limited;
        total = limit;
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        if (total > 0) {
            long toWrite = Math.min(total, byteCount);
            limited.write(source, toWrite);
            total -= toWrite;
        }
    }

    @Override
    public void flush() throws IOException {
        limited.flush();
    }

    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }

    @Override
    public void close() throws IOException {
        limited.close();
    }
}