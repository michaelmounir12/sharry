package com.example.app.sharry.dto;

import com.example.app.sharry.model.Image;

public class ImageDto {
    private long id;
    private String mediaUrl;
    private boolean isPrivate;
    private String originalFileName;

    // constructor
    public ImageDto(Image image) {
        this.id = image.getId();
        this.mediaUrl = image.getMediaUrl();
        this.isPrivate = image.isPrivate();
        this.originalFileName = image.getOriginalFileName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
