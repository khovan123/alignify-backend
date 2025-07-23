package com.api.security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.model.Application;
import com.api.model.Campaign;
import com.api.model.CampaignTracking;
import com.api.model.ContentPosting;
import com.api.model.Invitation;
import com.api.repository.ApplicationRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.ContentPostingRepository;
import com.api.repository.InvitationRepository;

@Service
public class SecurityService {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private ContentPostingRepository contentPostingRepository;

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

        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        String userId = userDetails.getUserId();

        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaignId);
        if (!optionalCampaign.isPresent()) {
            return false;
        }

        Campaign campaign = optionalCampaign.get();

        boolean isOwner = campaign.getBrandId().equals(userId);

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

    public boolean isJoinedCampaignTracking(String campaignId, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        String userId = ((CustomUserDetails) principal).getUserId();
        if (hasInfluencerRole((CustomUserDetails) principal)) {
            Optional<CampaignTracking> campaignTrackingOptByInfluencer = campaignTrackingRepository
                    .findByCampaignIdAndInfluencerId(campaignId, userId);
            return campaignTrackingOptByInfluencer.isPresent();
        } else if (hasBrandRole((CustomUserDetails) principal)) {
            List<CampaignTracking> campaignTrackings = campaignTrackingRepository
                    .findAllByCampaignIdAndBrandId(campaignId, userId);
            return !campaignTrackings.isEmpty();
        } else {
            return false;
        }
    }

    public boolean hasInfluencerRole(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_INFLUENCER"));
    }

    public boolean hasBrandRole(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_BRAND"));
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
        return invitationOpt.isPresent() && (invitationOpt.get().getBrandId().equals(userId)
                || invitationOpt.get().getInfluencerId().equals(userId));
    }

    public boolean isContentPostingOwner(String contentId, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        String userId = ((CustomUserDetails) principal).getUserId();
        Optional<ContentPosting> contentPostingOpt = contentPostingRepository.findById(contentId);
        return contentPostingOpt.isPresent() && contentPostingOpt.get().getContentId().equals(contentId)
                && contentPostingOpt.get().getUserId().equals(userId);
    }
}