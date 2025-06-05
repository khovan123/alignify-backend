package com.api.model;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contentPostings")
public class ContentPosting {

    @Id
    private String contentId;

    private String userId;
    private String content;
    private String imageUrl;
    private List<String> categoryIds;

    @CreatedDate
    private Date timestamp;
    private boolean isPublic;
    private List<String> commentIds;
    private int like;

    public ContentPosting() {
        this.like=0;
        this.isPublic=true;
    }

    public ContentPosting(String contentId, String userId, String content, String imageUrl, List<String> categoryIds, Date timestamp, boolean isPublic, List<String> commentIds, int like) {
        this.contentId = contentId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.timestamp = timestamp;
        this.isPublic = isPublic;
        this.commentIds = commentIds;
        this.like = like;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

}
