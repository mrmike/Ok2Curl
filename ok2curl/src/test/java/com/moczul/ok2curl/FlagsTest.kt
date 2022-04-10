package com.moczul.ok2curl

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
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
        val expectedFlags = listOf("--max-time 120", "--connect-timeout 60", "--retry 3")
        assertEquals(expectedFlags, flags.list())
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
        val expectedFlags = listOf("--insecure", "--compressed", "--location")
        assertEquals(expectedFlags, flags.list())
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
        assertEquals(listOf("--insecure"), flags.list())
    }
}