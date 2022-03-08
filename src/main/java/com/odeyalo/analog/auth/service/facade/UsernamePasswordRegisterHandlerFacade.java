package com.odeyalo.analog.auth.service.facade;

import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.User;

import javax.security.auth.message.AuthException;

public interface UsernamePasswordRegisterHandlerFacade {

    JwtTokenResponseDTO save(User user) throws AuthException;
}
