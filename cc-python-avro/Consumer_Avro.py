import os
from dotenv import load_dotenv
from confluent_kafka import DeserializingConsumer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import StringDeserializer
from avro_schema import AVRO_SCHEMA, message_to_dict

# Load environment variables
load_dotenv()

class MessageConsumer:
    def __init__(self):
        # Schema Registry configuration
        schema_registry_conf = {
            'url': os.getenv('SR_BOOTSTRAP_URL'),
            'basic.auth.user.info': f"{os.getenv('SR_API_KEY')}:{os.getenv('SR_API_SECRET')}"
        }
        schema_registry_client = SchemaRegistryClient(schema_registry_conf)

        # AVRO deserializer
        avro_deserializer = AvroDeserializer(
            schema_registry_client=schema_registry_client,
            schema_str=AVRO_SCHEMA,
            from_dict=message_to_dict
        )

        # Consumer configuration
        consumer_conf = {
            'bootstrap.servers': os.getenv('BOOTSTRAP_URL'),
            'security.protocol': 'SASL_SSL',
            'sasl.mechanism': 'PLAIN',
            'sasl.username': os.getenv('CLUSTER_API_KEY'),
            'sasl.password': os.getenv('CLUSTER_API_SECRET'),
            'group.id': os.getenv('GROUP_ID'),
            'auto.offset.reset': 'earliest',
            'key.deserializer': StringDeserializer('utf_8'),
            'value.deserializer': avro_deserializer,
            'session.timeout.ms': 45000,
            'heartbeat.interval.ms': 15000,
            'max.poll.interval.ms': 300000,
            'enable.auto.commit': True,
            'auto.commit.interval.ms': 5000,
            'partition.assignment.strategy': 'roundrobin'
        }

        self.consumer = DeserializingConsumer(consumer_conf)
        self.topic = os.getenv('TOPIC')
        self.group_id = os.getenv('GROUP_ID')

    def consume(self):
        """Consume messages from topic"""
        self.consumer.subscribe([self.topic])
        try:
            while True:
                msg = self.consumer.poll(1.0)
                if msg is None:
                    continue
                if msg.error():
                    print(f"Consumer error: {msg.error()}")
                    continue
                print(f'Received message: Key={msg.key()}, Value={msg.value()}')
        except KeyboardInterrupt:
            print("Stopping consumer...")
        finally:
            self.consumer.close()

def main():
    # Create consumer
    consumer = MessageConsumer()

    # Start consuming messages
    consumer.consume()

if __name__ == "__main__":
    main()