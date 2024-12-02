AVRO_SCHEMA = """
{
    "namespace": "example.avro",
    "type": "record",
    "name": "Message",
    "fields": [
        {"name": "field1", "type": "string"},
        {"name": "field2", "type": "string"},
        {"name": "field3", "type": "string"}
    ]
}
"""

def dict_to_message(obj, ctx):
    """Convert dict to message"""
    return obj

def message_to_dict(obj, ctx):
    """Convert message to dict"""
    return obj