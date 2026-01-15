package com.flickrapp.backend.model.flickr;

public class FlickrSizesResponse {
    private FlickrSizes sizes;
    private String stat;

    public FlickrSizes getSizes() {
        return sizes;
    }

    public void setSizes(FlickrSizes sizes) {
        this.sizes = sizes;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}