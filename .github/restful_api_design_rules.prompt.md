## 📏 Hướng Dẫn Thiết Kế RESTful API (Backend)

Thiết kế API cho backend của Alignify (sử dụng Java Spring Boot) phải tuân thủ các nguyên tắc RESTful để đảm bảo tính nhất quán, dễ sử dụng và khả năng mở rộng:

- **Công nghệ cốt lõi**:

  - Xây dựng API bằng **Java Spring Boot**.
  - Sử dụng **MongoDB** làm cơ sở dữ liệu NoSQL.
  - Tích hợp **Cloudinary** để quản lý và lưu trữ tài nguyên đa phương tiện.
  - Xử lý tuần tự hóa/giải tuần tự hóa JSON bằng **Jackson**.
  - Tận dụng **RapidAPI** cho các tích hợp API bên ngoài khi cần.

- **Nguyên tắc RESTful chung**:

  - Sử dụng các danh từ số nhiều cho các tài nguyên (ví dụ: `/api/campaigns`, `/api/users`, `/api/contents`).
  - Sử dụng các phương thức HTTP chuẩn (`GET`, `POST`, `PUT`, `DELETE`) cho các hoạt động CRUD tương ứng.

- **Quy ước Đặt Tên Endpoint**:

  - Sử dụng **snake_case** cho các endpoint path.
  - Ví dụ:
    - `POST /api/campaigns` - Tạo chiến dịch mới.
    - `GET /api/campaigns/{id}` - Lấy thông tin chi tiết một chiến dịch.
    - `GET /api/users/{userId}/campaigns` - Lấy tất cả chiến dịch của một người dùng.

- **Các Phương Thức HTTP**:

  - `GET`: Lấy dữ liệu hoặc danh sách tài nguyên.
  - `POST`: Tạo mới một tài nguyên.
  - `PUT`: Cập nhật toàn bộ một tài nguyên hiện có.
  - `PATCH`: Cập nhật một phần của tài nguyên hiện có.
  - `DELETE`: Xóa một tài nguyên.

- **Định Dạng Phản Hồi (Response Format)**:

  - Tất cả các response phải tuân theo định dạng JSON chuẩn hóa như sau:
    ```json
    {
      "status": 200, // Mã HTTP Status
      "message": "Chi tiết thông báo",
      "data": { ... }, // Dữ liệu trả về (có thể là null)
      "timestamp": "2025-07-13T05:36:55.000+07:00[Asia/Ho_Chi_Minh]",
      "path": "/api/your_endpoint"
    }
    ```
  - Sử dụng lớp `ApiResponse` trong Spring Boot để đảm bảo tính nhất quán:
    ```java
    public class ApiResponse {
        public static <T> ResponseEntity<?> sendSuccess(int code, String message, T data, String path) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", code);
            map.put("message", message);
            map.put("data", data);
            map.put("timestamp", ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            map.put("path", path);
            return ResponseEntity.status(code).body(map);
        }
        public static ResponseEntity<?> sendError(int code, String error, String path) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", code);
            map.put("error", error);
            map.put("timestamp", ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            map.put("path", path);
            return ResponseEntity.status(code).body(map);
        }
    }
    ```

- **Xử Lý Lỗi (Error Handling)**:

  - Sử dụng các mã lỗi HTTP chuẩn xác:
    - `400 Bad Request`: Yêu cầu không hợp lệ (ví dụ: dữ liệu thiếu, định dạng sai).
    - `401 Unauthorized`: Không có thông tin xác thực hoặc thông tin xác thực không hợp lệ.
    - `403 Forbidden`: Người dùng đã xác thực nhưng không có quyền truy cập tài nguyên.
    - `404 Not Found`: Tài nguyên không tồn tại.
    - `500 Internal Server Error`: Lỗi không mong muốn từ phía server.
  - Trả về thông báo lỗi chi tiết trong response JSON theo định dạng:
    ```json
    {
      "status": 400, // Mã HTTP Status
      "error": "Chi tiết lỗi",
      "timestamp": "2025-07-13T05:36:55.000+07:00[Asia/Ho_Chi_Minh]",
      "path": "/api/your_endpoint"
    }
    ```

- **Xác Thực (Authentication)**:

  - Sử dụng **JWT Token** cho tất cả các endpoint yêu cầu xác thực.
  - Các endpoint không yêu cầu xác thực bao gồm: `login`, `register`, và các endpoint công khai khác.

- **Phân Trang (Pagination)**:

  - Sử dụng query parameters để hỗ trợ phân trang cho các API trả về danh sách dữ liệu.
  - Ví dụ: `GET /api/campaigns?page=1&limit=10` (trang 1, 10 mục mỗi trang).

