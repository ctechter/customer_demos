# Project cc-spring-avro-producer

## Contents
1. [Project Description](#project-description)
2. [Prerequisites](#prerequisites)
3. [Running the Spring Boot Producer](#running-the-spring-boot-producer)
4. [Sending a kafka message](#sending-a-kafka-message)
5. [Stopping the project](#stopping-the-project)

## Project Description
This Spring Boot producer project is designed to demonstrate the integration of kafka with AVRO serialization.  This application serves as a kafka producer, exposing a RESTful HTTP POST endpoint to publish a schema-compliant message to a pre-defined kafka topic.

## Prerequisites
- Spring boot version 3.3.5 (the version this project was written/compiled)
- Java 17 (Any newer versions of Java may encounter a compile/run-time execution error) 
- Maven
- All required configuration settings can be found and, if needed, modified in the `/src/main/resources/application.properties` file.
- The topic name provided in the `application.properties` file must already exist.  If it does not exist then create the topic in your Confluent Cloud cluster before running the application.

## Running the spring boot producer
- Rename the `application_sample.properties` file in the `src/main/resources` directory to `application.properties` 
- Fill in all required confluent cloud connection and API key/secret values in the `application.properties` file
- Confirm the topic entered into the `topic.name` variable in the `application.properties` file exists in your Confluent Cloud cluster.
- `mvn generate-sources` 
     - This creates the required AVRO class files
- `mvn clean install` 
- `mvn spring-boot:run`

## Sending a kafka message
The application exposes a RESTful endpoint at `localhost:8080/api/users/sync` to publish a message formatted as follows:
```json
{
    "id": "123",
    "name": "John Smith",
    "email": "jsmith@email.com"
}
```
The endpoint publishes the message to the configured kafka topic.

## Stopping the project
Type `ctrl-c` to cancel the spring boot producer
