package com.moczul.ok2curl

import okio.Buffer
import okio.Sink
import okio.Timeout

class LimitedSink(limited: Buffer?, limit: Long) : Sink {
    private val limited: Buffer
    private var total: Long

    override fun write(source: Buffer, byteCount: Long) {
        if (total > 0) {
            val toWrite = total.coerceAtMost(byteCount)
            limited.write(source, toWrite)
            total -= toWrite
        }
    }

    override fun flush() {
        limited.flush()
    }

    override fun timeout(): Timeout {
        return Timeout.NONE
    }

    override fun close() {
        limited.close()
    }

    init {
        if (limited == null) throw NullPointerException("limited can not be null")
        require(limit > 0) { "limit has to be grater than 0" }
        this.limited = limited
        total = limit
    }
}