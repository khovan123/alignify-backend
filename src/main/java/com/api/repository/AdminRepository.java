package com.api.repository;

import com.api.model.Admin;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {

    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);
}
