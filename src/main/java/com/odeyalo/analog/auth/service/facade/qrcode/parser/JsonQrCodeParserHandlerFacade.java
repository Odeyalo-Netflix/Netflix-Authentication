package com.odeyalo.analog.auth.service.facade.qrcode.parser;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.dto.response.QrCodeDTO;
import com.odeyalo.analog.auth.dto.response.QrCodeLoginSuccessMessageDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.repository.QrCodeRepository;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import com.odeyalo.analog.auth.service.support.parser.QrCodeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonQrCodeParserHandlerFacade implements QrCodeParserHandlerFacade {
    private final QrCodeParser parser;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    private final Logger logger = LoggerFactory.getLogger(JsonQrCodeParserHandlerFacade.class);

    public JsonQrCodeParserHandlerFacade(QrCodeParser parser,
                                         SimpMessagingTemplate messagingTemplate,
                                         JwtTokenProvider jwtTokenProvider,
                                         RefreshTokenProvider refreshTokenProvider) {
        this.parser = parser;
        this.messagingTemplate = messagingTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    @Override
    public void parseAndVerify(InputStream stream) throws IOException {
        QrCodeDTO qrCodeDTO = this.parser.parseQrCode(stream);
        String clientId = qrCodeDTO.getClientId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        User user = details.getUser();
        String jwtToken = this.jwtTokenProvider.generateJwtToken(details);
        RefreshToken refreshToken = this.refreshTokenProvider.createAndSaveToken(user);
        this.messagingTemplate.convertAndSend("/topic/messages/qrcode/login/" + clientId,
                new QrCodeLoginSuccessMessageDTO(
                        new JwtTokenResponseDTO(true, jwtToken, refreshToken.getRefreshToken())));
        this.logger.info("Successful send message to client: " + clientId);
    }

    @Override
    public void parseAndVerify(MultipartFile file) throws IOException {
        this.parseAndVerify(file.getInputStream());
    }
}
