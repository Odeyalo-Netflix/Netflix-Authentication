package com.odeyalo.analog.auth.service.login;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.LoginException;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsernamePasswordLoginHandler implements LoginHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UsernamePasswordLoginHandler.class);

    public UsernamePasswordLoginHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(User user) throws LoginException {
        Optional<User> optional = this.userRepository.findUserByNickname(user.getNickname());
        if (!optional.isPresent() || !optional.get().isAccountActivated() || !passwordEncoder.matches(user.getPassword(), optional.get().getPassword())) {
            this.logger.info("Error in auth process: Wrong nickname or password");
            throw new UserNotExistException("Wrong nickname or password");
        }
        return optional.get();
    }
}
