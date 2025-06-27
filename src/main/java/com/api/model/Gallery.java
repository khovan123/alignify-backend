package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "galleries")
public class Gallery {

    @Id
    private String galleryId;
    private List<String> images;
    private ZonedDateTime createdAt;

    public Gallery() {
        this.images = new ArrayList<>();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Gallery(String galleryId, List<String> images, ZonedDateTime createdAt) {
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
