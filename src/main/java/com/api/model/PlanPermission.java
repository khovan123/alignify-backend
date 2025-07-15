package com.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "planPermissions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class PlanPermission {
  @Id
  private String planPermissionId;
  private String roleId;
  private String planPermissionName;
  private int limited;

  public String getPlanPermissionId() {
    return planPermissionId;
  }

  public void setPlanPermissionId(String planPermissionId) {
    this.planPermissionId = planPermissionId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getPlanPermissionName() {
    return planPermissionName;
  }

  public void setPlanPermissionName(String planPermissionName) {
    this.planPermissionName = planPermissionName;
  }

  public int getLimited() {
    return limited;
  }

  public void setLimited(int limited) {
    this.limited = limited;
  }
}