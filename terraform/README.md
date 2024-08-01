## Requirements

| Name | Version |
|------|---------|
| <a name="requirement_terraform"></a> [terraform](#requirement\_terraform) | ~> 1.7.5 |
| <a name="requirement_aws"></a> [aws](#requirement\_aws) | ~> 5.60.0 |

## Providers

| Name | Version |
|------|---------|
| <a name="provider_aws"></a> [aws](#provider\_aws) | 5.60.0 |

## Modules

No modules.

## Resources

| Name | Type |
|------|------|
| [aws_codebuild_project.maven](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/codebuild_project) | resource |
| [aws_ecr_repository.cudl_viewer](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/ecr_repository) | resource |
| [aws_ecr_repository.cudl_viewer_db](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/ecr_repository) | resource |
| [aws_iam_role.code_build](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/iam_role) | resource |
| [aws_iam_role_policy.code_build](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/iam_role_policy) | resource |
| [aws_ssm_parameter.github_token](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/ssm_parameter) | resource |
| [aws_ssm_parameter.github_user](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/ssm_parameter) | resource |
| [aws_caller_identity.current](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/caller_identity) | data source |
| [aws_iam_policy_document.assume_role](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/iam_policy_document) | data source |
| [aws_iam_policy_document.code_build](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/iam_policy_document) | data source |
| [aws_security_groups.code_build](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/security_groups) | data source |
| [aws_subnets.code_build](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/subnets) | data source |
| [aws_vpc.code_build](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/vpc) | data source |

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| <a name="input_aws_region"></a> [aws\_region](#input\_aws\_region) | AWS Region where all resources are deployed | `string` | `"eu-west-1"` | no |
| <a name="input_cloudwatch_log_group_name"></a> [cloudwatch\_log\_group\_name](#input\_cloudwatch\_log\_group\_name) | Name of the CloudWatch Log Group for build logs | `string` | `"/ecs/CUDLContent"` | no |
| <a name="input_cloudwatch_log_stream_name"></a> [cloudwatch\_log\_stream\_name](#input\_cloudwatch\_log\_stream\_name) | Name of the CloudWatch Log Stream for build logs | `string` | `"codebuild"` | no |
| <a name="input_codebuild_compute_type"></a> [codebuild\_compute\_type](#input\_codebuild\_compute\_type) | Information about the compute resources the build project will use | `string` | `"BUILD_GENERAL1_SMALL"` | no |
| <a name="input_codebuild_environment_type"></a> [codebuild\_environment\_type](#input\_codebuild\_environment\_type) | Type of build environment to use for related builds | `string` | `"LINUX_CONTAINER"` | no |
| <a name="input_codebuild_git_submodule_data_samples"></a> [codebuild\_git\_submodule\_data\_samples](#input\_codebuild\_git\_submodule\_data\_samples) | Path to the data-samples git submodule used in DB Docker build | `string` | `"docker/db/dl-data-samples-http"` | no |
| <a name="input_codebuild_github_token"></a> [codebuild\_github\_token](#input\_codebuild\_github\_token) | n/a | `string` | `null` | no |
| <a name="input_codebuild_github_user"></a> [codebuild\_github\_user](#input\_codebuild\_github\_user) | n/a | `string` | `null` | no |
| <a name="input_codebuild_image"></a> [codebuild\_image](#input\_codebuild\_image) | Docker image to use for this build project | `string` | `"aws/codebuild/amazonlinux2-x86_64-standard:4.0"` | no |
| <a name="input_codebuild_image_pull_credentials_type"></a> [codebuild\_image\_pull\_credentials\_type](#input\_codebuild\_image\_pull\_credentials\_type) | Type of credentials AWS CodeBuild uses to pull images in your build | `string` | `"CODEBUILD"` | no |
| <a name="input_codebuild_privileged_mode"></a> [codebuild\_privileged\_mode](#input\_codebuild\_privileged\_mode) | Whether to run CodeBuild in privileged mode. This must be set to true to run docker-in-docker | `bool` | `true` | no |
| <a name="input_codebuild_project_name"></a> [codebuild\_project\_name](#input\_codebuild\_project\_name) | Name of the CodeBuild Build Project | `string` | `"cudl-viewer"` | no |
| <a name="input_ecr_repository_force_delete"></a> [ecr\_repository\_force\_delete](#input\_ecr\_repository\_force\_delete) | Whether to force delete the ECR repositories on Terraform destroy | `bool` | `true` | no |
| <a name="input_iam_role_name"></a> [iam\_role\_name](#input\_iam\_role\_name) | Name of the IAM role used by the CodeBuild build project | `string` | `"codebuild-cudl-viewer"` | no |
| <a name="input_s3_bucket_name"></a> [s3\_bucket\_name](#input\_s3\_bucket\_name) | Name of the S3 Bucket used for artifacts | `string` | `"rmm98-sandbox-cudl-ecs"` | no |
| <a name="input_ssm_parameter_default_value"></a> [ssm\_parameter\_default\_value](#input\_ssm\_parameter\_default\_value) | Default value for Parameter Store parameters (cannot be empty string) | `string` | `"%"` | no |
| <a name="input_tag_component"></a> [tag\_component](#input\_tag\_component) | e.g. Deposit Service \| All | `string` | `"cudl-viewer"` | no |
| <a name="input_tag_environment"></a> [tag\_environment](#input\_tag\_environment) | The environment you're working with. Live \| Staging \| Development \| All | `string` | `"sandbox"` | no |
| <a name="input_tag_owner"></a> [tag\_owner](#input\_tag\_owner) | Optional Owner tag. Your CRSid, e.g. jag245 | `string` | n/a | yes |
| <a name="input_tag_project"></a> [tag\_project](#input\_tag\_project) | Project or Service name, e.g. DPS, CUDL, Darwin | `string` | `"CUDL"` | no |
| <a name="input_tag_subcomponent"></a> [tag\_subcomponent](#input\_tag\_subcomponent) | If applicable: any value, e.g. Fedora | `string` | `"viewer"` | no |
| <a name="input_vpc_name"></a> [vpc\_name](#input\_vpc\_name) | Name of the VPC used by CodeBuild | `string` | `"cudl-vpc"` | no |
| <a name="input_vpc_security_group_names"></a> [vpc\_security\_group\_names](#input\_vpc\_security\_group\_names) | Name of the Security Groups used by CodeBuild | `list(string)` | <pre>[<br>  "cudl-vpc-security-group"<br>]</pre> | no |
| <a name="input_vpc_subnet_names"></a> [vpc\_subnet\_names](#input\_vpc\_subnet\_names) | Name of the Subnets used by CodeBuild | `list(string)` | <pre>[<br>  "cudl-subnet-private1-eu-west-1a",<br>  "cudl-subnet-private2-eu-west-1b"<br>]</pre> | no |

## Outputs

No outputs.
