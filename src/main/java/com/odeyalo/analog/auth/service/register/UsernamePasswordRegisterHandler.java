package com.odeyalo.analog.auth.service.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;
import com.odeyalo.analog.auth.exceptions.EmailExistException;
import com.odeyalo.analog.auth.exceptions.NicknameExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.support.Checker;
import com.odeyalo.analog.auth.service.validators.RequestUserDTOValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.Collections;

import static java.lang.String.format;

@Service
public class UsernamePasswordRegisterHandler implements RegisterHandler {
    private final UserRepository userRepository;
    private final Checker userEmailChecker;
    private final Checker userNicknameChecker;
    private final PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(UsernamePasswordRegisterHandler.class);

    public UsernamePasswordRegisterHandler(UserRepository userRepository,
                                           Checker userEmailChecker,
                                           Checker userNicknameChecker,
                                           PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userEmailChecker = userEmailChecker;
        this.userNicknameChecker = userNicknameChecker;
        this.encoder = encoder;
    }

    @Override
    public User register(User user) throws AuthException {
        if (this.userEmailChecker.check(user.getEmail())) {
            this.logger.error(format("User with email: %s already exist", user.getEmail()));
            throw new EmailExistException(format("User with email: %s already exist", user.getEmail()));
        }
        if (this.userNicknameChecker.check(user.getNickname())) {
            this.logger.error(format("User with nickname: %s already exist", user.getNickname()));
            throw new NicknameExistException(format("User with nickname: %s already exist", user.getNickname()));
        }
        user.setPassword(this.encoder.encode(user.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setImage("https://i1.sndcdn.com/avatars-ehmlKIYByI87eXnU-U8iJXg-t500x500.jpg");
        user.setRoles(Collections.singleton(Role.USER));
        return this.userRepository.save(user);
    }

    @Override
    public AuthProvider getAuthProvider() {
        return AuthProvider.LOCAL;
    }
}
