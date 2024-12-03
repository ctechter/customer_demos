# Confluent Cloud - Cluster Creation / Configuration and Client Demos

This project contains a Confluent Cloud terraform cluster build and various client language producer and consumer examples.  See the readme files within each folder for a further description of what is being built.  

## A few things of note:
- An API key/secret with scope “Cloud resource management access” is required to run the Terraform script (if a cluster is needed).  Instructions on how to obtain a cloud API key can be found here.
- RBAC has been setup to allow for ‘DeveloperRead’ access for ANY consumer whose consumer group ID starts with ‘consumer’.  Any changes or new development should ensure their consumer group ID starts with ‘consumer’ or there will be RBAC permission errors.
- These samples are based on AVRO schemas and sample messages located in the client projects.  These can be changed as desired, care should be taken to ensure sample messages sent to the topic are modified accordingly so schema validation errors do not occur.
