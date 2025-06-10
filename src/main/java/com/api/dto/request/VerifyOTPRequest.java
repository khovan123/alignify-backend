package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyOTPRequest {

    @JsonProperty("email")
    private String email;
    @JsonProperty("otp")
    private String otp;

    public VerifyOTPRequest(@JsonProperty("email") String email, @JsonProperty("otp") String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
