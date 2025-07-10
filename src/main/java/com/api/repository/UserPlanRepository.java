package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.UserPlan;

@Repository
public interface UserPlanRepository extends MongoRepository<UserPlan, String> {
}
