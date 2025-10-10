locals {
  user_data = <<-EOF
    #!/bin/bash
    set -eux
    apt-get update -y
    apt-get install -y wget unzip
    # Corretto 17 설치
    wget -O- https://apt.corretto.aws/corretto.key | gpg --dearmor | tee /usr/share/keyrings/corretto-archive-keyring.gpg
    echo "deb [signed-by=/usr/share/keyrings/corretto-archive-keyring.gpg] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list
    apt-get update -y
    apt-get install -y java-17-amazon-corretto-jdk
  EOF
}

# EIP 추가
resource "aws_eip" "app" {
  domain = "vpc"
  tags = {
    Name = "${var.project_name}-eip"
  }
}

resource "aws_eip_association" "app_eip_assoc" {
  allocation_id = aws_eip.app.id
  instance_id   = aws_instance.app.id
}

resource "aws_instance" "app" {
  ami                         = var.ami_id
  instance_type               = var.instance_type
  subnet_id                   = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.ec2_sg.id]
  key_name                    = var.key_name
  associate_public_ip_address = true
  user_data                   = local.user_data
  iam_instance_profile        = "readycode-ec2-role"

  tags = { Name = "${var.project_name}-ec2" }
}