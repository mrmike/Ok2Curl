package com.moczul.sample.modifier

import com.moczul.ok2curl.Header
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BasicAuthorizationHeaderModifierTest {

    @Mock
    private lateinit var base64Decoder: Base64Decoder

    @InjectMocks
    private lateinit var headerModifier: BasicAuthorizationHeaderModifier

    private val basicAuthHeader = Header("Authorization", "Basic bWFjaWVrOnRham5laGFzbG8xMjM=")

    @Test
    fun `basic authorization header should match header`() {
        assertTrue(headerModifier.matches(basicAuthHeader))
    }

    @Test
    fun `non basic authorization header should match header`() {
        // when
        val header = Header("Authorization", "Non basic header value")

        // then
        assertFalse(headerModifier.matches(header))
    }

    @Test
    fun `should decode header value with modifier`() {
        // given
        `when`(base64Decoder.decode("bWFjaWVrOnRham5laGFzbG8xMjM=")).thenReturn("maciej:tajnehaslo123")

        // when
        val modifiedHeader = headerModifier.modify(basicAuthHeader)

        // then
        assertEquals("Authorization", modifiedHeader.name)
        assertEquals("maciej:tajnehaslo123", modifiedHeader.value)
    }
}