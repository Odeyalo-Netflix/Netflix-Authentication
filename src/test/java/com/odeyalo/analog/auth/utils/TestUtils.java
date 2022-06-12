package com.odeyalo.analog.auth.utils;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.VerificationCode;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

public class TestUtils {
    public static User buildUser(String email, String nickname, String password, boolean isBanned, Set<Role> roles) {
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(isBanned).build();
        user.setRoles(roles);
        return user;
    }

    public static User buildUser(String email, String nickname, String password, boolean isBanned, Role role) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(isBanned)
                .role(role)
                .build();
    }
    public static User buildUser(String email, String nickname, String password, boolean isBanned, String phoneNumber, Role role) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(isBanned)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }

    public static User buildUser(Integer id, String email, String nickname,
                                      String password, boolean isBanned,
                                      AuthProvider provider, boolean activated, String image, Role role) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(isBanned)
                .role(role)
                .authProvider(provider)
                .activated(activated)
                .image(image)
                .build();
    }
    public static User buildUser(String email, String nickname,
                                 String password, boolean isBanned,
                                 AuthProvider provider, boolean activated, String image, String phoneNumber, Role role) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(isBanned)
                .role(role)
                .authProvider(provider)
                .phoneNumber(phoneNumber)
                .activated(activated)
                .image(image)
                .build();
    }
    public static User buildUser(String email, String nickname,
                                 String password, boolean isBanned,
                                 AuthProvider provider, boolean activated, String image, Role role) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .banned(isBanned)
                .role(role)
                .authProvider(provider)
                .activated(activated)
                .image(image)
                .build();
    }

    public static User buildGeneratedUser(Integer userId) {
        return User.builder()
                .id(userId)
                .email("generated@gmail.com")
                .password("password")
                .role(Role.USER)
                .authProvider(AuthProvider.LOCAL)
                .nickname("generated123")
                .banned(false)
                .image("")
                .build();
    }

    public static VerificationCode buildVerificationCode(String codeValue, User user, Integer id) {
        return VerificationCode.builder()
                .id(id)
                .codeValue(codeValue)
                .user(user)
                .expired(LocalDateTime.now().plusMinutes(3))
                .isActivated(false)
                .build();
    }
}
