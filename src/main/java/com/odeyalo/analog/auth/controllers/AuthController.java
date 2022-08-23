package com.odeyalo.analog.auth.controllers;

import com.odeyalo.analog.auth.dto.request.*;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.dto.response.RefreshTokenResponseDTO;
import com.odeyalo.analog.auth.exceptions.ValidationException;
import com.odeyalo.analog.auth.service.facade.EmailCodeVerificationHandlerFacade;
import com.odeyalo.analog.auth.service.facade.JwtWithRefreshTokenResponseDTOBuilder;
import com.odeyalo.analog.auth.service.facade.login.UsernamePasswordLoginHandlerFacade;
import com.odeyalo.analog.auth.service.facade.register.UsernamePasswordRegisterHandlerFacade;
import com.odeyalo.analog.auth.service.recovery.PasswordRecoveryManagerFactory;
import com.odeyalo.analog.auth.service.recovery.PasswordRecoveryType;
import com.odeyalo.analog.auth.service.support.UserConverter;
import com.odeyalo.analog.auth.service.validators.RequestUserDTOValidator;
import com.odeyalo.analog.auth.service.validators.ValidationResult;
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
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UsernamePasswordRegisterHandlerFacade registerHandler;
    private final UsernamePasswordLoginHandlerFacade loginHandler;
    private final JwtWithRefreshTokenResponseDTOBuilder jwtWithRefreshTokenResponseDTOBuilder;
    private final EmailCodeVerificationHandlerFacade verificationHandler;
    private final RequestUserDTOValidator validator;
    private final PasswordRecoveryManagerFactory passwordRecoveryManagerFactory;

    public AuthController(@Qualifier("emailVerificationUsernamePasswordRegisterHandlerFacade") UsernamePasswordRegisterHandlerFacade registerHandler,
                          UsernamePasswordLoginHandlerFacade loginHandler,
                          JwtWithRefreshTokenResponseDTOBuilder jwtWithRefreshTokenResponseDTOBuilder,
                          EmailCodeVerificationHandlerFacade verificationHandler, RequestUserDTOValidator validator, PasswordRecoveryManagerFactory passwordRecoveryManagerFactory) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
        this.jwtWithRefreshTokenResponseDTOBuilder = jwtWithRefreshTokenResponseDTOBuilder;
        this.verificationHandler = verificationHandler;
        this.validator = validator;
        this.passwordRecoveryManagerFactory = passwordRecoveryManagerFactory;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> auth(@RequestBody RegisterUserDTO userDTO) throws AuthException, IOException {
        ValidationResult validationResult = this.validator.validate(userDTO.getEmail(), userDTO.getNickname(), userDTO.getPassword());
        if (!validationResult.isSuccess()) {
            throw new ValidationException(validationResult.getMessage());
        }
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

    @PostMapping(value = "/password/recovery/phone/number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendPhoneNumberRecoveryPasswordCode(@RequestBody PhoneNumberMethodPasswordRecoveryDTO dto) {
        this.passwordRecoveryManagerFactory.getManager(PasswordRecoveryType.PHONE_NUMBER).sendResetPasswordCode(dto.getPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/password/recovery/phone/number/code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPasswordUsingPhoneNumberCode(@RequestParam String code, @RequestBody NewPasswordDTO dto) {
        this.passwordRecoveryManagerFactory.getManager(PasswordRecoveryType.PHONE_NUMBER).changePassword(code, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/password/recovery/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendEmailRecoveryPasswordCode(@RequestBody EmailMethodPasswordRecoveryDTO dto) {
        this.passwordRecoveryManagerFactory.getManager(PasswordRecoveryType.EMAIL).sendResetPasswordCode(dto.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/password/recovery/email/code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPasswordUsingEmailCode(@RequestParam String code, @RequestBody NewPasswordDTO dto) {
        this.passwordRecoveryManagerFactory.getManager(PasswordRecoveryType.EMAIL).changePassword(code, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
