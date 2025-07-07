package com.api.dto.statistic;

public class ForumData {
    private String month;
    private long posts;
    private long likes;
    private long comments;
    private long shares;
    private long views;

    public ForumData() {}
    public ForumData(String month, long posts, long likes, long comments, long shares, long views) {
        this.month = month;
        this.posts = posts;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
        this.views = views;
    }
    
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}