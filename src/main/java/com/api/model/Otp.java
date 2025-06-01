package com.api.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "otps")
public class Otp {

    @Id
    private String id;

    @Indexed
    private String email;

    private String otpCode;

    private int requestCount;

    private int attemptCount;
    
    @CreatedDate
    private LocalDateTime createdAt;

    public Otp() {
    }

    public Otp(String email, String otpCode) {
        this.email = email;
        this.otpCode = otpCode;
        this.requestCount = 1;
        this.attemptCount = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Otp(String email, String otpCode, int requestCount, LocalDateTime createdAt) {
        this.email = email;
        this.otpCode = otpCode;
        this.requestCount = requestCount;
        this.createdAt = createdAt;
    }

    public Otp(String id, String email, String otpCode, int requestCount, int attemptCount, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.otpCode = otpCode;
        this.requestCount = requestCount;
        this.attemptCount = attemptCount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
