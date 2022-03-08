package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEmailChecker implements Checker {
    private final UserRepository userRepository;

    public UserEmailChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean check(String email) {
        Optional<User> optionalUser = this.userRepository.findUserByEmail(email);
        return optionalUser.isPresent();
    }
}
