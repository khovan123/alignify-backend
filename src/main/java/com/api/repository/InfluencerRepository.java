package com.api.repository;

import com.api.model.Influencer;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerRepository extends MongoRepository<Influencer, String> {
    List<Influencer> findTop2ByOrderByFollowerDesc();
    Page<Influencer> findByCategoryIdsInOrderByRatingDesc(String category, Pageable pageable);

}
