package com.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Campaign;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {

    @Override
    Page<Campaign> findAll(Pageable pageable);

    Page<Campaign> findAllByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    Page<Campaign> findAllByBrandIdAndStatusOrderByCreatedAtDesc(String brandId, String status, Pageable pageable);

    List<Campaign> findAllByCampaignIdInAndStatus(List<String> campaignIds, String status);

    Page<Campaign> findAllByBrandId(String userId, Pageable pageable);

    Page<Campaign> findAllByCampaignIdIn(List<String> campaignIds, Pageable pageable);

    Optional<Campaign> findByCampaignIdAndBrandId(String campaignId, String brandId);

    List<Campaign> findAllByCampaignIdIn(Set<String> campaignIds);

    Page<Campaign> findByCategoryIdsInAndStatusOrderByCreatedAtDesc(String category, String status, Pageable pageable);

    List<Campaign> findTop3ByOrderByApplicationTotalDescCreatedAtDesc();
}
