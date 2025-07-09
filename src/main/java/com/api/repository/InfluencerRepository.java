package com.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.model.Influencer;

@Repository
public interface InfluencerRepository extends MongoRepository<Influencer, String> {
    List<Influencer> findTop2ByOrderByFollowerDesc();

    Page<Influencer> findByCategoryIdsInOrderByRatingDesc(String category, Pageable pageable);

    @Query("{ '_id': { $in: ?0, $nin: ?1 } }")
    Page<Influencer> findAllByUserIdsAndNot(List<String> userIds, List<String> userBanIds, Pageable pageable);

    @Query("{ '_id': { $nin: ?0 } }")
    Page<Influencer> findAllNotIn(List<String> userBanIds, Pageable pageable);
}
