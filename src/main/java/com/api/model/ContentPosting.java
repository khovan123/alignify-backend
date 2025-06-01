package com.api.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contentPostings")
public class ContentPosting {

    @Id
    private String contentId;

    private String influencerID;
    private String content;
    private String imageUrl;
    private String categoryId;

    @CreatedDate
    private Date timeStamp;
    private boolean isPublic;
    private String commentId;
    private int like;

    public ContentPosting() {
    }

    public ContentPosting(String contentId, String influencerID, String content, String imageUrl, String categoryId, Date timeStamp, boolean isPublic, String commentId, int like) {
        this.contentId = contentId;
        this.influencerID = influencerID;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.timeStamp = timeStamp;
        this.isPublic = isPublic;
        this.commentId = commentId;
        this.like = like;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getInfluencerID() {
        return influencerID;
    }

    public void setInfluencerID(String influencerID) {
        this.influencerID = influencerID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

}
