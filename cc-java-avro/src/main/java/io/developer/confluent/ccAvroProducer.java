package io.developer.confluent;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ccAvroProducer {

    public void run(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java ccAvroProducer <schemaFile> <messageFile> <clientProperties>");
            System.exit(1);
        }
        String schemaFile = args[0];
        String messageFile = args[1];
        String clientPropertiesFile = args[2];

        try {
            // Load client properties
            final Properties properties = new Properties();
            try (InputStream inputStream = new FileInputStream(clientPropertiesFile)) {
                properties.load(new FileReader(clientPropertiesFile));
            }

            // Configure Kafka producer
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

            // Load Avro schema
            Schema schema = new Schema.Parser().parse(new File(schemaFile));

            // Parse JSON message
            ObjectMapper mapper = new ObjectMapper();
            JsonNode messageJson = mapper.readTree(new File(messageFile));

            // Create a GenericRecord based on the schema
            GenericRecord avroRecord = new GenericData.Record(schema);
            for (Schema.Field field : schema.getFields()) {
                String fieldName = field.name();
                Schema.Type fieldType = field.schema().getType();
            
                if (messageJson.has(fieldName)) {
                    JsonNode value = messageJson.get(fieldName);
            
                    switch (fieldType) {
                        case INT:
                            // Handle age as a number
                            avroRecord.put(fieldName, Integer.parseInt(value.asText())); // Fix for stringified numbers
                            break;
                        case STRING:
                            avroRecord.put(fieldName, value.asText());
                            break;
                        default:
                            System.err.println("Unsupported field type: " + fieldType);
                    }
                } else {
                    System.err.println("Warning: Missing field in message: " + fieldName);
                }
            }
            
            String topic = properties.getProperty("topic.name");
            if (topic == null || topic.isEmpty()) {
                System.err.println("Error: 'topic' must be specified in the client properties.\n");
                System.exit(1);
            }

            // Create Kafka Producer
            KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(properties);
            System.out.printf("Sending Message: " + avroRecord + "\n");

            // Send message
            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(topic, avroRecord);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.printf("Message sent to topic %s, partition %d, offset %d%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });

            producer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
