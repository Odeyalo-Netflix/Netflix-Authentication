package com.odeyalo.analog.auth.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import com.odeyalo.analog.auth.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QrCodeControllerTest {
    private static final String QR_CODE_CONTROLLER_GENERATE_ENDPOINT = "/api/v1/qrcode/generate";
    private static final String QR_CODE_CONTROLLER_VERIFY_ENDPOINT = "/api/v1/qrcode/verify";
    private static final String CLIENT_ID = "DM-HSJCLaNc";
    private static final String CURRENT_PATH = Paths.get(".").toAbsolutePath().normalize().toString();
    private static final String IMAGE_WITH_QR_CODE = CURRENT_PATH + "\\src\\test\\resources\\login\\test.qrcode.png";
    private static final String IMAGE_WITHOUT_QR_CODE = CURRENT_PATH + "\\src\\test\\resources\\login\\test.no_qrcode.jpg";
    private static final String USER_EMAIL_TEXT_VALUE = "email@gmail.com";
    private static final String USER_NICKNAME_TEXT_VALUE = "NICKNAME";
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Value("${app.qrcode.path}")
    String QR_CODE_PATH_DIRECTORY_PATH;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        User user = TestUtils.buildUser(USER_EMAIL_TEXT_VALUE, USER_NICKNAME_TEXT_VALUE, "123", false, Role.USER);
        this.userRepository.save(user);
    }

    @Test
    @DisplayName("Generate qr code with client id and expect qr code image")
    void generateQrcode() throws Exception {
        this.mockMvc
                .perform(get(QR_CODE_CONTROLLER_GENERATE_ENDPOINT).param("clientId", CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    @DisplayName("Generate qr code without client id and expect bad request")
    void generateQrcodeWithoutClientId() throws Exception {
        this.mockMvc
                .perform(get(QR_CODE_CONTROLLER_GENERATE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //401 if jwt token not presented
    @Test
    @DisplayName("Parse qr code without jwt token and expect 401")
    void parseQrCodeWithoutJwtToken() throws Exception {
        this.mockMvc.perform(get(QR_CODE_CONTROLLER_VERIFY_ENDPOINT))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Make request to verify qr code without image")
    void parseQrCodeWithoutImage() throws Exception {
        String jwtToken = this.generateJwtToken(User.builder().email(USER_EMAIL_TEXT_VALUE).nickname(USER_NICKNAME_TEXT_VALUE).build());
        this.mockMvc.perform(post(QR_CODE_CONTROLLER_VERIFY_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Parse correct qr code with jwt token and expect 200")
    void parseCorrectQrCodeWithJwtToken() throws Exception {
        String jwtToken = this.generateJwtToken(User.builder().email(USER_EMAIL_TEXT_VALUE).id(1).nickname(USER_NICKNAME_TEXT_VALUE).build());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new FileInputStream(IMAGE_WITH_QR_CODE));
        this.mockMvc.perform(multipart(QR_CODE_CONTROLLER_VERIFY_ENDPOINT).file(mockMultipartFile)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Parse image without qr code and expect 400")
    void parseImageWithoutQrCode() throws Exception {
        String jwtToken = this.generateJwtToken(User.builder().email(USER_EMAIL_TEXT_VALUE).id(1).nickname(USER_NICKNAME_TEXT_VALUE).build());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new FileInputStream(IMAGE_WITHOUT_QR_CODE));
        this.mockMvc.perform(multipart(QR_CODE_CONTROLLER_VERIFY_ENDPOINT)
                .file(mockMultipartFile).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    public String generateJwtToken(User user) {
        return this.jwtTokenProvider.generateJwtToken(new CustomUserDetails(user));
    }

    @AfterEach
    void clear() {
        this.userRepository.deleteAll();
        File directory = new File(QR_CODE_PATH_DIRECTORY_PATH);
        if (directory.isDirectory() && directory.listFiles() != null) {
            File[] files = directory.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }
}
