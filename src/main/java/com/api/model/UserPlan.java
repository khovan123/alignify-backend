package com.api.model;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userPlans")
public class UserPlan {
  @Id
  private String userPlanId;
  private String userId;
  private String planId;
  private ZonedDateTime createdAt;
  private long orderCode;

  public String getUserPlanId() {
    return userPlanId;
  }

  public void setUserPlanId(String userPlanId) {
    this.userPlanId = userPlanId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getPlanId() {
    return planId;
  }

  public void setPlanId(String planId) {
    this.planId = planId;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public long getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(long orderCode) {
    this.orderCode = orderCode;
  }
}
