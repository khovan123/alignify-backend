
--- 

# 🚀 Alignify Backend

> **EN:**  
> Alignify is a management platform connecting social network influencers and brands, providing robust APIs for campaign, content, chat, and admin management.
>
> **VI:**  
> Alignify là nền tảng quản lý kết nối Influencer và Brand trên mạng xã hội, cung cấp API mạnh mẽ cho quản lý chiến dịch, nội dung, chat và admin.

---

## ✨ Features

- **Brand:** Launch & manage campaigns, send invitations, review influencer progress.
- **Influencer:** Create content, apply to campaigns, update progress, interact.
- **Admin:** Moderate content/users, handle reports, create upgrade packages.
- **Group chat:** Brands & Influencers chat during collaborations.
- **Interactions:** Like, comment on influencer posts.

---

## 🛠️ Tech Stack

|            |                |
|------------|----------------|
| ☕ Java 21+ | 🧑‍💻 Spring Boot 3.x |
| 🍃 MongoDB  | 📨 Gmail SMTP       |
| ☁️ Cloudinary | 💳 PAYOS, PayPal  |
| 📦 Docker   | 🔑 JWT Auth         |
| 📝 Swagger UI |                  |

---

## 🚦 Getting Started

### 1️⃣ Clone & Build

```sh
git clone https://github.com/khovan123/alignify-backend.git
cd alignify-backend
./mvnw clean install
```

### 2️⃣ Configure Environment Variables

Create a `.env` file or set the following environment variables:

```properties
API_SECRET_KEY=...
MONGODB_URI=...
MAIL_USERNAME=...
MAIL_PASSWORD=...
CLOUDINARY_CLOUD_NAME=...
CLOUDINARY_API_KEY=...
CLOUDINARY_API_SECRET=...
GOOGLE_CLIENT_ID=...
GOOGLE_SECRET_KEY=...
GEMINI_API_KEY=...
RAPIDAPI_KEY=...
PAYOS_CLIENT_ID=...
PAYOS_API_KEY=...
PAYOS_CHECKSUM_KEY=...
PAYPAL_CLIENT_ID=...
PAYPAL_CLIENT_SECRET=...
```

### 3️⃣ Run Locally

```sh
./mvnw spring-boot:run
```

