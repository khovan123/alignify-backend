package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Reason;

@Repository
public interface ReasonRepository extends MongoRepository<Reason, Object> {

}
