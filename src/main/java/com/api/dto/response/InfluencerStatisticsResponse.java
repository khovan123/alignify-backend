package com.api.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class InfluencerStatisticsResponse {
    private List<Application> applications;
    private List<Invitation> invitations;
    private List<Income> income;
    private List<Forum> forum;
    private int totalApplications;
    private int totalApproved;
    private int totalPending;
    private int totalRejected;
    private double approvalRate;
    private int totalInvitations;
    private int totalAccepted;
    private int totalRejectedInvitations;
    private double invitationAcceptanceRate;
    private int currentMonthIncome;
    private int totalForumPosts;
    private int totalIncome;
    private int totalCampaigns;
    private double avgIncomePerCampaign;
    private double avgIncome;

    public void setApplications(List<Application> applicationStats) {
        this.applications = applicationStats;
    }

    public void setInvitations(List<Invitation> invitationStats) {
        this.invitations = invitationStats;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public void setTotalApproved(int totalApproved) {
        this.totalApproved = totalApproved;
    }

    public void setTotalPending(int totalPending) {
        this.totalPending = totalPending;
    }

    public void setTotalRejected(int totalRejected) {
        this.totalRejected = totalRejected;
    }

    public void setApprovalRate(double approvalRate) {
        this.approvalRate = approvalRate;
    }

    public void setTotalInvitations(int totalInvitations) {
        this.totalInvitations = totalInvitations;
    }

    public void setTotalAccepted(int totalAccepted) {
        this.totalAccepted = totalAccepted;
    }

    public void setTotalRejectedInvitations(int totalRejectedInvitations) {
        this.totalRejectedInvitations = totalRejectedInvitations;
    }

    public void setInvitationAcceptanceRate(double invitationAcceptanceRate) {
        this.invitationAcceptanceRate = invitationAcceptanceRate;
    }

    @Data
    public static class Application {
        private String month;
        private int total;
        private int approved;
        private int pending;
        private int rejected;
    }

    @Data
    public static class Invitation {
        private String month;
        private int sent;
        private int accepted;
        private int rejected;
    }

    @Data
    public static class Income {
        private String month;
        private int income;
        private int campaigns;
    }

    @Data
    public static class Forum {
        private String month;
        private int posts;
        private int likes;
        private int comments;
        private int shares;
        private int views;
    }
}
