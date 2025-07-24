package com.api.controller.websocket.chatting;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.CommonPageRequest;
import com.api.dto.request.ReadMessagePayload;
import com.api.dto.response.ChatRoomResponse;
import com.api.dto.response.ReadStatusUpdate;
import com.api.model.Campaign;
import com.api.model.User;
import com.api.repository.CampaignRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.api.dto.UserDTO;
import com.api.dto.response.ChatMessageResponse;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.security.StompPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            @Payload ChatMessage chatMessage,
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
            if (chatMessage.getSendAt() == null) {
                chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            }
            chatMessage.setChatRoomId(roomId);
            chatMessage.setUserId(userId);
            ChatRoom chatRoom = roomOpt.get();
            chatRoom.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            chatMessageRepository.save(chatMessage);
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
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied at: " + roomId);
        }
        if (principal instanceof StompPrincipal stompPrincipal) {
            String userId = stompPrincipal.getUserId();
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
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    @MessageMapping("/rooms")
    public void getRooms(
            @Payload CommonPageRequest commonPageRequest,
            Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied");
        }
        if (principal instanceof StompPrincipal stompPrincipal) {

            String userId = stompPrincipal.getUserId();
            Pageable pageable = PageRequest.of(commonPageRequest.getPageNumber(), commonPageRequest.getPageSize());

            if (stompPrincipal.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
                if (campaignRepository.countByBrandIdAndStatusNot(userId, "DRAFT") != chatRoomRepository
                        .countByRoomOwnerId(userId)) {
                    List<Campaign> campaigns = campaignRepository
                            .findAllByBrandIdAndStatusNot(userId, "DRAFT");
                    campaigns.forEach(campaign -> {
                        Optional<ChatRoom> roomOpt = chatRoomRepository.findById(campaign.getCampaignId());
                        if (!roomOpt.isPresent()) {
                            ChatRoom chatRoom = new ChatRoom();
                            chatRoom.setChatRoomId(campaign.getCampaignId());
                            chatRoom.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                            chatRoom.setRoomAvatarUrl(campaign.getImageUrl());
                            chatRoom.setRoomOwnerId(campaign.getBrandId());
                            chatRoom.setRoomName(campaign.getCampaignName());
                            chatRoom.setMembers(new ArrayList<>(Arrays.asList(campaign.getBrandId())));
                            chatRoomRepository.save(chatRoom);
                        }
                    });
                }
            }
            List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();

            List<ChatRoom> rooms = chatRoomRepository.findAllByRoomOwnerIdOrMemberOrderByCreatedAtDesc(userId, pageable)
                    .getContent();
            rooms.forEach(room -> {
                ChatMessage chatMessage = ensureSystemMessage(room, userId);
                chatRoomResponses.add(new ChatRoomResponse(room, chatMessage));
            });

            messagingTemplate.convertAndSend(
                    "/topic/rooms/" + userId,
                    chatRoomResponses);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    private ChatMessage ensureSystemMessage(ChatRoom room, String userId) {
        ChatMessage chatMessage = chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(room.getChatRoomId())
                .orElse(null);
        if (chatMessage == null) {
            User user = userRepository.findById(userId).get();
            List<String> readBy = new ArrayList<>();
            chatMessage = new ChatMessage();
            readBy.add(userId);
            chatMessage.setMessage(user.getName() + " đã vào phòng chat.");
            chatMessage.setChatRoomId(room.getChatRoomId());
            chatMessage.setName(user.getName());
            chatMessage.setReadBy(readBy);
            chatMessage.setUserId("#SYS");
            chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            room.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            chatMessage = chatMessageRepository.save(chatMessage);
            chatRoomRepository.save(room);
        }
        return chatMessage;
    }
}
