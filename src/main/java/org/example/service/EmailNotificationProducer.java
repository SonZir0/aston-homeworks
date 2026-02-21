package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationProducer {

    @Autowired
    KafkaTemplate<String, String[]> template;

    public void sendEmailNotification(String email, String message) {
        template.send("emailNotifications",
                new String[]{email, message});
    }
}
