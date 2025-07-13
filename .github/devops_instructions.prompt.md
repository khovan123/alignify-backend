## 🛠️ Hướng Dẫn DevOps

Quy trình DevOps của dự án Alignify sẽ được tự động hóa hoàn toàn bằng **GitHub Actions**, tuân thủ các nguyên tắc sau:

- **CI/CD Pipeline**:
  - Sử dụng **GitHub Actions** để thiết lập pipeline tự động với các giai đoạn:
    - **Build**: Kiểm tra và build ứng dụng ReactJS (frontend) và Spring Boot (backend).
    - **Test**: Chạy **unit test** và **integration test** cho cả frontend và backend.
    - **Deploy**: Triển khai ứng dụng lên môi trường `staging` hoặc `production` sau khi các bước trước thành công.
- **Branching Strategy**:
  - Sử dụng mô hình **Gitflow** để quản lý các nhánh code:
    - `main`: Chứa code đã được kiểm duyệt và sẵn sàng triển khai lên production.
    - `develop`: Chứa code đang trong quá trình phát triển và tích hợp.
    - `feat/*`: Dành cho việc phát triển các tính năng mới, mỗi tính năng có nhánh riêng.
    - `fix/*`: Dành cho việc sửa lỗi khẩn cấp trên production.
- **Code Quality**:
  - Thiết lập **ESLint** và **Prettier** để kiểm tra và định dạng code frontend tự động.
  - Sử dụng **SonarQube** để phân tích chất lượng code backend, phát hiện lỗi và lỗ hổng bảo mật.
- **Secrets Management**:
  - Lưu trữ tất cả các secrets nhạy cảm (như API keys, JWT secret, database credentials) trong **GitHub Secrets**.
  - Tuyệt đối **không commit** bất kỳ thông tin nhạy cảm nào trực tiếp vào repository.
- **Monitoring**:
  - Sử dụng **RapidAPI** để giám sát hiệu suất API backend, theo dõi các chỉ số quan trọng.
  - Thiết lập cảnh báo (alerts) khi pipeline thất bại, hiệu suất API giảm, hoặc có bất kỳ sự cố nào khác phát sinh.

---
