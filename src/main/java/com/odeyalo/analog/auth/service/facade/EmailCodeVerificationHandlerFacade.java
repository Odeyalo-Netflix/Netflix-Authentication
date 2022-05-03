package com.odeyalo.analog.auth.service.facade;

import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.exceptions.CodeVerificationException;

public interface EmailCodeVerificationHandlerFacade {

    JwtTokenResponseDTO verifyCode(String codeValue) throws CodeVerificationException;

}
