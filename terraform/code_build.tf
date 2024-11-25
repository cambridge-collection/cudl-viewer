resource "aws_codebuild_project" "maven" {
  name                   = join("-", [var.tag_environment, var.codebuild_project_name])
  build_timeout          = 5
  service_role           = aws_iam_role.code_build.arn
  concurrent_build_limit = 1

  artifacts {
    type           = "S3"
    location       = var.s3_bucket_name
    namespace_type = "NONE"
    packaging      = "NONE"
    path           = "codebuild/cudl-viewer"
  }

  cache {
    modes = []
    type  = "NO_CACHE"
  }

  environment {
    compute_type                = var.codebuild_compute_type
    image                       = var.codebuild_image
    type                        = var.codebuild_environment_type
    image_pull_credentials_type = var.codebuild_image_pull_credentials_type
    privileged_mode             = var.codebuild_privileged_mode

    environment_variable {
      name  = "GITHUB_USER"
      value = aws_ssm_parameter.github_user.name
      type  = "PARAMETER_STORE"
    }

    environment_variable {
      name  = "GITHUB_TOKEN"
      value = aws_ssm_parameter.github_token.name
      type  = "PARAMETER_STORE"
    }

    environment_variable {
      name  = "AWS_ACCOUNT_ID"
      value = data.aws_caller_identity.current.account_id
    }

    environment_variable {
      name  = "REPOSITORY_URI_VIEWER"
      value = aws_ecr_repository.cudl_viewer.repository_url
    }

    environment_variable {
      name  = "GIT_SUBMODULE_DATA_SAMPLES"
      value = var.codebuild_git_submodule_data_samples
    }

    environment_variable {
      name  = "GIT_SUBMODULE_CUDL_VIEWER_UI"
      value = var.codebuild_git_submodule_cudl_viewer_ui
    }
  }

  logs_config {
    cloudwatch_logs {
      group_name  = var.cloudwatch_log_group_name
      stream_name = var.cloudwatch_log_stream_name
      status      = "ENABLED"
    }

    s3_logs {
      encryption_disabled = false
      status              = "DISABLED"
    }
  }

  source {
    type                = "GITHUB"
    location            = "https://github.com/cambridge-collection/cudl-viewer.git"
    git_clone_depth     = 1
    insecure_ssl        = false
    report_build_status = false
  }

  source_version = var.codebuild_git_source_version

  # vpc_config {
  #   vpc_id = data.aws_vpc.code_build.id

  #   subnets = data.aws_subnets.code_build.ids

  #   security_group_ids = data.aws_security_groups.code_build.ids
  # }
}
