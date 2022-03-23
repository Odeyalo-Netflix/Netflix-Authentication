package com.odeyalo.analog.auth.controllers;

import com.odeyalo.analog.auth.dto.LoginUserDTO;
import com.odeyalo.analog.auth.dto.RegisterUserDTO;
import com.odeyalo.analog.auth.dto.request.RefreshTokenRequest;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.dto.response.RefreshTokenResponseDTO;
import com.odeyalo.analog.auth.service.facade.EmailCodeVerificationHandlerFacade;
import com.odeyalo.analog.auth.service.facade.JwtWithRefreshTokenResponseDTOBuilder;
import com.odeyalo.analog.auth.service.facade.login.UsernamePasswordLoginHandlerFacade;
import com.odeyalo.analog.auth.service.facade.register.UsernamePasswordRegisterHandlerFacade;
import com.odeyalo.analog.auth.service.support.UserConverter;
import com.odeyalo.analog.auth.service.validators.RequestUserDTOValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UsernamePasswordRegisterHandlerFacade registerHandler;
    private final UsernamePasswordLoginHandlerFacade loginHandler;
    private final JwtWithRefreshTokenResponseDTOBuilder jwtWithRefreshTokenResponseDTOBuilder;
    private final EmailCodeVerificationHandlerFacade verificationHandler;
    private final RequestUserDTOValidator validator;
    public AuthController(@Qualifier("emailVerificationUsernamePasswordRegisterHandlerFacade") UsernamePasswordRegisterHandlerFacade registerHandler,
                          UsernamePasswordLoginHandlerFacade loginHandler,
                          JwtWithRefreshTokenResponseDTOBuilder jwtWithRefreshTokenResponseDTOBuilder,
                          EmailCodeVerificationHandlerFacade verificationHandler, RequestUserDTOValidator validator) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
        this.jwtWithRefreshTokenResponseDTOBuilder = jwtWithRefreshTokenResponseDTOBuilder;
        this.verificationHandler = verificationHandler;
        this.validator = validator;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> auth(@RequestBody RegisterUserDTO userDTO) throws AuthException, IOException {
        this.validator.validate(userDTO.getEmail(), userDTO.getNickname(), userDTO.getPassword());
        this.registerHandler.save(UserConverter.convertToUser(userDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginUserDTO userDto) {
        JwtTokenResponseDTO jwtTokenResponseDTO = this.loginHandler.login(UserConverter.convertToUser(userDto));
        return new ResponseEntity<>(jwtTokenResponseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest tokenRequest) {
        RefreshTokenResponseDTO dto = this.jwtWithRefreshTokenResponseDTOBuilder.generateResponseDTO(tokenRequest.getRefreshToken());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/verify/code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> codeVerify(@RequestParam String code) {
        JwtTokenResponseDTO dto = this.verificationHandler.verifyCode(code);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
