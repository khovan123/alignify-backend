package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecoveryPasswordRequest {

    @JsonProperty("email")
    private String email;
    @JsonProperty("url")
    private String url;

    public RecoveryPasswordRequest(@JsonProperty("email") String email, @JsonProperty("url") String url) {
        this.email = email;
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
