package com.moczul.ok2curl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Options {

    public static final Options EMPTY = new Options(Collections.emptyList());

    private final List<String> options;

    public static Builder builder() {
        return new Builder();
    }

    private Options(Collection<String> options) {
        this.options = new ArrayList<>(options);
    }

    public List<String> list() {
        return options;
    }

    public static class Builder {

        private final Set<String> options = new HashSet<>();

        public Builder insecure() {
            options.add("--insecure");
            return this;
        }

        public Builder maxTime(int seconds) {
            options.add(String.format(Locale.getDefault(), "--max-time %d", seconds));
            return this;
        }

        public Builder connectTimeout(int seconds) {
            options.add(String.format(Locale.getDefault(), "--connect-timeout %d", seconds));
            return this;
        }

        public Builder retry(int num) {
            options.add(String.format(Locale.getDefault(), "--retry %d", num));
            return this;
        }

        public Builder compressed() {
            options.add("--compressed");
            return this;
        }

        public Builder location() {
            options.add("--location");
            return this;
        }

        public Options build() {
            return new Options(options);
        }
    }

}
