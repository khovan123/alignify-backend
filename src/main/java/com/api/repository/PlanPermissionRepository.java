package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.PlanPermission;

public interface PlanPermissionRepository extends MongoRepository<PlanPermission, String> {
}
