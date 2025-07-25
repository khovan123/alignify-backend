package com.api.dto.response;

public class AdminStatisticsResponse {
    private long totalUsers;
    private long totalCampaigns;
    private long totalContentPostings;

    public AdminStatisticsResponse(long totalUsers, long totalCampaigns, long totalContentPostings) {
        this.totalUsers = totalUsers;
        this.totalCampaigns = totalCampaigns;
        this.totalContentPostings = totalContentPostings;
    }

    public long getTotalUsers() {
        return totalUsers;
    }
    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }
    public long getTotalCampaigns() {
        return totalCampaigns;
    }
    public void setTotalCampaigns(long totalCampaigns) {
        this.totalCampaigns = totalCampaigns;
    }
    public long getTotalContentPostings() {
        return totalContentPostings;
    }
    public void setTotalContentPostings(long totalContentPostings) {
        this.totalContentPostings = totalContentPostings;
    }
}
