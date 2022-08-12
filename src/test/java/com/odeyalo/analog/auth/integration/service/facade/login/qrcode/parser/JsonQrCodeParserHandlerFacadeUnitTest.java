package com.odeyalo.analog.auth.integration.service.facade.login.qrcode.parser;

import com.odeyalo.analog.auth.config.security.jwt.utils.SecretKeyJwtTokenProvider;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.QrCodeNotFoundException;
import com.odeyalo.analog.auth.service.facade.qrcode.parser.JsonQrCodeParserHandlerFacade;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import com.odeyalo.analog.auth.service.support.parser.JsonQrCodeParser;
import com.odeyalo.analog.auth.service.support.parser.QrCodeParser;
import com.odeyalo.analog.auth.service.support.ws.WebSocketMessageChannelSender;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JsonQrCodeParserHandlerFacadeUnitTest {
    @Spy
    QrCodeParser qrCodeParser = new JsonQrCodeParser();
    @Mock
    SimpMessagingTemplate template;
    @Mock
    SecretKeyJwtTokenProvider secretKeyJwtTokenProvider;
    @Mock
    RefreshTokenProvider refreshTokenProvider;
    @Mock
    WebSocketMessageChannelSender sender;
    @InjectMocks
    JsonQrCodeParserHandlerFacade parserFacade;
    public static final String WEB_SOCKET_CLIENT_ID = "123";
    public static final String CURRENT_PATH = Paths.get(".").toAbsolutePath().normalize().toString();
    public static final String QR_CODE_IMAGE = new StringBuilder(CURRENT_PATH)
            .append(File.separator)
            .append("src")
            .append(File.separator)
            .append("test")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append("login")
            .append(File.separator)
            .append("test.qrcode.png").toString();
    public static final String IMAGE_WITHOUT_QR_CODE = new StringBuilder(CURRENT_PATH)
            .append(File.separator)
            .append("src")
            .append(File.separator)
            .append("test")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append("login")
            .append(File.separator)
            .append("test.no_qrcode.jpg").toString();
    public static final String WEB_SOCKET_MESSAGE_DELIVERY_ENDPOINT = "/topic/messages/qrcode/login/" + WEB_SOCKET_CLIENT_ID;
    public static final String REFRESH_TOKEN_TEXT_VALUE = "REFRESH_TOKEN_TEXT_VALUE";

    @BeforeAll
    void setup() {
        User user = TestUtils.buildUser(1, "email@gmail.com", "nickname", "password", false, AuthProvider.LOCAL,true, "", Role.USER);
        CustomUserDetails principal = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, user.getPassword(), user.getRoles());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    void beforeEach() {
        Mockito.when(refreshTokenProvider.createAndSaveToken(any(User.class))).thenReturn(RefreshToken.builder().refreshToken(REFRESH_TOKEN_TEXT_VALUE).build());
    }

    @Test
    @DisplayName("Parse and verify input stream with correct image and expect no exception throwing")
    void parseAndVerifyCorrectImage() throws IOException {
        FileInputStream inputStream = new FileInputStream(QR_CODE_IMAGE);
        assertDoesNotThrow(() -> this.parserFacade.parseAndVerify(inputStream));
    }

    @Test
    @DisplayName("Parse and verify multipart file with correct image and expect no exception throwing")
    void parseAndVerifyMultipartFileCorrectImage() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", new FileInputStream(QR_CODE_IMAGE));

        assertDoesNotThrow(() -> this.parserFacade.parseAndVerify(mockMultipartFile));
    }

    @Test
    @DisplayName("Parse and verify input stream with wrong image without code and expect exception")
    void parseAndVerifyInputStreamImageWithoutQrCode() throws IOException {
        FileInputStream inputStream = new FileInputStream(IMAGE_WITHOUT_QR_CODE);
        assertThrows(QrCodeNotFoundException.class, () -> this.parserFacade.parseAndVerify(inputStream));
    }

    @Test
    @DisplayName("Parse and verify multipart file with wrong image without code and expect exception")
    void parseAndVerifyMultipartFileImageWithoutQrCode() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", new FileInputStream(IMAGE_WITHOUT_QR_CODE));
        assertThrows(QrCodeNotFoundException.class, () -> this.parserFacade.parseAndVerify(mockMultipartFile));
    }
}
