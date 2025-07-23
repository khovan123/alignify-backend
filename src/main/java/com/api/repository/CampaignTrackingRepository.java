package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.CampaignTracking;

@Repository
public interface CampaignTrackingRepository extends MongoRepository<CampaignTracking, Object> {

    Optional<CampaignTracking> findByCampaignTrackingIdAndCampaignId(String campaignTrackingId, String campaignId);

    List<CampaignTracking> findAllByCampaignId(String campaignId);

    List<CampaignTracking> findAllByInfluencerId(String influencerId);

    List<CampaignTracking> findAllByCampaignIdAndStatus(String campaignId, String status);

    default List<String> findCampaignIdsByInfluencerId(String influencerId) {
        return findAllByInfluencerId(influencerId)
                .stream()
                .map(CampaignTracking::getCampaignId)
                .toList();
    }

    void deleteAllByCampaignId(String campaignId);

    Optional<CampaignTracking> findByCampaignIdAndInfluencerId(String campaignId, String influencerId);

    Optional<CampaignTracking> findByCampaignIdAndBrandId(String campaignId, String brandId);

    List<CampaignTracking> findAllByCampaignIdAndBrandId(String campaignId, String brandI);
}
