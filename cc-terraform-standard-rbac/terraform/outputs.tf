# --------------------------------------------------------
# Confluent Cloud Information
# --------------------------------------------------------
output "cc_settings" {
  value = <<-EOT
  Environment ID:          ${confluent_environment.cc_demo_env.id}
  CC Cluster ID:           ${confluent_kafka_cluster.cc_standard_cluster.id}
  CC Cluster Name:         ${confluent_kafka_cluster.cc_standard_cluster.display_name}
  Kafka Bootstrap:         ${confluent_kafka_cluster.cc_standard_cluster.bootstrap_endpoint}
  Schema Registry URL:     ${data.confluent_schema_registry_cluster.demo_sr.rest_endpoint}
  Schema Registry ID:      ${data.confluent_schema_registry_cluster.demo_sr.id}
  Schema Registry Package: ${data.confluent_schema_registry_cluster.demo_sr.package} 

  Service Accounts and their Confluent Cloud API Keys (API Keys inherit the permissions granted to the owner):
  NOTE: Envirornment API Key/Secret is used for Schema Registry access
  ${confluent_service_account.env-manager.display_name}:            ${confluent_service_account.env-manager.id}
  ${confluent_service_account.env-manager.display_name} API Key:    ${confluent_api_key.env-manager-schema-registry-api-key.id}
  ${confluent_service_account.env-manager.display_name} API Secret: ${confluent_api_key.env-manager-schema-registry-api-key.secret}

  ${confluent_service_account.app-manager.display_name}:            ${confluent_service_account.app-manager.id}
  ${confluent_service_account.app-manager.display_name} API Key:    ${confluent_api_key.app-manager-kafka-api-key.id}
  ${confluent_service_account.app-manager.display_name} API Secret: ${confluent_api_key.app-manager-kafka-api-key.secret}
  
  ${confluent_service_account.applications.display_name}:            ${confluent_service_account.applications.id}
  ${confluent_service_account.applications.display_name} API Key:    ${confluent_api_key.applications-kafka-api-key.id}
  ${confluent_service_account.applications.display_name} API Secret: ${confluent_api_key.applications-kafka-api-key.secret}

  EOT
  sensitive = true
}
