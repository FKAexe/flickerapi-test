package com.flickrapp.backend.model.flickr;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlickrPhotoInfo {
    private String id;
    private FlickrTitle title;
    private FlickrDescription description;
    private FlickrOwner owner;
    private FlickrTags tags;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public FlickrTitle getTitle() { return title; }
    public void setTitle(FlickrTitle title) { this.title = title; }

    public FlickrDescription getDescription() { return description; }
    public void setDescription(FlickrDescription description) { this.description = description; }

    public FlickrOwner getOwner() { return owner; }
    public void setOwner(FlickrOwner owner) { this.owner = owner; }

    public FlickrTags getTags() { return tags; }
    public void setTags(FlickrTags tags) { this.tags = tags; }
}