- **Phiên Bản Hóa (Versioning)**:

  - Sử dụng versioning trong URL để quản lý các thay đổi API trong tương lai.
  - Ví dụ: `GET /api/v1/campaigns`.

- **Giới Hạn Tỷ Lệ (Rate Limiting)**:
  - Áp dụng giới hạn số lượng request bằng **Spring Security** hoặc thông qua các tính năng của **RapidAPI** để bảo vệ API khỏi các cuộc tấn công DDoS và lạm dụng.

---

### 📞 Hướng Dẫn Tích Hợp WebSocket (STOMP) Backend

Dự án Alignify sử dụng WebSocket (qua SockJS và STOMP) để triển khai các tính năng thời gian thực như chat và thông báo.

- **Cấu hình WebSocket (`WebSocketConfig.java`)**:

  - Sử dụng `@EnableWebSocketMessageBroker` để bật xử lý tin nhắn WebSocket dựa trên STOMP.
  - **Message Broker**: Cấu hình simple broker `/topic` cho các tin nhắn công khai và tiền tố `/app` cho các tin nhắn được gửi đến các `@MessageMapping` trong controller.
  - **STOMP Endpoints**: Đăng ký endpoint `/ws` với SockJS và cho phép các Origin cụ thể (`https://alignify-rose.vercel.app`, `http://localhost:3000`).
  - **Channel Interceptor**: Triển khai `ChannelInterceptor` để xử lý xác thực **JWT Token** từ header `Authorization` cho các lệnh `CONNECT` và `SEND`.
    - Trích xuất token, giải mã để lấy `userId`.
    - Tìm kiếm `User` trong `UserRepository` và thiết lập `StompPrincipal` cho phiên WebSocket, chứa `userId`, `name`, `roleId`, và `avatarUrl`.
    - Xử lý các ngoại lệ bảo mật nếu token không hợp lệ hoặc người dùng không tồn tại.

- **Thiết kế Socket Endpoint (Controller)**:

  - Sử dụng `@Controller` và `@MessageMapping` để định nghĩa các endpoint WebSocket.
  - **Xác thực trong Controller**:
    - Luôn kiểm tra `Principal` để đảm bảo người dùng đã được xác thực qua WebSocket interceptor.
    - Sử dụng `instanceof StompPrincipal` để truy cập thông tin người dùng (userId, name, avatarUrl).
  - **Gửi tin nhắn (Sending Messages)**:
    - Sử dụng `SimpMessagingTemplate` (`@Autowired private SimpMessagingTemplate messagingTemplate;`) để gửi tin nhắn đến các topic hoặc user cụ thể.
    - Ví dụ: `messagingTemplate.convertAndSend("/topic/messages/" + roomId, new ChatMessageResponse(...));` để gửi tin nhắn chat đến một phòng chat cụ thể.
  - **Tuân thủ quy ước endpoint**: Các endpoint socket (ví dụ: `/app/chat/{roomId}` cho gửi tin nhắn) và các topic (ví dụ: `/topic/messages/{roomId}` cho nhận tin nhắn) phải tuân thủ như ví dụ được cung cấp.

- **Tích hợp Logic Nghiệp vụ (Service & Repository)**:
  - **Controller gọi Service**: Controller chỉ nên xử lý việc nhận request, xác thực cơ bản và chuyển giao cho tầng Service.
    - Ví dụ: `@PostMapping("/login")` gọi `authService.loginAccount(loginRequest, request)`.
  - **Service xử lý nghiệp vụ**: Tầng Service chứa logic nghiệp vụ phức tạp, tương tác với Repository để thao tác dữ liệu.
    - Sử dụng các phương thức được kế thừa từ `MongoRepository` (ví dụ: `findById`, `findByEmail`, `save`).
    - Tạo các phương thức `Query` tùy chỉnh trong Repository nếu các phương thức mặc định không đủ (ví dụ: `@Query("{ 'roleId': ?0, '_id': { $ne: ?1 } }")`).
    - Đảm bảo xử lý lỗi và trả về `ApiResponse.sendError` nhất quán từ tầng Service.
  - **Quản lý dữ liệu Chat/Notification**:
    - `ChatMessageRepository` và `ChatRoomRepository` được sử dụng để lưu trữ và truy xuất dữ liệu tin nhắn/phòng chat.
    - Cập nhật trạng thái `ChatRoom` khi có tin nhắn mới (ví dụ: `chatRoom.setCreatedAt`).

