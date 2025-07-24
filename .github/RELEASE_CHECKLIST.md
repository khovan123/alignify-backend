# Release Checklist Template

Use this checklist for every release to ensure consistency and quality.

## 📋 Pre-Release Checklist

### 🔍 Code Quality & Testing
- [ ] All tests pass locally
- [ ] All CI/CD pipelines pass
- [ ] Code coverage meets requirements (>80%)
- [ ] No critical security vulnerabilities
- [ ] Performance benchmarks meet requirements
- [ ] Manual testing completed for new features

### 📝 Documentation
- [ ] Update version in `pom.xml`
- [ ] Update `CHANGELOG.md` with new changes
- [ ] Update `RELEASE.md` with release notes
- [ ] API documentation updated (Swagger)
- [ ] README.md updated if needed
- [ ] Migration guide written (if breaking changes)

### 🏷️ Versioning
- [ ] Version number follows semantic versioning
- [ ] Tag name follows convention (`v{MAJOR}.{MINOR}.{PATCH}`)
- [ ] Pre-release tags used appropriately (alpha/beta/rc)
- [ ] Breaking changes documented

## 🚀 Release Process

### 1. Prepare Release Branch
- [ ] Create release branch from `develop`
- [ ] Final version bump commit
- [ ] All documentation updates committed

### 2. Testing Phase
- [ ] Alpha release created (if applicable)
  - [ ] Tag: `v{VERSION}-alpha.{NUMBER}`
  - [ ] Internal testing completed
- [ ] Beta release created (if applicable)
  - [ ] Tag: `v{VERSION}-beta.{NUMBER}`
  - [ ] Community testing feedback addressed
- [ ] Release candidate created (if applicable)
  - [ ] Tag: `v{VERSION}-rc.{NUMBER}`
  - [ ] Final bug fixes applied

### 3. Final Release
- [ ] Create annotated git tag
  ```bash
  git tag -a v{VERSION} -m "Release v{VERSION}: {SUMMARY}"
  ```
- [ ] Push tag to repository
  ```bash
  git push origin v{VERSION}
  ```
- [ ] Merge release branch to `main`
- [ ] Merge release branch back to `develop`

### 4. GitHub Release
- [ ] Create GitHub release from tag
- [ ] Copy release notes from `RELEASE.md`
- [ ] Upload any release artifacts
- [ ] Mark as pre-release (if applicable)
- [ ] Mark as latest release (for stable releases)

## 🌐 Deployment

### Production Deployment
- [ ] Deploy to staging environment
- [ ] Smoke tests pass on staging
- [ ] Deploy to production environment
- [ ] Health checks pass
- [ ] Monitor for immediate issues

### Documentation Updates
- [ ] Live API documentation updated
- [ ] Swagger UI reflects new version
- [ ] Frontend team notified of API changes
- [ ] Integration partners notified (if needed)

## 📢 Communication

### Internal Communication
- [ ] Development team notified
- [ ] QA team notified
- [ ] DevOps team notified
- [ ] Product team notified

### External Communication
- [ ] Release announcement prepared
- [ ] Social media posts scheduled
- [ ] User documentation updated
- [ ] Support team briefed on changes

## 🔍 Post-Release

### Monitoring
- [ ] Application health monitored for 24 hours
- [ ] Error rates within normal ranges
- [ ] Performance metrics stable
- [ ] User feedback monitored

### Follow-up Actions
- [ ] Create milestone for next version
- [ ] Plan hotfix process (if issues found)
- [ ] Update project roadmap
- [ ] Schedule retrospective meeting

## 📋 Release Template

### Tag Message Template
```
Release v{VERSION}: {SHORT_DESCRIPTION}

New Features:
- {FEATURE_1}
- {FEATURE_2}

Improvements:
- {IMPROVEMENT_1}
- {IMPROVEMENT_2}

Bug Fixes:
- {BUGFIX_1}
- {BUGFIX_2}

Breaking Changes:
- {BREAKING_CHANGE_1} (if any)

Migration Notes:
- {MIGRATION_STEP_1} (if needed)

Dependencies:
- Updated {DEPENDENCY} to v{VERSION}
```

### GitHub Release Description Template
```markdown
## 🚀 What's New in v{VERSION}

### ✨ New Features
- **{Feature Name}**: {Description}
- **{Feature Name}**: {Description}

### 🔧 Improvements
- {Improvement description}
- {Improvement description}

### 🐛 Bug Fixes
- Fixed {bug description}
- Resolved {issue description}

### ⚠️ Breaking Changes
- {Breaking change description and migration steps}

### 📦 Dependencies
- Updated {dependency} to v{version}
- Added {new dependency} v{version}

## 📋 Installation

```bash
# Maven
<dependency>
    <groupId>com.api</groupId>
    <artifactId>alignify-backend</artifactId>
    <version>{VERSION}</version>
</dependency>

# Docker
docker pull khovan123/alignify-backend:{VERSION}
```

## 🔗 Links
- **API Documentation**: [Swagger UI](https://alignify-backend.onrender.com/swagger-ui)
- **Changelog**: [CHANGELOG.md](./CHANGELOG.md)
- **Migration Guide**: [RELEASE.md](./RELEASE.md)

## 💬 Feedback
If you encounter any issues, please [open an issue](https://github.com/khovan123/alignify-backend/issues).
```

## ⚠️ Hotfix Release Process

### Emergency Hotfix
- [ ] Create hotfix branch from affected release tag
- [ ] Apply minimal fix
- [ ] Test thoroughly
- [ ] Create patch version tag
- [ ] Deploy immediately
- [ ] Merge fix back to `develop` and `main`

### Hotfix Checklist
- [ ] Issue severity validated (critical/high)
- [ ] Fix is minimal and targeted
- [ ] No new features introduced
- [ ] Patch version number used
- [ ] Emergency deployment approved
- [ ] Stakeholders notified

---

**Remember**: Always follow the principle of least change for releases. Better to have frequent small releases than large risky ones.