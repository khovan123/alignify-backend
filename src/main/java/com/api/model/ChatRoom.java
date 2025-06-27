package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatRooms")
public class ChatRoom {

    @Id
    private String chatRoomId;
    private String roomName;
    private String roomAvatarUrl;
    private String roomOwnerId;
    private List<String> members;
    private ZonedDateTime createdAt;

    public ChatRoom() {
        this.members = new ArrayList<>();
    }

    public ChatRoom(String chatRoomId, String roomOwnerId, String roomName, String roomAvatarUrl) {
        this.chatRoomId = chatRoomId;
        this.roomOwnerId = roomOwnerId;
        this.roomName = roomName;
        this.roomAvatarUrl = roomAvatarUrl;
        this.members = new ArrayList<>();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public ChatRoom(String chatRoomId, String roomName, String roomAvatarUrl, String roomOwnerId, List<String> members,
            ZonedDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.roomName = roomName;
        this.roomAvatarUrl = roomAvatarUrl;
        this.roomOwnerId = roomOwnerId;
        this.members = members;
        this.createdAt = createdAt;
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

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getRoomOwnerId() {
        return roomOwnerId;
    }

    public void setRoomOwnerId(String roomOwnerId) {
        this.roomOwnerId = roomOwnerId;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
