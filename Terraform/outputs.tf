output "ec2_public_ip" {
  description = "Public IP of the EC2 instance"
  value       = aws_instance.app_server.public_ip
}

output "frontend_bucket_url" {
  description = "S3 static website URL"
  value       = aws_s3_bucket_website_configuration.frontend.website_endpoint
}

output "frontend_cdn_url" {
  description = "CloudFront URL for the frontend (https)"
  value       = "https://${aws_cloudfront_distribution.frontend.domain_name}"
}

output "backend_cdn_url" {
  description = "CloudFront URL for the backend API (https)"
  value       = "https://${aws_cloudfront_distribution.backend.domain_name}"
}


