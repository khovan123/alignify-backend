
package com.api.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "likes")
public class Likes {
    @Id
    private String likeId;
    private String userId;
    private String contentId;
    @CreatedDate
    private Date createdAt;

    public Likes() {
    }

    public Likes(String userId, String contentId) {
        this.userId = userId;
        this.contentId = contentId;
    }

    public Likes(String likeId, String userId, String contentId, Date createdAt) {
        this.likeId = likeId;
        this.userId = userId;
        this.contentId = contentId;
        this.createdAt = createdAt;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    
}
