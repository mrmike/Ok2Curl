package com.moczul.sample.modifier;

import com.moczul.ok2curl.Header;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicAuthorizationHeaderModifierTest {
    @Mock
    Base64Decoder base64Decoder;
    @InjectMocks
    BasicAuthorizationHeaderModifier headerModifier;

    private final Header basicAuthHeader = new Header("Authorization", "Basic bWFjaWVrOnRham5laGFzbG8xMjM=");

    @Test
    public void basicAuthorizationHeader_shouldMatch_modifier() throws Exception {
        assertThat(headerModifier.matches(basicAuthHeader), is(true));
    }

    @Test
    public void nonBasicAuthorizationHeader_shouldNotMatch_modifier() throws Exception {
        final Header header = new Header("Authorization", "Non basic header value");

        assertThat(headerModifier.matches(header), is(false));
    }

    @Test
    public void modifier_shouldReturnHeader_withDecodedValue() throws Exception {
        when(base64Decoder.decode("bWFjaWVrOnRham5laGFzbG8xMjM=")).thenReturn("maciej:tajnehaslo123");

        final Header modifiedHeader = headerModifier.modify(basicAuthHeader);

        assertThat(modifiedHeader.name(), is("Authorization"));
        assertThat(modifiedHeader.value(), is("maciej:tajnehaslo123"));
    }
}
