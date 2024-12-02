Project cc-spring-avro-producer

Contents:
1. Project Description
2. Prerequisites 
3. Running the Spring Boot Producer
4. Sending a kafka message
5. Stopping the project

Project Description:
This Spring Boot producer project is designed to demonstrate the integration of kafka with AVRO serialization.  This application serves as a kafka producer, exposing a RESTful HTTP POST endpoint to publish a schema-compliant message to a pre-defined kafka topic.

Prerequisites:
- Spring boot version 3.3.5 (the version this project was written/compiled)
- Java 17
     - Any newer versions of Java may encounter a compile/run-time execution error.  
     - To check and, if needed, explicitly set your java version to 17 run the following commands:
          1. java -version
          2. export JAVA_HOME=`/usr/libexec/java_home -v 17.0`
          3. java -version (should see version 17 listed)
- Maven
- All required configuration settings can be found and, if needed, modified in the /src/main/resources/application.properties file.
- The topic name provided in the application.properties file MUST already exist.  If it does not exist then create the topic in Confluent Cloud before running the application.

Running the spring boot producer
- Rename the ‘application_sample.properties’ file in the ‘src/main/resources’ directory to ‘application.properties’ 
- Fill in all required confluent cloud connection and API key/secret values in the application.properties file
- Confirm the topic entered into the ‘topic.name’ variable in the ‘application.properties’ file exists in your Confluent Cloud cluster.
- mvn generate-sources 
     - This creates the required AVRO class files
- mvn clean install 
- mvn spring-boot:run

Sending a kafka message:
The application exposes a RESTful endpoint at localhost:8080/api/users/sync to publish a message formatted as follows:
{
    "id": "123",
    "name": "John Smith",
    "email": "jsmith@email.com"
}
The endpoint publishes the message to the configured kafka topic.

Stopping the project:
Type ctrl-c to cancel the spring boot producer