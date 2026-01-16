package com.flickrapp.backend.model.flickr;

import java.util.List;

public class FlickrPhotos {
    private int page;
    private int pages;
    private int perpage;
    private String total;
    private List<FlickrPhoto> photo;

    // Getters y Setters
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }

    public int getPerpage() { return perpage; }
    public void setPerpage(int perpage) { this.perpage = perpage; }

    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }

    public List<FlickrPhoto> getPhoto() { return photo; }
    public void setPhoto(List<FlickrPhoto> photo) { this.photo = photo; }
}