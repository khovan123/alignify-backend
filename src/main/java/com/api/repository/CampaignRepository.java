package com.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Campaign;
import java.util.Set;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {

    @Override
    Page<Campaign> findAll(Pageable pageable);

    Page<Campaign> findByUserId(String userId, Pageable pageable);

    List<Campaign> findAllByCampaignIdIn(Set<String> campaignIds);

}
