package com.odeyalo.analog.auth.service.facade.login;

import com.odeyalo.analog.auth.dto.response.JwtTokenResponseDTO;
import com.odeyalo.analog.auth.entity.User;

public interface UsernamePasswordLoginHandlerFacade {

    JwtTokenResponseDTO login(User user);
}
