package com.moczul.ok2curl;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptionsTest {

    @Test
    public void defaultBuilderShouldReturnEmptyOptions() {
        final Options options = Options.builder().build();

        assertThat(options.list().size(), is(0));
    }

    @Test
    public void shouldCorrectlyHandleOptionsParameters() {
        final Options options = Options
                .builder()
                .maxTime(120)
                .connectTimeout(60)
                .retry(3)
                .build();

        assertThat(options.list().size(), is(3));
        assertThat(options.list(), hasItem("--max-time 120"));
        assertThat(options.list(), hasItem("--connect-timeout 60"));
        assertThat(options.list(), hasItem("--retry 3"));
    }

    @Test
    public void shouldReturnCorrectListOfParameters() {
        final Options options = Options.builder()
                .insecure()
                .compressed()
                .location()
                .build();

        assertThat(options.list().size(), is(3));
        assertThat(options.list(), hasItem("--insecure"));
        assertThat(options.list(), hasItem("--compressed"));
        assertThat(options.list(), hasItem("--location"));
    }

    @Test
    public void shouldIgnoreDuplicatedParameteres() {
        final Options options = Options.builder()
                .insecure()
                .insecure()
                .insecure()
                .build();

        assertThat(options.list().size(), is(1));
        assertThat(options.list(), hasItem("--insecure"));
    }
}