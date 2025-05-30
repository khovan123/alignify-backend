package com.api.repository;

import com.api.model.AccountVerified;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountVerifiedRepository extends MongoRepository<AccountVerified, String> {

    boolean existsByEmail(String email);

}
