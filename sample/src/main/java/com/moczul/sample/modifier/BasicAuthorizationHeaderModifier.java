package com.moczul.sample.modifier;

import com.moczul.ok2curl.Header;
import com.moczul.ok2curl.modifier.HeaderModifier;

public class BasicAuthorizationHeaderModifier implements HeaderModifier {

    private final Base64Decoder base64Decoder;

    public BasicAuthorizationHeaderModifier(Base64Decoder base64Decoder) {
        this.base64Decoder = base64Decoder;
    }

    @Override
    public boolean matches(Header header) {
        return "Authorization".equals(header.name())
                && header.value().startsWith("Basic");
    }

    @Override
    public Header modify(Header header) {
        final String valueToDecode = header.value().replace("Basic", "").trim();
        final String decodedHeaderValue = base64Decoder.decode(valueToDecode);
        return new Header(header.name(), decodedHeaderValue);
    }
}
