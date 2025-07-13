## 🚀 Hướng Dẫn Chung cho GitHub Copilot

Là một **Expert Software Engineer** chịu trách nhiệm cho dự án **Alignify**, tôi mong muốn mọi đóng góp code và quy trình làm việc phải luôn tuân thủ nghiêm ngặt **file detailed design** của dự án. Mục tiêu là phát triển Alignify với chất lượng cao, tuân thủ các nguyên tắc thiết kế tốt nhất và được triển khai hiệu quả. Copilot cần hiểu rõ ngữ cảnh dự án Alignify, các chức năng chính như quản lý chiến dịch, đăng bài nội dung, hệ thống chat, và các vai trò (Brand, Influencer, Admin).

---

### 🌐 Tổng Quan Dự Án Alignify

Alignify là một nền tảng hỗ trợ kết nối và quản lý quá trình hợp tác giữa **Influencer** và **Brand**. Dự án có các chức năng chính như sau:

- **Chức năng cho Brand**:

  - **Đăng bài chiến dịch**: Brand có thể tạo và đăng các chiến dịch quảng bá.
  - **Quản lý trạng thái chiến dịch**: Brand có thể quản lý luồng trạng thái của chiến dịch: `DRAFT` -> `RECRUITING` -> `PENDING` -> `PARTICIPATING` -> `COMPLETED`.
  - **Gửi lời mời trực tiếp**: Brand có thể gửi lời mời hợp tác trực tiếp đến Influencer.
  - **Review tiến độ**: Brand có thể xem xét và xử lý (chấp nhận hoặc từ chối) tiến độ mà Influencer cập nhật trong giai đoạn `PARTICIPATING`.

- **Chức năng cho Influencer**:

  - **Đăng bài content/idea**: Influencer có thể chia sẻ các nội dung hoặc ý tưởng của mình.
  - **Ứng tuyển chiến dịch**: Influencer có thể nộp đơn vào các chiến dịch mà Brand đã đăng ở trạng thái `RECRUITING`.
  - **Chấp nhận/Từ chối lời mời**: Influencer có quyền chấp nhận hoặc từ chối lời mời trực tiếp từ Brand.
  - **Cập nhật tiến độ**: Influencer sẽ cập nhật tiến độ công việc trong giai đoạn `PARTICIPATING` của chiến dịch.

- **Chức năng chung**:

  - **Hệ thống chat**: Brand và Influencer có thể chat nhóm với nhau sau khi hợp tác.
  - **Tương tác bài viết**: Các bài viết content/idea của Influencer có thể được like hoặc comment từ bất kỳ người dùng nào.

- **Chức năng cho Admin**:
  - **Quản lý bài đăng**: Admin có quyền quản lý và xóa các bài đăng content/idea cũng như các chiến dịch.
  - **Quản lý người dùng**: Admin có thể cấm (ban) hoặc hạn chế quyền (restrict permission) của người dùng.
  - **Tiếp nhận báo cáo**: Admin nhận và xử lý các báo cáo/report từ người dùng.
  - **Tạo gói nâng cấp**: Admin có thể tạo các gói nâng cấp tài khoản cho người dùng.

Copilot cần hiểu rõ các vai trò (`ADMIN`, `BRAND`, `INFLUENCER`) và quyền hạn tương ứng của họ trong hệ thống.

---

### 📚 Các Hướng Dẫn Chi Tiết Khác

Để có được sự hỗ trợ chính xác nhất, Copilot hãy tham khảo thêm các file hướng dẫn chuyên biệt sau:

- **Phát triển Frontend (ReactJS)**: Để biết chi tiết về cấu trúc component, quản lý trạng thái, routing, UI/UX và testing, hãy tham khảo file:
  `./reactjs_development_instructions.prompt.md`

- **DevOps (GitHub Actions)**: Để hiểu rõ về quy trình CI/CD, chiến lược nhánh, kiểm soát chất lượng code và quản lý secrets, hãy tham khảo file:
  `./devops_instructions.prompt.md`

- **Thiết kế RESTful API (Backend)**: Để nắm vững các quy tắc đặt tên endpoint, phương thức HTTP, định dạng response, xử lý lỗi, xác thực, phân trang và versioning, hãy tham khảo file:
  `./restful_api_design_rules.prompt.md`

---

### 📚 Nếu Không Truy Cập Được Các Chi Tiết Ở File Hướng Dẫn Khác Hãy Đọc Tại Đây

## 💻 Hướng Dẫn Phát Triển Frontend (ReactJS)

Đối với phát triển frontend của Alignify, hãy tuân thủ các nguyên tắc và công nghệ sau:

