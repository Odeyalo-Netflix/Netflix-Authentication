package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.dto.request.LoginUserDTO;
import com.odeyalo.analog.auth.dto.request.RegisterUserDTO;
import com.odeyalo.analog.auth.entity.User;

public class UserConverter {

    public static User convertToUser(RegisterUserDTO dto) {
        return User.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public static RegisterUserDTO convertToRegisterUserDto(User user) {
        return new RegisterUserDTO(user.getNickname(), user.getEmail(), user.getPassword());
    }

    public static User convertToUser(LoginUserDTO dto) {
        return User.builder()
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .build();
    }

    public static LoginUserDTO convertToLoginUserDto(User user) {
        return new LoginUserDTO(user.getNickname(), user.getPassword());
    }
}
