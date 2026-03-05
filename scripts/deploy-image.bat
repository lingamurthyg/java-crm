@echo off
setlocal enabledelayedexpansion

echo =====================================
echo AWS EKS Deployment Script
echo =====================================
echo.

REM Prompt for AWS region and cluster name
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
if "!AWS_REGION!"==" " (
    echo Error: AWS Region is required
    exit /b 1
)

set /p CLUSTER_NAME="Enter EKS Cluster Name: "
if "!CLUSTER_NAME!"=="" (
    echo Error: EKS Cluster Name is required
    exit /b 1
)

set /p IMAGE_URI="Enter Docker Image URI (full path with tag): "
if "!IMAGE_URI!"=="" (
    echo Error: Docker Image URI is required
    exit /b 1
)

REM Prompt for database configuration
echo.
echo === Database Configuration ===
set /p DB_HOST="Enter Database Host [3.227.166.251]: "
if "!DB_HOST!"=="" set DB_HOST=3.227.166.251

set /p DB_NAME="Enter Database Name [U07k1T]: "
if "!DB_NAME!"=="" set DB_NAME=U07k1T

set /p DB_USER="Enter Database User [U07k1T]: "
if "!DB_USER!"=="" set DB_USER=U07k1T

set /p DB_PASSWORD="Enter Database Password [53689053296]: "
if "!DB_PASSWORD!"=="" set DB_PASSWORD=53689053296

echo.
echo === Configuring kubectl for EKS ===
aws eks update-kubeconfig --region !AWS_REGION! --name !CLUSTER_NAME!
if !ERRORLEVEL! neq 0 (
    echo Error: Failed to configure kubectl
    exit /b 1
)

echo.
echo === Verifying Cluster Connectivity ===
kubectl cluster-info
if !ERRORLEVEL! neq 0 (
    echo Error: Failed to connect to cluster
    exit /b 1
)

echo.
echo === Updating Kubernetes Manifests ===
REM Update deployment.yaml with image URI and environment variables
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content kubernetes\deployment.yaml"
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{DB_HOST}}', '!DB_HOST!' | Set-Content kubernetes\deployment.yaml"
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{DB_NAME}}', '!DB_NAME!' | Set-Content kubernetes\deployment.yaml"
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{DB_USER}}', '!DB_USER!' | Set-Content kubernetes\deployment.yaml"
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{DB_PASSWORD}}', '!DB_PASSWORD!' | Set-Content kubernetes\deployment.yaml"

echo.
echo === Applying Kubernetes Manifests ===
echo Creating namespace...
kubectl apply -f kubernetes\namespace.yaml

echo Creating deployment...
kubectl apply -f kubernetes\deployment.yaml

echo Creating service...
kubectl apply -f kubernetes\service.yaml

echo Creating ingress...
kubectl apply -f kubernetes\ingress.yaml

echo.
echo === Waiting for Deployment Rollout ===
kubectl rollout status deployment/java-crm -n java-crm --timeout=300s

echo.
echo === Verifying Deployment ===
kubectl get pods,svc,ingress -n java-crm

echo.
echo === Retrieving Application URL ===
for /f "delims=" %%i in ('kubectl get ingress java-crm-ingress -n java-crm -o jsonpath="{.status.loadBalancer.ingress[0].hostname}" 2^>nul') do set INGRESS_URL=%%i
if "!INGRESS_URL!"=="" set INGRESS_URL=Pending...

echo.
echo =====================================
echo Deployment completed successfully!
echo =====================================
echo Namespace: java-crm
echo Application URL: http://!INGRESS_URL!
echo.
echo To check pod status: kubectl get pods -n java-crm
echo To view logs: kubectl logs -f deployment/java-crm -n java-crm
echo To rollback: kubectl rollout undo deployment/java-crm -n java-crm
echo =====================================

endlocal