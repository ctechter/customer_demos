Project cc-java-avro

Contents:
1. Project Description
2. Prerequisites
3. Running the java client - Producer
4. Running the java client - Consumer

Project Description:
This project demonstrates using the Apache Kafka Java Client to produce and consume messages.  
- When producing message there are three command line inputs:
    1. File location of the AVRO schema to validate the kafka message
    2. File location of the kafka message to be sent
    3. File location of the client.properties file 
- When consuming messages there is only one command line input:
    1. File location of the client.properties file

Prerequisites:
- Java 17
    - Any newer versions of Java may encounter a compile/run-time execution error.  
    - To check and, if needed, explicitly set your java version to 17 run the following commands:
        1. java -version
        2. export JAVA_HOME=`/usr/libexec/java_home -v 17.0`
        3. java -version (should see version 17 listed)
- Maven
- All required configuration settings can be found and, if needed, modified in the /src/main/resources/client.properties file.
- Java Producer; the topic in the ‘topic.name’ property within the client.properties file must exist.  If it does not exist then create the topic in Confluent Cloud before running the application.
- Java Consumer; if using the Terraform scripts provided with this project the consumer name in the ‘group.id’ property within the client.properties file must start with ‘consumer’ or else a RBAC error will occur when connecting to Confluent Cloud.

Running the java client - producer
1. Rename the ‘client_sample.properties’ file  in the ‘src/main/resources’ directory to ‘client.properties’ 
2. Fill in all required confluent cloud connection and API key/secret values in the client.properties file
3. mvn clean install
4a. Using Maven:
    - mvn exec:java -Dexec.mainClass="io.developer.confluent.ccJavaAvroClientApp" -Dexec.args="--producer src/main/AvroSchema/UserRecord.avsc src/main/messages/avro_message.json src/main/resources/client.properties"
4b. Using Java:
    -java -jar target/java-ccloud-avro-1.0.jar --producer src/main/AvroSchema/UserRecord.avsc src/main/messages/avro_message.json src/main/resources/client.properties

Running the java client - producer
1. Rename the ‘client_sample.properties’ file  in the ‘src/main/resources’ directory to ‘client.properties’ 
2. Fill in all required confluent cloud connection and API key/secret values in the client.properties file
3. mvn clean install
4a. Using Maven:
    - mvn exec:java -Dexec.mainClass="io.developer.confluent.ccJavaAvroClientApp" -Dexec.args="--consumer src/main/resources/client.properties"
4b. Using Java:
    - java -jar target/java-ccloud-avro-1.0.jar --consumer src/main/resources/client.properties