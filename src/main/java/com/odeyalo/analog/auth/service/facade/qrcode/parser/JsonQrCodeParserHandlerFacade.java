package com.odeyalo.analog.auth.service.facade.qrcode.parser;

import com.odeyalo.analog.auth.config.security.jwt.utils.JwtTokenProvider;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.dto.response.QrCodeDTO;
import com.odeyalo.analog.auth.dto.response.QrCodeLoginSuccessMessageDTO;
import com.odeyalo.analog.auth.entity.RefreshToken;
import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.refresh.RefreshTokenProvider;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import com.odeyalo.analog.auth.service.support.parser.QrCodeParser;
import com.odeyalo.analog.auth.service.support.ws.WebSocketMessageChannelSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Verify qr code and sends jwt and refresh tokens to user using websockets
 */
@Component
public class JsonQrCodeParserHandlerFacade implements QrCodeParserHandlerFacade {
    private final QrCodeParser parser;
    private final WebSocketMessageChannelSender sender;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    private final Logger logger = LoggerFactory.getLogger(JsonQrCodeParserHandlerFacade.class);
    private static final String QR_CODE_ON_SUCCESS_LOGIN_DESTINATION = "/topic/messages/qrcode/login/";

    public JsonQrCodeParserHandlerFacade(QrCodeParser parser,
                                         WebSocketMessageChannelSender sender,
                                         JwtTokenProvider jwtTokenProvider,
                                         RefreshTokenProvider refreshTokenProvider) {
        this.parser = parser;
        this.sender = sender;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    @Override
    public void parseAndVerify(InputStream stream) throws IOException {
        QrCodeDTO qrCodeDTO = this.parser.parseQrCode(stream);
        String clientId = qrCodeDTO.getClientId();
        CustomUserDetails details = this.getCustomDetails();
        User user = details.getUser();
        GenericMessage<QrCodeLoginSuccessMessageDTO> message = getOnSuccessMessageDto(details, user);
        this.sender.sendMessage(QR_CODE_ON_SUCCESS_LOGIN_DESTINATION + clientId, message);
        this.logger.info("Successful send message to client: " + clientId);
    }

    private CustomUserDetails getCustomDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

    private GenericMessage<QrCodeLoginSuccessMessageDTO> getOnSuccessMessageDto(CustomUserDetails details, User user) {
        String jwtToken = this.jwtTokenProvider.generateJwtToken(details);
        RefreshToken refreshToken = this.refreshTokenProvider.createAndSaveToken(user);
        return new GenericMessage<>(new QrCodeLoginSuccessMessageDTO(new JwtTokenResponseDTO(true, jwtToken, refreshToken.getRefreshToken())));
    }

    @Override
    public void parseAndVerify(MultipartFile file) throws IOException {
        this.parseAndVerify(file.getInputStream());
    }
}
