variable "user_data_version" {
  description = "bump this to force instance replace and re-run user_data"
  type        = string
  default     = "v5"
}

locals {
  user_data = templatefile("${path.module}/scripts/user_data.sh.tftpl", {
    project_name      = var.project_name
    aws_region        = var.aws_region
    ssm_prefix        = "/readycode/prod"
    artifact_bucket   = "${var.project_name}-tfstate-bucket"
    artifact_prefix   = var.artifact_prefix
    user_data_version = var.user_data_version

    APP_DIR   = "/opt/${var.project_name}"
    APP_USER  = "ubuntu"
    APP_GROUP = "ubuntu"
  })
}

resource "aws_eip" "app" {
  domain = "vpc"
  tags = {
    Name = "${var.project_name}-eip"
  }
}

resource "aws_instance" "app" {
  ami           = data.aws_ami.ubuntu_2204.id
  instance_type = var.instance_type
  subnet_id     = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.ec2_sg.id]

  user_data                   = local.user_data
  user_data_replace_on_change = true

  iam_instance_profile = aws_iam_instance_profile.ec2.name

  tags = { Name = "${var.project_name}-ec2" }
}

resource "aws_eip_association" "app_eip_assoc" {
  allocation_id = aws_eip.app.id
  instance_id   = aws_instance.app.id
}

data "aws_ami" "ubuntu_2204" {
  most_recent = true
  owners = ["099720109477"] # Canonical

  filter {
    name = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name = "virtualization-type"
    values = ["hvm"]
  }
}