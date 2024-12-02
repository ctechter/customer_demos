# ----------------------------------------
# Confluent Cloud Kafka cluster variables
# ----------------------------------------
variable "confluent_cloud_api_key" {
  description = "Confluent Cloud API Key (also referred as Cloud API ID)"
  type        = string
}

variable "confluent_cloud_api_secret" {
  description = "Confluent Cloud API Secret"
  type        = string
  sensitive   = true
}

variable "cc_env_name" {
  type    = string
  default = "Confluent_SE_demoenv"
}

variable "cc_cluster_name" {
  type    = string
  default = "Confluent_SE_democluster"
}

variable "cc_availability" {
  type    = string
  default = "SINGLE_ZONE"
}

variable "numCKUs" {
  type = number
  default = 1
}

variable "cloud_provider" {
  type    = string
  default = "AWS"
}

variable "cloud_region" {
  type    = string
  default = "us-east-1"
}

# ------------------------------------------
# Confluent Cloud Schema Registry variables
# --------------------------------------------------------
# NOTE: As of Confluent TF provider v2.0 you CANNOT
# explicitly select the CSP and region.  Per a support note:
# "The cloud provider and region for the environment Schema Registry 
# and Stream Catalog will be the cloud provider and region of the 
# first cluster you create on the cloud environment."
# --------------------------------------------------------
variable "sr_package" {
  type    = string
  default = "ESSENTIALS"
}

# --------------------------------------------------------
# This 'random_id_4' will make whatever you create (names, etc)
# unique in your account.
# --------------------------------------------------------
resource "random_id" "id" {
  byte_length = 4
}
