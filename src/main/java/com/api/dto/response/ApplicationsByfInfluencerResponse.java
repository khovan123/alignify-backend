package com.api.dto.response;

import java.util.List;

import com.api.model.Application;
import com.api.model.Campaign;
import com.api.model.User;
import com.api.repository.CategoryRepository;

public class ApplicationsByfInfluencerResponse {

    private CampaignResponse campaignResponse;
    private List<Application> applications;

    public ApplicationsByfInfluencerResponse(User user, Campaign campaign, List<Application> applications,
            CategoryRepository categoryRepository) {
        this.campaignResponse = new CampaignResponse(user, campaign, categoryRepository);
        this.applications = applications;
    }

    public CampaignResponse getCampaignResponse() {
        return campaignResponse;
    }

    public void setCampaignResponse(CampaignResponse campaignResponse) {
        this.campaignResponse = campaignResponse;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

}
