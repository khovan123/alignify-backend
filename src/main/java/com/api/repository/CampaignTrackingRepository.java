package com.api.repository;

import com.api.model.CampaignTracking;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignTrackingRepository extends MongoRepository<CampaignTracking, Object> {

    Optional<CampaignTracking> findByCampaignTrackingIdAndCampaignId(String campaignTrackingId, String campaignId);
}
