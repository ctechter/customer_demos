Project cc-terraform-standard-rbac

Contents:
1. Project Description
2. Prerequisites
3. Setting up the project
4. Running the TF scripts
5. Post-execution considerations
6. Deleting the Confluent Cloud environment

Project Description:
This project contains the Terraform script that builds out a Confluent Cloud cluster for use with the various client language examples.  The cluster is built out as follows:
- Confluent Cloud standard cluster
- Networking: Public Endpoints
- Schema Registry with ADVANCED governance package
- Three Service Accounts:
    1. ‘env-manager’ (RBAC Role: EnvironmentAdmin)
    2. ‘app-manager’ (RBAC Role: CloudClusterAdmin)
    3. ‘applications’ (ALL producer/consumer applications should use this service account)
- RBAC Roles:
    1. DeveloperWrite for ALL topics
    2. DeveloperRead for ALL topics
    3. DeveloperRead for consumer group IDs starting with ‘confluent_cli_consumer_’
    4. DeveloperRead for consumer group IDs starting with ‘consumer’
- Three API keys:
    1. API Key for ‘env-manager’ used for Schema Registry access
    2. API key for ‘app-manager’ (if required for cluster-level activities)
    3. API key for ‘applications’ used for clients to connect to Confluent Cloud

Prerequisites:
1. Terraform installed
2. Confluent CLI installed.
3. An API key/secret with scope “Cloud resource management access” is required to run the Terraform script (if a cluster is needed).  Instructions on how to obtain a cloud API key can be found here.

Setting up the project:
- The only OS environment variables needed are a Confluent Cloud API Key/Secret; these should be entered into the ‘env_vars.txt’ file at the root directory or source directly.
- All terraform input variables are defined/set in the “terraform.tfvars” file; modify if needed/desired

Running the terraform scripts:
Run the following commands from the ‘cc-terraform-standard-rbac’ directory
1. If needed, create a Confluent Cloud API key as described here.
2. Input the API key/secret into the ‘env_vars.txt’ file, then run
source env_vars.txt  [or location of file holding required environment variables]
3. [OPTIONAL] change any variable settings in the “terraform.tfvars” file.
4. Run the following commands to build out the Confluent Cloud resources:
     a. terraform init
     b. terraform plan -out tf_cc_planresults.txt
     c. terraform apply tf_cc_planresults.txt
5. Once the script is completed and all resources are created run the following command to retrieve cluster settings include API keys:
     a. terraform output -raw cc_settings

Post-execution considerations:
- No topics are created in the above scripts.  Any topics to be used in the other exercises should be created via the UI.

Deleting the Confluent Cloud environment
To delete all created Confluent Cloud resources execute the following command:
1. terraform destroy
