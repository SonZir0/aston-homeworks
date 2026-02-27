package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationProducer {
    @Value("${app.kafka.topics.my-topic}")
    private String topicName;

    @Autowired
    KafkaTemplate<String, String[]> template;

    public void sendEmailNotification(String email, String message) {
        template.send(topicName,
                new String[]{email, message});
    }
}
