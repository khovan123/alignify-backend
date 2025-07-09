package com.api.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class InfluencerStatisticsResponse {
    private List<Application> applications;
    private List<Invitation> invitations;
    private int totalApplications;
    private int totalApproved;
    private int totalPending;
    private int totalRejected;
    private double approvalRate;
    private int totalInvitations;
    private int totalAccepted;
    private int totalRejectedInvitations;
    private double invitationAcceptanceRate;

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

        public void setMonth(String month) {
            this.month = month;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setApproved(int approved) {
            this.approved = approved;
        }

        public void setPending(int pending) {
            this.pending = pending;
        }

        public void setRejected(int rejected) {
            this.rejected = rejected;
        }
    }
    @Data
    public static class Invitation {
        private String month;
        private int sent;
        private int accepted;
        private int rejected;

        public void setMonth(String month) {
            this.month = month;
        }

        public void setSent(int sent) {
            this.sent = sent;
        }

        public void setAccepted(int accepted) {
            this.accepted = accepted;
        }

        public void setRejected(int rejected) {
            this.rejected = rejected;
        }
    }
}
