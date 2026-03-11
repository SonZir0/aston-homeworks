package org.example.notification_service.service;

import ch.martinelli.oss.testcontainers.mailpit.MailpitClient;
import ch.martinelli.oss.testcontainers.mailpit.MailpitContainer;
import ch.martinelli.oss.testcontainers.mailpit.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class EmailServiceTest {

    @Container
    @ServiceConnection
    static MailpitContainer mailpit = new MailpitContainer();

    @Autowired
    MailpitClient client;
    @Autowired
    EmailService emailService;

    @Test
    void sendEmailTest_SendsEmailWithSetParameters () {
        String email = "someTest@email";
        String subject = "Testing function";
        String body = "Some message body";

        emailService.sendEmail(email, subject, body);

        List<Message> messages = client.getAllMessages();
        assertEquals(1, messages.size());
        assertAll("Checking the email properties",
                () -> assertEquals(subject, messages.getFirst().subject()),
                () -> assertEquals(body, messages.getFirst().snippet()),
                () -> assertEquals(email, messages.getFirst().to().getFirst().toString()));
    }
}