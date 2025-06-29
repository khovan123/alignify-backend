package com.api.controller.websocket.chatting;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.UserDTO;
import com.api.dto.response.ChatMessageResponse;
import com.api.dto.response.ChatRoomResponse;
import com.api.model.Campaign;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.model.User;
import com.api.repository.CampaignRepository;
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
    @Autowired
    private CampaignRepository campaignRepository;

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
        Pageable pageable = PageRequest.of(page, size);

        List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();

        if (userDetails.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
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

        List<ChatRoom> rooms = chatRoomRepository.findAllByRoomOwnerIdOrMemberOrderByCreatedAtDesc(userId, pageable)
                .getContent();
        rooms.forEach(room -> {
            ChatMessage chatMessage = ensureSystemMessage(room, userId);
            chatRoomResponses.add(new ChatRoomResponse(room, chatMessage));
        });

        return ApiResponse.sendSuccess(200, "Response successfully", chatRoomResponses, request.getRequestURI());
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
            chatMessage = chatMessageRepository.save(chatMessage);
        }
        return chatMessage;
    }

    // @GetMapping("/rooms")
    // public ResponseEntity<?> getRoomIds(
    // @RequestParam(value = "page", defaultValue = "0") int page,
    // @RequestParam(value = "size", defaultValue = "20") int size,
    // @AuthenticationPrincipal CustomUserDetails userDetails,
    // HttpServletRequest request) {
    // String userId = userDetails.getUserId();
    // Pageable pageable = PageRequest.of(page, size);

    // if (userDetails.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
    // if (campaignRepository.countByBrandIdAndStatusNot(userId) !=
    // chatRoomRepository
    // .countByRoomOwnerId(userId)) {
    // List<Campaign> campaigns = campaignRepository
    // .findAllByBrandIdAndStatusNotOrderByCreatedAtDesc(userId, "DRAFT", pageable)
    // .getContent();
    // campaigns.forEach(campaign -> {
    // Optional<ChatRoom> roomOpt =
    // chatRoomRepository.findById(campaign.getCampaignId());
    // ChatRoom room;
    // if (!roomOpt.isPresent()) {
    // ChatRoom chatRoom = new ChatRoom();
    // chatRoom.setChatRoomId(campaign.getCampaignId());
    // chatRoom.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
    // chatRoom.setRoomAvatarUrl(campaign.getImageUrl());
    // chatRoom.setRoomOwnerId(campaign.getBrandId());
    // chatRoom.setRoomName(campaign.getCampaignName());
    // chatRoom.setMembers(new ArrayList<>(Arrays.asList(campaign.getBrandId())));
    // room = chatRoomRepository.save(chatRoom);
    // } else {
    // room = roomOpt.get();
    // }
    // ChatMessage chatMessage =
    // chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(
    // campaign.getCampaignId())
    // .orElse(null);
    // if (chatMessage == null) {
    // User user = userRepository.findById(userId).get();
    // List<String> readBy = new ArrayList<>();
    // chatMessage = new ChatMessage();
    // readBy.add(userId);
    // chatMessage.setMessage(user.getName() + " đã vào phòng chat.");
    // chatMessage.setChatRoomId(room.getChatRoomId());
    // chatMessage.setName(user.getName());
    // chatMessage.setReadBy(readBy);
    // chatMessage.setUserId("#SYS");
    // chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
    // chatMessageRepository.save(chatMessage);
    // }
    // });
    // }
    // }
    // List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();
    // List<ChatRoom> rooms =
    // chatRoomRepository.findAllByRoomOwnerIdOrMemberOrderByCreatedAtDesc(userId,
    // pageable)
    // .getContent();
    // rooms.forEach(room -> {
    // ChatMessage chatMessage =
    // chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(
    // room.getChatRoomId())
    // .orElse(null);
    // if (chatMessage == null) {
    // User user = userRepository.findById(userId).get();
    // List<String> readBy = new ArrayList<>();
    // chatMessage = new ChatMessage();
    // readBy.add(userId);
    // chatMessage.setMessage(user.getName() + " đã vào phòng chat.");
    // chatMessage.setChatRoomId(room.getChatRoomId());
    // chatMessage.setName(user.getName());
    // chatMessage.setReadBy(readBy);
    // chatMessage.setUserId("#SYS");
    // chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
    // chatMessage = chatMessageRepository.save(chatMessage);
    // }
    // chatRoomResponses.add(new ChatRoomResponse(room, chatMessage));
    // });
    // return ApiResponse.sendSuccess(200, "Response successfully",
    // chatRoomResponses, request.getRequestURI());
    // }
}
