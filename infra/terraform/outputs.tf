output "account_id" {
  value = data.aws_caller_identity.me.account_id
}

output "arn" {
  value = data.aws_caller_identity.me.arn
}

output "ec2_public_ip" {
  value = aws_instance.app.public_ip
}

output "ec2_public_dns" {
  value = aws_instance.app.public_dns
}

output "rds_endpoint" {
  value = aws_db_instance.mysql.address
}

output "rds_port" {
  value = aws_db_instance.mysql.port
}