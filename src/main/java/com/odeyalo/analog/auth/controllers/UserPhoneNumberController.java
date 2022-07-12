package com.odeyalo.analog.auth.controllers;

import com.odeyalo.analog.auth.dto.ChangeUserPhoneNumberDTO;
import com.odeyalo.analog.auth.service.phone.PhoneNumberBinderManager;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/phone/number")
public class UserPhoneNumberController {
    private final PhoneNumberBinderManager manager;
    private final Logger logger = LoggerFactory.getLogger(UserPhoneNumberController.class);

    @Autowired
    public UserPhoneNumberController(PhoneNumberBinderManager manager) {
        this.manager = manager;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUserPhoneNumber(@RequestBody ChangeUserPhoneNumberDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        this.logger.info("Principal is: {}", principal);
        this.manager.sendVerificationCode(principal.getUser(), dto.getNewPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/confirm/code")
    public ResponseEntity<?> changePhoneIfPasswordIsCorrect(@RequestParam String code, @RequestBody ChangeUserPhoneNumberDTO dto) {
        this.manager.changeUserPhoneIfCodeCorrect(code, dto.getNewPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
