#!/bin/bash

# Alignify Backend Release Helper Script
# Usage: ./scripts/release.sh [version] [type]
# Example: ./scripts/release.sh 1.3.1 minor

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Functions
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_header() {
    echo -e "\n${BLUE}🚀 $1${NC}"
    echo "----------------------------------------"
}

# Check if version is provided
if [ -z "$1" ]; then
    print_error "Version number is required!"
    echo "Usage: $0 [version] [type]"
    echo "Example: $0 1.3.1 minor"
    exit 1
fi

VERSION=$1
TYPE=${2:-"minor"}
TAG="v$VERSION"

print_header "Alignify Backend Release Helper"
print_info "Version: $VERSION"
print_info "Type: $TYPE"
print_info "Tag: $TAG"

# Validate version format
if ! [[ $VERSION =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    print_error "Invalid version format! Use MAJOR.MINOR.PATCH (e.g., 1.0.0)"
    exit 1
fi

# Check if we're on the right branch
CURRENT_BRANCH=$(git branch --show-current)
print_info "Current branch: $CURRENT_BRANCH"

if [[ "$CURRENT_BRANCH" != "develop" && "$CURRENT_BRANCH" != "main" ]]; then
    print_warning "You're not on develop or main branch. Continue? (y/N)"
    read -r response
    if [[ ! "$response" =~ ^[Yy]$ ]]; then
        print_error "Aborted!"
        exit 1
    fi
fi

# Check if tag already exists
if git tag -l | grep -q "^$TAG$"; then
    print_error "Tag $TAG already exists!"
    exit 1
fi

print_header "Pre-release Checks"

# Check if working directory is clean
if [[ -n $(git status --porcelain) ]]; then
    print_error "Working directory is not clean! Commit or stash your changes."
    git status --short
    exit 1
fi
print_success "Working directory is clean"

# Check if all tests pass
print_info "Running tests..."
if ./mvnw test -q; then
    print_success "All tests pass"
else
    print_error "Tests failed! Fix them before releasing."
    exit 1
fi

# Update version in pom.xml
print_header "Updating Version"
print_info "Updating pom.xml version to $VERSION..."

# Backup pom.xml
cp pom.xml pom.xml.backup

# Update version in pom.xml
sed -i.bak "s/<version>[0-9]\+\.[0-9]\+\.[0-9]\+<\/version>/<version>$VERSION<\/version>/" pom.xml

# Verify the change
if grep -q "<version>$VERSION</version>" pom.xml; then
    print_success "Version updated in pom.xml"
    rm pom.xml.bak
else
    print_error "Failed to update version in pom.xml"
    mv pom.xml.backup pom.xml
    exit 1
fi

# Update CHANGELOG.md
print_header "Updating Documentation"
print_info "Please update CHANGELOG.md with the new version details."
print_info "Opening CHANGELOG.md for editing..."

# Check if CHANGELOG.md exists
if [[ ! -f "CHANGELOG.md" ]]; then
    print_error "CHANGELOG.md not found!"
    exit 1
fi

# Open CHANGELOG.md in default editor
${EDITOR:-nano} CHANGELOG.md

print_warning "Have you updated CHANGELOG.md with the new version? (y/N)"
read -r response
if [[ ! "$response" =~ ^[Yy]$ ]]; then
    print_error "Please update CHANGELOG.md before continuing!"
    exit 1
fi

# Commit version changes
print_header "Committing Changes"
git add pom.xml CHANGELOG.md
git commit -m "chore: bump version to $VERSION"
print_success "Version changes committed"

# Create tag
print_header "Creating Release Tag"
print_info "Creating annotated tag $TAG..."

# Create tag with message
git tag -a "$TAG" -m "Release $TAG

Version: $VERSION
Type: $TYPE release
Date: $(date +%Y-%m-%d)

See CHANGELOG.md for detailed changes."

print_success "Tag $TAG created"

# Push changes
print_header "Publishing Release"
print_warning "Ready to push changes and tag to remote? This will trigger the release! (y/N)"
read -r response
if [[ "$response" =~ ^[Yy]$ ]]; then
    git push origin "$CURRENT_BRANCH"
    git push origin "$TAG"
    print_success "Changes and tag pushed to remote"
    
    print_header "Release Complete!"
    print_success "Release $TAG has been created and pushed"
    print_info "Next steps:"
    echo "  1. Go to GitHub and create a release from tag $TAG"
    echo "  2. Copy release notes from CHANGELOG.md"
    echo "  3. Monitor deployment and application health"
    echo "  4. Update project boards and documentation"
    
    print_info "GitHub release URL:"
    echo "  https://github.com/khovan123/alignify-backend/releases/new?tag=$TAG"
    
else
    print_warning "Release not published. You can push manually later:"
    echo "  git push origin $CURRENT_BRANCH"
    echo "  git push origin $TAG"
fi

print_header "Release Summary"
echo "📦 Version: $VERSION"
echo "🏷️  Tag: $TAG"
echo "📝 Type: $TYPE"
echo "🌿 Branch: $CURRENT_BRANCH"
echo "📅 Date: $(date)"

print_success "Release process completed!"
