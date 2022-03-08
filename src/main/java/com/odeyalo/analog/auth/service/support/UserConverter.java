package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.dto.LoginUserDto;
import com.odeyalo.analog.auth.dto.RegisterUserDTO;
import com.odeyalo.analog.auth.entity.User;

public class UserConverter {

    public static User convertToUser(RegisterUserDTO dto) {
        return new User.UserBuilder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public static RegisterUserDTO convertToRegisterUserDto(User user) {
        return new RegisterUserDTO(user.getNickname(), user.getEmail(), user.getPassword());
    }

    public static User convertToUser(LoginUserDto dto) {
        return new User.UserBuilder()
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .build();
    }

    public static LoginUserDto convertToLoginUserDto(User user) {
        return new LoginUserDto(user.getNickname(), user.getPassword());
    }
}
