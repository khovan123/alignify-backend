package com.api.dto.statistic;

import java.util.List;

public class InfluencerStatsResponse {
    private List<InvitationData> invitationData;
    private List<ApplicationData> applicationData;
    private List<IncomeData> incomeData;
    private List<ForumData> forumData;

    public InfluencerStatsResponse() {}

    public InfluencerStatsResponse(List<InvitationData> invitationData,
                                   List<ApplicationData> applicationData,
                                   List<IncomeData> incomeData,
                                   List<ForumData> forumData) {
        this.invitationData = invitationData;
        this.applicationData = applicationData;
        this.incomeData = incomeData;
        this.forumData = forumData;
    }

    public List<InvitationData> getInvitationData() {
        return invitationData;
    }

    public void setInvitationData(List<InvitationData> invitationData) {
        this.invitationData = invitationData;
    }

    public List<ApplicationData> getApplicationData() {
        return applicationData;
    }

    public void setApplicationData(List<ApplicationData> applicationData) {
        this.applicationData = applicationData;
    }

    public List<IncomeData> getIncomeData() {
        return incomeData;
    }

    public void setIncomeData(List<IncomeData> incomeData) {
        this.incomeData = incomeData;
    }

    public List<ForumData> getForumData() {
        return forumData;
    }

    public void setForumData(List<ForumData> forumData) {
        this.forumData = forumData;
    }
}