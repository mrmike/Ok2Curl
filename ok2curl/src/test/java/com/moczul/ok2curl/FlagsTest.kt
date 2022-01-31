package com.moczul.ok2curl.util

import com.moczul.ok2curl.Flags
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FlagsTest {

    @Test
    fun `should return empty options for default builder`() {
        // when
        val flags = Flags.builder().build()

        // when
        assertThat(flags.list().size, `is`(0))
    }

    @Test
    fun `should correctly handle options parameters`() {
        // when
        val flags = Flags.builder()
            .maxTime(120)
            .connectTimeout(60)
            .retry(3)
            .build()

        // then
        assertThat(flags.list().size, `is`(3))
        assertThat<List<String?>>(flags.list(), hasItem("--max-time 120"))
        assertThat<List<String?>>(flags.list(), hasItem("--connect-timeout 60"))
        assertThat<List<String?>>(flags.list(), hasItem("--retry 3"))
    }

    @Test
    fun `should return correct list of parameters`() {
        // when
        val flags = Flags.builder()
            .insecure()
            .compressed()
            .location()
            .build()

        // then
        assertThat(flags.list().size, `is`(3))
        assertThat<List<String?>>(flags.list(), hasItem("--insecure"))
        assertThat<List<String?>>(flags.list(), hasItem("--compressed"))
        assertThat<List<String?>>(flags.list(), hasItem("--location"))
    }

    @Test
    fun `should ignore duplicated parameters`() {
        // when
        val flags = Flags.builder()
            .insecure()
            .insecure()
            .insecure()
            .build()

        // then
        assertThat(flags.list().size, `is`(1))
        assertThat<List<String?>>(flags.list(), hasItem("--insecure"))
    }
}