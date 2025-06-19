package com.api.dto.response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContentPostingResponse {

    private String contentId;
    private String contentName;
    private String userId;
    private String userName;
    private String content;
    private String imageUrl;
    private List<Map<String, String>> categories;
    private LocalDateTime createdDate;
    private boolean isPublic;
    private int commentCount;
    private int likeCount;

    public ContentPostingResponse() {
    }

    public ContentPostingResponse(String contentId, String contentName, String userId, String userName, String content, String imageUrl, List<Map<String, String>> categories, LocalDateTime createdDate, boolean isPublic, int commentCount, int likeCount) {
        this.contentId = contentId;
        this.contentName = contentName;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categories = categories;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Map<String, String>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, String>> categories) {
        this.categories = categories;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
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
