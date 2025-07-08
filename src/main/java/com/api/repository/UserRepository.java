package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{ 'roleId': ?0, '_id': { $ne: ?1 } }")
    List<User> findByRoleIdAndUserIdNot(String roleId, String excludedId);

    Optional<User> findByUserId(String userId);

    List<User> findByNameContainingIgnoreCaseAndRoleId(String name, String roleId);

    Page<User> findByNameContainingIgnoreCaseAndRoleId(String name, String roleId, Pageable pageable);

    List<User> findByNameContainingIgnoreCase(String name);

    @Query("{ 'roleId': ?0, '_id': { $ne: ?1 }, 'isActive': true }")
    Page<User> findByRoleIdAndUserIdNotAndIsActiveTrue(String roleId, String excludedId, Pageable pageable);

    List<User> findAllByUserIds(List<String> userIds);

    @Query("{ 'roleId': ?0, '_id': { $nin: ?1 } }")
    Page<User> findAllByRoleIdAndNotIn(String roleId, List<String> userBanIds, Pageable pageable);
}
