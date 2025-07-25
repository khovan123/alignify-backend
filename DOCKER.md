# 🐳 Docker Configuration for Alignify Backend

This directory contains a complete Docker setup for the Alignify Backend application, including production-ready configurations, development tools, and deployment scripts.

## 📋 Table of Contents

- [Overview](#overview)
- [Quick Start](#quick-start)
- [Docker Files](#docker-files)
- [Environment Configurations](#environment-configurations)
- [Development Setup](#development-setup)
- [Production Deployment](#production-deployment)
- [Monitoring and Logging](#monitoring-and-logging)
- [Security Considerations](#security-considerations)
- [Troubleshooting](#troubleshooting)

## 🔍 Overview

The Docker setup includes:

- **Multi-stage Dockerfile** with optimized build and runtime stages
- **Development and Production** docker-compose configurations
- **Nginx reverse proxy** with SSL termination and rate limiting
- **MongoDB database** with initialization scripts
- **Security hardening** with non-root user and read-only filesystem
- **Health checks** and monitoring capabilities
- **Automated build scripts** for CI/CD integration

## 🚀 Quick Start

### Prerequisites

- Docker Engine 20.10+ 
- Docker Compose 2.0+
- Git

### Development Environment

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd alignify-backend
   ```

2. **Start the development environment:**
   ```bash
   docker-compose up -d
   ```

3. **Access the application:**
   - API: http://localhost:8080
   - MongoDB Express: http://localhost:8081
   - MongoDB: localhost:27017

### Production Environment

1. **Set up environment variables:**
   ```bash
   cp .env.example .env
   # Edit .env with your production values
   ```

2. **Deploy with production configuration:**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

## 📁 Docker Files

### Core Files

| File | Description |
|------|-------------|
| `Dockerfile` | Multi-stage build with runtime and development targets |
| `.dockerignore` | Optimizes build context and improves security |
| `docker-compose.yml` | Development environment with MongoDB |
| `docker-compose.prod.yml` | Production environment with Nginx |
| `docker-build.sh` | Automated build script with options |

### Configuration Files

```
docker/
├── nginx/
│   └── nginx.conf          # Nginx reverse proxy configuration
└── mongodb/
    └── init-scripts/
        └── 01-init-db.sh    # MongoDB initialization script
```

## 🔧 Environment Configurations

### Development (.env.dev)

```env
# Spring Configuration
SPRING_PROFILES_ACTIVE=dev
MONGODB_URI=mongodb://admin:admin123@mongodb:27017/alignify?authSource=admin

# Application Configuration
JWT_SECRET=dev-jwt-secret-key
SERVER_PORT=8080

# External Services (Development)
CLOUDINARY_CLOUD_NAME=your-dev-cloud-name
GOOGLE_API_KEY=your-dev-google-api-key
```

### Production (.env.prod)

```env
# Spring Configuration
SPRING_PROFILES_ACTIVE=prod
MONGODB_URI=mongodb://your-prod-user:your-prod-password@your-mongodb-host:27017/alignify

# Security (Use strong secrets in production)
JWT_SECRET=your-very-strong-jwt-secret-key-here

# External Services (Production)
CLOUDINARY_CLOUD_NAME=your-prod-cloud-name
CLOUDINARY_API_KEY=your-prod-api-key
CLOUDINARY_API_SECRET=your-prod-api-secret
```

## 💻 Development Setup

### Building Development Image

```bash
# Build development image with debugging enabled
./docker-build.sh --target development --name alignify-dev --tag dev

# Or manually
docker build --target development -t alignify-backend:dev .
```

### Running with Debug Mode

```bash
# Start with development target for debugging
docker-compose up alignify-backend

# Debug port 5005 is exposed for remote debugging
```

### Development Features

- **Hot reload** capabilities (when volumes are mounted)
- **Debug port** (5005) exposed for IDE integration
- **Development tools** (bash, vim, git, htop)
- **Relaxed security** for easier development

## 🏭 Production Deployment

### Building Production Image

```bash
# Build and push to registry
./docker-build.sh \
  --name alignify-backend \
  --tag v1.0.0 \
  --push \
  --registry your-registry.com/namespace
```

### Production Features

- **Security hardening** with non-root user
- **Read-only filesystem** with specific writable volumes
- **Resource limits** and reservations
- **Health checks** and monitoring
- **Nginx reverse proxy** with SSL and rate limiting
- **Log rotation** and management

### SSL Configuration

1. **Generate SSL certificates:**
   ```bash
   mkdir -p docker/nginx/ssl
   # Add your SSL certificates:
   # - cert.pem (certificate)
   # - key.pem (private key)
   ```

2. **Update Nginx configuration** if needed in `docker/nginx/nginx.conf`

## 📊 Monitoring and Logging

### Health Checks

- **Application**: `GET /actuator/health`
- **Database**: MongoDB ping check
- **Nginx**: Built-in health monitoring

### Accessing Logs

```bash
# Application logs
docker-compose logs -f alignify-backend

# Database logs
docker-compose logs -f mongodb

# All services
docker-compose logs -f
```

### Log Files

- Application logs: `/app/logs/` (mounted volume)
- Nginx logs: Default Docker logging
- MongoDB logs: Default Docker logging

## 🔒 Security Considerations

### Container Security

- **Non-root user**: Application runs as `alignify:alignify` (UID/GID 1001)
- **Read-only filesystem**: Container filesystem is read-only except for specific volumes
- **No new privileges**: Security option prevents privilege escalation
- **Resource limits**: Memory and CPU limits prevent resource exhaustion

### Network Security

- **Rate limiting**: Nginx implements API and authentication rate limiting
- **SSL/TLS**: HTTPS with modern TLS configuration
- **Security headers**: Standard security headers are set
- **Internal networking**: Services communicate on isolated Docker network

### Secret Management

⚠️ **Important**: Never commit secrets to version control!

**Development:**
- Use `.env` files (add to `.gitignore`)
- Use Docker secrets for sensitive data

**Production:**
- Use external secret management (HashiCorp Vault, AWS Secrets Manager, etc.)
- Use Docker swarm secrets or Kubernetes secrets
- Use environment variable injection from CI/CD pipeline

## 🛠 Troubleshooting

### Common Issues

#### Build Failures

```bash
# Clear Docker cache
docker system prune -a

# Rebuild without cache
docker build --no-cache -t alignify-backend .
```

#### Connection Issues

```bash
# Check container logs
docker-compose logs alignify-backend

# Check network connectivity
docker-compose exec alignify-backend curl -f http://localhost:8080/actuator/health
```

#### Database Issues

```bash
# Access MongoDB shell
docker-compose exec mongodb mongosh --username admin --password admin123

# Reset database
docker-compose down -v  # WARNING: This removes all data!
docker-compose up -d
```

### Performance Tuning

#### JVM Tuning

Edit `docker-compose.yml` or `docker-compose.prod.yml`:

```yaml
environment:
  - JAVA_OPTS=-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

#### MongoDB Tuning

Add to MongoDB configuration:

```yaml
command: mongod --auth --bind_ip_all --wiredTigerCacheSizeGB 2
```

### Debug Commands

```bash
# Check container resource usage
docker stats

# Inspect container configuration
docker inspect alignify-backend

# Execute shell in running container
docker-compose exec alignify-backend sh

# Check application metrics
curl http://localhost:8080/actuator/metrics
```

## 📚 Additional Resources

- [Spring Boot Docker Documentation](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [MongoDB Docker Documentation](https://hub.docker.com/_/mongo)
- [Nginx Docker Documentation](https://hub.docker.com/_/nginx)

## 🤝 Contributing

When modifying Docker configurations:

1. Test changes in development environment first
2. Update this documentation if needed
3. Ensure security best practices are maintained
4. Test production configuration in staging environment

---

**Alignify Backend Docker Configuration**  
*Production-ready containerization for Spring Boot application*