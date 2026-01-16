package com.flickrapp.backend.model.flickr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlickrPhoto {
    private String id;
    private String title;

    @JsonProperty("owner_name")
    private String ownerName;

    private FlickrDescription description;
    private String tags;

    @JsonProperty("url_m")
    private String urlM;

    @JsonProperty("url_l")
    private String urlL;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public FlickrDescription getDescription() { return description; }
    public void setDescription(FlickrDescription description) { this.description = description; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getUrlM() { return urlM; }
    public void setUrlM(String urlM) { this.urlM = urlM; }

    public String getUrlL() { return urlL; }
    public void setUrlL(String urlL) { this.urlL = urlL; }
}