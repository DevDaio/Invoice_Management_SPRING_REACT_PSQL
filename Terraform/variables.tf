variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "frontend_bucket_name" {
  description = "S3 bucket name for frontend static hosting"
  type        = string
  default     = "invoice-mgmt-frontend-timo"
}

variable "ssh_allowed_cidr" {
  description = "CIDR block allowed to SSH into EC2"
  type        = string
  default     = "0.0.0.0/0"
}


