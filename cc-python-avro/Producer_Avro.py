import os
import sys
import json
from dotenv import load_dotenv
from confluent_kafka import SerializingProducer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer
from confluent_kafka.serialization import StringSerializer
from avro_schema import AVRO_SCHEMA, dict_to_message

# Load environment variables
load_dotenv()

class MessageProducer:
    def __init__(self):
        # Schema Registry configuration
        schema_registry_conf = {
            'url': os.getenv('SR_BOOTSTRAP_URL'),
            'basic.auth.user.info': f"{os.getenv('SR_API_KEY')}:{os.getenv('SR_API_SECRET')}"
        }
        schema_registry_client = SchemaRegistryClient(schema_registry_conf)

        # AVRO serializer
        avro_serializer = AvroSerializer(
            schema_registry_client=schema_registry_client,
            schema_str=AVRO_SCHEMA,
            to_dict=dict_to_message
        )

        # Producer configuration
        producer_conf = {
            'bootstrap.servers': os.getenv('BOOTSTRAP_URL'),
            'security.protocol': 'SASL_SSL',
            'sasl.mechanism': 'PLAIN',
            'sasl.username': os.getenv('CLUSTER_API_KEY'),
            'sasl.password': os.getenv('CLUSTER_API_SECRET'),
            'key.serializer': StringSerializer('utf_8'),
            'value.serializer': avro_serializer,
            'acks': 'all',
            'enable.idempotence': 'true'
        }

        self.producer = SerializingProducer(producer_conf)
        self.topic = os.getenv('TOPIC')

    def produce(self, key: str, value: dict):
        """Produce message to topic"""
        try:
            self.producer.produce(
                topic=self.topic,
                key=key,
                value=value,
                on_delivery=self.delivery_report
            )
            self.producer.flush()
        except Exception as e:
            print(f"Exception while producing record - {value}: {e}")

    def delivery_report(self, err, msg):
        """Callback for message delivery"""
        if err is not None:
            print(f'Message delivery failed: {err}')
        else:
            print(f'Message delivered to {msg.topic()} [{msg.partition()}] at offset {msg.offset()}')

def load_message_from_file(filename):
    """Load message from JSON file and validate fields"""
    try:
        with open(filename, 'r') as file:
            data = json.load(file)
        
        # Extract required fields dynamically from AVRO_SCHEMA
        schema = json.loads(AVRO_SCHEMA)
        required_fields = [field['name'] for field in schema.get('fields', [])]
        
        # Check for missing fields
        missing_fields = [field for field in required_fields if field not in data]
        if missing_fields:
            raise ValueError(f"Missing required fields in the JSON message: {', '.join(missing_fields)}")
        
        # Prepare message
        message = {field: data[field] for field in required_fields}
        
        # Validate key
        key = data.get('key', 'default-key')
        
        return key, message
    except FileNotFoundError:
        print(f"File {filename} not found.")
        sys.exit(1)
    except json.JSONDecodeError:
        print(f"Invalid JSON in {filename}")
        sys.exit(1)
    except ValueError as e:
        print(f"Error: {e}")
        sys.exit(1)

def main():
    # Check if filename is provided
    if len(sys.argv) < 2:
        print("Usage: python producer.py <message_file.json>")
        sys.exit(1)

    # Create producer
    producer = MessageProducer()

    # Load message from file
    filename = sys.argv[1]
    key, message = load_message_from_file(filename)

    # Produce message
    producer.produce(key, message)
    print(f"Sent message from {filename}")

if __name__ == "__main__":
    main()