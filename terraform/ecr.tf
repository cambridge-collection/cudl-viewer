resource "aws_ecr_repository" "cudl_viewer" {
  name         = join("-", [var.tag_environment, "cudl-viewer"])
  force_delete = var.ecr_repository_force_delete
}
