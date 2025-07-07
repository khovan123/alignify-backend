package com.api.model;

public class PlatformRequirementDetails {
  private String post_type;
  private int like;
  private int comment;
  private int share;

  public PlatformRequirementDetails() {
    this.like = 0;
    this.comment = 0;
    this.share = 0;
  }

  public PlatformRequirementDetails(String post_type, int like, int comment, int share) {
    this.post_type = post_type;
    this.like = like;
    this.comment = comment;
    this.share = share;
  }

  public PlatformRequirementDetails(String post_type, int like, int comment) {
    this.post_type = post_type;
    this.like = like;
    this.comment = comment;
  }

  public PlatformRequirementDetails(String post_type, int like) {
    this.post_type = post_type;
    this.like = like;
  }

  public String getPost_type() {
    return post_type;
  }

  public int getLike() {
    return like;
  }

  public int getComment() {
    return comment;
  }

  public int getShare() {
    return share;
  }

  public void setPost_type(String post_type) {
    this.post_type = post_type;
  }

  public void setLike(int like) {
    this.like = like;
  }

  public void setComment(int comment) {
    this.comment = comment;
  }

  public void setShare(int share) {
    this.share = share;
  }
}