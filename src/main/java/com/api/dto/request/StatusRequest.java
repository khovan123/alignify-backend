package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusRequest {

    @JsonProperty("status")
    private String status;

    public StatusRequest(@JsonProperty("status") String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
