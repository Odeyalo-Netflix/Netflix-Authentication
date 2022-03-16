package com.odeyalo.analog.auth.controllers;

import com.odeyalo.analog.auth.dto.MailMessageDTO;
import com.odeyalo.analog.auth.service.register.mail.SimpleMicroserviceDelegateMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    private final SimpleMicroserviceDelegateMailSender simpleMicroserviceDelegateMailSender;

    public MailController(SimpleMicroserviceDelegateMailSender simpleMicroserviceDelegateMailSender) {
        this.simpleMicroserviceDelegateMailSender = simpleMicroserviceDelegateMailSender;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody MailMessageDTO dto) {
        this.simpleMicroserviceDelegateMailSender.send(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
