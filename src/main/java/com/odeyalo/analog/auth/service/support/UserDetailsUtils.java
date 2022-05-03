package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsUtils {
    private static UserRepository userRepository;

    public UserDetailsUtils(UserRepository userRepository) {
        UserDetailsUtils.userRepository = userRepository;
    }

    public static User getUserFromAuthentication(Authentication authentication) {
        CustomUserDetails details = (CustomUserDetails) authentication.getDetails();
        return details.getUser();
    }
}
