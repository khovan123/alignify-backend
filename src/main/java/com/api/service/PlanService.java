package com.api.service;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.request.PlanRequest;
import com.api.dto.response.PlanResponse;
import com.api.model.Permission;
import com.api.model.Plan;
import com.api.model.PlanPermission;
import com.api.repository.PermissionRepository;
import com.api.repository.PlanPermissionRepository;
import com.api.repository.PlanRepository;
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

    public ResponseEntity<?> createPlan(PlanRequest planrequest, HttpServletRequest request) {
        List<String> permissionIds = planrequest.getPermissionIds();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<String> validPermissionIds = permissionRepository.findAllById(permissionIds)
                    .stream()
                    .map(p -> p.getPermissionId())
                    .toList();

            if (validPermissionIds.size() != permissionIds.size()) {
                return ApiResponse.sendError(400, "Some permissionIds are invalid", request.getRequestURI());
            }
            planrequest.setPermissionIds(validPermissionIds);
        }

        List<PlanPermission> planPermissions = planrequest.getPlanPermissions();
        List<PlanPermission> savedPlanPermissions = planPermissions.stream()
                .map(planPermissionRepository::save)
                .toList();
        planrequest.setPlanPermissions(savedPlanPermissions);
        Plan plan = new Plan();
        plan.setPlanName(planrequest.getPlanName());
        plan.setDescription(planrequest.getDescription());
        plan.setRoleId(planrequest.getRoleId());
        plan.setPermissionIds(planrequest.getPermissionIds());
        plan.setPlanPermissionIds(planrequest.getPlanPermissions().stream().map(p -> p.getPlanPermissionId()).toList());
        plan.setPrice(planrequest.getPrice());
        plan.setDiscount(planrequest.getDiscount());
        plan.setPlanType(planrequest.getPlanType());
        plan.setIsPopular(planrequest.isIsPopular());
        plan.setIsActive(planrequest.isIsActive());
        PlanResponse planResponse = new PlanResponse(plan, permissionRepository, planPermissionRepository);
        planRepository.save(plan);
        return ApiResponse.sendSuccess(200, "Success", planResponse, request.getRequestURI());
    }

    public ResponseEntity<?> getAllPlanByBrandRole(HttpServletRequest request) {
        List<Plan> plans = planRepository.findByRoleId(EnvConfig.BRAND_ROLE_ID);
        List<PlanResponse> planResponses = plans.stream()
                .map(plan -> new PlanResponse(plan, permissionRepository, planPermissionRepository))
                .toList();
        return ApiResponse.sendSuccess(200, "Response successfully", planResponses, request.getRequestURI());
    }

    public ResponseEntity<?> getAllPlanByInfluencerRole(HttpServletRequest request) {
        List<Plan> plans = planRepository.findByRoleId(EnvConfig.INFLUENCER_ROLE_ID);
        List<PlanResponse> planResponses = plans.stream()
                .map(plan -> new PlanResponse(plan, permissionRepository, planPermissionRepository))
                .toList();
        return ApiResponse.sendSuccess(200, "Response successfully", planResponses, request.getRequestURI());
    }

    public ResponseEntity<?> deletePlan(String planId, HttpServletRequest request) {
        Optional<Plan> packageOpt = planRepository.findById(planId);
        if (!packageOpt.isPresent()) {
            return ApiResponse.sendError(403, "Plan not found", request.getRequestURI());
        }
        planRepository.deleteById(planId);
        return ApiResponse.sendSuccess(204, "Plan deleted successfully", null, request.getRequestURI());
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

        plan.setIsPopular(updatedPlan.isIsPopular());
        plan.setIsActive(updatedPlan.isIsActive());
        PlanResponse planResponse = new PlanResponse(plan, permissionRepository, planPermissionRepository);

        planRepository.save(plan);
        return ApiResponse.sendSuccess(200, "Plan updated successfully", planResponse, request.getRequestURI());
    }

    public ResponseEntity<?> getPermission(HttpServletRequest request) {
        List<Permission> permissions = permissionRepository.findAll();
        if(permissions.isEmpty()){
            return ApiResponse.sendError(400, "No permissions found", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Successfully", permissions, request.getRequestURI());
    }
}
