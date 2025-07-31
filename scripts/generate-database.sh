#!/bin/bash

# Alignify Database Generator CLI
# 
# This script provides a convenient command-line interface for generating
# the Alignify MongoDB database with all collections, schemas, and default data.
#
# Usage: ./scripts/generate-database.sh [options]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
DEFAULT_DB_NAME="alignify_db"
DEFAULT_MONGO_URI="mongodb://localhost:27017"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

# Functions
print_header() {
    echo -e "${PURPLE}🚀 Alignify Database Generator${NC}"
    echo -e "${PURPLE}======================================${NC}"
}

print_info() { echo -e "${BLUE}ℹ️  $1${NC}"; }
print_success() { echo -e "${GREEN}✅ $1${NC}"; }
print_warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }
print_error() { echo -e "${RED}❌ $1${NC}"; }

show_help() {
    print_header
    echo ""
    echo "Generate Alignify MongoDB database with all collections, schemas, and default data."
    echo ""
    echo "Usage:"
    echo "  $0 [options]"
    echo ""
    echo "Options:"
    echo "  -h, --help              Show this help message"
    echo "  -d, --database NAME     Database name (default: $DEFAULT_DB_NAME)"
    echo "  -u, --uri URI           MongoDB connection URI (default: $DEFAULT_MONGO_URI)"
    echo "  -m, --method METHOD     Generation method: 'js' or 'java' (default: js)"
    echo "  -v, --verbose           Enable verbose output"
    echo "  --check-deps           Check dependencies and exit"
    echo "  --dry-run              Show what would be done without executing"
    echo ""
    echo "Methods:"
    echo "  js                     Use MongoDB shell script (requires mongosh)"
    echo "  java                   Use Java Spring Boot utility (requires Maven)"
    echo ""
    echo "Examples:"
    echo "  $0                                          # Use defaults"
    echo "  $0 -d my_alignify_db                       # Custom database name"
    echo "  $0 -u mongodb://user:pass@host:27017       # Custom MongoDB URI"
    echo "  $0 -m java                                  # Use Java method"
    echo "  $0 --dry-run                               # Preview actions"
    echo ""
    echo "Prerequisites:"
    echo "  - MongoDB server running and accessible"
    echo "  - mongosh installed (for js method)"
    echo "  - Maven and Java 17+ (for java method)"
    echo "  - Appropriate database permissions"
}

check_dependencies() {
    local method=$1
    local issues=0
    
    print_info "Checking dependencies for method: $method"
    
    if [[ "$method" == "js" ]]; then
        if ! command -v mongosh >/dev/null 2>&1; then
            print_error "mongosh not found. Please install MongoDB Shell."
            print_info "Install: https://docs.mongodb.com/mongodb-shell/install/"
            ((issues++))
        else
            print_success "mongosh found: $(mongosh --version | head -n1)"
        fi
    elif [[ "$method" == "java" ]]; then
        if ! command -v mvn >/dev/null 2>&1; then
            print_error "Maven not found. Please install Maven."
            ((issues++))
        else
            print_success "Maven found: $(mvn --version | head -n1)"
        fi
        
        if ! command -v java >/dev/null 2>&1; then
            print_error "Java not found. Please install Java 17+."
            ((issues++))
        else
            local java_version=$(java -version 2>&1 | head -n1)
            print_success "Java found: $java_version"
        fi
    fi
    
    return $issues
}

test_mongodb_connection() {
    local uri=$1
    local db_name=$2
    
    print_info "Testing MongoDB connection..."
    
    if command -v mongosh >/dev/null 2>&1; then
        if mongosh "$uri/$db_name" --eval "db.runCommand('ping')" >/dev/null 2>&1; then
            print_success "MongoDB connection successful"
            return 0
        else
            print_error "Cannot connect to MongoDB at $uri"
            print_info "Please check if MongoDB is running and the URI is correct"
            return 1
        fi
    else
        print_warning "Cannot test connection (mongosh not available)"
        return 0
    fi
}

