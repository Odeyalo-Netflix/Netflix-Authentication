package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserNicknameChecker implements Checker {
    private final UserRepository userRepository;

    public UserNicknameChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean check(String nickname) {
        Optional<User> optionalUser = this.userRepository.findUserByNickname(nickname);
        return optionalUser.isPresent();
    }
}
