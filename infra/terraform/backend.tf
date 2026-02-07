terraform {
  backend "s3" {
    bucket       = "readycode-tfstate-bucket"
    key          = "dev/terraform.tfstate"
    region       = "ap-northeast-2"
    encrypt      = true
    use_lockfile = true
  }
}