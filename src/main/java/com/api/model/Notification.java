package com.api.model;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "notifications")
@CompoundIndex(name = "userId_createdAt_idx", def = "{'userId': 1, 'createdAt': -1}")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Notification {
  @Id
  private String notificationId;
  private String userId;
  private String avatarUrl;
  private String name;
  private String content;
  @CreatedDate
  private ZonedDateTime createdAt;
  private boolean isRead = false;
}
