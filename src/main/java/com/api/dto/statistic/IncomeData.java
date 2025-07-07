package com.api.dto.statistic;

public class IncomeData {
    private String month;
    private long income;
    private int campaigns;

    public IncomeData() {}
    public IncomeData(String month, long income, int campaigns) {
        this.month = month;
        this.income = income;
        this.campaigns = campaigns;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public int getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(int campaigns) {
        this.campaigns = campaigns;
    }
}