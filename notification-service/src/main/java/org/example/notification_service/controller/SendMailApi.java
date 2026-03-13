package org.example.notification_service.controller;

import jakarta.validation.Valid;
import org.example.notification_service.dto.SendEmailRequestDto;
import org.example.notification_service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
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
