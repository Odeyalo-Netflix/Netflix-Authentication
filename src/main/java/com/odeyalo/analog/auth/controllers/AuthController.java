package com.odeyalo.analog.auth.controllers;

import com.odeyalo.analog.auth.dto.LoginUserDTO;
import com.odeyalo.analog.auth.dto.RegisterUserDTO;
import com.odeyalo.analog.auth.dto.request.RefreshTokenRequest;
import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.dto.response.RefreshTokenResponseDTO;
import com.odeyalo.analog.auth.service.facade.UsernamePasswordLoginHandlerFacade;
import com.odeyalo.analog.auth.service.facade.UsernamePasswordRegisterHandlerFacade;
import com.odeyalo.analog.auth.service.facade.JwtWithRefreshTokenResponseDTOBuilder;
import com.odeyalo.analog.auth.service.support.UserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UsernamePasswordRegisterHandlerFacade registerHandler;
    private final UsernamePasswordLoginHandlerFacade loginHandler;
    private final JwtWithRefreshTokenResponseDTOBuilder jwtWithRefreshTokenResponseDTOBuilder;

    public AuthController(UsernamePasswordRegisterHandlerFacade registerHandler, UsernamePasswordLoginHandlerFacade loginHandler, JwtWithRefreshTokenResponseDTOBuilder jwtWithRefreshTokenResponseDTOBuilder) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
        this.jwtWithRefreshTokenResponseDTOBuilder = jwtWithRefreshTokenResponseDTOBuilder;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody RegisterUserDTO userDTO) throws AuthException {
        JwtTokenResponseDTO jwtTokenResponseDTO = registerHandler.save(UserConverter.convertToUser(userDTO));
        return new ResponseEntity<>(jwtTokenResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO userDto) {
        JwtTokenResponseDTO jwtTokenResponseDTO = this.loginHandler.login(UserConverter.convertToUser(userDto));
        return new ResponseEntity<>(jwtTokenResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest tokenRequest) {
        RefreshTokenResponseDTO dto = this.jwtWithRefreshTokenResponseDTOBuilder.generateResponseDTO(tokenRequest.getRefreshToken());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
