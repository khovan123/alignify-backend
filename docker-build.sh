#!/bin/bash
# ==============================================================================
# Docker Build Script for Alignify Backend
# ==============================================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
IMAGE_NAME="alignify-backend"
TAG="latest"
TARGET="runtime"
PUSH_TO_REGISTRY=false
REGISTRY=""

# Function to print colored output
print_message() {
    echo -e "${2}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -n, --name NAME          Image name (default: alignify-backend)"
    echo "  -t, --tag TAG            Image tag (default: latest)"
    echo "  --target TARGET          Build target (runtime|development) (default: runtime)"
    echo "  -p, --push               Push to registry after build"
    echo "  -r, --registry REGISTRY  Registry URL (required if --push is used)"
    echo "  -h, --help               Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                                           # Build with defaults"
    echo "  $0 --target development                      # Build development image"
    echo "  $0 --name my-app --tag v1.0.0              # Custom name and tag"
    echo "  $0 --push --registry docker.io/username     # Build and push to registry"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -n|--name)
            IMAGE_NAME="$2"
            shift 2
            ;;
        -t|--tag)
            TAG="$2"
            shift 2
            ;;
        --target)
            TARGET="$2"
            shift 2
            ;;
        -p|--push)
            PUSH_TO_REGISTRY=true
            shift
            ;;
        -r|--registry)
            REGISTRY="$2"
            shift 2
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Validate inputs
if [[ "$TARGET" != "runtime" && "$TARGET" != "development" ]]; then
    print_message "Error: Target must be 'runtime' or 'development'" $RED
    exit 1
fi

if [[ "$PUSH_TO_REGISTRY" == true && -z "$REGISTRY" ]]; then
    print_message "Error: Registry URL is required when pushing to registry" $RED
    exit 1
fi

# Set full image name
if [[ -n "$REGISTRY" ]]; then
    FULL_IMAGE_NAME="$REGISTRY/$IMAGE_NAME:$TAG"
else
    FULL_IMAGE_NAME="$IMAGE_NAME:$TAG"
fi

print_message "Starting Docker build process..." $BLUE
print_message "Image: $FULL_IMAGE_NAME" $BLUE
print_message "Target: $TARGET" $BLUE

# Check if Docker is running
if ! docker info >/dev/null 2>&1; then
    print_message "Error: Docker is not running" $RED
    exit 1
fi

# Build the image
print_message "Building Docker image..." $YELLOW
docker build \
    --target "$TARGET" \
    --tag "$FULL_IMAGE_NAME" \
    --build-arg BUILD_DATE="$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
    --build-arg VCS_REF="$(git rev-parse --short HEAD 2>/dev/null || echo 'unknown')" \
    .

if [[ $? -eq 0 ]]; then
    print_message "Build completed successfully!" $GREEN
else
    print_message "Build failed!" $RED
    exit 1
fi

# Show image information
print_message "Image information:" $BLUE
docker images "$FULL_IMAGE_NAME"

# Push to registry if requested
if [[ "$PUSH_TO_REGISTRY" == true ]]; then
    print_message "Pushing image to registry..." $YELLOW
    docker push "$FULL_IMAGE_NAME"
    
    if [[ $? -eq 0 ]]; then
        print_message "Push completed successfully!" $GREEN
    else
        print_message "Push failed!" $RED
        exit 1
    fi
fi

print_message "Docker build process completed!" $GREEN
print_message "You can now run the container with:" $BLUE
echo "  docker run -p 8080:8080 $FULL_IMAGE_NAME"
echo ""
print_message "Or use docker-compose:" $BLUE
echo "  docker-compose up"