package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

    boolean existsByInfluencerIdAndCampaignId(String influencerId, String campaignId);

    Optional<Application> findByApplicationIdAndBrandId(String applicationId, String brandId);

    Optional<Application> findByApplicationIdAndInfluencerId(String applicationId, String influencerId);

    List<Application> findAllByCampaignId(String campaignId);

    List<Application> findAllByInfluencerId(String influencerId);

    List<Application> findAllByCampaignIdIn(List<String> campaignIds);

    List<Application> findAllByBrandId(String brandId);

    List<Application> findAllByCampaignIdAndStatus(String campaignId, String status);

    void deleteAllByCampaignId(String campaignId);

    List<Application> findAllByInfluencerIdAndStatus(String influencerId, String status);

    List<Application> findAllByCampaignIdAndInfluencerIdIn(String campaignId, List<String> applicationIds);

    long countByInfluencerId(String influencerId);

    long countByInfluencerIdAndStatus(String influencerId, String status);
}
