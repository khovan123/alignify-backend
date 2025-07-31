# Alignify Database Generation

This directory contains comprehensive tools for generating the Alignify MongoDB database with all collections, validation schemas, indexes, and default data.

## 🚀 Quick Start

### Option 1: Automated CLI (Recommended)
```bash
# Use the automated CLI script with defaults
./scripts/generate-database.sh

# Custom database name
./scripts/generate-database.sh -d my_alignify_db

# Use Java method instead of MongoDB shell
./scripts/generate-database.sh -m java

# Help and all options
./scripts/generate-database.sh --help
```

### Option 2: MongoDB Shell Script
```bash
# Direct execution with mongosh
mongosh alignify_db --file scripts/generate-database.js

# Or load from within mongosh
mongosh
use alignify_db
load('scripts/generate-database.js')
```

### Option 3: Java Spring Boot Utility
```bash
# Build and run the Java generator
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.main-class=com.api.utils.DatabaseGenerator
```

## 📋 What Gets Created

### Collections (27 total)
- **Core User Management**: `users`, `roles`, `permissions`, `admins`
- **Influencer System**: `influencers`, `categories`, `contentPostings`, `likes`, `comments`
- **Brand System**: `brands`, `campaigns`, `applications`, `invitations`
- **Campaign Management**: `campaignTrackings`, `plans`, `userPlans`, `planPermissions`
- **Communication**: `chatRooms`, `messages`, `notifications`, `assistantMessages`
- **Media & Files**: `galleries`, `galleryImages`
- **Security & Verification**: `otps`, `accountVerifieds`, `userBans`, `reasons`

### Default Data
- **3 Roles**: ADMIN, BRAND, INFLUENCER (with predefined ObjectIds)
- **15 Categories**: thời trang, mỹ phẩm, công nghệ, etc.
- **3 Permissions**: posting, comment, all

### Performance Indexes
- User email (unique), role-based queries
- Campaign lookups by brand, status, categories
- Application/invitation uniqueness constraints
- Message/chat room optimizations
- Content interaction indexes
- TTL index for OTP expiration (5 minutes)

## 🛠️ Available Methods

### 1. CLI Script (`generate-database.sh`)
**Pros**: 
- User-friendly interface
- Dependency checking
- Connection testing
- Dry-run support
- Method switching

**Usage**:
```bash
./scripts/generate-database.sh [options]

Options:
  -d, --database NAME     Database name (default: alignify_db)
  -u, --uri URI           MongoDB URI (default: mongodb://localhost:27017)
  -m, --method METHOD     Method: 'js' or 'java' (default: js)
  --dry-run              Preview without executing
  --check-deps           Check dependencies only
```

### 2. MongoDB Shell Script (`generate-database.js`)
**Pros**:
- Direct MongoDB execution
- Fast and lightweight
- No compilation needed
- Detailed logging

**Prerequisites**: `mongosh` installed

### 3. Java Utility (`DatabaseGenerator.java`)
**Pros**:
- Uses existing Spring Boot configuration
- Leverages MongoConfig.java directly
- Type-safe execution
- Integrated with application

**Prerequisites**: Maven, Java 17+

## 📦 Prerequisites

### For MongoDB Shell Method
- MongoDB server running
- `mongosh` installed and accessible
- Database connection permissions

### For Java Method
- Java 17+ installed
- Maven installed
- MongoDB server running
- Spring Boot dependencies resolved

### Connection Requirements
- MongoDB server accessible at specified URI
- User permissions to create databases/collections
- Network connectivity to MongoDB instance

## 🔧 Configuration

### Database Name
Default: `alignify_db`

Change in:
- CLI script: `-d your_db_name`
- JS script: Modify `DATABASE_NAME` constant
- Java utility: Set `spring.data.mongodb.database` property

### MongoDB Connection
Default: `mongodb://localhost:27017`

