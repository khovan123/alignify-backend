package com.api.dto.statistic;

public class ApplicationData {
    private String month;
    private long sent;
    private long accepted;
    private long rejected;

    public ApplicationData() {}
    public ApplicationData(String month, long sent, long accepted, long rejected) {
        this.month = month;
        this.sent = sent;
        this.accepted = accepted;
        this.rejected = rejected;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getSent() {
        return sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
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