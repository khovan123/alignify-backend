#!/bin/sh
# ==============================================================================
# Health check script for Alignify Backend Docker container
# ==============================================================================

# Check if the application is responding on the health endpoint
curl -f http://localhost:8080/actuator/health >/dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "Health check passed"
    exit 0
else
    echo "Health check failed"
    exit 1
fi