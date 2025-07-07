package com.api.dto.statistic;

public class InvitationData {
    private String month;
    private long received;
    private long accepted;
    private long rejected;

    public InvitationData() {}
    public InvitationData(String month, long received, long accepted, long rejected) {
        this.month = month;
        this.received = received;
        this.accepted = accepted;
        this.rejected = rejected;
    }
    
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getReceived() {
        return received;
    }

    public void setReceived(long received) {
        this.received = received;
    }

    public long getAccepted() {
        return accepted;
    }

    public void setAccepted(long accepted) {
        this.accepted = accepted;
    }

    public long getRejected() {
        return rejected;
    }

    public void setRejected(long rejected) {
        this.rejected = rejected;
    }
}