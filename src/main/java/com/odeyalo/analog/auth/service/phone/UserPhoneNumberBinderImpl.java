package com.odeyalo.analog.auth.service.phone;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.PhoneNumberBindException;
import com.odeyalo.analog.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserPhoneNumberBinderImpl implements UserPhoneNumberBinder {
    private final UserRepository userRepository;

    @Autowired
    public UserPhoneNumberBinderImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void bindUserPhone(User user, String phoneNumber) {
        // TODO: create validation to user phone
        Optional<User> userOptional = this.userRepository.findUserByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            throw new PhoneNumberBindException("Cannot bind this phone to this user, phone is being used by other user. Please try another phone number");
        }
        user.setPhoneNumber(phoneNumber);
        this.userRepository.save(user);
    }
}
