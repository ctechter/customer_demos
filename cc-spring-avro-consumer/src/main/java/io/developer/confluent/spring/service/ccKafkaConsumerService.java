package io.developer.confluent.spring.service;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ccKafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ccKafkaConsumerService.class);

    @KafkaListener(topics = "${app.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, GenericRecord> record) {
        // Print a separator line for better readability
        System.out.println("\n" + "=".repeat(80));
        
        // Print message metadata
        System.out.println("📥 New Message Received!");
        System.out.println("🕒 Timestamp: " + new Date(record.timestamp()));
        System.out.println("🔑 Key: " + formatValue(record.key()));
        System.out.println("📍 Topic: " + record.topic());
        System.out.println("📎 Partition: " + record.partition());
        System.out.println("📌 Offset: " + record.offset());
        
        // Print the AVRO record contents
        GenericRecord avroRecord = record.value();
        System.out.println("\n📄 Message Content:");
        System.out.println("-".repeat(40));
        
        // Iterate through all fields in the AVRO record and use formatValue
        avroRecord.getSchema().getFields().forEach(field -> {
            String fieldName = field.name();
            Object fieldValue = avroRecord.get(fieldName);
            System.out.printf("%-20s: %s%n", fieldName, formatValue(fieldValue));
        });

        // Print footer separator
        System.out.println("=".repeat(80) + "\n");

        // Keep logging for file-based tracking
        logger.info("Processed message - Key: {} Partition: {} Offset: {}", 
            formatValue(record.key()), record.partition(), record.offset());
    }

    // Helper method to handle null values - now used throughout the code
    private String formatValue(Object value) {
        return value == null ? "<null>" : value.toString();
    }
}