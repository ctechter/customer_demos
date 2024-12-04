# Project cc-spring-avro-consumer

## Contents
1. [Project Description](#project-description)
2. [Prerequisites](#prerequisites)
3. [Running the Spring Boot Consumer](#running-the-spring-boot-consumer)
4. [Stopping the project](#stopping-the-project)

## Project Description
This Spring Boot Consumer project is designed to demonstrate how spring boot can be used to operate as a microservice to consume messages from a Confluent Cloud kafka topic.

## Prerequisites
- Spring boot version 3.3.5 (the version this project was written/compiled)
- Java 17
- Any newer versions of Java may encounter a compile/run-time execution error.  
    - To check and, if needed, explicitly set your java version to 17 run the following commands:
        1. `java -version`
        2. export JAVA_HOME=`/usr/libexec/java_home -v 17.0`
        3. `java -version (should see version 17 listed)`
- Maven
- All required configuration settings can be found and, if needed, modified in the `/src/main/resources/application.properties' file.
- The topic name provided in the `application.properties` file under the `topic.name` variable should be the same as the topic used for the spring boot producer application.  If this property was modified when executing the spring boot producer make sure the topic name is changed accordingly.

## Running the Spring Boot Consumer
1. Rename the `application_sample.properties` file  in the `src/main/resources` directory to `application.properties` 
2. Fill in all required confluent cloud connection and API key/secret values in the `application.properties` file
3. `mvn clean install`
4. `mvn spring-boot:run`

## Stopping the project
Type `ctrl-c` to cancel the execution of the spring boot consumer
