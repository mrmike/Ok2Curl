package com.moczul.sample.modifier;

import android.util.Base64;

public class Base64Decoder {

    public String decode(String value) {
        final byte[] decodedBytes = Base64.decode(value.getBytes(), Base64.DEFAULT);
        return new String(decodedBytes);
    }
}