---

### 🧠 Hướng Dẫn Tích Hợp API Gemini cho Các Tính Năng Thông Minh

Để cung cấp các tính năng đề xuất và chatbot thông minh, Alignify sẽ tích hợp **Google Gemini API**.

- **Mục tiêu**:

  - **Chatbot đề xuất chiến dịch**: Xây dựng một chatbot có khả năng đề xuất các chiến dịch phù hợp dựa trên truy vấn hoặc hành vi của người dùng (Brand/Influencer).
  - **Đề xuất chiến dịch trên trang chủ**: Tự động gợi ý các chiến dịch liên quan khi người dùng truy cập trang chủ.
  - **Đề xuất Influencer cho Brand**: Khi Brand tạo lời mời hợp tác, hệ thống sẽ đề xuất các Influencer tiềm năng dựa trên tiêu chí chiến dịch.

- **Kiến trúc tích hợp**:

  - Tạo một **Service riêng biệt** (ví dụ: `GeminiAIService.java`) để xử lý các cuộc gọi đến Gemini API.
  - Service này sẽ chịu trách nhiệm định dạng dữ liệu đầu vào cho Gemini (prompts), gửi yêu cầu, và phân tích phản hồi.
  - Tùy thuộc vào yêu cầu, service có thể cần truy cập **database (MongoDB)** để lấy thông tin về chiến dịch, Influencer, Brand hoặc lịch sử tương tác của người dùng để làm ngữ cảnh cho các đề xuất.

- **Cách thức hoạt động**:

  - **Quản lý API Key**:

    - Sử dụng `@Value("${GOOGLE_API_KEY}") private String GOOGLE_API_KEY;` để inject API Key từ file cấu hình (ví dụ: `application.properties` hoặc biến môi trường). **Không bao giờ hardcode hoặc commit API Key trực tiếp vào mã nguồn.**
    - Khởi tạo Gemini client bằng API Key đã được inject:

      ```java
      import com.google.cloud.vertexai.api.GenerateContentResponse;
      import com.google.cloud.vertexai.generativeai.GenerativeModel; // Hoặc thư viện client tương ứng bạn đang dùng

      // ... trong service hoặc component cần dùng Gemini
      // Khởi tạo client
      Client client = Client.builder().apiKey(GOOGLE_API_KEY).build(); // Giả sử Client là lớp client của thư viện Gemini bạn đang dùng
      ```

  - **Gửi yêu cầu tới Gemini**:
    - Sử dụng client đã khởi tạo để gọi API Gemini.
    - Ví dụ để tạo nội dung:
      ```java
      // message ở đây là Content hoặc List<Content> theo định dạng của Gemini API
      GenerateContentResponse response = client.models.generateContent(
          "gemini-2.5-flash", // Chọn model phù hợp (ví dụ: gemini-2.5-flash, gemini-1.5-pro, v.v.)
          message,
          null // Tham số tùy chọn, ví dụ: generationConfig, safetySettings
      );
      // Xử lý phản hồi từ 'response'
      ```
  - **Đề xuất chiến dịch/Influencer**:
    - Backend sẽ xây dựng các prompts cho Gemini dựa trên dữ liệu người dùng, dữ liệu chiến dịch/Influencer từ MongoDB.
    - Ví dụ: "Hãy đề xuất 5 Influencer phù hợp với chiến dịch marketing về mỹ phẩm, đối tượng là gen Z, ngân sách 50 triệu VND. Dữ liệu Influencer: [list các Influencer có sẵn từ DB]".
    - Gemini sẽ phản hồi với các đề xuất, sau đó backend sẽ xử lý kết quả và trả về cho frontend.
  - **Chatbot**:
    - Frontend gửi truy vấn của người dùng đến backend.
    - Backend chuyển tiếp truy vấn này đến Gemini API, có thể kèm theo lịch sử hội thoại hoặc ngữ cảnh từ database.
    - Gemini phản hồi, và backend truyền phản hồi về cho frontend.

- **Lưu ý khi tích hợp**:
  - **Xử lý Rate Limit**: Triển khai cơ chế xử lý giới hạn tỷ lệ (rate limiting) của Gemini API để tránh bị chặn.
  - **Xử lý phản hồi**: Đảm bảo rằng backend có khả năng phân tích và xử lý các phản hồi từ Gemini API, bao gồm các trường hợp lỗi hoặc phản hồi không mong muốn.
  - **Tối ưu hóa Prompts**: Thiết kế các prompts hiệu quả và rõ ràng cho Gemini để đạt được kết quả đề xuất tốt nhất.

---
