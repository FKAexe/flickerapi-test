package com.flickrapp.backend.model.flickr;

import java.util.List;

public class FlickrTags {
    private List<FlickrTag> tag;

    public List<FlickrTag> getTag() {
        return tag;
    }

    public void setTag(List<FlickrTag> tag) {
        this.tag = tag;
    }
}