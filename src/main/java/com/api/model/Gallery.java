package com.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "galleries")
public class Gallery {

    @Id
    private String galleryId;
    private List<String> images;

    @CreatedDate
    private LocalDateTime createdAt;

    public Gallery() {
        this.images = new ArrayList<>();
    }

    public Gallery(String galleryId, List<String> images, LocalDateTime createdAt) {
        this.galleryId = galleryId;
        this.images = images;
        this.createdAt = createdAt;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
