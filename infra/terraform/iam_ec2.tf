data "aws_iam_policy_document" "ec2_assume_role" {
  statement {
    effect = "Allow"
    principals {
      type = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
    actions = ["sts:AssumeRole"]
  }
}

resource "aws_iam_role" "ec2" {
  name               = "${var.project_name}-ec2-role"
  assume_role_policy = data.aws_iam_policy_document.ec2_assume_role.json
}

resource "aws_iam_role_policy_attachment" "ec2_ssm_core" {
  role       = aws_iam_role.ec2.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

data "aws_iam_policy_document" "ec2_s3_read" {
  statement {
    effect = "Allow"
    actions = [
      "s3:GetObject"
    ]
    resources = [
      "arn:aws:s3:::${var.project_name}-tfstate-bucket/${var.artifact_prefix}*"
    ]
  }

  statement {
    effect = "Allow"
    actions = ["s3:ListBucket"]
    resources = ["arn:aws:s3:::${var.project_name}-tfstate-bucket"]

    condition {
      test     = "StringLike"
      variable = "s3:prefix"
      values = ["${var.artifact_prefix}*"]
    }
  }
}

resource "aws_iam_policy" "ec2_s3_read" {
  name   = "${var.project_name}-ec2-s3-read"
  policy = data.aws_iam_policy_document.ec2_s3_read.json
}

resource "aws_iam_role_policy_attachment" "ec2_s3_read" {
  role       = aws_iam_role.ec2.name
  policy_arn = aws_iam_policy.ec2_s3_read.arn
}

resource "aws_iam_instance_profile" "ec2" {
  name = "${var.project_name}-ec2-profile"
  role = aws_iam_role.ec2.name
}

variable "artifact_prefix" {
  type        = string
  default     = "artifacts/"
  description = "버킷 내 prefix(폴더) 경로"
}

data "aws_caller_identity" "current" {}
data "aws_region" "current" {}

data "aws_iam_policy_document" "ec2_ssm_param_read" {
  statement {
    effect = "Allow"
    actions = [
      "ssm:GetParameter",
      "ssm:GetParameters",
      "ssm:GetParametersByPath"
    ]
    resources = [
      "arn:aws:ssm:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:parameter/readycode/prod",
      "arn:aws:ssm:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:parameter/readycode/prod/*"
    ]
  }

  # SecureString 복호화용 (SSM 경유로만 허용)
  statement {
    effect = "Allow"
    actions = ["kms:Decrypt"]
    resources = ["*"]

    condition {
      test     = "StringEquals"
      variable = "kms:ViaService"
      values = ["ssm.${data.aws_region.current.name}.amazonaws.com"]
    }
  }
}

resource "aws_iam_policy" "ec2_ssm_param_read" {
  name   = "${var.project_name}-ec2-ssm-param-read"
  policy = data.aws_iam_policy_document.ec2_ssm_param_read.json
}

resource "aws_iam_role_policy_attachment" "ec2_ssm_param_read" {
  role       = aws_iam_role.ec2.name
  policy_arn = aws_iam_policy.ec2_ssm_param_read.arn
}