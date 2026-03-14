package org.example.notification_service.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.*;


@SpringBootTest
@Testcontainers
class KafkaNotificationsConsumerTest {
    @Value("${app.kafka.topics.my-topic}")
    private String topicName;

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka-native:latest")
    );

    @MockitoBean
    EmailService emailService;

    @Autowired
    private KafkaTemplate<String, String[]> kafkaTemplate;

    @Test
    void listenToTopicTest_SendsEmailOnKafkaEvent() {
        kafkaTemplate.send(topicName, new String[] {"mailFrom@testTemplate", "Test message body"});
        kafkaTemplate.send(topicName, new String[] {"test@mail2", "Test message body"});
        verify(emailService, timeout(10000).times(2)).sendEmail(any(), any(), any());
    }
}