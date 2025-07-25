package com.api.dto.response;

import com.api.dto.UserDTO;
import com.api.model.CampaignTracking;
import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CampaignTrackingResponse {
    CampaignTracking campaignTracking;
    UserDTO user;
}
