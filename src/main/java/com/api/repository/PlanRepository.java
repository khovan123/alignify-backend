package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.Plan;

public interface PlanRepository extends MongoRepository<Plan, String> {
}
