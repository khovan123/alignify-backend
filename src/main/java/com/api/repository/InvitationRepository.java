package com.api.repository;

import com.api.model.Invitation;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends MongoRepository<Invitation, String> {

    Optional<Invitation> findByCampaignIdAndInfluencerId(String campaignId, String influencerId);

    boolean existsByCampaignIdAndInfluencerId(String campaignId, String influencerId);

}
