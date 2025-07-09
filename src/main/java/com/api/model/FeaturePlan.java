
package com.api.model;


public class FeaturePlan {
    private String futureName;
    private int amount;

    public FeaturePlan(String futureName, int amount) {
        this.futureName = futureName;
        this.amount = amount;
    }

    public String getFutureName() {
        return futureName;
    }

    public void setFutureName(String futureName) {
        this.futureName = futureName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
