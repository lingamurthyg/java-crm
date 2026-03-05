# Java CRM Application - AWS EKS Deployment Guide

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [Building and Pushing Docker Images](#building-and-pushing-docker-images)
4. [AWS EKS Cluster Setup](#aws-eks-cluster-setup)
5. [Deploying to AWS EKS](#deploying-to-aws-eks)
6. [Configuration Management](#configuration-management)
7. [Monitoring and Logging](#monitoring-and-logging)
8. [Troubleshooting](#troubleshooting)
9. [Security Considerations](#security-considerations)
10. [Scaling and Updates](#scaling-and-updates)

---

## Prerequisites

### Required Software
- **Docker**: Version 20.10 or higher
  - Download: https://docs.docker.com/get-docker/
- **Docker Compose**: Version 2.0 or higher (included with Docker Desktop)
- **AWS CLI**: Version 2.x
  - Installation: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
- **kubectl**: Version 1.23 or higher
  - Installation: https://kubernetes.io/docs/tasks/tools/
- **eksctl** (optional but recommended): Latest version
  - Installation: https://eksctl.io/installation/

### AWS Requirements
- AWS Account with appropriate permissions
- IAM user or role with the following policies:
  - AmazonEKSClusterPolicy
  - AmazonEKSWorkerNodePolicy
  - AmazonEC2ContainerRegistryFullAccess
  - AmazonEKS_CNI_Policy
- Configured AWS CLI credentials:
  ```bash
  aws configure
  ```

### System Requirements
- Operating System: Linux, macOS, or Windows 10/11
- Minimum 4GB RAM
- Minimum 20GB free disk space

---

## Local Development Setup

### 1. Clone the Repository
```bash
cd /path/to/project
```

### 2. Verify Project Structure
Ensure the following structure exists:
```
.
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── java-crm/
│   ├── src/
│   ├── build.xml
│   └── nbproject/
├── kubernetes/
│   ├── namespace.yaml
│   ├── deployment.yaml
│   ├── service.yaml
│   └── ingress.yaml
└── scripts/
    ├── build-push.sh
    ├── build-push.bat
    ├── deploy-image.sh
    └── deploy-image.bat
```

### 3. Run Locally with Docker Compose

```bash
# Build and start the application
docker-compose up --build

# Access the application
# The application will be available at http://localhost:8080

# Stop the application
docker-compose down
```

### 4. Test the Docker Build

```bash
# Build the Docker image
docker build -t java-crm:test .

# Run the container
docker run -d -p 8080:8080 --name java-crm-test java-crm:test

# Check logs
docker logs -f java-crm-test

# Stop and remove
docker stop java-crm-test
docker rm java-crm-test
```

---

## Building and Pushing Docker Images

### Option 1: Push to AWS ECR

#### Using the Build Script (Recommended)

**Linux/macOS:**
```bash
chmod +x scripts/build-push.sh
./scripts/build-push.sh
```

**Windows:**
```cmd
scripts\build-push.bat
```

The script will prompt for:
- Registry type (select 1 for AWS ECR)
- AWS Region (e.g., us-east-1)
- AWS Account ID
- ECR Repository Name
- Image Tag (default: latest)

#### Manual ECR Push

```bash
# Set variables
export AWS_REGION="us-east-1"
export AWS_ACCOUNT_ID="123456789012"
export ECR_REPO="java-crm"
export IMAGE_TAG="v1.0.0"

# Authenticate Docker to ECR
aws ecr get-login-password --region $AWS_REGION | \
  docker login --username AWS --password-stdin \
  ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com

# Create ECR repository (if not exists)
aws ecr create-repository --repository-name $ECR_REPO --region $AWS_REGION || true

# Build and tag the image
docker build -t ${ECR_REPO}:${IMAGE_TAG} .
docker tag ${ECR_REPO}:${IMAGE_TAG} \
  ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${IMAGE_TAG}

# Push to ECR
docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${IMAGE_TAG}
```

### Option 2: Push to Docker Hub

```bash
# Login to Docker Hub
docker login

# Build and tag
docker build -t your-username/java-crm:latest .

# Push to Docker Hub
docker push your-username/java-crm:latest
```

---

## AWS EKS Cluster Setup

### Option 1: Using eksctl (Recommended)

```bash
# Create EKS cluster
exsctl create cluster \
  --name java-crm-cluster \
  --region us-east-1 \
  --nodegroup-name java-crm-nodes \
  --node-type t3.medium \
  --nodes 2 \
  --nodes-min 1 \
  --nodes-max 4 \
  --managed

# This process takes approximately 15-20 minutes
```

### Option 2: Using AWS Console

1. Navigate to AWS EKS Console
2. Click "Create Cluster"
3. Configure:
   - Cluster name: `java-crm-cluster`
   - Kubernetes version: 1.27 or higher
   - Cluster service role: Create or select existing
   - VPC and subnets: Use default or custom
4. Add node group:
   - Name: `java-crm-nodes`
   - Instance type: t3.medium
   - Desired size: 2 nodes

### Configure kubectl

```bash
# Update kubeconfig
aws eks update-kubeconfig --region us-east-1 --name java-crm-cluster

# Verify connection
kubectl cluster-info
kubectl get nodes
```

### Install AWS Load Balancer Controller

Required for Ingress to work with AWS ALB:

```bash
# Download IAM policy
curl -o iam_policy.json https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/v2.6.0/docs/install/iam_policy.json

# Create IAM policy
aws iam create-policy \
    --policy-name AWSLoadBalancerControllerIAMPolicy \
    --policy-document file://iam_policy.json

# Create service account
exsctl create iamserviceaccount \
  --cluster=java-crm-cluster \
  --namespace=kube-system \
  --name=aws-load-balancer-controller \
  --attach-policy-arn=arn:aws:iam::${AWS_ACCOUNT_ID}:policy/AWSLoadBalancerControllerIAMPolicy \
  --override-existing-serviceaccounts \
  --approve

# Install the controller using Helm
helm repo add eks https://aws.github.io/eks-charts
helm repo update
helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=java-crm-cluster \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller
```

---

## Deploying to AWS EKS

### Using the Deployment Script (Recommended)

**Linux/macOS:**
```bash
chmod +x scripts/deploy-image.sh
./scripts/deploy-image.sh
```

**Windows:**
```cmd
scripts\deploy-image.bat
```

The script will prompt for:
- AWS Region
- EKS Cluster Name
- Docker Image URI (full path from ECR or Docker Hub)
- Database Host
- Database Name
- Database User
- Database Password

### Manual Deployment

```bash
# Set variables
export IMAGE_URI="123456789012.dkr.ecr.us-east-1.amazonaws.com/java-crm:latest"
export DB_HOST="3.227.166.251"
export DB_NAME="U07k1T"
export DB_USER="U07k1T"
export DB_PASSWORD="53689053296"

# Update deployment.yaml with values
sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" kubernetes/deployment.yaml
sed -i "s|{{DB_HOST}}|$DB_HOST|g" kubernetes/deployment.yaml
sed -i "s|{{DB_NAME}}|$DB_NAME|g" kubernetes/deployment.yaml
sed -i "s|{{DB_USER}}|$DB_USER|g" kubernetes/deployment.yaml
sed -i "s|{{DB_PASSWORD}}|$DB_PASSWORD|g" kubernetes/deployment.yaml

# Apply Kubernetes manifests
kubectl apply -f kubernetes/namespace.yaml
kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/service.yaml
kubectl apply -f kubernetes/ingress.yaml

# Wait for deployment
kubectl rollout status deployment/java-crm -n java-crm

# Verify
kubectl get pods,svc,ingress -n java-crm
```

### Access the Application

```bash
# Get the Load Balancer URL
kubectl get ingress java-crm-ingress -n java-crm

# The application will be accessible at:
# http://<load-balancer-dns-name>
```

---

## Configuration Management

### Environment Variables

The application uses the following environment variables (configured in `kubernetes/deployment.yaml`):

- `JAVA_OPTS`: JVM options for memory and performance
- `TZ`: Timezone setting (default: UTC)
- `DB_HOST`: Database host address
- `DB_NAME`: Database name
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password

### Using Kubernetes Secrets (Recommended)

For production, store sensitive data in Kubernetes Secrets:

```bash
# Create secret for database credentials
kubectl create secret generic db-credentials \
  --from-literal=username=U07k1T \
  --from-literal=password=53689053296 \
  -n java-crm

# Update deployment.yaml to use secrets:
# env:
# - name: DB_USER
#   valueFrom:
#     secretKeyRef:
#       name: db-credentials
#       key: username
# - name: DB_PASSWORD
#   valueFrom:
#     secretKeyRef:
#       name: db-credentials
#       key: password
```

### Using ConfigMaps

For non-sensitive configuration:

```bash
# Create ConfigMap
kubectl create configmap app-config \
  --from-literal=db.host=3.227.166.251 \
  --from-literal=db.name=U07k1T \
  -n java-crm
```

---

## Monitoring and Logging

### View Logs

```bash
# View logs for all pods
kubectl logs -f deployment/java-crm -n java-crm

# View logs for specific pod
kubectl logs -f <pod-name> -n java-crm

# View previous logs (for crashed containers)
kubectl logs --previous <pod-name> -n java-crm
```

### Check Pod Status

```bash
# Get pod status
kubectl get pods -n java-crm

# Describe pod for detailed information
kubectl describe pod <pod-name> -n java-crm

# Get pod events
kubectl get events -n java-crm --sort-by='.lastTimestamp'
```

### Resource Usage

```bash
# Check resource usage
kubectl top pods -n java-crm
kubectl top nodes
```

---

## Troubleshooting

### Common Issues

#### 1. Pods Not Starting

**Symptoms:** Pods in `Pending`, `CrashLoopBackOff`, or `ImagePullBackOff` state

```bash
# Check pod status and events
kubectl describe pod <pod-name> -n java-crm

# Common fixes:
# - ImagePullBackOff: Check image URI and ECR permissions
# - CrashLoopBackOff: Check application logs
# - Pending: Check node resources and pod resource requests
```

#### 2. Cannot Pull Image from ECR

```bash
# Verify ECR repository exists
aws ecr describe-repositories --region us-east-1

# Verify IAM permissions for EKS nodes
# Ensure node role has AmazonEC2ContainerRegistryReadOnly policy

# Create imagePullSecret (if needed)
kubectl create secret docker-registry ecr-secret \
  --docker-server=${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com \
  --docker-username=AWS \
  --docker-password=$(aws ecr get-login-password --region ${AWS_REGION}) \
  -n java-crm
```

#### 3. Ingress Not Working

```bash
# Check ingress status
kubectl describe ingress java-crm-ingress -n java-crm

# Verify AWS Load Balancer Controller is running
kubectl get pods -n kube-system | grep aws-load-balancer-controller

# Check controller logs
kubectl logs -n kube-system deployment/aws-load-balancer-controller
```

#### 4. Database Connection Issues

```bash
# Check environment variables in pod
kubectl exec -it <pod-name> -n java-crm -- env | grep DB

# Test database connectivity from pod
kubectl exec -it <pod-name> -n java-crm -- sh
# Inside pod:
# telnet $DB_HOST 3306
```

#### 5. High Memory Usage

```bash
# Check current memory usage
kubectl top pods -n java-crm

# Adjust JVM heap size in deployment.yaml:
# env:
# - name: JAVA_OPTS
#   value: "-Xmx768m -Xms384m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Update resource limits in deployment.yaml
```

### Debug Commands

```bash
# Execute shell in running pod
kubectl exec -it <pod-name> -n java-crm -- sh

# Port forward for local debugging
kubectl port-forward deployment/java-crm 8080:8080 -n java-crm

# Get all resources in namespace
kubectl get all -n java-crm

# Delete and recreate deployment
kubectl delete deployment java-crm -n java-crm
kubectl apply -f kubernetes/deployment.yaml
```

---

## Security Considerations

### 1. Use Kubernetes Secrets

Never store sensitive data in plain text in deployment manifests. Always use Kubernetes Secrets or AWS Secrets Manager.

### 2. Network Policies

Implement network policies to restrict pod-to-pod communication:

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: java-crm-network-policy
  namespace: java-crm
spec:
  podSelector:
    matchLabels:
      app: java-crm
  policyTypes:
  - Ingress
  ingress:
  - from:
    - namespaceSelector:
        matchLabels:
          name: kube-system
    ports:
    - protocol: TCP
      port: 8080
```

### 3. RBAC

Implement Role-Based Access Control for namespace:

```bash
# Create service account
kubectl create serviceaccount java-crm-sa -n java-crm

# Create role and rolebinding
# (Define appropriate permissions based on requirements)
```

### 4. Image Scanning

```bash
# Scan Docker image for vulnerabilities
aws ecr start-image-scan \
  --repository-name java-crm \
  --image-id imageTag=latest \
  --region us-east-1

# Get scan results
aws ecr describe-image-scan-findings \
  --repository-name java-crm \
  --image-id imageTag=latest \
  --region us-east-1
```

### 5. Pod Security Standards

Apply pod security standards to namespace:

```bash
kubectl label namespace java-crm \
  pod-security.kubernetes.io/enforce=restricted \
  pod-security.kubernetes.io/audit=restricted \
  pod-security.kubernetes.io/warn=restricted
```

---

## Scaling and Updates

### Manual Scaling

```bash
# Scale deployment
kubectl scale deployment java-crm --replicas=5 -n java-crm

# Verify scaling
kubectl get pods -n java-crm
```

### Horizontal Pod Autoscaler (HPA)

```bash
# Create HPA
kubectl autoscale deployment java-crm \
  --cpu-percent=70 \
  --min=2 \
  --max=10 \
  -n java-crm

# Check HPA status
kubectl get hpa -n java-crm
```

### Rolling Updates

```bash
# Update image
kubectl set image deployment/java-crm \
  java-crm=${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/java-crm:v2.0.0 \
  -n java-crm

# Monitor rollout
kubectl rollout status deployment/java-crm -n java-crm

# Check rollout history
kubectl rollout history deployment/java-crm -n java-crm
```

### Rollback

```bash
# Rollback to previous version
kubectl rollout undo deployment/java-crm -n java-crm

# Rollback to specific revision
kubectl rollout undo deployment/java-crm --to-revision=2 -n java-crm
```

---

## Technology-Specific Notes

### Java 8 and JavaFX Considerations

1. **JavaFX Runtime:** This application uses JavaFX, which requires a GUI environment. For containerized deployment:
   - The current setup assumes headless operation for backend services
   - If GUI is required, consider using VNC or Xvfb
   - For production, consider migrating to a web-based UI framework

2. **JVM Memory Management:**
   - Java 8 has limited container awareness
   - Use explicit `-Xmx` and `-Xms` flags
   - Monitor memory usage and adjust accordingly

3. **Database Driver:**
   - Application uses MySQL JDBC driver
   - Ensure MySQL database is accessible from EKS cluster
   - Consider using AWS RDS for managed database

4. **Ant Build System:**
   - Legacy build system using Apache Ant
   - Consider migrating to Maven or Gradle for better dependency management

### Performance Tuning

```bash
# Adjust JVM options in deployment.yaml:
env:
- name: JAVA_OPTS
  value: "-Xmx1g -Xms512m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"
```

---

## Cleanup

### Delete Application from EKS

```bash
# Delete all resources
kubectl delete namespace java-crm

# Or delete individually
kubectl delete -f kubernetes/ingress.yaml
kubectl delete -f kubernetes/service.yaml
kubectl delete -f kubernetes/deployment.yaml
kubectl delete -f kubernetes/namespace.yaml
```

### Delete EKS Cluster

```bash
# Using eksctl
exsctl delete cluster --name java-crm-cluster --region us-east-1

# Or via AWS Console
# Navigate to EKS Console and delete cluster
```

### Delete ECR Repository

```bash
# Delete ECR repository
aws ecr delete-repository \
  --repository-name java-crm \
  --region us-east-1 \
  --force
```

---

## Additional Resources

- [AWS EKS Documentation](https://docs.aws.amazon.com/eks/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Docker Documentation](https://docs.docker.com/)
- [AWS Load Balancer Controller](https://kubernetes-sigs.github.io/aws-load-balancer-controller/)
- [eksctl Documentation](https://eksctl.io/)

---

## Support and Feedback

For issues, questions, or feedback:
- Review application logs: `kubectl logs -f deployment/java-crm -n java-crm`
- Check AWS EKS cluster health in AWS Console
- Consult troubleshooting section above

---

**Document Version:** 1.0  
**Last Updated:** 2026-03-05  
**Target Platform:** AWS EKS (Elastic Kubernetes Service)