package com.odeyalo.analog.auth.utils;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.Role;

import java.util.Set;

public class TestUtils {
    public static User buildUser(String email, String nickname, String password, boolean isBanned, Set<Role> roles) {
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(false).build();
        user.setRoles(roles);
        return user;
    }

    public static User buildUser(String email, String nickname, String password, boolean isBanned, Role role) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(false)
                .roles(role)
                .build();
    }

}
