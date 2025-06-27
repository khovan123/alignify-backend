package com.api.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "messages")
@CompoundIndex(name = "chatRoomId_sendAt_idx", def = "{'chatRoomId': 1, 'sendAt': -1}")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ChatMessage {

    @Id
    private String messageId;
    private String userId;
    private String name;
    private String chatRoomId;
    private String message;
    @CreatedDate
    private ZonedDateTime sendAt;
    private String tempId;
    private List<String> readBy = new ArrayList<>();

}
