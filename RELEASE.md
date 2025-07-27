# Release Documentation

## 📦 Current Release: v1.3.1

**Release Date**: July 24, 2025  
**Release Type**: Major Release (Initial Release)  
**Compatibility**: Breaking changes from pre-release versions  

### 🎯 Release Overview

This is the initial stable release of **Alignify Backend**, a comprehensive platform designed to connect social network influencers with brands for marketing collaborations. This release establishes the foundation for all core functionality and provides a production-ready API.

---

## 🏷️ Tagging and Versioning

### Semantic Versioning

This project follows [Semantic Versioning](https://semver.org/) principles:

```
MAJOR.MINOR.PATCH
```

- **MAJOR**: Breaking changes that require updates to client applications
- **MINOR**: New features added in a backwards-compatible manner  
- **PATCH**: Backwards-compatible bug fixes

### Tagging Guidelines

All releases are tagged following these conventions:

#### Production Releases
- **Format**: `v{MAJOR}.{MINOR}.{PATCH}`
- **Examples**: `v1.0.0`, `v1.2.3`, `v2.0.0`

#### Pre-release Versions
- **Alpha**: `v{MAJOR}.{MINOR}.{PATCH}-alpha.{NUMBER}`
  - Example: `v1.1.0-alpha.1`, `v2.0.0-alpha.3`
- **Beta**: `v{MAJOR}.{MINOR}.{PATCH}-beta.{NUMBER}`
  - Example: `v1.1.0-beta.1`, `v2.0.0-beta.2`
- **Release Candidate**: `v{MAJOR}.{MINOR}.{PATCH}-rc.{NUMBER}`
  - Example: `v1.1.0-rc.1`, `v2.0.0-rc.2`

#### Development Tags
- **Snapshot**: `v{MAJOR}.{MINOR}.{PATCH}-SNAPSHOT`
- **Development**: `v{MAJOR}.{MINOR}.{PATCH}-dev.{NUMBER}`

---

## 🚀 What's New in v1.3.1

### Core Platform Features

#### 👥 Multi-Role User System
- **Admin**: Platform management with full moderation capabilities
- **Brand**: Campaign creation and influencer collaboration management  
- **Influencer**: Content creation and campaign participation

#### 📊 Campaign Management System
- Complete campaign lifecycle management
- Status transitions: `DRAFT` → `RECRUITING` → `PENDING` → `PARTICIPATING` → `COMPLETED`
- Application and invitation system
- Progress tracking and review workflows

#### 💬 Real-time Communication
- WebSocket-based chat system using STOMP protocol
- Group chat functionality for brand-influencer collaboration
- Real-time notifications for all user activities

#### 🎨 Content & Interaction System
- Content/idea posting for influencers
- Like and comment system
- Content moderation tools for admins

### Technical Improvements

#### 🔒 Security Features
- JWT-based authentication and authorization
- Role-based access control (RBAC)
- Spring Security integration
- Rate limiting protection

#### 🔌 Third-Party Integrations
- **Payment Processing**: PayOS (Vietnam) and PayPal (International)
- **Media Management**: Cloudinary for image/video storage
- **AI Features**: Google Gemini integration for smart recommendations
- **Email Service**: Gmail SMTP for notifications
- **Social Auth**: Google OAuth integration

#### 📊 API & Documentation
- RESTful API design with consistent response format
- Comprehensive Swagger UI documentation
- API versioning strategy (`/api/v1/`)
- Standardized error handling

---

## 🔧 Installation & Setup

### Prerequisites
- Java 21 or higher
- MongoDB 4.4+
- Maven 3.6+
- Docker (optional)

### Quick Start

```bash
# Clone the repository
git clone https://github.com/khovan123/alignify-backend.git
cd alignify-backend

# Build the project
./mvnw clean install

# Set up environment variables (copy from template)
cp .env.example .env
# Edit .env with your configuration

# Run the application
./mvnw spring-boot:run
```

### Docker Deployment

```bash
# Build Docker image
docker build -t alignify-backend .

# Run with environment file
docker run -d --env-file .env -p 8080:8080 alignify-backend
```

---

## 🌐 Deployment Information

### Production Environment
- **Backend API**: https://alignify-backend.onrender.com
- **API Documentation**: https://alignify-backend.onrender.com/swagger-ui
- **Frontend Application**: https://alignify-rose.vercel.app

### Health Checks
- **Health Endpoint**: `GET /actuator/health`
- **API Status**: `GET /api/v1/status`

---

## 📚 Documentation Links

- **API Documentation**: [Swagger UI](https://alignify-backend.onrender.com/swagger-ui)
- **README**: [Project Overview](./README.md)
- **Changelog**: [Release History](./CHANGELOG.md)
- **API Design Rules**: [RESTful API Guidelines](./.github/restful_api_design_rules.prompt.md)
- **Development Guide**: [ReactJS Instructions](./.github/reactjs_development_instructions.prompt.md)
- **DevOps Guide**: [CI/CD Instructions](./.github/devops_instructions.prompt.md)

---

## 🔄 Migration Guide

### From Pre-release Versions

This is the initial stable release. If you were using any pre-release or development versions:

1. **Database Migration**: Ensure MongoDB collections match the final schema
2. **API Changes**: Update client applications to use `/api/v1/` endpoints
3. **Authentication**: Migrate to JWT-based authentication system
4. **Environment Variables**: Update configuration with new required variables

### Configuration Changes

Required environment variables for v1.3.1:

```env
# Core Configuration
API_SECRET_KEY=your_jwt_secret
MONGODB_URI=mongodb://localhost:27017/alignify

# Email Configuration  
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Cloud Storage
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# OAuth
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_SECRET_KEY=your_google_secret

# AI Integration
GEMINI_API_KEY=your_gemini_api_key
RAPIDAPI_KEY=your_rapidapi_key

# Payment Gateways
PAYOS_CLIENT_ID=your_payos_client_id
PAYOS_API_KEY=your_payos_api_key
PAYOS_CHECKSUM_KEY=your_payos_checksum_key
PAYPAL_CLIENT_ID=your_paypal_client_id
PAYPAL_CLIENT_SECRET=your_paypal_client_secret
```

---

## 🐛 Known Issues

### Current Limitations
- Email verification required for all new user registrations
- Chat message history limited to recent conversations
- File upload size limited to 10MB per file

### Workarounds
- For email issues, check spam folder or use different email provider
- For large file uploads, consider using external storage solutions
- For chat history, export important conversations regularly

---

## 🗺️ Roadmap

### v1.1.* (Planned)
- Enhanced analytics dashboard
- Advanced search and filtering
- Bulk operations for admin users
- Performance optimizations

### v1.2.* (Planned)  
- Multi-language support
- Advanced notification preferences
- Integration with more payment gateways
- Enhanced AI recommendations

### v2.*.* (Future)
- Mobile app API extensions
- Advanced reporting system
- Third-party platform integrations
- Enterprise features

---

## 🆘 Support

### Getting Help
- **Issues**: [GitHub Issues](https://github.com/khovan123/alignify-backend/issues)
- **Documentation**: [Project Wiki](https://github.com/khovan123/alignify-backend/wiki)
- **API Reference**: [Swagger UI](https://alignify-backend.onrender.com/swagger-ui)

### Contributing
1. Fork the repository
2. Create a feature branch
3. Follow the [detailed design guidelines](./DETAILED_DESIGN.md)
4. Submit a pull request with clear description

---

## 📄 License

MIT License © [khovan123](https://github.com/khovan123)

See [LICENSE](./LICENSE) file for details.

---

**🎉 Thank you for using Alignify Backend v1.0.0!**

For the latest updates and releases, watch our [GitHub repository](https://github.com/khovan123/alignify-backend).
