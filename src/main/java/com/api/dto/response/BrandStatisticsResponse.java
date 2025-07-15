package com.api.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class BrandStatisticsResponse {
    private List<Invitation> invitations;
    private List<Application> applications;
    private List<Cost> costs;
    private int totalInvitations;
    private double acceptanceRate;
    private int totalApplications;
    private int currentMonthCost;
    private int totalPaid;
    private int totalPending;
    private int totalCost;
    private int avgCost;

    @Data
    public static class Invitation {
        private String month;
        private int sent;
        private int accepted;
        private int rejected;
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
    public static class Cost {
        private String month;
        private int paid;
        private int pending;
    }
}
