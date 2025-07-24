package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.Plan;

public interface PlanRepository extends MongoRepository<Plan, String> {

    List<Plan> findByRoleId(String roleId);

    Optional<Plan> findByPlanId(String planId);

    List<Plan> findByRoleIdAndPlanTypeAndIsActive(String roleId, String planType, boolean isActive);

    List<Plan> findByRoleIdAndPlanTypeAndIsPopular(String roleId, String planType, boolean popular);

}
