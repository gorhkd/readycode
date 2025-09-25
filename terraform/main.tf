terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  # 첫 배포는 로컬 상태파일로 시작(간단). 후에 S3 백엔드로 전환.
  # backend "s3" { ... }  // Day 3~4에 적용 예정
}

provider "aws" {
  region  = var.aws_region
  profile = var.aws_profile
  # 로컬에서 실행: AWS_PROFILE 또는 기본 자격증명 사용
  # GitHub Actions(OIDC)는 별도 워크플로우에서 적용 (이미 확인 완료)
}