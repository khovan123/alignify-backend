package com.api.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class InvitationRequest {

    @JsonProperty("influencerIds")
    private List<String> influencerIds;
    @JsonProperty("campaignId")
    private String campaignId;
    @JsonProperty("message")
    private String message;

    public InvitationRequest(
            @JsonProperty("campaignId") String campaignId,
            @JsonProperty("influencerIds") List<String> influencerIds,
            @JsonProperty("message") String message) {
        this.campaignId = campaignId;
        this.influencerIds = influencerIds;
        this.message = message;
    }

}
