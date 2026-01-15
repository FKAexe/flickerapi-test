package com.flickrapp.backend.model.flickr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlickrDescription {
    @JsonProperty("_content")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}