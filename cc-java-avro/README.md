# Project cc-java-avro

## Contents: 
1. [Project Description](#project-description)
2. [Prerequisites](#prerequisites)
3. [Running the java client Producer](#running-the-java-client-producer)
4. [Running the java client Consumer](#running-the-java-client-consumer)

## Project Description:
This project demonstrates using the Apache Kafka Java Client to produce and consume messages.  
- When producing message there are three command line inputs:
    1. File location of the AVRO schema to validate the kafka message
    2. File location of the kafka message to be sent
    3. File location of the client.properties file 
- When consuming messages there is only one command line input:
    1. File location of the client.properties file

## Prerequisites:
- Java 17 (Any newer versions of Java may encounter a compile/run-time execution error)  
- Maven
- All required configuration settings can be found and, if needed, modified in the `/src/main/resources/client.properties` file.
- Java Producer; the topic in the `topic.name` property within the `client.properties` file must exist.  If it does not exist then create the topic in your Confluent Cloud cluster before running the application.
- Java Consumer; if using the Terraform scripts provided with this project the consumer name in the `group.id` property within the `client.properties` file must start with `consumer` or else a RBAC error will occur when connecting to Confluent Cloud.

## Running the java client Producer
1. Rename the `client_sample.properties` file  in the `src/main/resources` directory to `client.properties` 
2. Fill in all required confluent cloud connection and API key/secret values in the client.properties file
3. `mvn clean install`
4. Using Maven:
   `mvn exec:java -Dexec.mainClass="io.developer.confluent.ccJavaAvroClientApp" -Dexec.args="--producer src/main/AvroSchema/UserRecord.avsc src/main/messages/avro_message.json src/main/resources/client.properties"`
5. Using Java:
   `java -jar target/java-ccloud-avro-1.0.jar --producer src/main/AvroSchema/UserRecord.avsc src/main/messages/avro_message.json src/main/resources/client.properties`

## Running the java client Consumer
1. Rename the `client_sample.properties` file  in the `src/main/resources` directory to `client.properties`
2. Fill in all required confluent cloud connection and API key/secret values in the `client.properties` file
3. `mvn clean install`
4. Using Maven:
   `mvn exec:java -Dexec.mainClass="io.developer.confluent.ccJavaAvroClientApp" -Dexec.args="--consumer src/main/resources/client.properties"`
5. Using Java:
   `java -jar target/java-ccloud-avro-1.0.jar --consumer src/main/resources/client.properties`
