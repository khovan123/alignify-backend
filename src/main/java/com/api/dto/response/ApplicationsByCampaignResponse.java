package com.api.dto.response;

import com.api.model.Application;
import com.api.model.Campaign;
import com.api.repository.CategoryRepository;
import java.util.List;

public class ApplicationsByCampaignResponse {

    private CampaignResponse campaignResponse;
    private List<Application> applications;

    public ApplicationsByCampaignResponse(Campaign campaign, List<Application> applications, CategoryRepository categoryRepository) {
        this.campaignResponse = new CampaignResponse(campaign, categoryRepository);
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
