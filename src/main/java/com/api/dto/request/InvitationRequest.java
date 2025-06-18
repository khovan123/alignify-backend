package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvitationRequest {

    @JsonProperty("influencerId")
    private String influencerId;
    @JsonProperty("message")
    private String message;

    public InvitationRequest(@JsonProperty("influencerId") String influencerId,@JsonProperty("message") String message) {
        this.influencerId = influencerId;
        this.message = message;
    }

    public String getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
