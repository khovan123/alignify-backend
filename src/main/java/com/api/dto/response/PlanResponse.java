package com.api.dto.response;

import com.api.model.Permission;
import com.api.model.Plan;
import com.api.model.PlanPermission;
import com.api.repository.PermissionRepository;
import com.api.repository.PlanPermissionRepository;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PlanResponse {

    private String planId;
    private String planName;
    private String description;
    private String roleId;
    private List<Permission> permissions;
    private List<PlanPermission> planPermissions;
    private Double price;
    private Double discount;
    private String planType;
    private int planCount;
    private ZonedDateTime createdAt;
    private boolean isPopular;
    private boolean isActive;

    public PlanResponse(Plan plan, PermissionRepository permissionRepository, PlanPermissionRepository planPermissionRepository) {
        this.planId = plan.getPlanId();
        this.planName = plan.getPlanName();
        this.description = plan.getDescription();
        this.roleId = plan.getRoleId();
        this.price = plan.getPrice();
        this.discount = plan.getDiscount();
        this.planType = plan.getPlanType();
        this.planCount = plan.getPlanCount();
        this.createdAt = plan.getCreatedAt();
        this.isPopular = plan.isIsPopular();
        this.permissions = (plan.getPermissionIds()!= null && !plan.getPermissionIds().contains(null))
                ? permissionRepository.findAllById(plan.getPermissionIds())
                : Collections.emptyList();
        this.planPermissions = (plan.getPlanPermissionIds()!= null && !plan.getPlanPermissionIds().contains(null))
                ? planPermissionRepository.findAllById(plan.getPlanPermissionIds())
                : Collections.emptyList();
        this.isActive = plan.isIsActive();
    }

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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissionIds) {
        this.permissions = permissionIds;
    }

    public List<PlanPermission> getPlanPermissions() {
        return planPermissions;
    }

    public void setPlanPermissions(List<PlanPermission> planPermissions) {
        this.planPermissions = planPermissions;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
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

    public boolean isIsPopular() {
        return isPopular;
    }

    public void setIsPopular(boolean isPopular) {
        this.isPopular = isPopular;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    

}
