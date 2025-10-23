# Docker Deployment Guide

This guide explains how to build and run the ZJU Tennis Player Analysis System using Docker and Docker Compose.

## Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+
- `.env` file with database credentials (see `.env.example`)

## Project Structure

```
zjutennis/
├── Dockerfile                    # Backend Spring Boot container
├── docker-compose.yml            # Orchestration configuration
├── .dockerignore                 # Backend build exclusions
└── frontend/
    ├── Dockerfile                # Frontend Vue.js container
    ├── nginx.conf                # Nginx web server configuration
    └── .dockerignore             # Frontend build exclusions
```

## Quick Start

### 1. Ensure .env file exists

Make sure you have a `.env` file in the project root with your database credentials:

```bash
DB_URL=jdbc:mysql://your-host:port/zjualumni?ssl-mode=REQUIRED
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### 2. Build and run with Docker Compose

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### 3. Access the application

- **Frontend**: http://localhost
- **Backend API**: http://localhost:8080/api/players

## Individual Container Management

### Backend Only

```bash
# Build backend image
docker build -t zjutennis-backend .

# Run backend container
docker run -d \
  --name zjutennis-backend \
  -p 8080:8080 \
  --env-file .env \
  zjutennis-backend
```

### Frontend Only

```bash
# Build frontend image
docker build -t zjutennis-frontend ./frontend

# Run frontend container
docker run -d \
  --name zjutennis-frontend \
  -p 80:80 \
  zjutennis-frontend
```

## Docker Compose Commands

```bash
# Build images
docker-compose build

# Start services in detached mode
docker-compose up -d

# Start services with build
docker-compose up -d --build

# View running containers
docker-compose ps

# View logs
docker-compose logs

# Follow logs for specific service
docker-compose logs -f backend
docker-compose logs -f frontend

# Stop services (keeps containers)
docker-compose stop

# Start stopped services
docker-compose start

# Stop and remove containers
docker-compose down

# Stop and remove containers, volumes, and images
docker-compose down -v --rmi all
```

## Health Checks

Both services include health checks:

### Backend
- Endpoint: `http://localhost:8080/api/players`
- Interval: 30 seconds
- Start period: 40 seconds

### Frontend
- Endpoint: `http://localhost/health`
- Interval: 30 seconds
- Start period: 10 seconds

Check health status:
```bash
docker-compose ps
```

## Environment Variables

### Backend (Spring Boot)

| Variable | Description | Required |
|----------|-------------|----------|
| `DB_URL` | JDBC connection URL | Yes |
| `DB_USERNAME` | Database username | Yes |
| `DB_PASSWORD` | Database password | Yes |

### Frontend (Nginx)

The frontend is statically built, so no runtime environment variables are needed.

## Networking

Both services are connected via a custom bridge network `zjutennis-network`. This allows:
- Service discovery by container name
- Isolated network communication
- Easy scaling and orchestration

## Production Deployment

### Security Best Practices

1. **Use secrets management**
   ```bash
   # Use Docker secrets instead of .env file
   echo "your_password" | docker secret create db_password -
   ```

2. **Update nginx.conf for production domain**
   ```nginx
   server_name your-domain.com;
   ```

3. **Enable SSL/TLS**
   - Add SSL certificates to frontend container
   - Configure nginx for HTTPS
   - Use Let's Encrypt for free SSL certificates

4. **Use non-root users**
   - Both Dockerfiles use non-root users where possible
   - Alpine-based images for minimal attack surface

### Resource Limits

Add resource constraints in docker-compose.yml:

```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M
```

### Persistent Data

If you need to persist application data:

```yaml
services:
  backend:
    volumes:
      - backend-data:/app/data

volumes:
  backend-data:
```

## Troubleshooting

### Container won't start

```bash
# Check logs
docker-compose logs backend
docker-compose logs frontend

# Check container status
docker-compose ps

# Restart specific service
docker-compose restart backend
```

### Port already in use

```bash
# Check what's using the port
lsof -ti:8080  # Backend
lsof -ti:80    # Frontend

# Kill the process or change port in docker-compose.yml
```

### Database connection issues

```bash
# Verify environment variables
docker-compose exec backend env | grep DB_

# Test database connectivity from container
docker-compose exec backend ping your-db-host
```

### Frontend can't reach backend

- Ensure both services are on the same network
- Check backend health: `curl http://localhost:8080/api/players`
- Verify API URL in frontend code matches deployed backend URL

### Build cache issues

```bash
# Rebuild without cache
docker-compose build --no-cache

# Remove all cached layers
docker system prune -a
```

## Multi-Stage Build Details

### Backend Dockerfile
1. **Build Stage**: Uses Maven to compile and package the Spring Boot application
2. **Runtime Stage**: Uses minimal JRE image to run the JAR file

Benefits:
- Smaller final image (only JRE, no Maven or build tools)
- Faster deployment
- Improved security

### Frontend Dockerfile
1. **Build Stage**: Uses Node.js to build the Vue.js application
2. **Runtime Stage**: Uses nginx to serve static files

Benefits:
- Production-optimized static files
- Efficient web server (nginx)
- Minimal image size

## Image Sizes

Approximate sizes:
- Backend: ~280 MB (JRE + Spring Boot JAR)
- Frontend: ~25 MB (nginx + static files)

## Continuous Integration/Deployment

### GitHub Actions Example

```yaml
name: Build and Push Docker Images

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build images
        run: docker-compose build

      - name: Push to registry
        run: |
          docker tag zjutennis-backend your-registry/zjutennis-backend
          docker push your-registry/zjutennis-backend
```

## Monitoring

### Container Stats

```bash
# View resource usage
docker stats

# Specific container stats
docker stats zjutennis-backend zjutennis-frontend
```

### Logs Management

```bash
# Export logs
docker-compose logs > deployment.log

# Limit log size in docker-compose.yml
services:
  backend:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

## Backup and Restore

### Export container

```bash
docker export zjutennis-backend > backend-backup.tar
```

### Save image

```bash
docker save zjutennis-backend:latest > backend-image.tar
```

### Load image

```bash
docker load < backend-image.tar
```

## Support

For issues or questions:
1. Check the logs: `docker-compose logs`
2. Verify environment configuration
3. Review this documentation
4. Open an issue on the project repository
