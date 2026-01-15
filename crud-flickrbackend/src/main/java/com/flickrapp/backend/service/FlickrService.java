package com.flickrapp.backend.service;

import com.flickrapp.backend.config.FlickrConfig;
import com.flickrapp.backend.model.ImageDTO;
import com.flickrapp.backend.model.SearchResponse;
import com.flickrapp.backend.model.flickr.FlickrPhoto;
import com.flickrapp.backend.model.flickr.FlickrResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlickrService {

    private final RestTemplate restTemplate;
    private final FlickrConfig flickrConfig;

    public FlickrService(RestTemplate restTemplate, FlickrConfig flickrConfig) {
        this.restTemplate = restTemplate;
        this.flickrConfig = flickrConfig;
    }

    public SearchResponse searchImages(String query, int page, int size) {
        String url = UriComponentsBuilder
                .fromUriString(flickrConfig.getBaseUrl())
                .queryParam("method", "flickr.photos.search")
                .queryParam("api_key", flickrConfig.getApiKey())
                .queryParam("text", query)
                .queryParam("page", page)
                .queryParam("per_page", size)
                .queryParam("format", "json")
                .queryParam("nojsoncallback", 1)
                .queryParam("extras", "description,owner_name,tags,url_m,url_l")
                .toUriString();

        FlickrResponse flickrResponse = restTemplate.getForObject(url, FlickrResponse.class);

        if (flickrResponse != null && flickrResponse.getPhotos() != null) {
            List<ImageDTO> images = flickrResponse.getPhotos().getPhoto()
                    .stream()
                    .map(this::convertToImageDTO)
                    .collect(Collectors.toList());

            return new SearchResponse(
                    images,
                    flickrResponse.getPhotos().getPage(),
                    flickrResponse.getPhotos().getPages(),
                    Integer.parseInt(flickrResponse.getPhotos().getTotal())
            );
        }

        return new SearchResponse(List.of(), 0, 0, 0);
    }

    public ImageDTO getImageDetail(String id) {
        String url = UriComponentsBuilder
                .fromUriString(flickrConfig.getBaseUrl())
                .queryParam("method", "flickr.photos.getInfo")
                .queryParam("api_key", flickrConfig.getApiKey())
                .queryParam("photo_id", id)
                .queryParam("format", "json")
                .queryParam("nojsoncallback", 1)
                .toUriString();

        return new ImageDTO();
    }

    private ImageDTO convertToImageDTO(FlickrPhoto photo) {
        ImageDTO dto = new ImageDTO();
        dto.setId(photo.getId());
        dto.setTitle(photo.getTitle());
        dto.setOwnerName(photo.getOwnerName());
        dto.setDescription(photo.getDescription() != null ?
                photo.getDescription().getContent() : "");
        dto.setTags(photo.getTags());
        dto.setThumbnailUrl(photo.getUrlM());
        dto.setLargeUrl(photo.getUrlL());
        return dto;
    }
}