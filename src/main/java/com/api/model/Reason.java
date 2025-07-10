package com.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "reasons")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Reason {
  private String reasonId;
  private String title;
  private String description;

}
