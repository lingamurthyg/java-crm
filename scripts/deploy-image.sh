#!/bin/bash
set -e
set -o pipefail

echo "====================================="
echo "AWS EKS Deployment Script"
echo "====================================="
echo ""

# Prompt for AWS region and cluster name
read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
if [ -z "$AWS_REGION" ]; then
    echo "Error: AWS Region is required"
    exit 1
fi

read -p "Enter EKS Cluster Name: " CLUSTER_NAME
if [ -z "$CLUSTER_NAME" ]; then
    echo "Error: EKS Cluster Name is required"
    exit 1
fi

read -p "Enter Docker Image URI (full path with tag): " IMAGE_URI
if [ -z "$IMAGE_URI" ]; then
    echo "Error: Docker Image URI is required"
    exit 1
fi

# Prompt for database configuration
echo ""
echo "=== Database Configuration ==="
read -p "Enter Database Host [3.227.166.251]: " DB_HOST
DB_HOST=${DB_HOST:-3.227.166.251}

read -p "Enter Database Name [U07k1T]: " DB_NAME
DB_NAME=${DB_NAME:-U07k1T}

read -p "Enter Database User [U07k1T]: " DB_USER
DB_USER=${DB_USER:-U07k1T}

read -sp "Enter Database Password [53689053296]: " DB_PASSWORD
echo ""
DB_PASSWORD=${DB_PASSWORD:-53689053296}

echo ""
echo "=== Configuring kubectl for EKS ==="
aws eks update-kubeconfig --region $AWS_REGION --name $CLUSTER_NAME
if [ $? -ne 0 ]; then
    echo "Error: Failed to configure kubectl"
    exit 1
fi

echo ""
echo "=== Verifying Cluster Connectivity ==="
kubectl cluster-info || exit 1

echo ""
echo "=== Updating Kubernetes Manifests ==="
# Update deployment.yaml with image URI and environment variables
sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" kubernetes/deployment.yaml
sed -i "s|{{DB_HOST}}|$DB_HOST|g" kubernetes/deployment.yaml
sed -i "s|{{DB_NAME}}|$DB_NAME|g" kubernetes/deployment.yaml
sed -i "s|{{DB_USER}}|$DB_USER|g" kubernetes/deployment.yaml
sed -i "s|{{DB_PASSWORD}}|$DB_PASSWORD|g" kubernetes/deployment.yaml

echo ""
echo "=== Applying Kubernetes Manifests ==="
echo "Creating namespace..."
kubectl apply -f kubernetes/namespace.yaml

echo "Creating deployment..."
kubectl apply -f kubernetes/deployment.yaml

echo "Creating service..."
kubectl apply -f kubernetes/service.yaml

echo "Creating ingress..."
kubectl apply -f kubernetes/ingress.yaml

echo ""
echo "=== Waiting for Deployment Rollout ==="
kubectl rollout status deployment/java-crm -n java-crm --timeout=300s

echo ""
echo "=== Verifying Deployment ==="
kubectl get pods,svc,ingress -n java-crm

echo ""
echo "=== Retrieving Application URL ==="
INGRESS_URL=$(kubectl get ingress java-crm-ingress -n java-crm -o jsonpath='{.status.loadBalancer.ingress[0].hostname}' 2>/dev/null || echo "Pending...")

echo ""
echo "====================================="
echo "Deployment completed successfully!"
echo "====================================="
echo "Namespace: java-crm"
echo "Application URL: http://$INGRESS_URL"
echo ""
echo "To check pod status: kubectl get pods -n java-crm"
echo "To view logs: kubectl logs -f deployment/java-crm -n java-crm"
echo "To rollback: kubectl rollout undo deployment/java-crm -n java-crm"
echo "====================================="