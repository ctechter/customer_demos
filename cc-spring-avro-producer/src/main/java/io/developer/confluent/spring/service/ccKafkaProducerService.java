package io.developer.confluent.spring.service;

import io.confluent.developer.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ccKafkaProducerService {

    private final KafkaTemplate<String, User> kafkaTemplate;
    private final String topicName;

    public ccKafkaProducerService(
            KafkaTemplate<String, User> kafkaTemplate,
            @Value("${app.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(User user) {
        CompletableFuture<SendResult<String, User>> future = 
            kafkaTemplate.send(topicName, user.getId().toString(), user);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully for user: {} with offset: {}", 
                    user.getId(), 
                    result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send message for user: {}", user.getId(), ex);
            }
        });
    }
    
    public CompletableFuture<SendResult<String, User>> sendMessageWithResponse(User user) {
        return kafkaTemplate.send(topicName, user.getId().toString(), user)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Message sent successfully for user: {} with offset: {}", 
                        user.getId(), 
                        result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send message for user: {}", user.getId(), ex);
                }
            });
    }
}
