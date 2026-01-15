package com.flickrapp.backend.model.flickr;

public class FlickrPhotoInfoResponse {
    private FlickrPhotoInfo photo;
    private String stat;

    public FlickrPhotoInfo getPhoto() { return photo; }
    public void setPhoto(FlickrPhotoInfo photo) { this.photo = photo; }

    public String getStat() { return stat; }
    public void setStat(String stat) { this.stat = stat; }
}