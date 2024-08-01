terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.60.0"
    }
  }

  backend "s3" {
    bucket         = "sandbox-cudl-terraform-state"
    key            = "sandbox-cudl-viewer-codebuild.tfstate"
    dynamodb_table = "terraform-state-lock-cudl"
    region         = "eu-west-1"
  }

  required_version = "~> 1.7.5"
}
