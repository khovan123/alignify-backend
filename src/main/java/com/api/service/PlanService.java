
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
        if (planrequest.isIsActive()) {
            if (hasExceededActivePlans(planrequest.getRoleId(), planrequest.getPlanType(), null)) {
                return ApiResponse.sendError(400, "Chỉ được phép có tối đa 3 plan đang hoạt động cho mỗi role theo planType", request.getRequestURI());
            }
        }
        if (planrequest.isIsPopular()) {
            if (hasPopularPlan(planrequest.getRoleId(), planrequest.getPlanType(), null)) {
                return ApiResponse.sendError(400, "Chỉ được phép có 1 plan popular cho mỗi role theo planType", request.getRequestURI());
            }
        }
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

    public ResponseEntity<?> updatePlan(String planId, PlanRequest updatedPlan, HttpServletRequest request) {
        Optional<Plan> planOpt = planRepository.findById(planId);
        if (planOpt.isEmpty()) {
            return ApiResponse.sendError(404, "Plan not found", request.getRequestURI());
        }

        Plan plan = planOpt.get();
        Double newPrice = updatedPlan.getPrice();
        Double discount = updatedPlan.getDiscount();
        if (newPrice == null || discount == null) {
            return ApiResponse.sendError(400, "Price and discount must not be null", request.getRequestURI());
        }
        if (newPrice < 0 || discount < 0) {
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

        if (updatedPlan.getPlanPermissions() != null && !updatedPlan.getPlanPermissions().isEmpty()) {
            List<String> inputPlanPermissionIds = updatedPlan.getPlanPermissions()
                    .stream()
                    .map(PlanPermission::getPlanPermissionId)
                    .toList();

            List<String> validPlanPermissionIds = planPermissionRepository.findAllById(inputPlanPermissionIds)
                    .stream()
                    .map(PlanPermission::getPlanPermissionId)
                    .toList();

            plan.setPlanPermissionIds(validPlanPermissionIds);
        }

        if (updatedPlan.isIsActive()) {
            if (hasExceededActivePlans(plan.getRoleId(), plan.getPlanType(), plan.getPlanId())) {
                return ApiResponse.sendError(400, "Chỉ được phép có tối đa 3 plan đang hoạt động cho mỗi role theo planType", request.getRequestURI());
            }
        }
        plan.setIsActive(updatedPlan.isIsActive());

        if (updatedPlan.isIsPopular()) {
            if (hasPopularPlan(plan.getRoleId(), plan.getPlanType(), plan.getPlanId())) {
                return ApiResponse.sendError(400, "Chỉ được phép có 1 plan popular cho mỗi role theo planType", request.getRequestURI());
            }
        }
        plan.setIsPopular(updatedPlan.isIsPopular());

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

    private boolean hasExceededActivePlans(String roleId, String planType, String excludedPlanId) {
        List<Plan> activePlans = planRepository.findByRoleIdAndPlanTypeAndIsActive(roleId, planType, true);
        if (excludedPlanId != null) {
            activePlans = activePlans.stream()
                    .filter(p -> !p.getPlanId().equals(excludedPlanId))
                    .toList();
        }
        return activePlans.size() >= 3;
    }

    private boolean hasPopularPlan(String roleId, String planType, String excludedPlanId) {
        List<Plan> popularPlans = planRepository.findByRoleIdAndPlanTypeAndIsPopular(roleId, planType, true);
        if (excludedPlanId != null) {
            popularPlans = popularPlans.stream()
                    .filter(p -> !p.getPlanId().equals(excludedPlanId))
                    .toList();
        }
        return !popularPlans.isEmpty();
    }
}