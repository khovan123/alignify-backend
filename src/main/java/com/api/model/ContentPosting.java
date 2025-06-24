package com.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contentPostings")
public class ContentPosting {

    @Id
    private String contentId;

    private String contentName;
    private String userId;
    private String content;
    private String imageUrl;
    private List<String> categoryIds;

    @CreatedDate
    private LocalDateTime createdDate;
    private boolean isPublic;
    private int commentCount;
    private int likeCount;

    public ContentPosting() {
        this.likeCount = 0;
        this.commentCount = 0;
        this.isPublic = true;
        this.categoryIds = new ArrayList<>();
    }

    public ContentPosting(String contentId, String contentName, String userId, String content, String imageUrl,
            List<String> categoryIds, LocalDateTime createdDate, boolean isPublic, int commentCount, int likeCount) {
        this.contentId = contentId;
        this.contentName = contentName;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdDate = createdDate;
        this.isPublic = isPublic;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

}
