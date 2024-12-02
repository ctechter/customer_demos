package io.developer.confluent;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ccAvroConsumer {
    public void run(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ConfluentCloudConsumer <client.properties>");
            System.exit(1);
        }

        String propertiesFile = args[0];
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(propertiesFile)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            System.exit(1);
        }

        String topic = props.getProperty("topic.name");
        String groupId = props.getProperty("group.id");

        if (topic == null || groupId == null) {
            System.err.println("Properties file must contain 'topic.name' and 'group.id'");
            System.exit(1);
        }

        // Add required consumer configurations
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));

            System.out.println("Consuming messages from topic: " + topic);
            System.out.printf("%-30s %-15s %-20s %-10s %-10s %s%n", 
                    "Timestamp", "Key", "Topic", "Partition", "Offset", "Message");
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            
                for (ConsumerRecord<String, String> record : records) {
                    String formattedOutput = String.format(
                            "%-30d %-15s %-20s %-10d %-10d %s",
                            record.timestamp(), // Timestamp
                            record.key() != null ? record.key() : "null", // Key
                            record.topic(), // Topic
                            record.partition(), // Partition
                            record.offset(), // Offset
                            record.value() // Message
                    );
            
                    System.out.println(formattedOutput);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in consumer: " + e.getMessage());
        }
    }
}
