package com.api.service;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.response.PlanResponse;
import com.api.model.Plan;
import com.api.repository.PermissionRepository;
import com.api.repository.PlanPermissionRepository;
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
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PlanPermissionRepository planPermissionRepository;

    public ResponseEntity<?> createPlan(Plan plan, HttpServletRequest request) {
        List<String> permissionIds = plan.getPermissionIds();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<String> validPermissionIds = permissionRepository.findAllById(permissionIds)
                    .stream()
                    .map(p -> p.getPermissionId())
                    .toList();

            if (validPermissionIds.size() != permissionIds.size()) {
                return ApiResponse.sendError(400, "Some permissionIds are invalid", request.getRequestURI());
            }
            plan.setPermissionIds(validPermissionIds);
        }

        List<String> planPermissionIds = plan.getPlanPermissionIds();
        if (planPermissionIds != null && !planPermissionIds.isEmpty()) {
            List<String> validPlanPermissionIds = planPermissionRepository.findAllById(planPermissionIds)
                    .stream()
                    .map(p -> p.getPlanPermissionId())
                    .toList();

            if (validPlanPermissionIds.size() != planPermissionIds.size()) {
                return ApiResponse.sendError(400, "Some planPermissionIds are invalid", request.getRequestURI());
            }
            plan.setPlanPermissionIds(validPlanPermissionIds);
        }
        PlanResponse planResponse = new PlanResponse(plan, permissionRepository, planPermissionRepository);
        planRepository.save(plan);
        return ApiResponse.sendSuccess(200, "Success", planResponse, request.getRequestURI());
    }

    public ResponseEntity<?> getAllPlanByRoleId(String roleId, HttpServletRequest request) {
        List<Plan> plans;

        if (roleId != null && (roleId.equalsIgnoreCase("INFLUENCER") || roleId.equalsIgnoreCase("BRAND"))) {
            plans = planRepository.findByRoleId(roleId);
        } else if (roleId == null || roleId.trim().isEmpty()) {
            plans = planRepository.findAll();
        } else {
            return ApiResponse.sendError(400, "RoleID not exist!", request.getRequestURI());
        }

        List<PlanResponse> planResponses = plans.stream()
                .map(plan -> new PlanResponse(plan, permissionRepository, planPermissionRepository))
                .toList();

        return ApiResponse.sendSuccess(200, "Response successfully", planResponses, request.getRequestURI());
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
        if (planOpt.isEmpty()) {
            return ApiResponse.sendError(404, "Plan not found", request.getRequestURI());
        }

        Plan plan = planOpt.get();
        Double newPrice = updatedPlan.getPrice();
        if (newPrice < 0 || updatedPlan.getDiscount() < 0) {
            return ApiResponse.sendError(400, "Price or discount must be a non-negative number", request.getRequestURI());
        }

        if (updatedPlan.getPlanName() != null) {
            plan.setPlanName(updatedPlan.getPlanName());
        }
        if (updatedPlan.getDescription() != null) {
            plan.setDescription(updatedPlan.getDescription());
        }
        if (updatedPlan.getRoleId() != null) {
            plan.setRoleId(updatedPlan.getRoleId());
        }
        if (updatedPlan.getPlanType() != null) {
            plan.setPlanType(updatedPlan.getPlanType());
        }
        if (newPrice != null) {
            plan.setPrice(newPrice);
        }

        if (updatedPlan.getPermissionIds() != null && !updatedPlan.getPermissionIds().isEmpty()) {
            List<String> inputPermissionIds = updatedPlan.getPermissionIds();
            List<String> validPermissionIds = permissionRepository.findAllById(inputPermissionIds)
                    .stream()
                    .map(permission -> permission.getPermissionId())
                    .toList();
            plan.setPermissionIds(validPermissionIds);
        }

        if (updatedPlan.getPlanPermissionIds() != null && !updatedPlan.getPlanPermissionIds().isEmpty()) {
            List<String> inputPlanPermissionIds = updatedPlan.getPlanPermissionIds();
            List<String> validPlanPermissionIds = planPermissionRepository.findAllById(inputPlanPermissionIds)
                    .stream()
                    .map(planPerm -> planPerm.getPlanPermissionId())
                    .toList();
            plan.setPlanPermissionIds(validPlanPermissionIds);
        }

        plan.setPopular(updatedPlan.isPopular());
        PlanResponse planResponse = new PlanResponse(plan, permissionRepository, planPermissionRepository);

        planRepository.save(plan);
        return ApiResponse.sendSuccess(200, "Plan updated successfully", planResponse, request.getRequestURI());
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