Change in:
- CLI script: `-u your_mongo_uri`
- JS script: Modify connection string in mongosh command
- Java utility: Set `spring.data.mongodb.uri` property

## 🎯 Use Cases

### Development Setup
```bash
# Quick local development database
./scripts/generate-database.sh

# Custom development database
./scripts/generate-database.sh -d alignify_dev
```

### Testing Environment
```bash
# Create test database with different name
./scripts/generate-database.sh -d alignify_test -u mongodb://test-server:27017
```

### Production Setup
```bash
# Preview production setup (dry run)
./scripts/generate-database.sh -d alignify_prod -u mongodb://prod-server:27017 --dry-run

# Execute with confirmation
./scripts/generate-database.sh -d alignify_prod -u mongodb://prod-server:27017
```

### CI/CD Integration
```bash
# Check dependencies in CI
./scripts/generate-database.sh --check-deps

# Automated database setup
./scripts/generate-database.sh -d alignify_ci -u $MONGODB_URI
```

## 🔍 Verification

After generation, verify the setup:

```bash
# Check collections were created
mongosh alignify_db --eval "db.getCollectionNames()"

# Check default data
mongosh alignify_db --eval "db.roles.find().pretty()"
mongosh alignify_db --eval "db.categories.find().pretty()"

# Check indexes
mongosh alignify_db --eval "db.users.getIndexes()"
```

## 🐛 Troubleshooting

### Common Issues

#### MongoDB Connection Failed
```bash
# Check if MongoDB is running
mongosh --eval "db.runCommand('ping')"

# Test specific connection
mongosh "mongodb://your-host:27017" --eval "db.runCommand('ping')"
```

#### Permission Denied
```bash
# Grant database admin rights
mongosh admin --eval "db.grantRolesToUser('your_user', ['dbAdmin'])"
```

#### mongosh Not Found
```bash
# Install MongoDB Shell
# Ubuntu/Debian:
sudo apt-get install mongodb-mongosh

# macOS:
brew install mongosh

# Or download from: https://docs.mongodb.com/mongodb-shell/install/
```

#### Java Compilation Error
```bash
# Check Java version
java -version

# Ensure Java 17+
# Update JAVA_HOME if needed
export JAVA_HOME=/path/to/java17
```

#### Maven Build Failed
```bash
# Clean and retry
mvn clean compile -DskipTests

# Check Maven version
mvn --version
```

### Getting Help

1. **Check dependencies**: `./scripts/generate-database.sh --check-deps`
2. **Use dry run**: `./scripts/generate-database.sh --dry-run`
3. **Check script help**: `./scripts/generate-database.sh --help`
4. **Review error logs**: Pay attention to specific error messages
5. **Test connection**: Verify MongoDB is accessible before running scripts

## 📝 Schema Details

The database generation creates collections with comprehensive JSON Schema validation:

- **Field types**: string, int, double, bool, date, array, object
- **Validation patterns**: Email regex, URL patterns, enum values
- **Required fields**: Enforced at database level
- **Array structures**: Nested objects with validation
- **Unique constraints**: Email uniqueness, compound keys

Example schema (users collection):
```javascript
{
  bsonType: "object",
  required: ["name", "email", "password", "roleId"],
  properties: {
    email: {
      bsonType: "string",
      pattern: "^.+@.+\\..+$"
    },
    roleId: {
      bsonType: "string"
    }
    // ... additional fields
  }
}
```

## 🚀 Next Steps

After database generation:

1. **Verify Setup**: Check all collections and data were created correctly
2. **Configure Application**: Update your Spring Boot application.properties with the database connection
3. **Test Connection**: Start your application and verify it connects successfully
4. **Populate Data**: Begin adding your application-specific data
5. **Set Up Monitoring**: Configure database monitoring for production environments

---

**📅 Last Updated**: January 2025  
**🔧 Maintainer**: [@khovan123](https://github.com/khovan123)  
**📖 Related**: See `scripts/README.md` for other available scripts