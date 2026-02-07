variable "project_name" {
  description = "태그/리소스 이름에 붙일 프로젝트 식별자"
  type        = string
  default     = "readycode"
}

variable "instance_type" {
  description = "EC2 인스턴스 타입"
  type        = string
  default     = "t3.small"
}

variable "aws_region" {
  type    = string
  default = "ap-northeast-2"
}

variable "availability_zones" {
  description = "서브넷을 만들 AZ 목록"
  type = list(string)
  default = ["ap-northeast-2a", "ap-northeast-2b"]
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"

  validation {
    condition = can(cidrhost(var.vpc_cidr, 0))
    error_message = "vpc_cidr은 CIDR 형식이어야 함. 예: 10.0.0.0/16"
  }
}

variable "public_subnet_cidr" {
  type    = string
  default = "10.0.1.0/24"
}

variable "private_subnet_cidr_a" {
  type    = string
  default = "10.0.2.0/24"
}

variable "private_subnet_cidr_b" {
  type    = string
  default = "10.0.3.0/24"
}

variable "allow_ssh_cidrs" {
  type = list(string)
  description = "SSH(22) 허용 CIDR 목록 (예: [\"x.x.x.x/32\"])"

  validation {
    condition = alltrue([for c in var.allow_ssh_cidrs : can(cidrhost(c, 0))])
    error_message = "allow_ssh_cidrs는 CIDR 목록이어야 함. 예: [\"1.2.3.4/32\"]"
  }
}

variable "allow_http8080_cidr" {
  type = string
}

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

variable "ami_id" {
  type = string
}