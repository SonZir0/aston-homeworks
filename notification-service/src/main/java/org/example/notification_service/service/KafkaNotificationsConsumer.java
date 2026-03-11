package org.example.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class KafkaNotificationsConsumer {
    EmailService emailService;

    @Autowired
    KafkaNotificationsConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(groupId = "testGroup", topics = "${app.kafka.topics.my-topic}")
    public void listenToTopic(String[] receivedData) {
        emailService.sendEmail(receivedData[0], "Your account status", receivedData[1]);
        System.out.println("Received message: " + Arrays.toString(receivedData));
    }
}
