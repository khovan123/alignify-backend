package com.api.security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.config.EnvConfig;
import com.api.model.Permission;
import com.api.model.Plan;
import com.api.model.PlanPermission;
import com.api.model.User;
import com.api.model.UserPlan;
import com.api.repository.PermissionRepository;
import com.api.repository.PlanPermissionRepository;
import com.api.repository.PlanRepository;
import com.api.repository.UserPlanRepository;
import com.api.repository.UserRepository;

@Service
public class PermissionService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserPlanRepository userPlanRepository;

  @Autowired
  private PlanRepository planRepository;

  @Autowired
  private PlanPermissionRepository planPermissionRepository;

  @Autowired
  private PermissionRepository permissionRepository;

  public boolean hasPermission(String action, Object principal) {
    if (!(principal instanceof CustomUserDetails)) {
      return false;
    }
    String userId = ((CustomUserDetails) principal).getUserId();
    Optional<User> userOpt = userRepository.findByUserId(userId);
    if (userOpt.isEmpty()) {
      return false;
    }
    User user = userOpt.get();

    if (EnvConfig.ADMIN_ROLE_ID.equals(user.getRoleId())) {
      return true;
    }

    List<String> permissionIds = user.getPermissionIds();
    if (permissionIds != null && !permissionIds.isEmpty()) {
      List<Permission> permissions = permissionRepository.findByPermissionIdIn(permissionIds);
      for (Permission perm : permissions) {
        if ("all".equals(perm.getPermissionName())) {
          return true;
        }
        if (action.equals(perm.getPermissionName())) {
          return true;
        }
      }
    }

    String userPlanId = user.getUserPlanId();
    if (userPlanId == null) {
      return false;
    }

    Optional<UserPlan> userPlanOpt = userPlanRepository.findById(userPlanId);
    if (userPlanOpt.isEmpty()) {
      return false;
    }
    UserPlan userPlan = userPlanOpt.get();

    Optional<Plan> planOpt = planRepository.findById(userPlan.getPlanId());
    if (planOpt.isEmpty()) {
      return false;
    }
    Plan plan = planOpt.get();

    for (Permission perm : permissionRepository.findByPermissionIdIn(plan.getPermissionIds())) {
      if ("all".equals(perm.getPermissionName()) || action.equals(perm.getPermissionName())) {
        return true;
      }
    }

    for (PlanPermission planPerm : planPermissionRepository.findByPlanPermissionIdIn(plan.getPlanPermissionIds())) {
      Optional<PlanPermission> planPermissionOpt = planPermissionRepository
          .findById(planPerm.getPlanPermissionId());
      if (planPermissionOpt.isPresent()) {
        PlanPermission planPermission = planPermissionOpt.get();
        if (action.equals(planPermission.getPlanPermissionName()) && planPermission.getLimited() > 0) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean consumePlanPermissionQuota(String userId, String action) {
    Optional<User> userOpt = userRepository.findByUserId(userId);
    if (userOpt.isEmpty() || userOpt.get().getUserPlanId() == null) {
      return false;
    }
    User user = userOpt.get();

    Optional<UserPlan> userPlanOpt = userPlanRepository.findById(user.getUserPlanId());
    if (userPlanOpt.isEmpty()) {
      return false;
    }
    UserPlan userPlan = userPlanOpt.get();

    Optional<Plan> planOpt = planRepository.findById(userPlan.getPlanId());
    if (planOpt.isEmpty()) {
      return false;
    }
    Plan plan = planOpt.get();

    for (PlanPermission planPerm : planPermissionRepository.findByPlanPermissionIdIn(plan.getPlanPermissionIds())) {
      Optional<PlanPermission> planPermissionOpt = planPermissionRepository
          .findById(planPerm.getPlanPermissionId());
      if (planPermissionOpt.isPresent()) {
        PlanPermission planPermission = planPermissionOpt.get();
        if (action.equals(planPermission.getPlanPermissionName()) && planPermission.getLimited() > 0) {
          planPermission.setLimited(planPermission.getLimited() - 1);
          planPermissionRepository.save(planPermission);
          return true;
        }
      }
    }
    return false;
  }
}