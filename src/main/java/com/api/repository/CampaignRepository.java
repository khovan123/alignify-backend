package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Campaign;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {

    @Override
    Page<Campaign> findAll(Pageable pageable);

    Page<Campaign> findAllByBrandId(String userId, Pageable pageable);

    Page<Campaign> findAllByCampaignIdIn(List<String> campaignIds, Pageable pageable);

    Optional<Campaign> findByCampaignIdAndBrandId(String campaignId, String brandId);

}
