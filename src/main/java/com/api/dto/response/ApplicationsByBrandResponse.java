package com.api.dto.response;

import java.util.List;

import com.api.model.Campaign;
import com.api.model.User;
import com.api.repository.CategoryRepository;
import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
public class ApplicationsByBrandResponse {

    private CampaignResponse campaignResponse;
    private List<ApplicationPlusInfluencer> applications;

    public ApplicationsByBrandResponse(User user, Campaign campaign, List<ApplicationPlusInfluencer> applications,
            CategoryRepository categoryRepository) {
        this.campaignResponse = new CampaignResponse(user, campaign, categoryRepository);
        this.applications = applications;
    }
}
