package com.moczul.ok2curl.util;

import com.moczul.ok2curl.logger.Loggable;

public class FakeLogger implements Loggable {

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
