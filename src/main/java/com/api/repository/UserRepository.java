package com.api.repository;

import com.api.model.User;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{ 'roleId': ?0, '_id': { $ne: ?1 } }")
    List<User> findByRoleIdAndUserIdNot(String roleId, String excludedId);

}