generate_with_js() {
    local uri=$1
    local db_name=$2
    local script_path="$SCRIPT_DIR/generate-database.js"
    
    print_info "Using MongoDB shell script method"
    
    if [[ ! -f "$script_path" ]]; then
        print_error "MongoDB script not found: $script_path"
        return 1
    fi
    
    print_info "Executing MongoDB script..."
    if mongosh "$uri/$db_name" --file "$script_path"; then
        print_success "Database generation completed using MongoDB shell"
        return 0
    else
        print_error "Database generation failed"
        return 1
    fi
}

generate_with_java() {
    local uri=$1
    local db_name=$2
    
    print_info "Using Java Spring Boot utility method"
    
    cd "$PROJECT_ROOT"
    
    print_info "Building project..."
    if ! mvn clean compile -DskipTests -q; then
        print_error "Failed to build project"
        return 1
    fi
    
    print_info "Running database generator..."
    local java_opts="-Dspring.main.web-application-type=none"
    java_opts+=" -Dspring.data.mongodb.uri=$uri"
    java_opts+=" -Dspring.data.mongodb.database=$db_name"
    
    if mvn spring-boot:run \
        -Dspring-boot.run.main-class=com.api.utils.DatabaseGenerator \
        -Dspring-boot.run.jvmArguments="$java_opts" \
        -q; then
        print_success "Database generation completed using Java utility"
        return 0
    else
        print_error "Database generation failed"
        return 1
    fi
}

# Parse arguments
DATABASE_NAME="$DEFAULT_DB_NAME"
MONGO_URI="$DEFAULT_MONGO_URI"
METHOD="js"
VERBOSE=false
DRY_RUN=false
CHECK_DEPS=false

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -d|--database)
            DATABASE_NAME="$2"
            shift 2
            ;;
        -u|--uri)
            MONGO_URI="$2"
            shift 2
            ;;
        -m|--method)
            METHOD="$2"
            if [[ "$METHOD" != "js" && "$METHOD" != "java" ]]; then
                print_error "Invalid method: $METHOD. Use 'js' or 'java'"
                exit 1
            fi
            shift 2
            ;;
        -v|--verbose)
            VERBOSE=true
            shift
            ;;
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --check-deps)
            CHECK_DEPS=true
            shift
            ;;
        *)
            print_error "Unknown option: $1"
            echo ""
            show_help
            exit 1
            ;;
    esac
done

# Main execution
main() {
    print_header
    
    if [[ "$CHECK_DEPS" == true ]]; then
        check_dependencies "$METHOD"
        exit $?
    fi
    
    print_info "Configuration:"
    print_info "  Database: $DATABASE_NAME"
    print_info "  MongoDB URI: $MONGO_URI"
    print_info "  Method: $METHOD"
    print_info "  Dry run: $DRY_RUN"
    echo ""
    
    # Check dependencies
    if ! check_dependencies "$METHOD"; then
        print_error "Dependency check failed"
        exit 1
    fi
    
    # Test MongoDB connection
    if ! test_mongodb_connection "$MONGO_URI" "$DATABASE_NAME"; then
        exit 1
    fi
    
    if [[ "$DRY_RUN" == true ]]; then
        print_warning "DRY RUN MODE - No actual changes will be made"
        print_info "Would execute $METHOD method to generate database '$DATABASE_NAME'"
        print_info "Would connect to: $MONGO_URI"
        print_success "Dry run completed successfully"
        exit 0
    fi
    
    # Confirm before proceeding
    echo ""
    print_warning "This will create/recreate the database '$DATABASE_NAME'"
    print_warning "Existing collections will be dropped and recreated"
    read -p "Do you want to continue? (y/N): " -n 1 -r
    echo ""
    
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "Operation cancelled"
        exit 0
    fi
    
    # Execute generation
    echo ""
    print_info "Starting database generation..."
    
    case "$METHOD" in
        "js")
            generate_with_js "$MONGO_URI" "$DATABASE_NAME"
            ;;
        "java") 
            generate_with_java "$MONGO_URI" "$DATABASE_NAME"
            ;;
    esac
    
    if [[ $? -eq 0 ]]; then
        echo ""
        print_success "🎉 Database generation completed successfully!"
        print_info "Database '$DATABASE_NAME' is ready for use"
        print_info "You can now start your Alignify application"
    else
        echo ""
        print_error "Database generation failed"
        exit 1
    fi
}

# Run main function
main "$@"