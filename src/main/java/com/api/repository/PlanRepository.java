package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.Plan;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends MongoRepository<Plan, String> {

    List<Plan> findByRoleId(String roleId);
    Optional<Plan> findByPlanId(String planId);

}
