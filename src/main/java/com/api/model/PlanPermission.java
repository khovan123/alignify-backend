package com.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "planPermissions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class PlanPermission {
  @Id
  private String planPermissionId;
  private String planPermissionName;
  private int limited;
}