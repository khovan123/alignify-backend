package com.api.model;

import java.util.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "galleries")
public class Gallery {

    @Id
    private String galleryId;
    private List<String> images;

    @CreatedDate
    private Date createAt;

    public Gallery() {
        this.images = new ArrayList<>();
    }

    public Gallery(String galleryId, List<String> images, Date createAt) {
        this.galleryId = galleryId;
        this.images = images;
        this.createAt = createAt;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

}
