/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Admin
 */
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String transactionId;
    private String cooperationId;
    private String userId;
    private double amount;
    private String payMethod;
    private String reference;
    private String status;
    private String description;
    private boolean isRefunded;
    @CreatedDate
    private Date createdDate;
    private Date completedDate;

    public Transaction() {
    }

    public Transaction(String transactionId, String cooperationId, double amount, String payMethod, String reference,String status, String description, boolean isRefunded, Date createdDate, Date completedDate) {
        this.transactionId = transactionId;
        this.cooperationId = cooperationId;
        this.amount = amount;
        this.payMethod = payMethod;
        this.reference = reference;
        this.status = status;
        this.description = description;
        this.isRefunded = isRefunded;
        this.createdDate = createdDate;
        this.completedDate = completedDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCooperationId() {
        return cooperationId;
    }

    public void setCooperationId(String cooperationId) {
        this.cooperationId = cooperationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDecription(String description) {
        this.description = description;
    }

    public boolean isIsRefunded() {
        return isRefunded;
    }

    public void setIsRefunded(boolean isRefunded) {
        this.isRefunded = isRefunded;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
    
}

