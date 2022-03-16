package com.odeyalo.analog.auth.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.analog.auth.dto.LoginUserDTO;
import com.odeyalo.analog.auth.dto.RegisterUserDTO;
import com.odeyalo.analog.auth.dto.request.RefreshTokenRequest;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenGenerator;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RefreshTokenGenerator generator;

    private static final String AUTH_ENTRYPOINT = "/api/v1/auth";
    private static final String LOGIN_ENTRYPOINT = "/api/v1/login";
    private static final String REFRESH_TOKEN_ENTRYPOINT = "/api/v1/refreshToken";

    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_NICKNAME = "nickname";
    private static final String USER_PASSWORD = "password";
    private static final String WRONG_EMAIL = "123";
    private static final String WRONG_PASSWORD = "1";
    private static final String WRONG_NICKNAME = "nick";
    private static final String EXISTED_USER_EMAIL = "existed@gmail.com";
    private static final String EXISTED_USER_NICKNAME = "existed1337";
    private static final String EXISTED_USER_PASSWORD = "password123";
    private static final String NOT_EXISTED_REFRESH_TOKEN = "NOT_EXISTED";

    @BeforeEach
    void setUp() {
        User user = this.userRepository.save(TestUtils.buildUser(EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, encoder.encode(EXISTED_USER_PASSWORD), false, Role.USER));
        this.tokenRepository.save(RefreshToken.builder()
                .refreshToken(this.generator.generate())
                .user(user)
                .expireDate(Instant.now().plusSeconds(60))
                .build());
    }

    @Test
    @DisplayName("auth with correct email and password and except201")
    void authWithCorrectEmailAndPasswordAndExcept201() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(USER_NICKNAME, USER_EMAIL, USER_PASSWORD);
        String value = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(AUTH_ENTRYPOINT).content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success").isBoolean())
                .andExpect(jsonPath("jwtToken").isString())
                .andExpect(jsonPath("jwtToken").isNotEmpty())
                .andExpect(jsonPath("refreshToken").isNotEmpty())
                .andExpect(jsonPath("refreshToken").isString());
    }

    @Test
    @DisplayName("auth with wrong email and password and except 400")
    void authWithWrongEmailAndPasswordAndExcept400() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(USER_NICKNAME, WRONG_EMAIL, USER_PASSWORD);
        String value = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(AUTH_ENTRYPOINT).content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("auth with wrong email and wrong password and except 400")
    void authWithWrongEmailAndWrongPasswordAndExcept400() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(USER_NICKNAME, WRONG_EMAIL, WRONG_PASSWORD);
        String value = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(AUTH_ENTRYPOINT).content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("auth with wrong email and wrong nickname and wrong password and except 400")
    void authWithWrongEmailAndWrongNicknameAndWrongPasswordAndExcept400() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(WRONG_NICKNAME, WRONG_EMAIL, WRONG_PASSWORD);
        String value = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(AUTH_ENTRYPOINT).content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("auth with existed email and expect 400")
    void authWithExistedEmail() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(USER_NICKNAME, EXISTED_USER_EMAIL, USER_PASSWORD);
        String value = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(AUTH_ENTRYPOINT).content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("auth with existed nickname and expect 400")
    void authWithExistedNickname() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(EXISTED_USER_NICKNAME, USER_EMAIL, USER_PASSWORD);
        String value = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(AUTH_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("login existed user")
    void loginWithExistedUser() throws Exception {
        LoginUserDTO loginUserDto = new LoginUserDTO(EXISTED_USER_NICKNAME, EXISTED_USER_PASSWORD);
        String value = this.mapper.writeValueAsString(loginUserDto);
        this.mockMvc.perform(post(LOGIN_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").isBoolean())
                .andExpect(jsonPath("jwtToken").isString())
                .andExpect(jsonPath("jwtToken").isNotEmpty())
                .andExpect(jsonPath("refreshToken").isNotEmpty())
                .andExpect(jsonPath("refreshToken").isString());
    }

    @Test
    @DisplayName("login existed user but with wrong password and expect 400")
    void loginWithExistedUserButWithWrongPassword() throws Exception {
        LoginUserDTO loginUserDto = new LoginUserDTO(EXISTED_USER_NICKNAME, WRONG_PASSWORD);
        String value = this.mapper.writeValueAsString(loginUserDto);
        this.mockMvc.perform(post(LOGIN_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("login not existed user and expect 400")
    void loginNotExistedUser() throws Exception {
        LoginUserDTO loginUserDto = new LoginUserDTO(WRONG_NICKNAME, WRONG_PASSWORD);
        String value = this.mapper.writeValueAsString(loginUserDto);
        this.mockMvc.perform(post(LOGIN_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("refresh not existed token ")
    void refreshNotExistedToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest(NOT_EXISTED_REFRESH_TOKEN);
        String value = this.mapper.writeValueAsString(request);
        this.mockMvc.perform(post(REFRESH_TOKEN_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Refresh existed token")
    void refreshExistedToken() throws Exception {
        String refreshToken = this.tokenRepository.findAll().get(0).getRefreshToken();
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        String value = this.mapper.writeValueAsString(request);
        this.mockMvc.perform(post(REFRESH_TOKEN_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("jwtToken").isString())
                .andExpect(jsonPath("jwtToken").isNotEmpty())
                .andExpect(jsonPath("refreshToken").isNotEmpty())
                .andExpect(jsonPath("refreshToken").isString());
    }

    @AfterEach
    void clear() {
        this.tokenRepository.deleteAll();
        this.userRepository.deleteAll();
    }

}
