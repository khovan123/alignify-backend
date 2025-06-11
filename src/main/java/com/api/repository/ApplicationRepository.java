package com.api.repository;

import com.api.model.Application;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

    boolean existsByInfluencerIdAndCampaignId(String influencerId, String campaignId);

    Optional<Application> findByApplicationIdAndBrandId(String applicationId, String brandId);

    Optional<Application> findByApplicationIdAndInfluencerId(String applicationId, String influencerId);

}
