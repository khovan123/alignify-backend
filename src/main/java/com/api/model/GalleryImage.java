package com.api.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "galleryImages")
public class GalleryImage {

    @Id
    private String imageId;
    private String imageUrl;
    @CreatedDate
    private LocalDateTime createdAt;

    public GalleryImage() {
    }

    public GalleryImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public GalleryImage(String imageId, String imageUrl, LocalDateTime createdAt) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
