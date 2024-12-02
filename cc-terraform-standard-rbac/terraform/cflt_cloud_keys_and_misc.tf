# ------------------------------------------------------------------
# The following service accounts are created:
# 1. 'env-manager' svc acct to manage enviornment-level resources 
#     (like Schema Registry)
# 2. 'app-manager' svc acct is used to create topics and assign roles to
#    'applications' service accounts.
# 3. 'applications' is used for clients to connect to the cluster
# ------------------------------------------------------------------
resource "confluent_service_account" "env-manager"{
  display_name = "${var.cc_env_name}-env-manager"
  description = "Service account to manage CC environment"
}

resource "confluent_role_binding" "env-manager-kafka-cluster-admin" {
  principal   = "User:${confluent_service_account.env-manager.id}"
  role_name   = "EnvironmentAdmin"
  crn_pattern = confluent_environment.cc_demo_env.resource_name
}

resource "confluent_api_key" "env-manager-schema-registry-api-key" {
  display_name = "env-manager-schema-registry-api-key"
  description  = "Schema Registry API Key that is owned by 'env-manager' service account"
  owner {
    id          = confluent_service_account.env-manager.id
    api_version = confluent_service_account.env-manager.api_version
    kind        = confluent_service_account.env-manager.kind
  }

  managed_resource {
    id          = data.confluent_schema_registry_cluster.demo_sr.id
    api_version = data.confluent_schema_registry_cluster.demo_sr.api_version
    kind        = data.confluent_schema_registry_cluster.demo_sr.kind

    environment {
      id = confluent_environment.cc_demo_env.id
    }
  }
}

resource "confluent_service_account" "app-manager" {
  display_name = "${var.cc_cluster_name}-app-manager"
  description  = "Service account to manage Kafka cluster"
}

resource "confluent_role_binding" "app-manager-kafka-cluster-admin" {
  principal   = "User:${confluent_service_account.app-manager.id}"
  role_name   = "CloudClusterAdmin"
  crn_pattern = confluent_kafka_cluster.cc_standard_cluster.rbac_crn
}

resource "confluent_api_key" "app-manager-kafka-api-key" {
  display_name = "app-manager-kafka-api-key"
  description  = "Kafka API Key that is owned by 'app-manager' service account"
  owner {
    id          = confluent_service_account.app-manager.id
    api_version = confluent_service_account.app-manager.api_version
    kind        = confluent_service_account.app-manager.kind
  }

  managed_resource {
    id          = confluent_kafka_cluster.cc_standard_cluster.id
    api_version = confluent_kafka_cluster.cc_standard_cluster.api_version
    kind        = confluent_kafka_cluster.cc_standard_cluster.kind

    environment {
      id = confluent_environment.cc_demo_env.id
    }
  }
  depends_on = [
    confluent_role_binding.app-manager-kafka-cluster-admin
  ]
}

resource "confluent_service_account" "applications" {
  display_name = "applications"
  description  = "Service account to produce and consume from topics"
}

resource "confluent_api_key" "applications-kafka-api-key" {
  display_name = "applications-kafka-api-key"
  description  = "Kafka API Key that is owned by 'applications' service account"
  owner {
    id          = confluent_service_account.applications.id
    api_version = confluent_service_account.applications.api_version
    kind        = confluent_service_account.applications.kind
  }

  managed_resource {
    id          = confluent_kafka_cluster.cc_standard_cluster.id
    api_version = confluent_kafka_cluster.cc_standard_cluster.api_version
    kind        = confluent_kafka_cluster.cc_standard_cluster.kind

    environment {
      id = confluent_environment.cc_demo_env.id
    }
  }
}

# ------------------------------------------------------------------
# From a RBAC standpoint I'm just giving the 'applications' svc acct
# Developer Read and Write access to all topics
# ------------------------------------------------------------------
resource "confluent_role_binding" "applications-developer-write" {
  principal   = "User:${confluent_service_account.applications.id}"
  role_name   = "DeveloperWrite"
  crn_pattern = "${confluent_kafka_cluster.cc_standard_cluster.rbac_crn}/kafka=${confluent_kafka_cluster.cc_standard_cluster.id}/topic=*"
}

resource "confluent_role_binding" "applications-developer-read-from-topic" {
  principal   = "User:${confluent_service_account.applications.id}"
  role_name   = "DeveloperRead"
  crn_pattern = "${confluent_kafka_cluster.cc_standard_cluster.rbac_crn}/kafka=${confluent_kafka_cluster.cc_standard_cluster.id}/topic=*"
}

// Note that in order to consume from a topic, the principal of the consumer ('applications' service account)
// needs to be authorized to perform 'READ' operation on both Topic and Group resources:
resource "confluent_role_binding" "applications-developer-read-from-group" {
  principal = "User:${confluent_service_account.applications.id}"
  role_name = "DeveloperRead"
  // The existing value of crn_pattern's suffix (group=confluent_cli_consumer_*) are set up to match Confluent CLI's default consumer group ID ("confluent_cli_consumer_<uuid>").
  // https://docs.confluent.io/confluent-cli/current/command-reference/kafka/topic/confluent_kafka_topic_consume.html
  // Update it to match your target consumer group ID.
  crn_pattern = "${confluent_kafka_cluster.cc_standard_cluster.rbac_crn}/kafka=${confluent_kafka_cluster.cc_standard_cluster.id}/group=confluent_cli_consumer_*"
}

resource "confluent_role_binding" "applications-developer-read-from-group-2" {
  principal = "User:${confluent_service_account.applications.id}"
  role_name = "DeveloperRead"
  // The existing value of crn_pattern's suffix (group=confluent_cli_consumer_*) are set up to match Confluent CLI's default consumer group ID ("confluent_cli_consumer_<uuid>").
  // https://docs.confluent.io/confluent-cli/current/command-reference/kafka/topic/confluent_kafka_topic_consume.html
  // Update it to match your target consumer group ID.
  crn_pattern = "${confluent_kafka_cluster.cc_standard_cluster.rbac_crn}/kafka=${confluent_kafka_cluster.cc_standard_cluster.id}/group=consumer*"
}