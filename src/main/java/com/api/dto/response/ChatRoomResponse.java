package com.api.dto.response;

import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class ChatRoomResponse {

    private String chatRoomId;
    private String roomName;
    private String roomAvatarUrl;
    private String name;
    private String lastMessage;
    private List<String> readBy;
    private ZonedDateTime sendAt;

    public ChatRoomResponse(ChatRoom chatRoom, ChatMessage chatMessage) {
        this.roomAvatarUrl = chatRoom.getRoomAvatarUrl();
        this.chatRoomId = chatRoom.getChatRoomId();
        this.roomName = chatRoom.getRoomName();
        this.name = chatMessage.getName();
        this.lastMessage = chatMessage.getMessage();
        this.readBy = chatMessage.getReadBy();
        this.sendAt = chatMessage.getSendAt();
    }
}
