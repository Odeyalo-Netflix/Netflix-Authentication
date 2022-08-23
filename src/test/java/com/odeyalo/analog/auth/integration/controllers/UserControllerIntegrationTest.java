package com.odeyalo.analog.auth.integration.controllers;


import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.integration.AbstractIntegrationTest;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    @Qualifier("rsaTokenPairJwtTokenProvider")
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_NICKNAME = "nickname";
    private static final String USER_RAW_PASSWORD = "password";

    public static final String USER_CONTROLLER_URL = "/user";
    public static final String USER_CONTROLLER_ME_URL = USER_CONTROLLER_URL + "/me";

    private User user;
    @BeforeEach
    public void setup() {
        String encodedPassword = this.encoder.encode(USER_RAW_PASSWORD);
        User user = User.builder()
                .email(USER_EMAIL)
                .nickname(USER_NICKNAME)
                .password(encodedPassword)
                .image("")
                .authProvider(AuthProvider.LOCAL)
                .role(Role.USER)
                .activated(true)
                .banned(false).build();
        this.user = this.userRepository.save(user);
    }

    @Test
    @DisplayName("Test method me")
    public void testMeWithToken() throws Exception {
        String token = this.jwtTokenProvider.generateJwtToken(new CustomUserDetails(user));
        this.mockMvc.perform(get(USER_CONTROLLER_ME_URL).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(jsonPath("image").isString())
                .andExpect(jsonPath("image").value(""))
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("name").value(USER_NICKNAME))
                .andExpect(jsonPath("email").isString())
                .andExpect(jsonPath("email").value(USER_EMAIL));

    }
    @Test
    @DisplayName("Test method me without token")
    public void testMeWithoutToken() throws Exception {
        this.mockMvc.perform(get(USER_CONTROLLER_ME_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @AfterEach
    void clear() {
        this.userRepository.deleteAll();
    }
}
