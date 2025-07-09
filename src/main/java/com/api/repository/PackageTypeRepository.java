package com.api.repository;

import com.api.model.PackageType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PackageTypeRepository extends MongoRepository<PackageType, String>{
    List<PackageType> findByRoleId(String roleId);
}
