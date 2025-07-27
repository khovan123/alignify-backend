# Scripts Directory

This directory contains utility scripts for the Alignify Backend project.

## 📊 Database Scripts

### `generate-database.js`

MongoDB database generation script that creates all collections with validation schemas based on the MongoConfig.java file.

#### Usage

```bash
# Option 1: Using mongosh with file
mongosh alignify_db --file scripts/generate-database.js

# Option 2: Load from within mongosh
mongosh
use alignify_db
load('scripts/generate-database.js')

# Option 3: Direct execution with mongosh
cat scripts/generate-database.js | mongosh alignify_db
```

#### Features

- ✅ **Complete schema validation** - Creates all 27 collections with proper JSON schemas
- ✅ **Default data insertion** - Inserts essential roles, categories, and permissions
- ✅ **Performance indexes** - Creates optimized indexes for common queries
- ✅ **Error handling** - Graceful error handling with detailed feedback
- ✅ **Comprehensive logging** - Detailed progress output with emojis
- ✅ **Idempotent execution** - Can be run multiple times safely

#### Collections Created

The script creates the following collections with validation schemas:

| Collection | Purpose | Key Features |
|------------|---------|--------------|
| `users` | User accounts | Email validation, role-based |
| `roles` | User roles | ADMIN, BRAND, INFLUENCER |
| `categories` | Content categories | 15 default categories |
| `influencers` | Influencer profiles | Social media links, ratings |
| `brands` | Brand profiles | Contact info, social media |
| `admins` | Admin profiles | Administrative accounts |
| `campaigns` | Marketing campaigns | Status workflow, requirements |
| `applications` | Campaign applications | Influencer applications |
| `invitations` | Direct invitations | Brand to influencer invites |
| `campaignTrackings` | Progress tracking | Campaign progress monitoring |
| `contentPostings` | Content posts | Influencer content/ideas |
| `likes` | Post interactions | Like system |
| `comments` | Post comments | Comment system |
| `chatRooms` | Chat functionality | Group chat rooms |
| `messages` | Chat messages | Real-time messaging |
| `notifications` | User notifications | System notifications |
| `permissions` | User permissions | Access control |
| `planPermissions` | Plan-based permissions | Feature limitations |
| `plans` | Subscription plans | Pricing plans |
| `userPlans` | User subscriptions | Active subscriptions |
| `galleries` | Image galleries | Media collections |
| `galleryImages` | Individual images | Image metadata |
| `otps` | OTP verification | Email verification codes |
| `accountVerifieds` | Verified accounts | Account verification status |
| `userBans` | User bans | Moderation system |
| `reasons` | Ban reasons | Predefined ban reasons |
| `assistantMessages` | AI assistant | Chatbot messages |

#### Default Data

The script automatically inserts essential data:

**Roles:**
- ADMIN (ID: 68485dcedda6867ca0d23e89)
- BRAND (ID: 68485dcedda6867ca0d23e8a)  
- INFLUENCER (ID: 68485dcedda6867ca0d23e8b)

**Categories:**
- thời trang, mỹ phẩm, công nghệ, nghệ thuật, thể thao
- ăn uống, du lịch, lối sống, âm nhạc, trò chơi điện tử
- handmade, phong tục và văn hóa, khởi nghiệp, kĩ năng mềm, mẹ và bé

**Permissions:**
- posting: Permission to create and manage posts
- comment: Permission to comment on posts  
- all: Full access permissions

#### Indexes Created

Performance indexes are automatically created for:
- User email (unique), roleId
- Campaign brandId, status, categoryIds
- Application campaignId+influencerId (unique), brandId
- Invitation campaignId+influencerId (unique)
- Message chatRoomId+sendAt, chatRoom members
- Notification userId+createdAt, isRead
- Content userId+createdDate, like userId+contentId (unique)
- Comment contentId+createdAt
- OTP email, TTL (5 minutes)
- Account verified email (unique)

#### Prerequisites

- MongoDB server running
- `mongosh` installed
- Appropriate database permissions
- Database name configured (default: `alignify_db`)

#### Configuration

You can modify the database name at the top of the script:

```javascript
const DATABASE_NAME = "your_database_name";
```

#### Output Example

```
🚀 Starting Alignify Database Generation...
📋 Database: alignify_db
⏰ Timestamp: 2025-01-15T10:30:00.000Z

📦 Creating collection: users
  ✅ Created collection with validation: users

📝 Inserting default roles...
  ✅ Default roles inserted successfully

📊 Database Statistics:
Collections created: 27
  - users: 0 documents
  - roles: 3 documents
  - categories: 15 documents
  ...

🎉 Alignify Database Generation Completed Successfully!
```

#### Troubleshooting

**Connection Issues:**
```bash
# Check MongoDB is running
mongosh --eval "db.runCommand('ping')"

# Specify connection string
mongosh "mongodb://localhost:27017/alignify_db" --file scripts/generate-database.js
```

**Permission Issues:**
```bash
# Ensure user has createCollection permissions
mongosh admin --eval "db.grantRolesToUser('your_user', ['dbAdmin'])"
```

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
ℹ️  Version: 1.3.1
ℹ️  Type: minor
ℹ️  Tag: v1.3.1

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
| `generate-database.js` | Create MongoDB database with validation schemas | `mongosh alignify_db --file scripts/generate-database.js` |
| `release.sh` | Automated release process | `./scripts/release.sh 1.3.1 minor` |

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
./scripts/release.sh 1.3.1 patch  # ✅ Correct
./scripts/release.sh v1.3.1       # ❌ Don't include 'v'
./scripts/release.sh 1.3          # ❌ Include patch version
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
