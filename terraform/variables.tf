variable "aws_region" {
  type        = string
  default     = "eu-west-1"
  description = "AWS Region where all resources are deployed"
}

variable "cloudwatch_log_group_name" {
  type        = string
  default     = "/ecs/CUDLContent"
  description = "Name of the CloudWatch Log Group for build logs"
}

variable "cloudwatch_log_stream_name" {
  type        = string
  default     = "codebuild"
  description = "Name of the CloudWatch Log Stream for build logs"
}

variable "codebuild_project_name" {
  type        = string
  default     = "cudl-viewer"
  description = "Name of the CodeBuild Build Project"
}

variable "codebuild_compute_type" {
  type        = string
  default     = "BUILD_GENERAL1_SMALL"
  description = "Information about the compute resources the build project will use"
}

variable "codebuild_image" {
  type        = string
  default     = "aws/codebuild/amazonlinux2-x86_64-standard:4.0"
  description = "Docker image to use for this build project"
}

variable "codebuild_environment_type" {
  type        = string
  default     = "LINUX_CONTAINER"
  description = "Type of build environment to use for related builds"
}

variable "codebuild_image_pull_credentials_type" {
  type        = string
  default     = "CODEBUILD"
  description = "Type of credentials AWS CodeBuild uses to pull images in your build"
}

variable "codebuild_privileged_mode" {
  type        = bool
  default     = true
  description = "Whether to run CodeBuild in privileged mode. This must be set to true to run docker-in-docker"
}

variable "codebuild_git_submodule_data_samples" {
  type        = string
  default     = "docker/db/dl-data-samples-http"
  description = "Path to the data-samples git submodule used in DB Docker build"
}

variable "codebuild_github_user" {
  type    = string
  default = null
}

variable "codebuild_github_token" {
  type    = string
  default = null
}

variable "s3_bucket_name" {
  type        = string
  default     = "rmm98-sandbox-cudl-ecs"
  description = "Name of the S3 Bucket used for artifacts"
}

variable "iam_role_name" {
  type        = string
  default     = "codebuild-cudl-viewer"
  description = "Name of the IAM role used by the CodeBuild build project"
}

variable "vpc_name" {
  type        = string
  default     = "cudl-vpc"
  description = "Name of the VPC used by CodeBuild"
}

variable "vpc_subnet_names" {
  type        = list(string)
  default     = ["cudl-subnet-private1-eu-west-1a", "cudl-subnet-private2-eu-west-1b"]
  description = "Name of the Subnets used by CodeBuild"
}

variable "vpc_security_group_names" {
  type        = list(string)
  default     = ["cudl-vpc-security-group"]
  description = "Name of the Security Groups used by CodeBuild"
}

variable "ecr_repository_force_delete" {
  type        = bool
  default     = true
  description = "Whether to force delete the ECR repositories on Terraform destroy"
}

variable "ssm_parameter_default_value" {
  type        = string
  default     = "%"
  description = "Default value for Parameter Store parameters (cannot be empty string)"
}

variable "tag_environment" {
  type        = string
  description = "The environment you're working with. Live | Staging | Development | All"
  default     = "sandbox"
}

variable "tag_project" {
  type        = string
  description = "Project or Service name, e.g. DPS, CUDL, Darwin"
  default     = "CUDL"
}

variable "tag_component" {
  type        = string
  description = "e.g. Deposit Service | All"
  default     = "cudl-viewer"
}

variable "tag_subcomponent" {
  type        = string
  description = "If applicable: any value, e.g. Fedora"
  default     = "viewer"
}

variable "tag_owner" {
  type        = string
  description = "Optional Owner tag. Your CRSid, e.g. jag245"
}
