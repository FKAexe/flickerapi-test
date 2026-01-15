package com.flickrapp.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlickrConfig {

    @Value("${flickr.api.key}")
    private String apiKey;

    @Value("${flickr.api.secret}")
    private String apiSecret;

    @Value("${flickr.api.base-url}")
    private String baseUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}