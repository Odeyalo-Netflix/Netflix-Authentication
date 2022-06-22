package com.odeyalo.analog.auth.service.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.oauth2.support.info.Oauth2UserInfo;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

@Service
public class GoogleOauth2RegisterHandler implements Oauth2RegisterHandler {
    private final UserRepository userRepository;

    public GoogleOauth2RegisterHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) throws AuthException {
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.addRole(Role.USER);
        return this.userRepository.save(user);
    }

    @Override
    public User register(Oauth2UserInfo info) throws AuthException {
        User user = User.builder()
                .email(info.getEmail())
                .nickname(info.getFirstName())
                .banned(false)
                .image(info.getImage())
                .activated(true)
                .build();
        return this.register(user);
    }

    @Override
    public AuthProvider getAuthProvider() {
        return AuthProvider.GOOGLE;
    }
}
