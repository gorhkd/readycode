variable "project_name" {
  type    = string
  default = "readycode"
}

variable "aws_region" {
  type = string
}

# 서울
variable "az" {
  type    = string
  default = "ap-northeast-2a"
}

variable "az1" {
  default = "ap-northeast-2b"
}

variable "az2" {
  default = "ap-northeast-2c"
}

# VPC/Subnet
variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  type    = string
  default = "10.0.1.0/24"
}

variable "private_subnet_cidr" {
  type    = string
  default = "10.0.2.0/24"
}

# EC2
variable "instance_type" {
  type    = string
  default = "t3.micro"
}

# 프리티어
variable "key_name" {
  type = string
}

# 로컬에 있는 키페어 이름 (예: readycode-key)
variable "allow_ssh_cidr" {
  type = string
}

# 본인 공인IP대역
variable "allow_http8080_cidr" {
  type = string
}

# 초기 8080 테스트용

# RDS(MySQL)
variable "db_username" {
  type    = string
  default = "readycode"
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "db_name" {
  type    = string
  default = "readycode"
}

variable "aws_profile" {
  type    = string
  default = "readycode-dev"
}
