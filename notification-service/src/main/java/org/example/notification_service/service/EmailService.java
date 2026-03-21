package org.example.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String sendFromMail;

    @Autowired
    EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendFromMail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Message is sent!");
            logger.info("Mail sent successfully to: {} ", toEmail);
        } catch (MailAuthenticationException e) {
            // Authentication errors (e.g., wrong credentials, app password needed)
            logger.error("Authentication failed: {}", e.getMessage());
        } catch (MailSendException e) {
            // General send errors (e.g., connection issues, invalid addresses)
            logger.error("Error sending email: {}", e.getMessage());
        } catch (MailException e) {
            logger.error("An unexpected mail error occurred: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
        }
    }
}
