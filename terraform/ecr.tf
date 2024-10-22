resource "aws_ecr_repository" "cudl_viewer" {
  name         = join("-", [var.tag_environment, "cudl-viewer"])
  force_delete = var.ecr_repository_force_delete
}

resource "aws_ecr_repository" "cudl_viewer_db" {
  name         = join("-", [var.tag_environment, "cudl-viewer-db"])
  force_delete = var.ecr_repository_force_delete
}
