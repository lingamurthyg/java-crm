=== scripts/deploy-image.bat ===
@echo off
setlocal enabledelayedexpansion

echo =====================================
echo   ECS Fargate Deployment Script
echo =====================================
echo.

set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "
set /p IMAGE_URI="Enter Docker Image URI: "

echo.
echo === Network Configuration ===
set /p VPC_ID="Enter VPC ID: "
set /p SUBNET_IDS="Enter Subnet IDs (comma-separated): "
set /p SECURITY_GROUP="Enter Security Group ID: "

for /f "tokens=1,2 delims=," %%a in ("!SUBNET_IDS!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)
set SUBNET_1=!SUBNET_1: =!
if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!
set SUBNET_2=!SUBNET_2: =!

echo.
echo === Database Configuration ===
set /p DB_HOST="Enter Database Host: "
set /p DB_NAME="Enter Database Name: "
set /p DB_USERNAME="Enter Database Username: "
set /p DB_PASSWORD="Enter Database Password: "

echo.
echo Retrieving AWS Account ID...
for /f "delims=" %%a in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%a
echo Account ID: !ACCOUNT_ID!

echo.
echo Checking ECS cluster...
aws ecs describe-clusters --clusters !CLUSTER_NAME! --region !AWS_REGION! >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Creating cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name !CLUSTER_NAME! --region !AWS_REGION!
)

echo.
echo Creating CloudWatch log group...
aws logs create-log-group --log-group-name /ecs/java-crm --region !AWS_REGION! >nul 2>&1

echo.
set /p NEED_LB="Do you need a load balancer? (y/n): "

if /i "!NEED_LB!"=="y" (
    echo.
    echo === Creating Application Load Balancer ===
    
    set ALB_NAME=java-crm-alb
    set TG_NAME=java-crm-tg
    
    echo Creating Application Load Balancer...
    for /f "delims=" %%a in ('aws elbv2 create-load-balancer --name !ALB_NAME! --subnets !SUBNET_1! !SUBNET_2! --security-groups !SECURITY_GROUP! --region !AWS_REGION! --query "LoadBalancers[0].LoadBalancerArn" --output text 2^>nul') do set ALB_ARN=%%a
    
    if "!ALB_ARN!"=="" (
        for /f "delims=" %%a in ('aws elbv2 describe-load-balancers --names !ALB_NAME! --region !AWS_REGION! --query "LoadBalancers[0].LoadBalancerArn" --output text') do set ALB_ARN=%%a
    )
    
    echo ALB ARN: !ALB_ARN!
    
    for /f "delims=" %%a in ('aws elbv2 describe-load-balancers --load-balancer-arns !ALB_ARN! --region !AWS_REGION! --query "LoadBalancers[0].DNSName" --output text') do set ALB_DNS=%%a
    
    echo Creating Target Group...
    for /f "delims=" %%a in ('aws elbv2 create-target-group --name !TG_NAME! --protocol HTTP --port 8080 --vpc-id !VPC_ID! --target-type ip --health-check-path /health --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%a
    
    if "!TARGET_GROUP_ARN!"=="" (
        for /f "delims=" %%a in ('aws elbv2 describe-target-groups --names !TG_NAME! --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%a
    )
    
    echo Target Group ARN: !TARGET_GROUP_ARN!
    
    echo Creating listener...
    aws elbv2 create-listener --load-balancer-arn !ALB_ARN! --protocol HTTP --port 80 --default-actions Type=forward,TargetGroupArn=!TARGET_GROUP_ARN! --region !AWS_REGION! >nul 2>&1
    
    set USE_LB=true
) else (
    set USE_LB=false
)

echo.
echo Updating task definition...
copy ecs\task-definition.json ecs\task-definition-resolved.json >nul

powershell -Command "(Get-Content ecs\task-definition-resolved.json) -replace '{{IMAGE_URI}}','!IMAGE_URI!' -replace '{{AWS_REGION}}','!AWS_REGION!' -replace '{{ACCOUNT_ID}}','!ACCOUNT_ID!' -replace '{{DB_HOST}}','!DB_HOST!' -replace '{{DB_NAME}}','!DB_NAME!' -replace '{{DB_USERNAME}}','!DB_USERNAME!' -replace '{{DB_PASSWORD}}','!DB_PASSWORD!' | Set-Content ecs\task-definition-resolved.json"

echo Registering task definition...
for /f "delims=" %%a in ('aws ecs register-task-definition --cli-input-json file://ecs/task-definition-resolved.json --region !AWS_REGION! --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%a

echo Task Definition ARN: !TASK_DEF_ARN!

echo.
echo Updating service definition...
copy ecs\service-definition.json ecs\service-definition-resolved.json >nul

powershell -Command "(Get-Content ecs\service-definition-resolved.json) -replace '{{CLUSTER_NAME}}','!CLUSTER_NAME!' -replace '{{SUBNET_1}}','!SUBNET_1!' -replace '{{SUBNET_2}}','!SUBNET_2!' -replace '{{SECURITY_GROUP}}','!SECURITY_GROUP!' -replace '{{TARGET_GROUP_ARN}}','!TARGET_GROUP_ARN!' | Set-Content ecs\service-definition-resolved.json"

if "!USE_LB!"=="false" (
    powershell -Command "$json = Get-Content ecs\service-definition-resolved.json | ConvertFrom-Json; $json.PSObject.Properties.Remove('loadBalancers'); $json.PSObject.Properties.Remove('healthCheckGracePeriodSeconds'); $json | ConvertTo-Json -Depth 10 | Set-Content ecs\service-definition-resolved.json"
)

echo.
echo Checking if service exists...
for /f "delims=" %%a in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services java-crm-service --region !AWS_REGION! --query "services[?status==`ACTIVE`].serviceName" --output text') do set SERVICE_EXISTS=%%a

if "!SERVICE_EXISTS!"=="" (
    echo Creating new ECS service...
    aws ecs create-service --cli-input-json file://ecs/service-definition-resolved.json --region !AWS_REGION!
) else (
    echo Updating existing ECS service...
    aws ecs update-service --cluster !CLUSTER_NAME! --service java-crm-service --task-definition !TASK_DEF_ARN! --force-new-deployment --region !AWS_REGION!
)

echo.
echo Waiting for service stability...
aws ecs wait services-stable --cluster !CLUSTER_NAME! --services java-crm-service --region !AWS_REGION!

echo.
for /f "delims=" %%a in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services java-crm-service --region !AWS_REGION! --query "services[0].runningCount" --output text') do set RUNNING_COUNT=%%a

echo =====================================
echo   Deployment Completed!
echo =====================================
echo Cluster: !CLUSTER_NAME!
echo Service: java-crm-service
echo Running Tasks: !RUNNING_COUNT!

if "!USE_LB!"=="true" (
    echo Load Balancer DNS: !ALB_DNS!
    echo Application URL: http://!ALB_DNS!
)

echo CloudWatch Logs: /ecs/java-crm
echo.

del ecs\task-definition-resolved.json ecs\service-definition-resolved.json >nul 2>&1

endlocal
