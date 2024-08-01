provider "aws" {
  region = var.aws_region

  default_tags {
    tags = local.default_tags
  }
}
