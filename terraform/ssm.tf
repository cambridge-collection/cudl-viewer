resource "aws_ssm_parameter" "github_user" {
  name  = "/Environments/${var.tag_environment}/GITHUB_USER"
  type  = "SecureString"
  value = local.ssm_github_user

  lifecycle {
    ignore_changes = [value]
  }
}

resource "aws_ssm_parameter" "github_token" {
  name  = "/Environments/${var.tag_environment}/GITHUB_TOKEN"
  type  = "SecureString"
  value = local.ssm_github_token

  lifecycle {
    ignore_changes = [value]
  }
}
