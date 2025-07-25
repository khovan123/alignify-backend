package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
public class VerifyOTPRequest {

    @JsonProperty("email")
    private String email;
    @JsonProperty("otp")
    private String otp;
    @JsonProperty("login")
    private boolean login;

    public VerifyOTPRequest(@JsonProperty("email") String email, @JsonProperty("otp") String otp, @JsonProperty("login") boolean login) {
        this.email = email;
        this.otp = otp;
        this.login = login;
    }
}
