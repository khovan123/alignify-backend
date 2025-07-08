package com.api.model;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "userBans")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserBan {
  @Id
  private String userId;
  private String reasonId;
  private ZonedDateTime createdAt;
}
