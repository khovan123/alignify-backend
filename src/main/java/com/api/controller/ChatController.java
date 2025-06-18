package com.api.controller;

import com.api.dto.UserDTO;
import com.api.dto.response.ChatMessageResponse;
import com.api.model.ChatMessage;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.security.StompPrincipal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public ChatMessageResponse sendMessage(
            @Payload ChatMessage chatMessage,
            @DestinationVariable("roomId") String roomId,
            Principal principal
    ) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("User not authorized for room: " + roomId);
        }
        if (principal instanceof StompPrincipal stompPrincipal) {
            String userId = stompPrincipal.getUserId();
            if (!chatRoomRepository.existsByChatRoomIdAndRoomOwnerIdOrMember(roomId, userId)) {
                throw new SecurityException("User not authorized for room: " + roomId);
            }
            UserDTO userDTO = new UserDTO(stompPrincipal.getUserId(), stompPrincipal.getName(), stompPrincipal.getAvatarUrl());
            chatMessage.setName(stompPrincipal.getName());
            chatMessage.setSendAt(LocalDateTime.now());
            chatMessage.setChatRoomId(roomId);
            chatMessage.setUserId(userId);
            chatMessageRepository.save(chatMessage);
            return new ChatMessageResponse(userDTO, chatMessage);
        }
        throw new SecurityException("Invalid principal type");
    }

    @MessageMapping("/read/{roomId}")
    public void markMessageAsRead(
            @Payload ReadMessagePayload payload,
            @DestinationVariable("roomId") String roomId,
            Principal principal
    ) {
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
                    new ReadStatusUpdate(payload.getMessageId(), Collections.singletonList(userId))
            );
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
