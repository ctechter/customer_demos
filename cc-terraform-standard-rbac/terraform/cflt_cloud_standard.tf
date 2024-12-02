# -------------------------------------------------------
# Confluent Cloud Environment and Clusters
# --------------------------------------------------------
# NOTE: As of Confluent TF provider v2.0:
# - Stream governance package is selected as in the environment resource
# - You CANNOT explicitly select the CSP and region.  Per a support note:
# "The cloud provider and region for the environment Schema Registry 
# and Stream Catalog will be the cloud provider and region of the 
# first cluster you create on the cloud environment."
# --------------------------------------------------------
resource "confluent_environment" "cc_demo_env" {
  display_name = "${var.cc_env_name}"
  stream_governance{
    package = var.sr_package
  }
  lifecycle {
    prevent_destroy = false
  }
}

data "confluent_schema_registry_cluster" "demo_sr" {
  environment {
    id = confluent_environment.cc_demo_env.id
  }

  depends_on = [
    confluent_kafka_cluster.cc_standard_cluster
  ]
}

# --------------------------------------------------------
# Confluent Cloud Kafka Cluster
# --------------------------------------------------------
resource "confluent_kafka_cluster" "cc_standard_cluster" {
  display_name = "${var.cc_cluster_name}"
  availability = var.cc_availability
  cloud        = var.cloud_provider
  region       = var.cloud_region
  standard {}
  environment {
    id = confluent_environment.cc_demo_env.id
  }
  lifecycle {
    prevent_destroy = false
  }
}