package com.api.repository;

import com.api.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

    boolean existsByInfluencerIdAndCampaignId(String influencerId, String campaignId);
}
