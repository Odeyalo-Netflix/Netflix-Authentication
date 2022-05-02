package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;
import com.odeyalo.analog.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptEncoderPasswordRecoverySaverSupport implements PasswordRecoverySaverSupport {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public BcryptEncoderPasswordRecoverySaverSupport(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void updatePassword(User user, String rawPassword) throws UserNotExistException {
        if (user == null)
            throw new UserNotExistException("User not exist.");
        String encodedPassword = this.passwordEncoder.encode(rawPassword);
        this.userRepository.updateUserPassword(user, encodedPassword);
    }

    @Override
    public void updatePassword(String email, String rawPassword) throws UserNotExistException {
        User user = this.userRepository.findUserByEmail(email)
            .orElseThrow(
                    () -> new UserNotExistException("Presented code is wrong")
            );
        String encodedPassword = this.passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        this.userRepository.save(user);
    }
}
