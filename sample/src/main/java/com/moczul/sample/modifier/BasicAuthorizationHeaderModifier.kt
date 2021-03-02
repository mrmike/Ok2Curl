package com.moczul.sample.modifier

import com.moczul.ok2curl.Header
import com.moczul.ok2curl.modifier.HeaderModifier

class BasicAuthorizationHeaderModifier(private val base64Decoder: Base64Decoder) : HeaderModifier {

    override fun matches(header: Header): Boolean {
        return "Authorization" == header.name && header.value.startsWith("Basic")
    }

    override fun modify(header: Header): Header {
        val valueToDecode = header.value.replace("Basic", "").trim()
        val decodedHeaderValue = base64Decoder.decode(valueToDecode)
        return Header(name = header.name, value = decodedHeaderValue)
    }
}