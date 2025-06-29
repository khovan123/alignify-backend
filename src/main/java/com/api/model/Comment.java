package com.api.model;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document("comments")
@CompoundIndex(name = "contentId_sendAt_idx", def = "{'contentId': 1, 'createdDate': -1}")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Comment {

    @Id
    private String commentId;
    private String userId;
    private String contentId;
    private String content;
    private ZonedDateTime createdDate;
}
