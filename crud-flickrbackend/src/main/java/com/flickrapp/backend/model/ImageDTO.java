package com.flickrapp.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageDTO {
    private String id;
    private String title;

    @JsonProperty("owner_name")
    private String ownerName;

    private String description;
    private String tags;

    @JsonProperty("url_m")
    private String thumbnailUrl;

    @JsonProperty("url_l")
    private String largeUrl;


    public ImageDTO() {}


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getLargeUrl() { return largeUrl; }
    public void setLargeUrl(String largeUrl) { this.largeUrl = largeUrl; }
}