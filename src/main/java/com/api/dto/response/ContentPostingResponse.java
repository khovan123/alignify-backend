package com.api.dto.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContentPostingResponse {

    private String contentId;
    private String userId;
    private String content;
    private String imageUrl;
    private List<Map<String, String>> categories;
    private Date createdDate;
    private boolean isPublic;
    private int commentCount;
    private int likeCount;

    public ContentPostingResponse() {
    }

    public ContentPostingResponse(String contentId, String userId, String content, String imageUrl, List<Map<String, String>> categories, Date createdDate, boolean isPublic, int commentCount, int likeCount) {
        this.contentId = contentId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categories = categories;
        this.createdDate = createdDate;
        this.isPublic = isPublic;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
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