- **Công nghệ cốt lõi**:

  - Sử dụng **ReactJS 18** làm thư viện UI chính.
  - Sử dụng **Vite** cho quá trình build nhanh và trải nghiệm phát triển mượt mà.
  - Bắt buộc sử dụng **TypeScript** để đảm bảo kiểu dữ liệu an toàn và cải thiện khả năng bảo trì code.

- **Cấu trúc Component**:

  - Sử dụng **Shadcn UI** để xây dựng các thành phần giao diện (UI components) theo thiết kế chi tiết.
  - Tách biệt các thành phần UI thành các thư mục riêng biệt theo chức năng: `components`, `features`, `pages`.
  - Sử dụng **TypeScript** để định nghĩa kiểu dữ liệu rõ ràng cho tất cả các props và state.

- **Quản lý Trạng Thái**:

  - Triển khai quản lý trạng thái toàn cục bằng **Redux Toolkit**.
  - Sử dụng **RTK Query** để gọi API và quản lý cache dữ liệu hiệu quả.
  - Áp dụng **Redux Persist** để lưu trữ trạng thái người dùng (ví dụ: thông tin đăng nhập, token) trong `localStorage`.

- **Routing**:

  - Sử dụng **React Router** để quản lý điều hướng giữa các trang.
  - Áp dụng bảo mật cho các route bằng cách kiểm tra **JWT Token** trước khi cho phép truy cập vào các trang yêu cầu xác thực.

- **UI/UX**:

  - Sử dụng **Tailwind CSS 4** để xây dựng giao diện nhanh chóng và tuân thủ thiết kế chi tiết.
  - Tối ưu hóa giao diện cho cả desktop và mobile (**responsive design**).

- **Testing**:
  - Viết **unit test** cho các component quan trọng bằng **React Testing Library**.
  - Đảm bảo các API call được mock và kiểm tra bằng **MSW (Mock Service Worker)** để cô lập các test và đảm bảo tính tin cậy.

---

### 📝 Hướng Dẫn Chi Tiết cho Form Components

Khi tạo một component form mới, hãy tuân thủ các yêu cầu sau:

- **Quản lý trạng thái Form với `react-hook-form`**:

  - Luôn định nghĩa **TypeScript types** rõ ràng cho dữ liệu của form.
  - Ưu tiên sử dụng các **uncontrolled components** thông qua hàm `register` để quản lý input.
  - Sử dụng thuộc tính `defaultValues` trong `useForm` để ngăn chặn các lần re-render không cần thiết và khởi tạo giá trị mặc định cho form.

- **Xác thực Form với `zod` và `zodResolver`**:

  - Sử dụng thư viện **Zod** để định nghĩa các schema xác thực.
  - Tạo các **validation schemas** có thể tái sử dụng và đặt chúng trong các file riêng biệt (ví dụ: `src/lib/validation/schemas.ts`).
  - Sử dụng **TypeScript types** được suy ra từ Zod schemas để đảm bảo an toàn kiểu dữ liệu giữa form data và validation.
  - Tùy chỉnh các quy tắc xác thực để cung cấp trải nghiệm người dùng (UX) thân thiện, với các thông báo lỗi rõ ràng.

- **Thành phần UI và Icon**:
  - Sử dụng các thành phần UI có sẵn từ thư mục `src/components/ui`, bao gồm:
    - **`FormField`**: Để tích hợp các input của form với `react-hook-form` và hiển thị thông báo lỗi.
    - **`Input`**, **`Textarea`**, **`Select`**, v.v., và các component form control khác để xây dựng các trường nhập liệu.
    - **`useFieldArray`**: Khi cần quản lý các danh sách trường động (dynamic field arrays) trong form.
  - Sử dụng các Icon có sẵn từ thư mục `src/components/icons`.

---

### 📡 Hướng Dẫn Tương Tác API & Định Dạng Dữ Liệu

- **Định dạng Response từ RTK Query**:
  - Các response thành công mà frontend nhận được từ RTK Query **luôn phải** tuân thủ cấu trúc TypeScript sau:
    ```typescript
    export interface ApiResponseSuccess<T> {
      status: number | string;
      message: string;
      data: T | null;
      timestamp: Date;
      path: string;
    }
    ```
- **Xử lý Lỗi API**:

  - Khi gặp lỗi trong quá trình `try-catch` hoặc từ các phản hồi lỗi API, dữ liệu lỗi phải được chuyển đổi về cấu trúc TypeScript sau:

    ```typescript
    export interface ErrorData {
      status: number | string;
      error: string;
      timestamp: Date;
      path: string;
    }

    export interface ApiResponseError {
      status: number | string; // Mã HTTP status lỗi
      data: ErrorData; // Chi tiết lỗi
    }
    ```

  - Sử dụng hàm kiểm tra kiểu `isApiResponseError` được định nghĩa trong `src/utils` để xác định loại lỗi:
    ```typescript
    // src/utils/index.ts (hoặc file tương tự)
    export const isApiResponseError = (
      err: unknown
    ): err is ApiResponseError => {
      return (
        typeof err === "object" &&
        err !== null &&
        "status" in err &&
        "data" in err &&
        typeof err.data === "object" &&
        err.data !== null &&
        "status" in err.data &&
        "error" in err.data &&
        "timestamp" in err.data &&
        "path" in err.data
      );
    };
    ```

