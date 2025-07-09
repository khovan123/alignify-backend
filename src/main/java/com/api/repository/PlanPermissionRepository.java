package com.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.PlanPermission;

public interface PlanPermissionRepository extends MongoRepository<PlanPermission, String> {

  List<PlanPermission> findByPlanPermissionIdIn(List<String> planPermissionIds);
}
