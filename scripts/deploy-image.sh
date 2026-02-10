=== scripts/deploy-image.sh ===
#!/bin/bash
set -e
set -o pipefail

echo "====================================="
echo "  ECS Fargate Deployment Script"
echo "====================================="
echo ""

# Prompt for AWS configuration
read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -p "Enter ECS Cluster Name: " CLUSTER_NAME
read -p "Enter Docker Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/java-crm:latest): " IMAGE_URI

echo ""
echo "=== Network Configuration ==="
read -p "Enter VPC ID: " VPC_ID
read -p "Enter Subnet IDs (comma-separated, at least 2): " SUBNET_IDS
read -p "Enter Security Group ID: " SECURITY_GROUP

# Parse subnets
IFS=',' read -ra SUBNETS <<< "$SUBNET_IDS"
SUBNET_1="${SUBNETS[0]}"
SUBNET_2="${SUBNETS[1]:-$SUBNET_1}"
SUBNET_1=$(echo $SUBNET_1 | xargs)
SUBNET_2=$(echo $SUBNET_2 | xargs)

echo ""
echo "=== Database Configuration ==="
read -p "Enter Database Host: " DB_HOST
read -p "Enter Database Name: " DB_NAME
read -p "Enter Database Username: " DB_USERNAME
read -sp "Enter Database Password: " DB_PASSWORD
echo ""

# Get AWS Account ID
echo ""
echo "Retrieving AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
echo "Account ID: $ACCOUNT_ID"

# Check and create ECS cluster if needed
echo ""
echo "Checking ECS cluster..."
aws ecs describe-clusters --clusters $CLUSTER_NAME --region $AWS_REGION >/dev/null 2>&1 || {
    echo "Cluster does not exist. Creating cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name $CLUSTER_NAME --region $AWS_REGION
    echo "Cluster created successfully"
}

# Create CloudWatch log group
echo ""
echo "Creating CloudWatch log group..."
aws logs create-log-group --log-group-name /ecs/java-crm --region $AWS_REGION 2>/dev/null || echo "Log group already exists"

# Load balancer configuration
echo ""
read -p "Do you need a load balancer for this service? (y/n): " NEED_LB

if [ "$NEED_LB" = "y" ] || [ "$NEED_LB" = "Y" ]; then
    echo ""
    echo "=== Creating Application Load Balancer ==="
    
    ALB_NAME="java-crm-alb"
    TG_NAME="java-crm-tg"
    
    # Create ALB
    echo "Creating Application Load Balancer..."
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name $ALB_NAME \
        --subnets $SUBNET_1 $SUBNET_2 \
        --security-groups $SECURITY_GROUP \
        --region $AWS_REGION \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text 2>/dev/null || aws elbv2 describe-load-balancers --names $ALB_NAME --region $AWS_REGION --query 'LoadBalancers[0].LoadBalancerArn' --output text)
    
    echo "ALB ARN: $ALB_ARN"
    
    # Get ALB DNS
    ALB_DNS=$(aws elbv2 describe-load-balancers --load-balancer-arns $ALB_ARN --region $AWS_REGION --query 'LoadBalancers[0].DNSName' --output text)
    
    # Create Target Group with target-type ip (required for Fargate)
    echo "Creating Target Group..."
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name $TG_NAME \
        --protocol HTTP \
        --port 8080 \
        --vpc-id $VPC_ID \
        --target-type ip \
        --health-check-path /health \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region $AWS_REGION \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || aws elbv2 describe-target-groups --names $TG_NAME --region $AWS_REGION --query 'TargetGroups[0].TargetGroupArn' --output text)
    
    echo "Target Group ARN: $TARGET_GROUP_ARN"
    
    # Create listener
    echo "Creating ALB listener..."
    aws elbv2 create-listener \
        --load-balancer-arn $ALB_ARN \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn=$TARGET_GROUP_ARN \
        --region $AWS_REGION >/dev/null 2>&1 || echo "Listener already exists"
    
    USE_LB="true"
else
    USE_LB="false"
    echo "Skipping load balancer configuration"
fi

# Update task definition with actual values
echo ""
echo "Updating task definition..."
cp ecs/task-definition.json ecs/task-definition-resolved.json

sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" ecs/task-definition-resolved.json
sed -i "s|{{AWS_REGION}}|$AWS_REGION|g" ecs/task-definition-resolved.json
sed -i "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" ecs/task-definition-resolved.json
sed -i "s|{{DB_HOST}}|$DB_HOST|g" ecs/task-definition-resolved.json
sed -i "s|{{DB_NAME}}|$DB_NAME|g" ecs/task-definition-resolved.json
sed -i "s|{{DB_USERNAME}}|$DB_USERNAME|g" ecs/task-definition-resolved.json
sed -i "s|{{DB_PASSWORD}}|$DB_PASSWORD|g" ecs/task-definition-resolved.json

# Register task definition
echo "Registering ECS task definition..."
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://ecs/task-definition-resolved.json \
    --region $AWS_REGION \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "Task Definition ARN: $TASK_DEF_ARN"

# Update service definition
echo ""
echo "Updating service definition..."
cp ecs/service-definition.json ecs/service-definition-resolved.json

sed -i "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" ecs/service-definition-resolved.json
sed -i "s|{{SUBNET_1}}|$SUBNET_1|g" ecs/service-definition-resolved.json
sed -i "s|{{SUBNET_2}}|$SUBNET_2|g" ecs/service-definition-resolved.json
sed -i "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" ecs/service-definition-resolved.json

if [ "$USE_LB" = "true" ]; then
    sed -i "s|{{TARGET_GROUP_ARN}}|$TARGET_GROUP_ARN|g" ecs/service-definition-resolved.json
else
    # Remove loadBalancers section if no LB needed
    cat ecs/service-definition-resolved.json | jq 'del(.loadBalancers) | del(.healthCheckGracePeriodSeconds)' > ecs/service-definition-temp.json
    mv ecs/service-definition-temp.json ecs/service-definition-resolved.json
fi

# Check if service exists
echo ""
echo "Checking if service exists..."
SERVICE_EXISTS=$(aws ecs describe-services \
    --cluster $CLUSTER_NAME \
    --services java-crm-service \
    --region $AWS_REGION \
    --query 'services[?status==`ACTIVE`].serviceName' \
    --output text)

if [ -z "$SERVICE_EXISTS" ]; then
    echo "Creating new ECS service..."
    aws ecs create-service \
        --cli-input-json file://ecs/service-definition-resolved.json \
        --region $AWS_REGION
else
    echo "Updating existing ECS service..."
    aws ecs update-service \
        --cluster $CLUSTER_NAME \
        --service java-crm-service \
        --task-definition $TASK_DEF_ARN \
        --force-new-deployment \
        --region $AWS_REGION
fi

# Wait for service to stabilize
echo ""
echo "Waiting for service to become stable (this may take several minutes)..."
aws ecs wait services-stable \
    --cluster $CLUSTER_NAME \
    --services java-crm-service \
    --region $AWS_REGION

# Verify deployment
echo ""
echo "Verifying deployment..."
RUNNING_COUNT=$(aws ecs describe-services \
    --cluster $CLUSTER_NAME \
    --services java-crm-service \
    --region $AWS_REGION \
    --query 'services[0].runningCount' \
    --output text)

echo ""
echo "====================================="
echo "  Deployment Completed Successfully!"
echo "====================================="
echo "Cluster: $CLUSTER_NAME"
echo "Service: java-crm-service"
echo "Running Tasks: $RUNNING_COUNT"
echo "Task Definition: $TASK_DEF_ARN"

if [ "$USE_LB" = "true" ]; then
    echo "Load Balancer DNS: $ALB_DNS"
    echo "Application URL: http://$ALB_DNS"
fi

echo "CloudWatch Logs: /ecs/java-crm"
echo ""
echo "To view logs:"
echo "  aws logs tail /ecs/java-crm --follow --region $AWS_REGION"
echo ""
echo "To check service status:"
echo "  aws ecs describe-services --cluster $CLUSTER_NAME --services java-crm-service --region $AWS_REGION"
echo ""

# Cleanup temporary files
rm -f ecs/task-definition-resolved.json ecs/service-definition-resolved.json
