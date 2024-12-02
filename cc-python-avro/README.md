Project cc-python-avro

Contents:
1. Project Description
2. Files
3. Considerations - AVRO Schema and Message configuration
4. Prerequisites
5. Running the Python client - producer
6. Running the Python client - consumer

Project Description:
This project contains a sample kafka Python producer and consumer that will send a ‘schema-compliant’ AVRO message to a topic within a Confluent Cloud cluster.

Contents:
1. avro_message.json
     - A schema-compliant message to be sent to Confluent Cloud when running the Python producer.  This message must be compliant based on the schema defined in ‘avro_schema.py’
2. avro_schema.py
     - Contains the schema to be used for validating the message being sent to Confluent Cloud
3. Consumer_Avro.py
     - A kafka Consumer that will connect to Confluent Cloud and consume messages from a specified topic
4. Producer_Avro.py
     - A kafka Producer that connects to Confluent Cloud and sends the message defined in ‘avro_message.json’ to a specified topic

Considerations - AVRO Schema and Message configuration:
The python producer takes as a command-line input a file that contains a JSON-formatted message to be sent to a topic in Confluent Cloud.  The schema is stored in the ‘avro_schema.py’ file.

Prerequisites:
- Python 3 (the project was written/executed using python version 3.11)
An accessible Confluent Cloud cluster.  This project assumes a Confluent Cloud standard cluster using public endpoints and RBAC.  A terraform script to create this cluster is provided in a separate part of this project.
- The topic name provided in the TOPIC environment variable MUST exist, otherwise an error will occur.
- Required environment variables can be found in the 'env_vars.txt' file found at the root directory of this project.  This includes the following:  
     - Topic name in CC cluster (this MUST be created before running the producer)
     - Name schema definition file (this exists in the root directory of the project)
     - Consumer group ID (this MUST start with ‘consumer’ or else an RBAC error will occur)

Running the python scripts - producer:
These commands are executed from within the root cc-python-avro folder:
1. source env_vars.txt [or location of file holding required environment variables]
2. Create the virtual environment
     a. virtualenv ccloud-env
3. source ccloud-env/bin/activate
4. pip install confluent-kafka requests avro-python3 fastavro python-dotenv
5. python Producer_Avro.py avro_message.json
6. python ccloud-env/bin/deactivate

Running the python scripts - consumer:
These commands are executed from within the root cc-python-avro folder:
1. source env_vars.txt [or location of file holding required environment variables]
2. Create the virtual environment
3. virtualenv ccloud-env
4. source ccloud-env/bin/activate
5. pip install confluent-kafka requests avro-python3 fastavro python-dotenv
6. python Consumer_Avro.py
7. type ctrl+c to cancel the consumer
8. python ccloud-env/bin/deactivate
