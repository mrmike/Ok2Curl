package com.moczul.sample.modifier

import android.util.Base64

class Base64Decoder {

    fun decode(value: String): String {
        val decodedBytes = Base64.decode(value.toByteArray(), Base64.DEFAULT)
        return String(decodedBytes)
    }
}