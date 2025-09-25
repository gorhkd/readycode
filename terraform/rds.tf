resource "aws_db_subnet_group" "this" {
  name = "${var.project_name}-db-subnet-group"
  subnet_ids = [
    aws_subnet.private.id,
    aws_subnet.private_b.id
  ]
  # 단일 AZ로 최소 구성
  tags = { Name = "${var.project_name}-db-subnet-group" }
}

resource "aws_db_instance" "mysql" {
  identifier           = "${var.project_name}-mysql"
  engine               = "mysql"
  engine_version       = "8.0"
  instance_class       = "db.t3.micro"
  allocated_storage    = 20
  db_name              = var.db_name
  username             = var.db_username
  password             = var.db_password
  db_subnet_group_name = aws_db_subnet_group.this.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  publicly_accessible  = false
  multi_az             = false
  skip_final_snapshot  = true
  deletion_protection  = false
  storage_encrypted    = true

  tags = { Name = "${var.project_name}-rds" }
}