package com.api.repository;

import com.api.model.Influencer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerRepository extends MongoRepository<Influencer, String> {

    @Query("{ '_id': ?0 }")
    boolean isPublic(String userId);
}
