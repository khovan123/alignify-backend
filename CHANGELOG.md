# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-07-24

### 🚀 Initial Release

This is the first stable release of Alignify Backend - a comprehensive platform connecting social network influencers and brands.

### ✨ Features

#### Brand Management
- **Campaign Management**: Create, update, and manage marketing campaigns
- **Influencer Invitations**: Send direct collaboration invitations to influencers
- **Progress Review**: Review and approve influencer progress during collaborations
- **Campaign Status Management**: Handle campaign lifecycle (`DRAFT` → `RECRUITING` → `PENDING` → `PARTICIPATING` → `COMPLETED`)

#### Influencer Features
- **Content Creation**: Post content and ideas to showcase creative work
- **Campaign Applications**: Apply to participate in brand campaigns
- **Invitation Management**: Accept or decline direct brand invitations
- **Progress Updates**: Update and track collaboration progress

#### Admin Capabilities
- **User Management**: Moderate users, handle bans and permission restrictions
- **Content Moderation**: Manage and delete posts and campaigns
- **Report Handling**: Process user reports and complaints
- **Upgrade Packages**: Create and manage account upgrade packages

#### Communication & Interaction
- **Real-time Chat**: WebSocket-based group chat for brand-influencer collaboration
- **Social Interactions**: Like and comment system for influencer posts
- **Notifications**: Real-time notification system

#### Payment Integration
- **PayOS Integration**: Vietnamese payment gateway support
- **PayPal Integration**: International payment processing
- **QR Code Generation**: Payment QR codes for transactions

### 🛠️ Technical Features

#### Core Technologies
- **Java 21** with **Spring Boot 3.4.5**
- **MongoDB** for flexible data storage
- **JWT Authentication** for secure user sessions
- **Spring Security** for comprehensive security
- **WebSocket (STOMP)** for real-time features

#### Third-Party Integrations
- **Cloudinary**: Media storage and management
- **Gmail SMTP**: Email notifications
- **Google OAuth**: Social authentication
- **Gemini AI**: AI-powered features and recommendations
- **RapidAPI**: External API integrations

#### API & Documentation
- **RESTful API** design with consistent response format
- **Swagger UI** for comprehensive API documentation
- **Versioned APIs** at `/api/v1/`
- **Rate Limiting** and security best practices

#### Infrastructure
- **Docker** support for containerized deployment
- **Maven** build system
- **Environment-based** configuration
- **Production-ready** deployment on Render.com

### 🔧 System Requirements
- Java 21 or higher
- MongoDB 4.4 or higher
- Maven 3.6 or higher

### 🌐 Deployment
- **Backend**: [https://alignify-backend.onrender.com](https://alignify-backend.onrender.com)
- **API Documentation**: [https://alignify-backend.onrender.com/swagger-ui](https://alignify-backend.onrender.com/swagger-ui)
- **Frontend**: [https://alignify-rose.vercel.app](https://alignify-rose.vercel.app)

### 📋 Release Notes
- This is the foundational release providing all core functionality for the Alignify platform
- Comprehensive API coverage for all user roles (Admin, Brand, Influencer)
- Production-ready deployment with proper security and monitoring
- Full documentation and development guidelines included

[1.0.0]: https://github.com/khovan123/alignify-backend/releases/tag/v1.0.0