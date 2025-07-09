package com.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.model.Permission;

public interface PermissionRepository extends MongoRepository<Permission, String> {
  List<Permission> findByPermissionIdIn(List<String> permissionIds);
}
