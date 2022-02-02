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


    @Test
    fun basicAuthorizationHeader_shouldMatch_modifier() {
        val header = Header("Authorization", "Basic bWFjaWVrOnRham5laGFzbG8xMjM=")

        assertTrue(headerModifier.matches(header))
    }

    @Test
    fun nonBasicAuthorizationHeader_shouldNotMatch_modifier() {
        val header = Header("Authorization", "Non basic header value")

        assertFalse(headerModifier.matches(header))
    }

    @Test
    fun modifier_shouldReturnHeader_withDecodedValue() {
        `when`(base64Decoder.decode("bWFjaWVrOnRham5laGFzbG8xMjM=")).thenReturn("maciej:tajnehaslo123")
        val header = Header("Authorization", "Basic bWFjaWVrOnRham5laGFzbG8xMjM=")

        val modifiedHeader = headerModifier.modify(header)

        val expectedHeader = Header(name = "Authorization", value = "maciej:tajnehaslo123")
        assertEquals(expectedHeader, modifiedHeader)
    }
}