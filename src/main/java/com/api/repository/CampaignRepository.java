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

    List<Campaign> findAllByBrandIdAndStatusOrderByCreatedAtDesc(String brandId, String status);

    Page<Campaign> findAllByBrandId(String userId, Pageable pageable);

    List<Campaign> findAllByBrandId(String userId);

    List<Campaign> findAllByBrandIdAndStatus(String userId, String status);

    List<Campaign> findAllByCampaignIdIn(List<String> campaignIds);

    Optional<Campaign> findByCampaignIdAndBrandId(String campaignId, String brandId);

    List<Campaign> findAllByCampaignIdIn(Set<String> campaignIds);

    List<Campaign> findAllByCampaignIdInAndStatus(List<String> campaignIds, String status);

    List<Campaign> findByBrandIdIn(List<String> brandIds);

    Page<Campaign> findByStatusAndBrandIdIn(String status, List<String> brandIds, Pageable pageable);

    Page<Campaign> findByStatusAndCampaignNameContainingIgnoreCase(String status, String campaignName, Pageable pageable);

    Page<Campaign> findByCategoryIdsInAndStatusOrderByCreatedAtDesc(String category, String status, Pageable pageable);

    List<Campaign> findTop3ByStatusOrderByApplicationTotalDescCreatedAtDesc(String status);

    Page<Campaign> findByBrandIdInOrCampaignNameContainingIgnoreCase(List<String> brandIds, String campaignName,
            Pageable pageable);

    List<Campaign> findAllByBrandIdAndStatusNot(String brandId, String status);

    int countByBrandIdAndStatusNot(String brandId, String status);

    List<Campaign> findAllByStatusOrderByCreatedAtDesc(String status);

    List<Campaign> findAllByBrandIdOrderByCreatedAtDesc(String userId);

}
