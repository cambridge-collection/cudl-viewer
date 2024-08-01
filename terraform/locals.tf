locals {
  environment = strcontains(lower(var.tag_environment), "sandbox") ? join("-", [var.tag_owner, var.tag_environment]) : var.tag_environment
  default_tags = {
    Environment  = title(var.tag_environment)
    Project      = var.tag_project
    Component    = var.tag_component
    Subcomponent = var.tag_subcomponent
    Deployment   = title(local.environment)
    Source       = "https://github.com/cambridge-collection/cudl-viewer"
    Owner        = var.tag_owner
    terraform    = true
  }
  s3_bucket_arns   = [format("arn:aws:s3:::%s", var.s3_bucket_name), format("arn:aws:s3:::%s/*", var.s3_bucket_name)]
  ssm_github_user  = coalesce(var.codebuild_github_user, var.tag_owner)
  ssm_github_token = coalesce(var.codebuild_github_token, var.ssm_parameter_default_value)
}
