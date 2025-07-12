package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Invitation;

@Repository
public interface InvitationRepository extends MongoRepository<Invitation, String> {

    Optional<Invitation> findByCampaignIdAndInfluencerId(String campaignId, String influencerId);

    boolean existsByCampaignIdAndInfluencerId(String campaignId, String influencerId);

    List<Invitation> findAllByInfluencerId(String influencerId);

    List<Invitation> findAllByBrandId(String brandId);

}
