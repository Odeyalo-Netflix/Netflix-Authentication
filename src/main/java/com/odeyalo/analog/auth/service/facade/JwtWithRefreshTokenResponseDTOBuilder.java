package com.odeyalo.analog.auth.service.facade;

import com.odeyalo.analog.auth.dto.response.RefreshTokenResponseDTO;
import com.odeyalo.analog.auth.exceptions.RefreshTokenException;

public interface JwtWithRefreshTokenResponseDTOBuilder {

    RefreshTokenResponseDTO generateResponseDTO(String token) throws RefreshTokenException;
}
