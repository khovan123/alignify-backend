package com.api.security;

import com.api.model.Application;
import com.api.model.Campaign;
import com.api.repository.ApplicationRepository;
import com.api.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    public boolean isCampaignOwner(String campaignId, Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        String userId = ((CustomUserDetails) principal).getUserId();
        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaignId);
        return optionalCampaign.isPresent() && optionalCampaign.get().getUserId().equals(userId);
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

}
