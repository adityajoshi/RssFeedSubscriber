# CI/CD Pipeline Setup Guide

This guide explains how to set up the CI/CD pipeline for the RSS Feed Subscriber application.

## Overview

The CI/CD pipeline includes:
- **Build & Test**: Compiles, tests, and builds the application
- **Code Quality**: SonarQube analysis for code quality
- **Security Scan**: OWASP dependency check for vulnerabilities
- **Docker Build**: Creates and pushes Docker images
- **Deployment**: Automated deployment to staging and production
- **Notifications**: Success/failure notifications

## Prerequisites

1. **GitHub Repository**: Push your code to GitHub
2. **Docker Hub Account**: For container registry
3. **Kubernetes Cluster**: For deployment (optional)
4. **SonarQube Server**: For code quality analysis (optional)

## GitHub Secrets Setup

Add the following secrets to your GitHub repository:

### Required Secrets

```bash
# Docker Hub credentials
DOCKER_USERNAME=your-dockerhub-username
DOCKER_PASSWORD=your-dockerhub-password

# Kubernetes configuration (base64 encoded)
KUBE_CONFIG=<base64-encoded-kubeconfig>

# Oracle Database credentials
DB_HOST=your-oracle-host
DB_PORT=1521
DB_NAME=your-database-name
DB_USERNAME=your-username
DB_PASSWORD=your-password
```

### Optional Secrets

```bash
# SonarQube configuration
SONAR_TOKEN=your-sonarqube-token
SONAR_HOST_URL=https://your-sonarqube-instance.com

# Notification webhooks (Slack, Teams, etc.)
SLACK_WEBHOOK_URL=https://hooks.slack.com/services/...
TEAMS_WEBHOOK_URL=https://your-teams-webhook-url
```

## Pipeline Workflows

### 1. Main CI/CD Pipeline (`.github/workflows/ci-cd.yml`)

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches

**Jobs:**
- `build-and-test`: Compiles, tests, and builds the application
- `code-quality`: Runs SonarQube analysis
- `security-scan`: Performs OWASP dependency check
- `docker-build`: Builds and pushes Docker images (main branch only)
- `deploy-staging`: Deploys to staging (develop branch)
- `deploy-production`: Deploys to production (main branch)
- `notify`: Sends success/failure notifications

### 2. Manual Deployment (`.github/workflows/deploy.yml`)

**Triggers:**
- Manual workflow dispatch

**Features:**
- Choose environment (staging/production)
- Specify Docker image tag
- Manual approval for production deployments

## Docker Configuration

### Dockerfile
- Multi-stage build for optimized image size
- Non-root user for security
- Health checks included
- JRE-only runtime for smaller footprint

### .dockerignore
- Excludes unnecessary files from build context
- Improves build performance

## Kubernetes Deployment

### Deployment Configuration (`k8s/deployment.yaml`)
- 3 replicas for high availability
- Resource limits and requests
- Health checks (liveness and readiness probes)
- Security context (non-root user)
- Environment variables from secrets
- Ingress configuration for external access

## Security Features

1. **OWASP Dependency Check**
   - Scans for known vulnerabilities
   - Fails build on CVSS score ≥ 7
   - Generates HTML and JSON reports

2. **Docker Security**
   - Non-root user execution
   - Read-only filesystem
   - Dropped capabilities
   - Multi-stage build to reduce attack surface

3. **Kubernetes Security**
   - Non-root container execution
   - Resource limits
   - Secrets for sensitive data
   - Network policies (can be added)

## Monitoring and Observability

### Health Checks
- Application health endpoint: `/actuator/health`
- Kubernetes liveness and readiness probes
- Docker health check

### Logging
- Structured logging with SLF4J
- Application logs available in Kubernetes pods

### Metrics
- Spring Boot Actuator metrics
- Can be integrated with Prometheus/Grafana

## Environment Configuration

### Development
```yaml
SPRING_PROFILES_ACTIVE: development
```

### Staging
```yaml
SPRING_PROFILES_ACTIVE: staging
```

### Production
```yaml
SPRING_PROFILES_ACTIVE: production
```

## Troubleshooting

### Common Issues

1. **Build Failures**
   - Check Java version compatibility
   - Verify Maven dependencies
   - Review test failures

2. **Docker Build Issues**
   - Ensure Docker Hub credentials are correct
   - Check Dockerfile syntax
   - Verify .dockerignore configuration

3. **Deployment Failures**
   - Verify Kubernetes cluster access
   - Check resource availability
   - Review pod logs: `kubectl logs -f deployment/rss-feed-subscriber`

4. **Security Scan Failures**
   - Review vulnerability reports
   - Update dependencies if needed
   - Consider security exceptions if false positives

### Useful Commands

```bash
# Check pipeline status
gh run list

# View workflow logs
gh run view <run-id>

# Manually trigger deployment
gh workflow run deploy.yml -f environment=staging -f version=latest

# Check Kubernetes deployment
kubectl get pods -l app=rss-feed-subscriber
kubectl describe deployment rss-feed-subscriber
kubectl logs -f deployment/rss-feed-subscriber

# Check Docker image
docker pull your-username/rss-feed-subscriber:latest
docker run -p 8080:8080 your-username/rss-feed-subscriber:latest
```

## Best Practices

1. **Branch Strategy**
   - Use feature branches for development
   - Merge to `develop` for staging deployment
   - Merge to `main` for production deployment

2. **Security**
   - Regularly update dependencies
   - Review security scan results
   - Rotate secrets periodically

3. **Monitoring**
   - Set up alerts for deployment failures
   - Monitor application health
   - Track performance metrics

4. **Backup and Recovery**
   - Backup database regularly
   - Test disaster recovery procedures
   - Maintain rollback procedures

## Customization

### Adding New Environments
1. Create new Kubernetes namespace
2. Add environment to deployment workflow
3. Configure environment-specific secrets
4. Update ingress rules

### Adding Notifications
1. Configure webhook URLs in secrets
2. Update notification steps in workflows
3. Test notification delivery

### Customizing Build Process
1. Modify Maven goals in workflow
2. Add custom build steps
3. Configure build artifacts

## Support

For issues with the CI/CD pipeline:
1. Check GitHub Actions logs
2. Review this documentation
3. Contact the DevOps team
4. Create an issue in the repository 