---

### 💡 Hướng Dẫn Tiện Ích & Helpers

- **Định dạng Dữ liệu**:
  - Đối với các tác vụ định dạng dữ liệu như ngày tháng (`Date`), tiền tệ (`currency`), v.v., **luôn sử dụng các hàm format** được cung cấp trong thư mục `src/utils`. Điều này đảm bảo tính nhất quán trong toàn bộ ứng dụng.

---

### 💬 Hướng Dẫn Tích Hợp WebSocket (STOMP)

Dự án sử dụng WebSocket (qua SockJS và STOMP) để triển khai các tính năng thời gian thực như hệ thống chat và thông báo.

- **Quản lý Kết nối STOMP**:

  - Sử dụng hàm `getStompClient` từ `src/lib/stom-client.ts` để lấy hoặc thiết lập kết nối STOMP client:

    ```typescript
    // src/lib/stom-client.ts
    import SockJS from "sockjs-client";
    import Stomp from "stompjs";

    import { host } from "@/config";

    let stompClient: Stomp.Client | null = null;
    let isConnected = false;
    let connectPromise: Promise<Stomp.Client> | null = null;

    export function getStompClient(token: string): Promise<Stomp.Client> {
      if (stompClient && isConnected) {
        return Promise.resolve(stompClient);
      }
      if (connectPromise) return connectPromise;

      connectPromise = new Promise((resolve, reject) => {
        const socket = new SockJS(`${host}/ws`);
        const client = Stomp.over(socket);
        client.debug = () => {}; // Tắt debug log của Stomp
        client.connect(
          { Authorization: `Bearer ${token}` }, // Gửi JWT Token để xác thực
          () => {
            stompClient = client;
            isConnected = true;
            resolve(client);
          },
          (error) => {
            isConnected = false;
            connectPromise = null;
            reject(error);
          }
        );
      });
      return connectPromise;
    }
    ```

  - Đảm bảo truyền **JWT Token** để xác thực kết nối WebSocket.

- **Gửi Thông báo (Notifications)**:

  - Sử dụng custom hook `useSendNotification` từ `src/hooks` để gửi thông báo đến người dùng khác:

    ```typescript
    // src/hooks/useSendNotification.ts
    import { useCallback } from "react";
    import { useAppDispatch, useAppSelector } from "@/hooks/redux";
    import { getStompClient } from "@/lib/stom-client";
    import type { RootState } from "@/redux/store";
    import { setNotificationSending } from "../features/notification/notification.slice";
    import type { NotificationSending } from "../features/notification/notification.type";

    export function useSendNotification() {
      const dispatch = useAppDispatch();
      const { token } = useAppSelector((state: RootState) => state.auth);

      const sendNotification = useCallback(
        async (notification: NotificationSending) => {
          if (!notification.userId || !token) return;
          const stompClient = await getStompClient(token);
          if (stompClient) {
            stompClient.send(
              `/app/notify/${notification.userId}`, // Endpoint gửi notification
              { Authorization: `Bearer ${token}` },
              JSON.stringify(notification)
            );
            dispatch(setNotificationSending(notification));
          }
        },
        [dispatch, token]
      );
      return sendNotification;
    }
    ```

  - Đường dẫn gửi notification là `/app/notify/{userId}`.

- **Gửi và Nhận Tin nhắn Chat**:
  - Trong các component chat (ví dụ: `ChatRoom`), sử dụng `getStompClient` để subscribe và gửi tin nhắn.
  - Subscribe đến topic `/topic/messages/{chatRoomId}` để nhận tin nhắn mới.
  - Gửi tin nhắn đến `/app/chat/{chatRoomId}`.
  - Luôn bao gồm **JWT Token** trong header `Authorization` khi gửi tin nhắn.
  - Xử lý các tin nhắn nhận được, cập nhật trạng thái tin nhắn (ví dụ: `isSending: false` sau khi nhận phản hồi từ server).
  - Đảm bảo xử lý lỗi khi parsing tin nhắn nhận được từ WebSocket.
  - Ví dụ về cách sử dụng (tham khảo đoạn code đã cung cấp): `useEffect` để subscribe, `sendMessage` để gửi tin.

---

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
