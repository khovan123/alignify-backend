package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.CampaignTracking;

@Repository
public interface CampaignTrackingRepository extends MongoRepository<CampaignTracking, Object> {

    Optional<CampaignTracking> findByCampaignTrackingIdAndCampaignId(String campaignTrackingId, String campaignId);

    List<CampaignTracking> findAllByCampaignId(String campaignId);

    Page<CampaignTracking> findAllByInfluencerId(String influencerId, Pageable pageable);

    List<CampaignTracking> findAllByCampaignIdAndStatus(String campaignId, String status);

    default Page<String> findCampaignIdsByInfluencerId(String influencerId, Pageable pageable) {
        return findAllByInfluencerId(influencerId, pageable)
                .map(CampaignTracking::getCampaignId);
    }

    void deleteAllByCampaignId(String campaignId);
}