Access Swagger UI for API documentation:  
[http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)  
Or online Swagger UI:  
[https://alignify-backend.onrender.com/swagger-ui](https://alignify-backend.onrender.com/swagger-ui)

### 4️⃣ Run with Docker

```sh
docker build -t alignify-backend .
docker run -d --env-file .env -p 8080:8080 alignify-backend
```

### 5️⃣ Live Frontend

Access the live web app:  
[https://alignify-rose.vercel.app](https://alignify-rose.vercel.app)

---

## 📚 Roles & Permissions

| 🛡️ Admin                 | 🎯 Brand                   | ⭐ Influencer                  |
|--------------------------|---------------------------|-------------------------------|
| Manage/ban users         | Create/manage campaigns   | Create post, apply campaign   |
| Delete content/campaign  | Invite influencer, review progress | Accept/decline invites     |
| Handle reports           | Track campaign progress   | Update progress               |
| Create upgrade packages  |                           |                               |

---

## 📦 Release Information

**Current Version**: `v1.3.1` (July 24, 2025)  
**Release Type**: Initial Stable Release  
**Compatibility**: Production Ready  

### 📋 Quick Links
- **📖 [Release Notes](./RELEASE.md)** - Detailed release information and migration guide
- **📝 [Changelog](./CHANGELOG.md)** - Complete version history and changes
- **🏷️ [Versioning Guide](./VERSIONING.md)** - Semantic versioning and tagging conventions
- **📚 [Documentation Index](./DOCUMENTATION_INDEX.md)** - Complete documentation overview
- **🔗 [GitHub Releases](https://github.com/khovan123/alignify-backend/releases)** - Download and release history
- **🚀 [Release Scripts](./scripts/README.md)** - Automated release tools

### 🏷️ Version Tags
This project follows [Semantic Versioning](https://semver.org/):
- **Production**: `v1.0.0`, `v1.1.0`, `v2.0.0`
- **Pre-release**: `v1.1.0-alpha.1`, `v1.1.0-beta.1`, `v1.1.0-rc.1`
- All tags are prefixed with `v` following best practices

---

## 🔗 API Overview

- RESTful & JWT Auth, versioned at `/api/v1/`
- Swagger UI: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui) or [https://alignify-backend.onrender.com/swagger-ui](https://alignify-backend.onrender.com/swagger-ui)
- Main endpoints: `/api/campaigns`, `/api/users`, `/api/posts`, `/api/auth`, `/api/reports`
- See [detailed design](./DETAILED_DESIGN.md) and [API rules](.github/restful_api_design_rules.prompt.md) for more information.

---

## 🌟 Contribution Guide

1. **Fork & create a new branch.**
2. **Follow the [DETAILED_DESIGN.md](./DETAILED_DESIGN.md) file (required!).**
3. **Pull Request:** Provide a clear description and link related tasks/discussions.
4. **Update API docs** if API structure changes.
5. **Thorough code review before merging.**

---

## 📜 License

MIT © [khovan123](https://github.com/khovan123)

---

> **EN:**  
> All changes must strictly follow the detailed design file and project conventions.

---

### 🎉 Welcome to Alignify — The modern platform for brand & influencer collaboration!

---

# 🚀 Alignify Backend (Tiếng Việt)

> **VI:**  
> Alignify là nền tảng quản lý kết nối Influencer và Brand trên mạng xã hội, cung cấp API mạnh mẽ cho quản lý chiến dịch, nội dung, chat và admin.

---

## ✨ Tính Năng

- **Brand:** Đăng & quản lý chiến dịch, gửi lời mời, duyệt tiến độ influencer.
- **Influencer:** Đăng bài, ứng tuyển chiến dịch, cập nhật tiến độ, tương tác.
- **Admin:** Quản lý bài đăng/người dùng, xử lý báo cáo, tạo gói nâng cấp.
- **Chat nhóm:** Brand & Influencer chat trao đổi khi hợp tác.
- **Tương tác:** Like, comment trên bài content/idea của influencer.

---

## 🛠️ Công Nghệ

|            |                    |
|------------|--------------------|
| ☕ Java 21+ | 🧑‍💻 Spring Boot 3.x |
| 🍃 MongoDB  | 📨 Gmail SMTP         |
| ☁️ Cloudinary | 💳 PAYOS, PayPal  |
| 📦 Docker   | 🔑 JWT Auth           |
| 📝 Swagger UI |                    |

---

## 🚦 Bắt Đầu Nhanh

### 1️⃣ Clone & Build

```sh
git clone https://github.com/khovan123/alignify-backend.git
cd alignify-backend
./mvnw clean install
```

### 2️⃣ Cấu Hình Biến Môi Trường

Tạo file `.env` hoặc đặt các biến môi trường như sau:

```properties
API_SECRET_KEY=...
MONGODB_URI=...
MAIL_USERNAME=...
MAIL_PASSWORD=...
CLOUDINARY_CLOUD_NAME=...
CLOUDINARY_API_KEY=...
CLOUDINARY_API_SECRET=...
GOOGLE_CLIENT_ID=...
GOOGLE_SECRET_KEY=...
GEMINI_API_KEY=...
RAPIDAPI_KEY=...
PAYOS_CLIENT_ID=...
PAYOS_API_KEY=...
PAYOS_CHECKSUM_KEY=...
PAYPAL_CLIENT_ID=...
PAYPAL_CLIENT_SECRET=...
```

### 3️⃣ Chạy Local

```sh
./mvnw spring-boot:run
```

Truy cập tài liệu API tại Swagger UI:  
[http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)  
Hoặc xem Swagger UI online:  
[https://alignify-backend.onrender.com/swagger-ui](https://alignify-backend.onrender.com/swagger-ui)

### 4️⃣ Chạy bằng Docker

```sh
docker build -t alignify-backend .
docker run -d --env-file .env -p 8080:8080 alignify-backend
```

### 5️⃣ Truy Cập Website

Truy cập web live tại:  
[https://alignify-rose.vercel.app](https://alignify-rose.vercel.app)

---

## 📚 Phân Quyền & Vai Trò

| 🛡️ Admin                | 🎯 Brand                        | ⭐ Influencer               |
|-------------------------|----------------------------------|----------------------------|
| Quản lý/cấm user        | Đăng & quản lý chiến dịch        | Đăng bài, ứng tuyển        |
| Xoá bài, chiến dịch     | Mời influencer, duyệt tiến độ    | Nhận/từ chối lời mời       |
| Xử lý báo cáo           | Quản lý tiến độ                  | Cập nhật tiến độ           |
| Tạo gói nâng cấp        |                                  |                            |

---

## 📦 Thông Tin Phiên Bản

**Phiên Bản Hiện Tại**: `v1.3.1` (24 tháng 7, 2025)  
**Loại Phiên Bản**: Phiên Bản Ổn Định Đầu Tiên  
**Tương Thích**: Sẵn Sàng Production  

### 📋 Liên Kết Nhanh
- **📖 [Ghi Chú Phiên Bản](./RELEASE.md)** - Thông tin chi tiết về phiên bản và hướng dẫn migration
- **📝 [Nhật Ký Thay Đổi](./CHANGELOG.md)** - Lịch sử phiên bản và thay đổi hoàn chình
- **🏷️ [Hướng Dẫn Versioning](./VERSIONING.md)** - Quy tắc semantic versioning và tagging
- **📚 [Chỉ Mục Tài Liệu](./DOCUMENTATION_INDEX.md)** - Tổng quan tài liệu hoàn chình
- **🔗 [GitHub Releases](https://github.com/khovan123/alignify-backend/releases)** - Tải xuống và lịch sử phiên bản
- **🚀 [Scripts Phát Hành](./scripts/README.md)** - Công cụ release tự động

### 🏷️ Tag Phiên Bản
Dự án tuân thủ [Semantic Versioning](https://semver.org/):
- **Production**: `v1.0.0`, `v1.1.0`, `v2.0.0`
- **Pre-release**: `v1.1.0-alpha.1`, `v1.1.0-beta.1`, `v1.1.0-rc.1`
- Tất cả tag đều có tiền tố `v` theo best practices

---

## 🔗 Tổng Quan API

- RESTful & JWT Auth, version `/api/v1/`
- Swagger UI: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui) hoặc [https://alignify-backend.onrender.com/swagger-ui](https://alignify-backend.onrender.com/swagger-ui)
- Các endpoint chính: `/api/campaigns`, `/api/users`, `/api/posts`, `/api/auth`, `/api/reports`
- Xem thêm tại [DETAILED_DESIGN.md](./DETAILED_DESIGN.md) và [API rules](./restful_api_design_rules.prompt.md).

---

## 🌟 Đóng Góp

1. **Fork & tạo branch mới.**
2. **Tuân thủ file [DETAILED_DESIGN.md](./DETAILED_DESIGN.md) (bắt buộc!).**
3. **Pull Request:** Mô tả rõ ràng, dẫn nguồn task/thảo luận liên quan.
4. **Update tài liệu API** nếu thay đổi cấu trúc API.
5. **Code review kỹ lưỡng trước khi merge.**

---

## 📜 Giấy Phép

MIT © [khovan123](https://github.com/khovan123)

---

> **VI:**  
> Mọi thay đổi phải tuân thủ nghiêm ngặt file thiết kế chi tiết & quy tắc dự án!

---

### 🎉 Chào mừng đến với Alignify — Nền tảng kết nối thương hiệu & người ảnh hưởng hiện đại!

---
