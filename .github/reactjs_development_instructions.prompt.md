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
    export const isApiResponseError = (err: unknown): err is ApiResponseError => {
      return (
        typeof err === 'object' &&
        err !== null &&
        'status' in err &&
        'data' in err &&
        typeof err.data === 'object' &&
        err.data !== null &&
        'status' in err.data &&
        'error' in err.data &&
        'timestamp' in err.data &&
        'path' in err.data
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
    import SockJS from 'sockjs-client';
    import Stomp from 'stompjs';

    import { host } from '@/config';

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
          },
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
    import { useCallback } from 'react';
    import { useAppDispatch, useAppSelector } from '@/hooks/redux';
    import { getStompClient } from '@/lib/stom-client';
    import type { RootState } from '@/redux/store';
    import { setNotificationSending } from '../features/notification/notification.slice';
    import type { NotificationSending } from '../features/notification/notification.type';

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
              JSON.stringify(notification),
            );
            dispatch(setNotificationSending(notification));
          }
        },
        [dispatch, token],
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
