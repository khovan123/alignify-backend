package com.api.controller.websocket.chatting;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.api.dto.UserDTO;
import com.api.dto.response.ChatMessageResponse;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.security.StompPrincipal;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    // @SendTo("/topic/messages/{roomId}")
    public void sendMessage(
            @Payload ChatMessage chatMessage,
            // @RequestParam(defaultValue = "0", name = "pageNumber") int pageNumber,
            // @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
            @DestinationVariable("roomId") String roomId,
            Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied at: " + roomId);
        }
        if (principal instanceof StompPrincipal stompPrincipal) {
            String userId = stompPrincipal.getUserId();
            Optional<ChatRoom> roomOpt = chatRoomRepository.findById(roomId);
            if (!roomOpt.isPresent()) {
                throw new SecurityException("Access is denied at: " + roomId);
            }
            UserDTO userDTO = new UserDTO(stompPrincipal.getUserId(), stompPrincipal.getName(),
                    stompPrincipal.getAvatarUrl());
            chatMessage.setName(stompPrincipal.getName());
            // chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            if (chatMessage.getSendAt() == null) {
                chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            }
            chatMessage.setChatRoomId(roomId);
            chatMessage.setUserId(userId);
            chatMessageRepository.save(chatMessage);
            ChatRoom chatRoom = roomOpt.get();
            chatRoom.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            chatRoomRepository.save(chatRoom);
            messagingTemplate.convertAndSend("/topic/messages/" + roomId,
                    new ChatMessageResponse(userDTO, chatMessage));

        }
        throw new SecurityException("Invalid principal type");
    }

    @MessageMapping("/read/{roomId}")
    public void markMessageAsRead(
            @Payload ReadMessagePayload payload,
            @DestinationVariable("roomId") String roomId,
            Principal principal) {
        String userId = principal.getName();
        if (!chatRoomRepository.existsByChatRoomIdAndRoomOwnerIdOrMember(roomId, userId)) {
            throw new SecurityException("User not authorized for room: " + roomId);
        }
        ChatMessage message = chatMessageRepository.findById(payload.getMessageId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Message not found: " + payload.getMessageId());
                });
        if (!message.getChatRoomId().equals(roomId)) {
            throw new SecurityException("Message does not belong to room: " + roomId);
        }
        if (!message.getReadBy().contains(userId)) {
            message.getReadBy().add(userId);
            chatMessageRepository.save(message);
            messagingTemplate.convertAndSend(
                    "/topic/read/" + roomId,
                    new ReadStatusUpdate(payload.getMessageId(), Collections.singletonList(userId)));
        }
    }
}

class ReadMessagePayload {

    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}

class ReadStatusUpdate {

    private String messageId;
    private List<String> readBy;

    public ReadStatusUpdate(String messageId, List<String> readBy) {
        this.messageId = messageId;
        this.readBy = readBy;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }
}
