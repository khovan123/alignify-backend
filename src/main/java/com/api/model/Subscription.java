package com.api.model;

import org.springframework.data.annotation.Id;

public class Subscription {

    @Id
    private String subscriptionId, name, description;
    private double price;
    private String currency,roleId;

    public Subscription() {
    }

    
    public Subscription(String subscriptionId, String name, String description, double price, String currency, String roleId) {
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.roleId = roleId;
    }

    public String getId() {
        return subscriptionId;
    }

    public void setId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    
}
