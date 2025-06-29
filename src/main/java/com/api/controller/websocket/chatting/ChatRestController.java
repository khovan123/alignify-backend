package com.api.controller.websocket.chatting;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.ApiResponse;
import com.api.dto.UserDTO;
import com.api.dto.response.ChatMessageResponse;
import com.api.dto.response.ChatRoomResponse;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.model.User;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/messages")
public class ChatRestController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getMessages(
            @PathVariable("roomId") String roomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        String userId = userDetails.getUserId();
        if (!chatRoomRepository.existsByChatRoomIdAndRoomOwnerIdOrMember(roomId, userId)) {
            throw new SecurityException("User not authorized for room: " + roomId);
        }
        Pageable pageable = PageRequest.of(page, size);
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderBySendAtAsc(roomId, pageable)
                .toList();
        List<ChatMessageResponse> chatMessageResposes = new ArrayList<>();
        chatMessages.forEach(msg -> {
            String avatarUrl = null;
            UserDTO userDTO;
            if (msg.getUserId().equals("#SYS")) {
                userDTO = new UserDTO(msg.getUserId(), "#SYS", avatarUrl);
            } else {
                User user = userRepository.findById(msg.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found: " + msg.getUserId()));
                avatarUrl = userRepository.findById(msg.getUserId())
                        .map(User::getAvatarUrl)
                        .orElse(null);

                userDTO = new UserDTO(msg.getUserId(), user.getName(), avatarUrl);
            }
            chatMessageResposes.add(new ChatMessageResponse(userDTO, msg));
        });
        return ApiResponse.sendSuccess(200, "Response successfully", chatMessageResposes, request.getRequestURI());
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getRoomIds(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        String userId = userDetails.getUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByRoomOwnerIdOrMember(userId, pageable).toList();
        List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        chatRooms.forEach(room -> {
            ChatMessage chatMessage = chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(room.getChatRoomId())
                    .orElse(null);
            if (chatMessage == null) {
                List<String> readBy = new ArrayList<>();
                ChatMessage newMsg = new ChatMessage();
                // readBy.add(userId);
                newMsg.setMessage(user.getName() + " đã vào phòng chat.");
                newMsg.setChatRoomId(room.getChatRoomId());
                newMsg.setName(user.getName());
                newMsg.setReadBy(readBy);
                newMsg.setUserId("#SYS");
                // newMsg.setUserId(user.getUserId());
                newMsg.setSendAt(ZonedDateTime.now());
                chatMessageRepository.save(newMsg);
                chatMessage = newMsg;
            }
            chatRoomResponses.add(new ChatRoomResponse(room, chatMessage));
        });
        return ApiResponse.sendSuccess(200, "Response successfully", chatRoomResponses, request.getRequestURI());
    }
}
