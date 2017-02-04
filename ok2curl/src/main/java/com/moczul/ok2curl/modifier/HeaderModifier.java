package com.moczul.ok2curl.modifier;

import com.moczul.ok2curl.Header;

/**
 * HeaderModifier allow for changing header name/value before creating curl log
 */
public interface HeaderModifier {

    /**
     * @param header
     * @return true if header should be modified and false otherwise.
     */
    boolean matches(Header header);

    /**
     * @param header
     * @return modified header or null to omit header in curl log
     */
    Header modify(Header header);
}
