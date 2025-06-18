package com.api.security;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.model.Application;
import com.api.model.Campaign;
import com.api.model.CampaignTracking;
import com.api.model.Invitation;
import com.api.repository.ApplicationRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.InvitationRepository;

@Service
public class SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;
    @Autowired
    private InvitationRepository invitationRepository;

    // public boolean isCampaignOwner(String campaignId, Object principal) {
    // System.out.println("hello");
    // if (!(principal instanceof CustomUserDetails)) {
    // return false;
    // }
    // String userId = ((CustomUserDetails) principal).getUserId();
    // Optional<Campaign> optionalCampaign =
    // campaignRepository.findById(campaignId);
    // System.out.println(campaignId);
    // System.out.println(optionalCampaign.get().getCampaignId());
    // System.out.println(userId);
    // System.out.println(optionalCampaign.get().getUserId());
    // return optionalCampaign.isPresent() &&
    // optionalCampaign.get().getUserId().equals(userId);
    // }
    public boolean isCampaignOwner(String campaignId, Object principal) {
        logger.debug("Checking if user is campaign owner for campaignId: {}", campaignId);

        if (!(principal instanceof CustomUserDetails)) {
            logger.warn("Principal is not an instance of CustomUserDetails: {}", principal);
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        String userId = userDetails.getUserId();
        logger.debug("UserId from principal: {}", userId);

        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaignId);
        if (!optionalCampaign.isPresent()) {
            logger.warn("Campaign not found for campaignId: {}", campaignId);
            return false;
        }

        Campaign campaign = optionalCampaign.get();
        logger.debug("Campaign found: campaignId={}, owner userId={}", campaign.getCampaignId(), campaign.getBrandId());

        boolean isOwner = campaign.getBrandId().equals(userId);
        logger.debug("Is user {} the owner of campaign {}? {}", userId, campaignId, isOwner);

        return isOwner;
    }

    public boolean isApplicationOwner(String applicationId, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
String userId = ((CustomUserDetails) principal).getUserId();
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        return applicationOpt.isPresent() && applicationOpt.get().getInfluencerId().equals(userId);
    }

    public boolean isApplicationReceiver(String applicationId, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        return applicationOpt.isPresent() && this.isCampaignOwner(applicationOpt.get().getCampaignId(), principal);
    }

    public boolean isJoinedCampaignTracking(String campaignId, String trackingId, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        String userId = ((CustomUserDetails) principal).getUserId();
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
                .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
        if (!campaignTrackingOpt.isPresent()) {
            return false;
        }
        if (hasInfluencerRole((CustomUserDetails) principal)) {
            return campaignTrackingOpt.isPresent() && campaignTrackingOpt.get().getInfluencerId().equals(userId);
        } else {
            return campaignTrackingOpt.isPresent() && campaignTrackingOpt.get().getBrandId().equals(userId);
        }
    }

    private boolean hasInfluencerRole(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_INFLUENCER"));
    }

    public boolean checkCampaignStatus(String campaignId, String status, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }

        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaignId);
        return optionalCampaign.isPresent() && optionalCampaign.get().getStatus().equals(status);
    }

    public boolean isJoinedInvitation(String invitationId, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        String userId = ((CustomUserDetails) principal).getUserId();

        Optional<Invitation> invitationOpt = invitationRepository.findById(invitationId);
        return invitationOpt.isPresent() && (invitationOpt.get().getBrandId().equals(userId) || invitationOpt.get().getInfluencerId().equals(userId));
    }
}