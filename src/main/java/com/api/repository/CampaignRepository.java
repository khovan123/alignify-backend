package com.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Campaign;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {

    Page<Campaign> findAll(Pageable pageable);

    Page<Campaign> findByUserId(String userId, Pageable pageable);
    Page<Campaign> findByUserIdAndIsPublic(String userId, boolean isPublic, Pageable pageable);


}
