package com.api.dto.response;

import java.time.ZonedDateTime;

import com.api.model.Comment;

public class CommentResponse {
  private String commentId;
  private String userId;
  private String contentId;
  private String content;
  private String name;
  private String avatarUrl;
  private ZonedDateTime createdDate;

  public CommentResponse(String name, String avatarUrl, Comment comment) {
    this.commentId = comment.getCommentId();
    this.userId = comment.getUserId();
    this.contentId = comment.getContentId();
    this.content = comment.getContent();
    this.name = name;
    this.avatarUrl = avatarUrl;
    this.createdDate = comment.getCreatedDate();
  }

  public CommentResponse() {
  }

  public String getCommentId() {
    return commentId;
  }

  public String getUserId() {
    return userId;
  }

  public String getContentId() {
    return contentId;
  }

  public String getContent() {
    return content;
  }

  public String getName() {
    return name;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCommentId(String commentId) {
    this.commentId = commentId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setContentId(String contentId) {
    this.contentId = contentId;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }
}
