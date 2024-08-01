data "aws_vpc" "code_build" {
  filter {
    name   = "tag:Name"
    values = [var.vpc_name]
  }
}

data "aws_subnets" "code_build" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.code_build.id]
  }

  filter {
    name   = "tag:Name"
    values = var.vpc_subnet_names
  }
}

data "aws_security_groups" "code_build" {
  filter {
    name   = "tag:Name"
    values = var.vpc_security_group_names
  }

  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.code_build.id]
  }
}
