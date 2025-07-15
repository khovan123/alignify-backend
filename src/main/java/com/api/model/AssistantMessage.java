package com.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assistantMessages")
public class AssistantMessage {
    @Id
    private String id;
    private String roomId;
    private String senderId;
    private SenderType senderType;
    private MessageType messageType;
    private String content;
    private ZonedDateTime createdAt;

    public enum SenderType {
        USER,
        ASSISTANT
    }

    public enum MessageType {
        TEXT,
        CAMPAIGN_RECOMMENDATIONS
    }
}