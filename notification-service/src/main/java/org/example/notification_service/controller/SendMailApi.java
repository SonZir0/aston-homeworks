package org.example.notification_service.controller;

import jakarta.validation.Valid;
import org.example.notification_service.dto.SendEmailRequestDto;
import org.example.notification_service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class SendMailApi {
    EmailService emailService;

    @Autowired
    SendMailApi(EmailService userService) {
        this.emailService = userService;
    }

    @PostMapping("sendMailTo")
    public void sendMailTo(@RequestBody @Valid SendEmailRequestDto mailDto) {
        emailService.sendEmail(mailDto.email(), mailDto.subject(), mailDto.body());
    }
}
