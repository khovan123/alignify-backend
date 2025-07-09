package com.api.model;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "plans")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Plan {
  @Id
  private String planId;
  private String planName;
  private String description;
  private String roleId;
  private List<String> permissionIds;
  private List<String> planPermissionIds;
  private double price;
  private double discount;
  private String planType;
  private int planCount;
  private ZonedDateTime createdAt;

  public String getPlanId() {
    return planId;
  }

  public void setPlanId(String planId) {
    this.planId = planId;
  }

  public String getPlanName() {
    return planName;
  }

  public void setPlanName(String planName) {
    this.planName = planName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public List<String> getPermissionIds() {
    return permissionIds;
  }

  public void setPermissionIds(List<String> permissionIds) {
    this.permissionIds = permissionIds;
  }

  public List<String> getPlanPermissionIds() {
    return planPermissionIds;
  }

  public void setPlanPermissionIds(List<String> planPermissionIds) {
    this.planPermissionIds = planPermissionIds;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public String getPlanType() {
    return planType;
  }

  public void setPlanType(String planType) {
    this.planType = planType;
  }

  public int getPlanCount() {
    return planCount;
  }

  public void setPlanCount(int planCount) {
    this.planCount = planCount;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
