package com.api.dto.response;

import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import java.time.ZonedDateTime;
import java.util.List;

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

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomAvatarUrl() {
        return roomAvatarUrl;
    }

    public void setRoomAvatarUrl(String roomAvatarUrl) {
        this.roomAvatarUrl = roomAvatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }

    public ZonedDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(ZonedDateTime sendAt) {
        this.sendAt = sendAt;
    }

}
