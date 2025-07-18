package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.Plan;
import java.util.List;

public interface PlanRepository extends MongoRepository<Plan, String> {

    List<Plan> findByRoleId(String roleId);

}
