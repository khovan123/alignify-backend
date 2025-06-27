package com.api.model;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accountVerifieds")
public class AccountVerified {

    @Id
    private String verification_id;
    private String email;
    @CreatedDate
    private ZonedDateTime createdAt;

    public AccountVerified() {
    }

    public AccountVerified(String email) {
        this.email = email;
    }

    public AccountVerified(String verification_id, String email, ZonedDateTime createdAt) {
        this.verification_id = verification_id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getVerification_id() {
        return verification_id;
    }

    public void setVerification_id(String verification_id) {
        this.verification_id = verification_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
