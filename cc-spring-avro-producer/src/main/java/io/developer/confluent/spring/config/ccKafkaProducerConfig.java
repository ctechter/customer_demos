package io.developer.confluent.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class ccKafkaProducerConfig {
    
    @Value("${app.topic.name}")
    private String topicName;
    
    public String getTopicName() {
        return topicName;
    }
}