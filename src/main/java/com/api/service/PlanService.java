package com.api.service;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.model.Plan;
import com.api.repository.PlanRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public ResponseEntity<?> createPlan(Plan plan, HttpServletRequest request) {
        planRepository.save(plan);
        return ApiResponse.sendSuccess(200, "Success", plan, request.getRequestURI());

    }

    public ResponseEntity<?> getAllPlanByRoleId(String roleId, HttpServletRequest request) {
        if (roleId.equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID) || roleId.equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
            List<Plan> plans = planRepository.findByRoleId(roleId);
            return ApiResponse.sendSuccess(200, "Response successfully", plans, request.getRequestURI());
        }
        if (roleId.isEmpty()) {
            List<Plan> plans = planRepository.findAll();
            return ApiResponse.sendSuccess(200, "Response successfully", plans, request.getRequestURI());
        }
        return ApiResponse.sendError(400, "RoleID not exist!", request.getRequestURI());
    }

    public ResponseEntity<?> deletePlan(String planId, HttpServletRequest request) {
        Optional<Plan> packageOpt = planRepository.findById(planId);
        if (!packageOpt.isPresent()) {
            return ApiResponse.sendError(403, "PackageType not found", request.getRequestURI());
        }
        planRepository.deleteById(planId);
        return ApiResponse.sendSuccess(204, "PackageType deleted successfully", null, request.getRequestURI());
    }

    public ResponseEntity<?> updatePlan(String planId, Plan updatedPlan, HttpServletRequest request) {
        Optional<Plan> planOpt = planRepository.findById(planId);
        if (planOpt.isPresent()) {
            Plan plan = planOpt.get();
            double newPrice = updatedPlan.getPrice();
            if (newPrice < 0 || updatedPlan.getDiscount() < 0 ) {
                return ApiResponse.sendError(400, "Price or discount must be a non-negative number", request.getRequestURI());
            }
            if (updatedPlan.getPlanName()!= null) {
                plan.setPlanName(updatedPlan.getPlanName());
            }
            if (updatedPlan.getDescription()!= null) {
                plan.setDescription(updatedPlan.getDescription());
            }
            if (updatedPlan.getRoleId() != null) {
                plan.setRoleId(updatedPlan.getRoleId());
            }
            if (updatedPlan.getPlanType()!= null) {
                plan.setPlanType(updatedPlan.getPlanType());
            }
            if (newPrice != plan.getPrice()) {
                plan.setPrice(newPrice);
            }
            if (updatedPlan.getFeature() != null
                    && !updatedPlan.getFeature().isEmpty()) {
                plan.setFeature(updatedPlan.getFeature());
            }
            if (updatedPlan.getPermissionIds()!= null
                    && !updatedPlan.getPermissionIds().isEmpty()) {
                plan.setPermissionIds(updatedPlan.getPermissionIds());
            }
            if (updatedPlan.getPlanPermissionIds()!= null
                    && !updatedPlan.getPlanPermissionIds().isEmpty()) {
                plan.setPlanPermissionIds(updatedPlan.getPlanPermissionIds());
            }
            planRepository.save(plan);
            return ApiResponse.sendSuccess(200, "Plan updated successfully", plan, request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Plan not found", request.getRequestURI());
        }
    }
     public Plan convertToPlan(String obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(obj, Plan.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid Plan JSON: " + e.getMessage(), e);
        }
    }
}
