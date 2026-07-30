terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.0.0"
    }
  }

  required_version = ">= 1.2"

  backend "s3" {
    bucket = "invoice-mgmt-tfstate"
    key    = "terraform.tfstate"
    region = "eu-central-1"
  }
}
