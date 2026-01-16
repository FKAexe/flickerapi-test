package com.flickrapp.backend.model.flickr;

public class FlickrSize {
    private String label;      // "Square", "Medium", "Large", "Original", etc.
    private int width;
    private int height;
    private String source;     // URL de la imagen
    private String url;        // URL de la página de Flickr
    private String media;      // "photo"

    // Getters y Setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}