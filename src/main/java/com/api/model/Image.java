package com.api.model;

import java.util.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
public class Image {

    @Id
    private String imageId;
    private String imageUrl;
    @CreatedDate
    private Date createAt;

    public Image() {
    }

    public Image(String imageId, String imageUrl, Date createAt) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

}
