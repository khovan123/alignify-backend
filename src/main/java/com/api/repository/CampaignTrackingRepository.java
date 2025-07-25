package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.CampaignTracking;

@Repository
public interface CampaignTrackingRepository extends MongoRepository<CampaignTracking, String> {

    Optional<CampaignTracking> findByCampaignTrackingIdAndCampaignId(String campaignTrackingId, String campaignId);

    List<CampaignTracking> findAllByInfluencerId(String influencerId);

    List<CampaignTracking> findAllByCampaignIdAndStatus(String campaignId, String status);

    void deleteAllByCampaignId(String campaignId);

    Optional<CampaignTracking> findByCampaignIdAndInfluencerId(String campaignId, String influencerId);
    
    List<CampaignTracking> findAllByCampaignIdAndBrandId(String campaignId, String brandId);
}
