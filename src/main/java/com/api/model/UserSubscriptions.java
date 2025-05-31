/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

/**
 *
 * @author Admin
 */
public class UserSubscriptions {
    @Id
    private String userSubscriptonId;
    private String userId, subscriptionId,status;
    
    private Date start_date, end_date;
    @CreatedDate
    private Date create_at;
    @LastModifiedDate
    private Date update_at;

    public UserSubscriptions() {
    }

    
    
    public UserSubscriptions(String userSubscriptonId, String userId, String subscriptionId, String status, Date start_date, Date end_date, Date create_at, Date update_at) {
        this.userSubscriptonId = userSubscriptonId;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.status = status;
        this.start_date = start_date;
        this.end_date = end_date;
        this.create_at = create_at;
        this.update_at = update_at;
    }

    public String getUserSubscriptonId() {
        return userSubscriptonId;
    }

    public void setUserSubscriptonId(String userSubscriptonId) {
        this.userSubscriptonId = userSubscriptonId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }
    
    
    
}
