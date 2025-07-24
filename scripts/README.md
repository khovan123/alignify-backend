# Scripts Directory

This directory contains utility scripts for the Alignify Backend project.

## 🚀 Release Scripts

### `release.sh`

Automated release helper script that streamlines the release process.

#### Usage

```bash
# Basic usage
./scripts/release.sh [version] [type]

# Examples
./scripts/release.sh 1.1.0 minor
./scripts/release.sh 1.0.1 patch
./scripts/release.sh 2.0.0 major
```

#### Features

- ✅ **Version validation** - Ensures semantic versioning format
- ✅ **Pre-release checks** - Validates clean working directory and passing tests
- ✅ **Automatic version updates** - Updates `pom.xml` with new version
- ✅ **Documentation prompts** - Reminds to update `CHANGELOG.md`
- ✅ **Git tag creation** - Creates annotated tags with proper messaging
- ✅ **Safe publishing** - Confirms before pushing to remote
- ✅ **Colored output** - Clear visual feedback throughout the process

#### Prerequisites

- Clean working directory (no uncommitted changes)
- All tests must pass
- Must be on `develop` or `main` branch (or confirm to continue)
- `CHANGELOG.md` must exist

#### Process Flow

1. **Validation** - Check version format and git status
2. **Testing** - Run test suite to ensure quality
3. **Version Update** - Update `pom.xml` with new version
4. **Documentation** - Prompt to update `CHANGELOG.md`
5. **Commit** - Commit version and documentation changes
6. **Tagging** - Create annotated git tag
7. **Publishing** - Push changes and tag to remote

#### Output Example

```
🚀 Alignify Backend Release Helper
----------------------------------------
ℹ️  Version: 1.1.0
ℹ️  Type: minor
ℹ️  Tag: v1.1.0

🚀 Pre-release Checks
----------------------------------------
✅ Working directory is clean
✅ All tests pass

🚀 Release Complete!
----------------------------------------
✅ Release v1.1.0 has been created and pushed
```

#### Error Handling

The script will stop and provide clear error messages for:
- Invalid version format
- Existing tags
- Dirty working directory
- Failed tests
- Missing documentation files

#### Next Steps After Running

1. Go to GitHub and create a release from the tag
2. Copy release notes from `CHANGELOG.md`
3. Monitor deployment and application health
4. Update project boards and documentation

---

## 🔧 Adding New Scripts

When adding new scripts to this directory:

1. **Make them executable**: `chmod +x scripts/script-name.sh`
2. **Add documentation** to this README
3. **Follow naming convention**: Use lowercase with hyphens
4. **Include help text**: Support `--help` flag
5. **Use color coding**: For better user experience
6. **Add error handling**: Fail fast and clearly
7. **Test thoroughly**: Especially with edge cases

### Script Template

```bash
#!/bin/bash

# Script Name - Brief description
# Usage: ./scripts/script-name.sh [options]

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Functions
print_info() { echo -e "${BLUE}ℹ️  $1${NC}"; }
print_success() { echo -e "${GREEN}✅ $1${NC}"; }
print_warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }
print_error() { echo -e "${RED}❌ $1${NC}"; }

# Help function
show_help() {
    echo "Usage: $0 [options]"
    echo "Description of what the script does"
    echo ""
    echo "Options:"
    echo "  -h, --help    Show this help message"
    echo "  -v, --version Show version"
}

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# Main script logic here
print_info "Script started"
# ... script logic ...
print_success "Script completed"
```

---

## 📋 Available Scripts

| Script | Purpose | Usage |
|--------|---------|-------|
| `release.sh` | Automated release process | `./scripts/release.sh 1.1.0 minor` |

---

## 🆘 Troubleshooting

### Common Issues

#### Permission Denied
```bash
# Make script executable
chmod +x scripts/script-name.sh
```

#### Script Not Found
```bash
# Run from project root
cd /path/to/alignify-backend
./scripts/script-name.sh
```

#### Version Format Error
```bash
# Use semantic versioning format
./scripts/release.sh 1.0.0 patch  # ✅ Correct
./scripts/release.sh v1.0.0       # ❌ Don't include 'v'
./scripts/release.sh 1.0          # ❌ Include patch version
```

### Getting Help

If you encounter issues with any script:

1. Check this documentation
2. Run script with `--help` flag (if supported)
3. Check script permissions and location
4. Review error messages carefully
5. Open an issue on GitHub if needed

---

**📝 Last Updated**: July 24, 2025  
**🔧 Maintainer**: [@khovan123](https://github.com/khovan123)