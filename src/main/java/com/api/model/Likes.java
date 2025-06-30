
package com.api.model;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "likes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Likes {
    @Id
    private String likeId;
    private String userId;
    private String contentId;
    private ZonedDateTime createdAt;
}
