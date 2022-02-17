package com.moczul.ok2curl.logger;

public interface Loggable {
    boolean isEnable();

    void log(String message);
}
