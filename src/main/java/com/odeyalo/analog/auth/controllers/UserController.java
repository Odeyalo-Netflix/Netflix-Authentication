package com.odeyalo.analog.auth.controllers;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        Optional<User> optionalUser = this.userRepository.findUserByNickname(principal.getName());
        User user = optionalUser.get();
        Map<String, Object> answer = new HashMap<>();
        answer.put("name", user.getNickname());
        answer.put("image", user.getImage());
        answer.put("email", user.getEmail());
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}
