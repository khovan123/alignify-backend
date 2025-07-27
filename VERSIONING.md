# Versioning and Tagging Guide

## 📏 Semantic Versioning Strategy

Alignify Backend follows [Semantic Versioning 2.0.0](https://semver.org/) for all releases.

### Version Format: `MAJOR.MINOR.PATCH`

```
v1.2.3
│ │ │
│ │ └── PATCH: Bug fixes and minor updates
│ └──── MINOR: New features (backwards compatible)
└────── MAJOR: Breaking changes
```

---

## 🏷️ Git Tagging Conventions

### Production Release Tags

#### Stable Releases
All production releases **must** be prefixed with `v`:

```bash
v1.0.0    # Initial release
v1.1.0    # Minor feature update
v1.1.1    # Patch/hotfix
v2.0.0    # Major release with breaking changes
```

#### Creating Release Tags

```bash
# Create annotated tag for release
git tag -a v1.0.0 -m "Release version 1.0.0 - Initial stable release"

# Push tag to remote
git push origin v1.0.0

# Push all tags
git push --tags
```

### Pre-release Tags

#### Alpha Releases
Early development versions for internal testing:

```bash
v1.1.0-alpha     # First alpha
v1.1.0-alpha.1   # First numbered alpha
v1.1.0-alpha.2   # Second alpha iteration
```

**Use cases:**
- Early feature development
- Internal testing
- Proof of concept features

#### Beta Releases
Feature-complete versions for broader testing:

```bash
v1.1.0-beta      # First beta
v1.1.0-beta.1    # First numbered beta
v1.1.0-beta.2    # Second beta iteration
```

**Use cases:**
- Feature-complete testing
- Community feedback
- Performance testing

#### Release Candidates
Final testing before stable release:

```bash
v1.1.0-rc        # First release candidate
v1.1.0-rc.1      # First numbered RC
v1.1.0-rc.2      # Second RC iteration
```

**Use cases:**
- Final bug fixes
- Production readiness testing
- Documentation finalization

### Development Tags

#### Snapshot Versions
Continuous development builds:

```bash
v1.1.0-SNAPSHOT  # Development snapshot
v2.0.0-SNAPSHOT  # Next major development
```

#### Feature Branch Tags
Specific feature development:

```bash
v1.1.0-feature-chat      # Chat feature development
v1.1.0-feature-payments  # Payment integration
```

---

## 📋 Release Workflow

### 1. Development Phase

```bash
# Start with snapshot version
git tag v1.1.0-SNAPSHOT

# Feature development
git tag v1.1.0-feature-newfeature
```

### 2. Testing Phase

```bash
# Alpha testing
git tag v1.1.0-alpha.1

# Beta testing  
git tag v1.1.0-beta.1

# Release candidate
git tag v1.1.0-rc.1
```

### 3. Release Phase

```bash
# Final release
git tag -a v1.1.0 -m "Release v1.1.0: New features and improvements"
git push origin v1.1.0
```

---

## 🔢 Version Numbering Rules

### MAJOR Version (X.0.0)

Increment when you make **incompatible API changes**:

- ❌ Breaking changes to existing endpoints
- ❌ Removing or renaming API endpoints
- ❌ Changing request/response formats
- ❌ Database schema breaking changes
- ❌ Configuration format changes

**Examples:**
- `v1.0.0` → `v2.0.0`: New authentication system
- `v2.0.0` → `v3.0.0`: API restructure

### MINOR Version (X.Y.0)

Increment when you add **backwards-compatible functionality**:

- ✅ New API endpoints
- ✅ New optional parameters
- ✅ New features
- ✅ Performance improvements
- ✅ New integrations

**Examples:**
- `v1.0.0` → `v1.1.0`: Added payment integration
- `v1.1.0` → `v1.2.0`: Added chat functionality

### PATCH Version (X.Y.Z)

Increment when you make **backwards-compatible bug fixes**:

- 🔧 Bug fixes
- 🔧 Security patches
- 🔧 Performance optimizations
- 🔧 Documentation updates
- 🔧 Dependency updates

**Examples:**
- `v1.1.0` → `v1.1.1`: Fixed authentication bug
- `v1.1.1` → `v1.1.2`: Security patch

---

## 🚀 GitHub Release Process

### 1. Prepare Release

```bash
# Update version in pom.xml
<version>1.1.0</version>

# Update CHANGELOG.md
## [1.1.0] - 2025-07-24
### Added
- New payment integration
### Fixed  
- Authentication issues

# Commit changes
git add .
git commit -m "chore: bump version to 1.1.0"
```

### 2. Create Tag

```bash
# Create annotated tag
git tag -a v1.1.0 -m "Release v1.1.0

New Features:
- Payment integration with PayOS and PayPal
- Enhanced chat functionality
- AI-powered recommendations

Bug Fixes:
- Fixed authentication timeout issues
- Resolved file upload errors

Breaking Changes:
- None

Migration Notes:
- Update environment variables for payment gateways"

# Push tag
git push origin v1.1.0
```

### 3. GitHub Release

1. **Go to GitHub repository**
2. **Click "Releases" → "Create a new release"**
3. **Select tag**: `v1.1.0`
4. **Release title**: `v1.1.0 - Payment Integration & Chat Enhancements`
5. **Description**: Copy from tag message
6. **Pre-release**: Check if alpha/beta/rc
7. **Set as latest**: Check for stable releases

---

## 📊 Version History Examples

### Current Versioning Plan

```
v1.0.0     ← Current Release (July 2025)
├── v1.0.1 ← Hotfix (planned)
├── v1.1.0 ← Minor Update (planned Q3 2025)
│   ├── v1.1.0-alpha.1
│   ├── v1.1.0-beta.1
│   └── v1.1.0-rc.1
└── v2.0.0 ← Major Release (planned Q1 2026)
    ├── v2.0.0-alpha.1
    ├── v2.0.0-beta.1
    └── v2.0.0-rc.1
```

### Version Comparison

| Version | Type | Description | Compatibility |
|---------|------|-------------|---------------|
| `v1.0.0` | Major | Initial release | N/A |
| `v1.0.1` | Patch | Bug fixes | ✅ Compatible |
| `v1.1.0` | Minor | New features | ✅ Compatible |
| `v2.0.0` | Major | Breaking changes | ❌ Breaking |

---

## 🔍 Best Practices

### DO ✅

- **Always prefix with `v`**: `v1.0.0` not `1.0.0`
- **Use annotated tags**: Include release notes
- **Follow semantic versioning**: Major.Minor.Patch
- **Update CHANGELOG.md**: Document all changes
- **Test pre-releases**: Alpha → Beta → RC → Stable
- **Use meaningful messages**: Detailed tag descriptions

### DON'T ❌

- **Skip version numbers**: Don't go from v1.0.0 to v1.2.0
- **Use random names**: Avoid `awesome-release` or `final-version`
- **Make breaking changes in patches**: Only bug fixes
- **Forget documentation**: Always update docs
- **Rush releases**: Proper testing phases

### Hotfix Workflow

```bash
# For critical production fixes
git checkout v1.0.0
git checkout -b hotfix/v1.0.1
# Make fixes
git tag v1.0.1
git push origin v1.0.1

# Merge back to develop
git checkout develop
git merge hotfix/v1.0.1
```

---

## 🎯 Release Checklist

### Pre-Release

- [ ] Update version in `pom.xml`
- [ ] Update `CHANGELOG.md`
- [ ] Update `RELEASE.md`
- [ ] Run full test suite
- [ ] Build and test Docker image
- [ ] Update API documentation
- [ ] Review breaking changes

### Release

- [ ] Create annotated git tag
- [ ] Push tag to repository
- [ ] Create GitHub release
- [ ] Deploy to production
- [ ] Update live documentation
- [ ] Announce release

### Post-Release

- [ ] Monitor for issues
- [ ] Update project boards
- [ ] Plan next version
- [ ] Archive old versions

---

This guide ensures consistent, predictable, and professional release management for the Alignify Backend project.