package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.UserBan;

@Repository
public interface UserBanRepository extends MongoRepository<UserBan, String> {

}
