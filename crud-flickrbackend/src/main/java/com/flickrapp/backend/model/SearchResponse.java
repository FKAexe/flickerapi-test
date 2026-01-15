package com.flickrapp.backend.model;

import java.util.List;

public class SearchResponse {
    private List<ImageDTO> images;
    private int page;
    private int totalPages;
    private int totalResults;

    public SearchResponse() {}

    public SearchResponse(List<ImageDTO> images, int page, int totalPages, int totalResults) {
        this.images = images;
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public List<ImageDTO> getImages() { return images; }
    public void setImages(List<ImageDTO> images) { this.images = images; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }
}