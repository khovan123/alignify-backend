package com.api.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("comments")
public class Comment {

    @Id
    private String commentId;
    private String userId;
    private String contentId;
    private String content;
    private int like;

    @CreatedDate
    private Date createdDate;

    public Comment() {
    }

    public Comment(String commentId, String content, int like, String userId, String contentId, Date createdDate) {
        this.commentId = commentId;
        this.content = content;
        this.like = like;
        this.userId = userId;
        this.contentId = contentId;
        this.createdDate = createdDate;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
