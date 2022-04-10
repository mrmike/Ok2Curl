package com.moczul.ok2curl

import okio.Buffer
import okio.Sink
import okio.Timeout
import java.io.IOException
import kotlin.math.min

internal class LimitedSink(limited: Buffer, limit: Long) : Sink {

    private val limited: Buffer
    private var total: Long

    init {
        require(limit > 0) { "limit has to be grater than 0" }
        this.limited = limited
        this.total = limit
    }

    @Throws(IOException::class)
    override fun write(source: Buffer, byteCount: Long) {
        if (total > 0) {
            val toWrite = min(total, byteCount)
            limited.write(source, toWrite)
            total -= toWrite
        }
    }

    @Throws(IOException::class)
    override fun flush() {
        limited.flush()
    }

    override fun timeout(): Timeout {
        return Timeout.NONE
    }

    @Throws(IOException::class)
    override fun close() {
        limited.close()
    }
}