package com.odeyalo.analog.auth.unit.service.oauth2.support;

import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.exceptions.NotSupportedOuath2Provider;
import com.odeyalo.analog.auth.service.oauth2.client.support.OAuth2UserInfoFactory;
import com.odeyalo.analog.auth.service.oauth2.client.support.info.GoogleOauth2UserInfo;
import com.odeyalo.analog.auth.service.oauth2.client.support.info.Oauth2UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OAuth2UserInfoFactoryTest {

    @Test
    @DisplayName("Return google oauth2 user info")
    void shouldReturnGoogleOauth2UserInfo() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "123@gmail.com");
        attributes.put("picture", "pict");
        attributes.put("name", "username");
        Oauth2UserInfo oauth2User = OAuth2UserInfoFactory.getOauth2User(AuthProvider.GOOGLE.toString(), attributes);
        assertNotNull(oauth2User);
        assertEquals(GoogleOauth2UserInfo.class, oauth2User.getClass());
        assertEquals("123@gmail.com", oauth2User.getEmail());
        assertEquals("pict", oauth2User.getImage());
        assertEquals("username", oauth2User.getFirstName());
    }
    @Test
    @DisplayName("Throw exception because auth provider not found")
    void shouldThrowException() {
        assertThrows(NotSupportedOuath2Provider.class, () -> OAuth2UserInfoFactory.getOauth2User("Not existed", new HashMap<>()));
    }
}
