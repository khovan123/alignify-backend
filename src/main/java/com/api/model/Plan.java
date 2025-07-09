package com.api.model;

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
  private List<Permission> permission;
  private List<PlanPermission> planPermissions;
  private double price;
  private double discount;
  private String planType;
  private int planCount;
  private java.util.Date createdAt;

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

  public List<Permission> getPermission() {
    return permission;
  }

  public void setPermission(List<Permission> permission) {
    this.permission = permission;
  }

  public List<PlanPermission> getPlanPermissions() {
    return planPermissions;
  }

  public void setPlanPermissions(List<PlanPermission> planPermissions) {
    this.planPermissions = planPermissions;
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

  public java.util.Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(java.util.Date createdAt) {
    this.createdAt = createdAt;
  }
}
