package com.odeyalo.analog.auth.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.analog.auth.dto.request.EmailMethodPasswordRecoveryDTO;
import com.odeyalo.analog.auth.dto.request.LoginUserDTO;
import com.odeyalo.analog.auth.dto.request.NewPasswordDTO;
import com.odeyalo.analog.auth.dto.request.RegisterUserDTO;
import com.odeyalo.analog.auth.dto.request.RefreshTokenRequest;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.integration.AbstractIntegrationTest;
import com.odeyalo.analog.auth.repository.RefreshTokenRepository;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.repository.VerificationCodeRepository;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenGenerator;
import com.odeyalo.analog.auth.service.sender.mail.MailSender;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthControllerTest extends AbstractIntegrationTest {
    @MockBean(name = "kafkaBrokerMicroserviceDelegateMailSender")
    private MailSender sender;
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
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    private static final String AUTH_ENTRYPOINT = "/auth/register";
    private static final String LOGIN_ENTRYPOINT = "/auth/login";
    private static final String REFRESH_TOKEN_ENTRYPOINT = "/auth/refreshToken";
    private static final String EMAIL_PASSWORD_RECOVERY_ENTRYPOINT = "/auth/password/recovery/email";
    private static final String EMAIL_PASSWORD_RECOVERY_CODE_ENTRYPOINT = "/auth/password/recovery/email/code";
    private static final String PHONE_NUMBER_PASSWORD_RECOVERY_ENTRYPOINT = "/auth/password/recovery/phone/number";
    private static final String PHONE_NUMBER_PASSWORD_RECOVERY_CODE_ENTRYPOINT = "/auth/password/recovery/phone/number/code";

    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_NICKNAME = "nickname";
    private static final String USER_PASSWORD = "password";
    private static final String WRONG_EMAIL = "123";
    private static final String WRONG_PASSWORD = "1";
    private static final String WRONG_NICKNAME = "nick";
    private static final String EXISTED_USER_EMAIL = "existed@gmail.com";
    private static final String EXISTED_USER_NICKNAME = "existed1337";
    private static final String EXISTED_USER_PASSWORD = "password123";
    private static final String NOT_EXISTED_USER_EMAIL = "notExisted@gmail.com";
    private static final String NOT_EXISTED_REFRESH_TOKEN = "NOT_EXISTED";
    private static final String NEW_USER_PASSWORD = "new_password";
    private static final String CORRECT_VERIFICATION_CODE_VALUE = "123456";
    private static final String WRONG_VERIFICATION_CODE_VALUE = "789412";

    @BeforeAll
    public void beforeAll() {
        this.tokenRepository.deleteAll();
        this.verificationCodeRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        User entity = TestUtils.buildUser(1, EXISTED_USER_EMAIL, EXISTED_USER_NICKNAME, encoder.encode(EXISTED_USER_PASSWORD), false, AuthProvider.LOCAL, true, "", Role.USER);
        User user = this.userRepository.save(entity);
        this.tokenRepository.save(RefreshToken.builder()
                .refreshToken(this.generator.generate())
                .user(user)
                .expireDate(Instant.now().plusSeconds(60))
                .build());
        this.verificationCodeRepository.save(VerificationCode.builder()
                .user(user)
                .codeValue(CORRECT_VERIFICATION_CODE_VALUE)
                .expired(LocalDateTime.now().plusMinutes(5))
                .isActivated(false)
                .build());
    }

    @Test
    @DisplayName("auth with correct email and password and except201")
    void authWithCorrectEmailAndPasswordAndExcept202() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(USER_NICKNAME, USER_EMAIL, USER_PASSWORD);
        String value = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(AUTH_ENTRYPOINT).content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
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

    @Test
    @DisplayName("Send reset password code to existed email and expect 200")
    void sendResetPasswordToExistedEmail() throws Exception {
        EmailMethodPasswordRecoveryDTO dto = new EmailMethodPasswordRecoveryDTO(EXISTED_USER_EMAIL);
        String json = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(EMAIL_PASSWORD_RECOVERY_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send reset password code to not existed email and expect 400")
    void sendResetPasswordToNotExistedEmail() throws Exception {
        EmailMethodPasswordRecoveryDTO dto = new EmailMethodPasswordRecoveryDTO(NOT_EXISTED_USER_EMAIL);
        String json = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(EMAIL_PASSWORD_RECOVERY_ENTRYPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Verify correct verification code for reset password  and expect 200")
    void verifyCorrectVerificationCodeForResetPasswordFromEmail() throws Exception {
        NewPasswordDTO dto = new NewPasswordDTO(NEW_USER_PASSWORD);
        String json = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(EMAIL_PASSWORD_RECOVERY_CODE_ENTRYPOINT)
                .param("code", CORRECT_VERIFICATION_CODE_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        User user = this.userRepository.findUserByEmail(EXISTED_USER_EMAIL).get();
        assertNotEquals(USER_PASSWORD, user.getPassword());
        assertFalse(encoder.matches(USER_PASSWORD, user.getPassword()));
        assertTrue(encoder.matches(NEW_USER_PASSWORD, user.getPassword()));

    }

    @Test
    @DisplayName("Verify wrong verification code for reset password and expect 400")
    void verifyWrongVerificationCodeForResetPasswordFromEmail() throws Exception {
        NewPasswordDTO dto = new NewPasswordDTO(NEW_USER_PASSWORD);
        String json = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(EMAIL_PASSWORD_RECOVERY_CODE_ENTRYPOINT)
                .param("code", WRONG_VERIFICATION_CODE_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
        User user = this.userRepository.findUserByEmail(EXISTED_USER_EMAIL).get();
        assertNotEquals(USER_PASSWORD, user.getPassword());
        assertFalse(encoder.matches(NEW_USER_PASSWORD, user.getPassword()));
        assertTrue(encoder.matches(EXISTED_USER_PASSWORD, user.getPassword()));
    }

    @Test
    @DisplayName("Verify correct verification code from phone number for reset password and expect 400")
    void verifyCorrectVerificationCodeForResetPasswordFromPhoneNumber() throws Exception {
        NewPasswordDTO dto = new NewPasswordDTO(NEW_USER_PASSWORD);
        String json = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(EMAIL_PASSWORD_RECOVERY_CODE_ENTRYPOINT)
                .param("code", CORRECT_VERIFICATION_CODE_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        User user = this.userRepository.findUserByEmail(EXISTED_USER_EMAIL).get();
        assertNotEquals(USER_PASSWORD, user.getPassword());
        assertTrue(encoder.matches(NEW_USER_PASSWORD, user.getPassword()));
        assertFalse(encoder.matches(EXISTED_USER_PASSWORD, user.getPassword()));
    }

    @Test
    @DisplayName("Verify correct verification code from phone number for reset password and expect 400")
    void verifyWrongVerificationCodeForResetPasswordFromPhoneNumber() throws Exception {
        NewPasswordDTO dto = new NewPasswordDTO(NEW_USER_PASSWORD);
        String json = this.mapper.writeValueAsString(dto);
        this.mockMvc.perform(post(EMAIL_PASSWORD_RECOVERY_CODE_ENTRYPOINT)
                .param("code", WRONG_VERIFICATION_CODE_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
        User user = this.userRepository.findUserByEmail(EXISTED_USER_EMAIL).get();
        assertNotEquals(USER_PASSWORD, user.getPassword());
        assertFalse(encoder.matches(NEW_USER_PASSWORD, user.getPassword()));
        assertTrue(encoder.matches(EXISTED_USER_PASSWORD, user.getPassword()));
    }

    @AfterEach
    void clear() {
        this.tokenRepository.deleteAll();
        this.verificationCodeRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @AfterAll
    public void afterAll() {
        this.tokenRepository.deleteAll();
        this.verificationCodeRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
