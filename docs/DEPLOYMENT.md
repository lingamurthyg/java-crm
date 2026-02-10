=== docs/DEPLOYMENT.md ===
# Java CRM Application - AWS ECS Fargate Deployment Guide

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Local Development Setup](#local-development-setup)
4. [AWS ECS Fargate Prerequisites](#aws-ecs-fargate-prerequisites)
5. [Building and Pushing Docker Images](#building-and-pushing-docker-images)
6. [ECS Fargate Deployment](#ecs-fargate-deployment)
7. [Configuration Management](#configuration-management)
8. [Monitoring and Logging](#monitoring-and-logging)
9. [Troubleshooting](#troubleshooting)
10. [Security Considerations](#security-considerations)

## Overview

This guide provides comprehensive instructions for containerizing and deploying the Java CRM application to AWS ECS Fargate. The application is a JavaFX-based customer relationship management system built with Java 8, featuring MySQL database connectivity and comprehensive appointment management capabilities.

### Technology Stack
- **Language**: Java 8
- **Framework**: JavaFX
- **Build Tool**: Apache Ant
- **Database**: MySQL 5.x
- **Target Platform**: AWS ECS Fargate
- **Container Runtime**: Docker

## Prerequisites

### Required Software
1. **Docker Desktop** (version 20.10 or higher)
   - Download: https://www.docker.com/products/docker-desktop
   - Verify installation: `docker --version`

2. **AWS CLI** (version 2.x)
   - Download: https://aws.amazon.com/cli/
   - Verify installation: `aws --version`
   - Configure credentials: `aws configure`

3. **Git** (for version control)
   - Download: https://git-scm.com/downloads

### AWS Account Requirements
- Active AWS account with appropriate permissions
- IAM user with programmatic access
- Required IAM permissions:
  - ECS full access
  - ECR full access
  - CloudWatch Logs write access
  - VPC and networking permissions
  - IAM role creation (for task execution)

## Local Development Setup

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd "Backend Services"
```

### Step 2: Build with Docker Compose
```bash
# Build the Docker image
docker-compose build

# Run locally
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Step 3: Test Locally
```bash
# Check container status
docker ps

# Access application logs
docker logs java-crm-app

# Test database connectivity
docker exec -it java-crm-app sh
```

## AWS ECS Fargate Prerequisites

### 1. VPC and Network Configuration

Create or identify an existing VPC with the following components:

```bash
# Create VPC (if needed)
aws ec2 create-vpc --cidr-block 10.0.0.0/16 --region us-east-1

# Create subnets in different availability zones
aws ec2 create-subnet --vpc-id vpc-xxxxx --cidr-block 10.0.1.0/24 --availability-zone us-east-1a
aws ec2 create-subnet --vpc-id vpc-xxxxx --cidr-block 10.0.2.0/24 --availability-zone us-east-1b

# Create Internet Gateway
aws ec2 create-internet-gateway
aws ec2 attach-internet-gateway --vpc-id vpc-xxxxx --internet-gateway-id igw-xxxxx
```

### 2. Security Group Configuration

Create a security group that allows:
- Inbound: Port 8080 (application)
- Inbound: Port 3306 (MySQL) from application security group
- Outbound: All traffic (for downloading dependencies and database access)

```bash
# Create security group
aws ec2 create-security-group \
  --group-name java-crm-sg \
  --description "Security group for Java CRM application" \
  --vpc-id vpc-xxxxx

# Add inbound rules
aws ec2 authorize-security-group-ingress \
  --group-id sg-xxxxx \
  --protocol tcp \
  --port 8080 \
  --cidr 0.0.0.0/0
```

### 3. IAM Roles

#### ECS Task Execution Role
This role allows ECS to pull images from ECR and write logs to CloudWatch.

```bash
# Create trust policy file
cat > ecs-trust-policy.json << EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF

# Create role
aws iam create-role \
  --role-name ecsTaskExecutionRole \
  --assume-role-policy-document file://ecs-trust-policy.json

# Attach AWS managed policy
aws iam attach-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
```

#### ECS Task Role (Optional)
For applications that need to access AWS services.

```bash
# Create task role
aws iam create-role \
  --role-name ecsTaskRole \
  --assume-role-policy-document file://ecs-trust-policy.json

# Attach custom policies as needed
```

### 4. CloudWatch Log Group

```bash
# Create log group
aws logs create-log-group --log-group-name /ecs/java-crm --region us-east-1

# Set retention policy (optional)
aws logs put-retention-policy \
  --log-group-name /ecs/java-crm \
  --retention-in-days 7
```

### 5. Database Setup

**Option A: AWS RDS MySQL**
```bash
# Create RDS MySQL instance
aws rds create-db-instance \
  --db-instance-identifier java-crm-db \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --master-username admin \
  --master-user-password <password> \
  --allocated-storage 20 \
  --vpc-security-group-ids sg-xxxxx \
  --db-subnet-group-name default
```

**Option B: External MySQL**
- Ensure network connectivity from ECS tasks
- Update security groups to allow access
- Note connection details for deployment

## Building and Pushing Docker Images

### Using Linux/macOS (build-push.sh)

```bash
# Make script executable
chmod +x scripts/build-push.sh

# Run the script
./scripts/build-push.sh
```

**Script will prompt for:**
1. Registry selection (AWS ECR or Docker Hub)
2. Registry credentials and configuration
3. Image tag (default: latest)

**For AWS ECR:**
- AWS Region
- AWS Account ID
- ECR Repository Name

**For Docker Hub:**
- Username
- Password/Token

### Using Windows (build-push.bat)

```cmd
REM Run from Command Prompt
scripts\build-push.bat
```

### Manual Build Process

```bash
# Build image
docker build -t java-crm:latest .

# Tag for ECR
docker tag java-crm:latest 123456789.dkr.ecr.us-east-1.amazonaws.com/java-crm:latest

# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789.dkr.ecr.us-east-1.amazonaws.com

# Push to ECR
docker push 123456789.dkr.ecr.us-east-1.amazonaws.com/java-crm:latest
```

## ECS Fargate Deployment

### Understanding ECS Fargate

AWS Fargate is a serverless compute engine for containers that:
- Eliminates the need to manage EC2 instances
- Automatically scales based on demand
- Uses `awsvpc` network mode (each task gets its own ENI)
- Requires specific CPU/memory combinations

### Valid Fargate CPU/Memory Combinations

| CPU (vCPU) | Memory (MB) |
|------------|-------------|
| 256 (.25)  | 512, 1024, 2048 |
| 512 (.5)   | 1024, 2048, 3072, 4096 |
| 1024 (1)   | 2048-8192 (increments of 1024) |
| 2048 (2)   | 4096-16384 (increments of 1024) |
| 4096 (4)   | 8192-30720 (increments of 1024) |

**Default Configuration**: CPU: 512, Memory: 1024

### ECS Task Definition Explained

The task definition (`ecs/task-definition.json`) defines:

1. **Launch Type Configuration**
   - `requiresCompatibilities`: ["FARGATE"]
   - `networkMode`: "awsvpc" (required for Fargate)

2. **Resource Allocation**
   - `cpu`: "512" (0.5 vCPU)
   - `memory`: "1024" (1 GB RAM)

3. **Container Definition**
   - Image URI (from ECR/Docker Hub)
   - Port mappings (containerPort only)
   - Environment variables
   - Health check configuration

4. **Logging Configuration**
   - CloudWatch Logs integration
   - Log group: `/ecs/java-crm`
   - Log stream prefix: `ecs`

5. **IAM Roles**
   - `executionRoleArn`: For ECS agent operations
   - `taskRoleArn`: For application AWS API calls

### ECS Service Configuration

The service definition (`ecs/service-definition.json`) configures:

1. **Service Settings**
   - `desiredCount`: 2 (number of tasks)
   - `launchType`: "FARGATE"

2. **Network Configuration**
   - `awsvpcConfiguration`:
     - Subnets (at least 2 for HA)
     - Security groups
     - Public IP assignment

3. **Deployment Configuration**
   - `maximumPercent`: 200 (rolling update)
   - `minimumHealthyPercent`: 50

4. **Load Balancer Integration** (optional)
   - Target group ARN
   - Container name and port
   - Health check grace period

5. **Tags and Metadata**
   - CRITICAL: Use `tags` parameter (NOT `serviceTags`)
   - `enableECSManagedTags`: true
   - `propagateTags`: "SERVICE"

### Deployment Walkthrough

#### Step 1: Prepare Configuration Files

Ensure you have:
- `ecs/task-definition.json`
- `ecs/service-definition.json`
- Built and pushed Docker image URI

#### Step 2: Run Deployment Script (Linux/macOS)

```bash
# Make script executable
chmod +x scripts/deploy-image.sh

# Run deployment
./scripts/deploy-image.sh
```

**The script will prompt for:**
1. AWS Region (e.g., us-east-1)
2. ECS Cluster Name (creates if doesn't exist)
3. Docker Image URI
4. VPC ID
5. Subnet IDs (comma-separated)
6. Security Group ID
7. Database connection details
8. Load balancer requirement (y/n)

#### Step 3: Run Deployment Script (Windows)

```cmd
scripts\deploy-image.bat
```

#### Step 4: Monitor Deployment

```bash
# Watch service status
aws ecs describe-services \
  --cluster java-crm-cluster \
  --services java-crm-service \
  --region us-east-1

# View task details
aws ecs list-tasks \
  --cluster java-crm-cluster \
  --service-name java-crm-service \
  --region us-east-1

# Check task status
aws ecs describe-tasks \
  --cluster java-crm-cluster \
  --tasks <task-arn> \
  --region us-east-1
```

### Load Balancer Configuration

If you chose to create a load balancer:

1. **Application Load Balancer (ALB)**
   - Created automatically by deployment script
   - Listens on port 80 (HTTP)
   - Forwards to target group on port 8080

2. **Target Group**
   - Target type: `ip` (required for Fargate)
   - Health check path: `/health`
   - Protocol: HTTP
   - Port: 8080

3. **Access Application**
   - URL: `http://<alb-dns-name>`
   - Find DNS: AWS Console > EC2 > Load Balancers

## Configuration Management

### Environment Variables

The application uses the following environment variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `JAVA_OPTS` | JVM options | `-Xmx512m -Xms256m` | No |
| `DB_HOST` | Database host | localhost | Yes |
| `DB_PORT` | Database port | 3306 | No |
| `DB_NAME` | Database name | crm_db | Yes |
| `DB_USERNAME` | Database user | crm_user | Yes |
| `DB_PASSWORD` | Database password | - | Yes |
| `VIEW_PATH` | View resources path | /View/ | No |

### Updating Configuration

**Method 1: Update Task Definition**
```bash
# Edit ecs/task-definition.json
# Update environment variables
# Re-run deploy-image.sh
```

**Method 2: AWS Systems Manager Parameter Store**
```bash
# Store sensitive values
aws ssm put-parameter \
  --name /java-crm/db-password \
  --value "<password>" \
  --type SecureString \
  --region us-east-1

# Reference in task definition
# "secrets": [
#   {
#     "name": "DB_PASSWORD",
#     "valueFrom": "/java-crm/db-password"
#   }
# ]
```

**Method 3: AWS Secrets Manager**
```bash
# Create secret
aws secretsmanager create-secret \
  --name java-crm-db-credentials \
  --secret-string '{"username":"admin","password":"password"}' \
  --region us-east-1
```

### JVM Memory Configuration

For optimal performance in containers:

```bash
# Recommended JAVA_OPTS for 1GB memory:
-Xmx768m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0

# For 2GB memory:
-Xmx1536m -Xms512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0
```

## Monitoring and Logging

### CloudWatch Logs

**View Logs:**
```bash
# Tail logs
aws logs tail /ecs/java-crm --follow --region us-east-1

# View specific time range
aws logs filter-log-events \
  --log-group-name /ecs/java-crm \
  --start-time 1609459200000 \
  --region us-east-1
```

**Log Insights Queries:**
```sql
# Error analysis
fields @timestamp, @message
| filter @message like /ERROR/
| sort @timestamp desc
| limit 100

# Application startup time
fields @timestamp
| filter @message like /Started/
| stats count() by bin(5m)
```

### ECS Service Metrics

**CloudWatch Metrics Available:**
- CPUUtilization
- MemoryUtilization
- TargetResponseTime (with ALB)
- RequestCount (with ALB)

**Create CloudWatch Dashboard:**
```bash
aws cloudwatch put-dashboard \
  --dashboard-name java-crm-dashboard \
  --dashboard-body file://dashboard.json
```

### Application Monitoring

**Health Check Endpoint:**
- Implement `/health` endpoint in your application
- Returns 200 OK when healthy
- Checks database connectivity

**Custom Metrics:**
- Use CloudWatch SDK to publish custom metrics
- Track business metrics (appointments, customers, etc.)

## Troubleshooting

### Common Issues

#### 1. Task Fails to Start

**Symptom:** Tasks repeatedly fail with "STOPPED" status

**Diagnosis:**
```bash
# Check stopped tasks
aws ecs list-tasks \
  --cluster java-crm-cluster \
  --desired-status STOPPED \
  --region us-east-1

# Get failure reason
aws ecs describe-tasks \
  --cluster java-crm-cluster \
  --tasks <task-arn> \
  --region us-east-1 \
  --query 'tasks[0].stoppedReason'
```

**Common Causes:**
- Invalid CPU/memory combination
- Image pull failure (check ECR permissions)
- Application crash on startup
- Database connectivity issues

**Solutions:**
- Verify task definition CPU/memory
- Check executionRoleArn has ECR permissions
- Review CloudWatch logs for errors
- Test database connection

#### 2. Cannot Pull Image from ECR

**Symptom:** "CannotPullContainerError"

**Solutions:**
```bash
# Verify image exists
aws ecr describe-images \
  --repository-name java-crm \
  --region us-east-1

# Check execution role permissions
aws iam get-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-name ecsTaskExecutionRolePolicy

# Test manual pull
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com
docker pull <image-uri>
```

#### 3. Task Runs But No Network Connectivity

**Symptom:** Tasks start but cannot reach database or internet

**Diagnosis:**
- Check subnet route tables
- Verify security group rules
- Confirm public IP assignment

**Solutions:**
```bash
# Verify subnet has route to Internet Gateway
aws ec2 describe-route-tables \
  --filters "Name=association.subnet-id,Values=<subnet-id>"

# Check security group rules
aws ec2 describe-security-groups \
  --group-ids <sg-id>

# Test with awsvpc mode
aws ecs describe-tasks \
  --cluster java-crm-cluster \
  --tasks <task-arn> \
  --query 'tasks[0].attachments[0].details'
```

#### 4. Out of Memory Errors

**Symptom:** Tasks stop with "OutOfMemoryError"

**Solutions:**
- Increase task memory allocation
- Adjust JVM heap settings
- Review application memory usage

```json
// Update task definition
"memory": "2048",
"environment": [
  {
    "name": "JAVA_OPTS",
    "value": "-Xmx1536m -Xms512m"
  }
]
```

#### 5. Database Connection Failures

**Symptom:** Application logs show "Cannot connect to database"

**Checklist:**
- [ ] Database host is reachable from ECS tasks
- [ ] Security group allows port 3306 from ECS security group
- [ ] Database credentials are correct
- [ ] Database exists and user has permissions

**Test Connectivity:**
```bash
# Get task ENI
aws ecs describe-tasks \
  --cluster java-crm-cluster \
  --tasks <task-arn> \
  --query 'tasks[0].attachments[0].details[?name==`networkInterfaceId`].value' \
  --output text

# Check security group rules
aws ec2 describe-network-interfaces \
  --network-interface-ids <eni-id>
```

### Debug Mode

Enable debug logging:

```json
// In task definition
"environment": [
  {
    "name": "JAVA_OPTS",
    "value": "-Xmx768m -Xms256m -Dlogging.level.root=DEBUG"
  }
]
```

### Getting Support

1. **Check CloudWatch Logs First**
   - Most issues visible in logs
   - Look for stack traces and error messages

2. **Review ECS Events**
   ```bash
   aws ecs describe-services \
     --cluster java-crm-cluster \
     --services java-crm-service \
     --query 'services[0].events[0:10]'
   ```

3. **AWS Support**
   - Create support case in AWS Console
   - Include task definition and logs

## Security Considerations

### Container Security

1. **Non-Root User**
   - Dockerfile runs application as `appuser`
   - Reduces attack surface

2. **Minimal Base Image**
   - Uses `eclipse-temurin:8-jre-alpine`
   - Smaller attack surface
   - Faster deployments

3. **No Hardcoded Secrets**
   - All sensitive data via environment variables
   - Use AWS Secrets Manager for production

### Network Security

1. **Security Group Rules**
   - Restrict inbound to necessary ports only
   - Use security group references (not CIDR blocks)
   - Example:
     ```bash
     # Allow only from ALB security group
     aws ec2 authorize-security-group-ingress \
       --group-id <task-sg> \
       --protocol tcp \
       --port 8080 \
       --source-group <alb-sg>
     ```

2. **Private Subnets (Recommended)**
   - Place ECS tasks in private subnets
   - Use NAT Gateway for internet access
   - ALB in public subnets

3. **VPC Endpoints**
   - Create VPC endpoints for ECR, CloudWatch
   - Reduces data transfer costs
   - No internet gateway required

### IAM Best Practices

1. **Least Privilege**
   - Grant only necessary permissions
   - Separate execution role from task role

2. **Task Role for AWS API**
   - If application needs AWS API access
   - Create specific policy
   - Example:
     ```json
     {
       "Version": "2012-10-17",
       "Statement": [
         {
           "Effect": "Allow",
           "Action": [
             "s3:GetObject",
             "s3:PutObject"
           ],
           "Resource": "arn:aws:s3:::java-crm-bucket/*"
         }
       ]
     }
     ```

3. **Secrets Management**
   - **Development**: Environment variables
   - **Production**: AWS Secrets Manager
   - **Compliance**: Use AWS KMS encryption

### Application Security

1. **Database Encryption**
   - Enable encryption in transit (SSL/TLS)
   - Enable encryption at rest (RDS)

2. **Logging**
   - Log security events
   - Monitor for suspicious activity
   - Set up CloudWatch Alarms

3. **Updates**
   - Regularly update base images
   - Patch Java runtime
   - Update dependencies

### Compliance

1. **Data Protection**
   - Encrypt data in transit and at rest
   - Use VPC endpoints to keep traffic within AWS

2. **Audit Logging**
   - Enable CloudTrail for API calls
   - Enable VPC Flow Logs
   - Retain logs per compliance requirements

3. **Access Control**
   - Use IAM roles, not access keys
   - Enable MFA for console access
   - Regular access reviews

## Scaling and Management

### Service Auto Scaling

**Setup Auto Scaling:**
```bash
# Register scalable target
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --resource-id service/java-crm-cluster/java-crm-service \
  --scalable-dimension ecs:service:DesiredCount \
  --min-capacity 2 \
  --max-capacity 10

# Create scaling policy (CPU-based)
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --resource-id service/java-crm-cluster/java-crm-service \
  --scalable-dimension ecs:service:DesiredCount \
  --policy-name cpu-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration file://scaling-policy.json
```

**scaling-policy.json:**
```json
{
  "TargetValue": 70.0,
  "PredefinedMetricSpecification": {
    "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
  },
  "ScaleInCooldown": 300,
  "ScaleOutCooldown": 60
}
```

### Blue/Green Deployments

**Using CodeDeploy:**
```bash
# Update service to use CODE_DEPLOY
aws ecs update-service \
  --cluster java-crm-cluster \
  --service java-crm-service \
  --deployment-controller type=CODE_DEPLOY
```

### Rolling Updates

```bash
# Force new deployment
aws ecs update-service \
  --cluster java-crm-cluster \
  --service java-crm-service \
  --force-new-deployment

# Update with new task definition
aws ecs update-service \
  --cluster java-crm-cluster \
  --service java-crm-service \
  --task-definition java-crm-task:2
```

## Cost Optimization

1. **Right-Size Resources**
   - Monitor CPU/memory usage
   - Adjust task definition accordingly

2. **Use Fargate Spot** (for non-critical workloads)
   ```bash
   # Update service to use Spot
   aws ecs update-service \
     --cluster java-crm-cluster \
     --service java-crm-service \
     --capacity-provider-strategy \
       capacityProvider=FARGATE_SPOT,weight=1
   ```

3. **Optimize Images**
   - Use multi-stage builds
   - Minimize layers
   - Use Alpine base images

4. **VPC Endpoints**
   - Reduce data transfer costs
   - Eliminate NAT Gateway costs for AWS services

## Additional Resources

- [AWS ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [AWS Fargate Documentation](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Java in Containers](https://developers.redhat.com/blog/2017/03/14/java-inside-docker/)

## Support

For issues or questions:
1. Check troubleshooting section above
2. Review CloudWatch logs
3. Consult AWS documentation
4. Contact your DevOps team

---

**Document Version**: 1.0
**Last Updated**: 2026-02-10
**Target Platform**: AWS ECS Fargate
