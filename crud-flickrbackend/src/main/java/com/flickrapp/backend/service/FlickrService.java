package com.flickrapp.backend.service;

import com.flickrapp.backend.config.FlickrConfig;
import com.flickrapp.backend.model.ImageDTO;
import com.flickrapp.backend.model.SearchResponse;
import com.flickrapp.backend.model.flickr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlickrService {



    private static final Logger logger = LoggerFactory.getLogger(FlickrService.class);

    private final RestTemplate restTemplate;
    private final FlickrConfig flickrConfig;

    public FlickrService(RestTemplate restTemplate, FlickrConfig flickrConfig) {
        this.restTemplate = restTemplate;
        this.flickrConfig = flickrConfig;
    }

    /**
     * Buscar imágenes por query
     */
    public SearchResponse searchImages(String query, int page, int size) {
        // Validaciones
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than 0");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }

        logger.info("Searching images: query='{}', page={}, size={}", query, page, size);

        try {
            String url = buildSearchUrl(query, page, size);
            FlickrResponse flickrResponse = restTemplate.getForObject(url, FlickrResponse.class);

            if (flickrResponse == null || !"ok".equals(flickrResponse.getStat())) {
                logger.error("Invalid response from Flickr API");
                throw new RuntimeException("Invalid response from Flickr");
            }

            return mapToSearchResponse(flickrResponse);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Flickr API error: {}", e.getMessage());
            throw e;
        } catch (ResourceAccessException e) {
            logger.error("Network error calling Flickr: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtener detalle de una imagen por ID
     * Usa flickr.photos.getInfo (metadata) + flickr.photos.getSizes (URLs)
     */
    public ImageDTO getImageDetail(String id) {
        logger.info("Getting detail for image id: {}", id);

        // URL para obtener información de la foto
        String urlInfo = UriComponentsBuilder
                .fromUriString(flickrConfig.getBaseUrl())
                .queryParam("method", "flickr.photos.getInfo")
                .queryParam("api_key", flickrConfig.getApiKey())
                .queryParam("photo_id", id)
                .queryParam("format", "json")
                .queryParam("nojsoncallback", 1)
                .toUriString();

        // URL para obtener tamaños disponibles
        String urlSizes = UriComponentsBuilder
                .fromUriString(flickrConfig.getBaseUrl())
                .queryParam("method", "flickr.photos.getSizes")
                .queryParam("api_key", flickrConfig.getApiKey())
                .queryParam("photo_id", id)
                .queryParam("format", "json")
                .queryParam("nojsoncallback", 1)
                .toUriString();

        try {
            // Llamada 1: Obtener información (título, descripción, autor, tags)
            logger.debug("Fetching photo info from: {}", urlInfo);
            FlickrPhotoInfoResponse infoResponse = restTemplate.getForObject(
                    urlInfo, FlickrPhotoInfoResponse.class);

            if (infoResponse == null || !"ok".equals(infoResponse.getStat())) {
                logger.warn("Invalid info response for image id: {}", id);
                return null;
            }

            if (infoResponse.getPhoto() == null) {
                logger.warn("Photo info is null for id: {}", id);
                return null;
            }

            // Llamada 2: Obtener tamaños (URLs de imágenes)
            logger.debug("Fetching photo sizes from: {}", urlSizes);
            FlickrSizesResponse sizesResponse = restTemplate.getForObject(
                    urlSizes, FlickrSizesResponse.class);

            if (sizesResponse == null || !"ok".equals(sizesResponse.getStat())) {
                logger.warn("Invalid sizes response for image id: {}", id);
                // Continuar sin URLs si falla getSizes
            }

            // Combinar ambas respuestas en un ImageDTO
            return convertPhotoInfoToImageDTO(infoResponse.getPhoto(), sizesResponse);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                logger.warn("Image not found with id: {}", id);
            } else {
                logger.error("Client error getting image detail for id {}: {}", id, e.getMessage());
            }
            return null;
        } catch (Exception e) {
            logger.error("Error getting image detail for id: {}", id, e);
            return null;
        }
    }

    // ==================== MÉTODOS PRIVADOS ====================

    /**
     * Construir URL de búsqueda
     */
    private String buildSearchUrl(String query, int page, int size) {
        return UriComponentsBuilder
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
    }

    /**
     * Mapear FlickrResponse a SearchResponse
     */
    private SearchResponse mapToSearchResponse(FlickrResponse flickrResponse) {
        if (flickrResponse.getPhotos() == null) {
            return new SearchResponse(List.of(), 0, 0, 0);
        }

        List<ImageDTO> images = flickrResponse.getPhotos().getPhoto()
                .stream()
                .map(this::convertToImageDTO)
                .filter(dto -> dto != null)  // Filtrar imágenes sin URL
                .collect(Collectors.toList());

        return new SearchResponse(
                images,
                flickrResponse.getPhotos().getPage(),
                flickrResponse.getPhotos().getPages(),
                parseTotal(flickrResponse.getPhotos().getTotal())
        );
    }

    /**
     * Convertir FlickrPhoto (de búsqueda) a ImageDTO
     */
    private ImageDTO convertToImageDTO(FlickrPhoto photo) {
        // LOG TEMPORAL
        logger.info("=== Processing photo: {} ===", photo.getId());
        logger.info("photo.getUrlM(): {}", photo.getUrlM());
        logger.info("photo.getUrlL(): {}", photo.getUrlL());

        // Solo convertir si tiene al menos url_m
        if (photo.getUrlM() == null || photo.getUrlM().isEmpty()) {
            logger.warn("⚠️ Skipping photo {} - no thumbnail URL", photo.getId());
            return null;
        }

        ImageDTO dto = new ImageDTO();
        dto.setId(photo.getId());
        dto.setTitle(photo.getTitle() != null && !photo.getTitle().isEmpty()
                ? photo.getTitle() : "Untitled");
        dto.setOwnerName(photo.getOwnerName() != null && !photo.getOwnerName().isEmpty()
                ? photo.getOwnerName() : "Unknown");
        dto.setDescription(photo.getDescription() != null ?
                photo.getDescription().getContent() : "");
        dto.setTags(photo.getTags() != null ? photo.getTags() : "");
        dto.setThumbnailUrl(photo.getUrlM());
        dto.setLargeUrl(photo.getUrlL() != null && !photo.getUrlL().isEmpty()
                ? photo.getUrlL() : photo.getUrlM());

        // LOG TEMPORAL
        logger.info("✅ DTO created - thumbnailUrl: {}", dto.getThumbnailUrl());
        logger.info("✅ DTO created - largeUrl: {}", dto.getLargeUrl());

        return dto;
    }

    /**
     * Convertir FlickrPhotoInfo (de getInfo) + FlickrSizesResponse a ImageDTO
     */
    private ImageDTO convertPhotoInfoToImageDTO(FlickrPhotoInfo photo, FlickrSizesResponse sizesResponse) {
        ImageDTO dto = new ImageDTO();

        // ===== INFORMACIÓN BÁSICA (de getInfo) =====
        dto.setId(photo.getId());

        // Título
        if (photo.getTitle() != null && photo.getTitle().getContent() != null) {
            dto.setTitle(photo.getTitle().getContent());
        } else {
            dto.setTitle("Untitled");
        }

        // Autor
        if (photo.getOwner() != null) {
            String ownerName = photo.getOwner().getRealname();
            if (ownerName == null || ownerName.isEmpty()) {
                ownerName = photo.getOwner().getUsername();
            }
            dto.setOwnerName(ownerName != null ? ownerName : "Unknown");
        } else {
            dto.setOwnerName("Unknown");
        }

        // Descripción
        if (photo.getDescription() != null && photo.getDescription().getContent() != null) {
            dto.setDescription(photo.getDescription().getContent());
        } else {
            dto.setDescription("");
        }

        // Tags
        if (photo.getTags() != null && photo.getTags().getTag() != null && !photo.getTags().getTag().isEmpty()) {
            String tags = photo.getTags().getTag().stream()
                    .map(FlickrTag::getContent)
                    .filter(tag -> tag != null && !tag.isEmpty())
                    .collect(Collectors.joining(" "));
            dto.setTags(tags);
        } else {
            dto.setTags("");
        }

        // ===== URLs DE IMÁGENES (de getSizes) =====
        if (sizesResponse != null &&
                sizesResponse.getSizes() != null &&
                sizesResponse.getSizes().getSize() != null) {

            List<FlickrSize> sizes = sizesResponse.getSizes().getSize();

            // Buscar tamaño Medium para thumbnail (500px)
            sizes.stream()
                    .filter(s -> "Medium".equals(s.getLabel()) || "Medium 500".equals(s.getLabel()))
                    .findFirst()
                    .ifPresent(s -> dto.setThumbnailUrl(s.getSource()));

            // Si no hay Medium, usar Small 320
            if (dto.getThumbnailUrl() == null) {
                sizes.stream()
                        .filter(s -> "Small 320".equals(s.getLabel()) || "Small".equals(s.getLabel()))
                        .findFirst()
                        .ifPresent(s -> dto.setThumbnailUrl(s.getSource()));
            }

            // Si aún no hay, usar el primer tamaño >= 200px
            if (dto.getThumbnailUrl() == null) {
                sizes.stream()
                        .filter(s -> s.getWidth() >= 200)
                        .findFirst()
                        .ifPresent(s -> dto.setThumbnailUrl(s.getSource()));
            }

            // Buscar tamaño Large para imagen grande (1024px - 2048px)
            sizes.stream()
                    .filter(s -> "Large".equals(s.getLabel()) ||
                            "Large 1600".equals(s.getLabel()) ||
                            "Large 2048".equals(s.getLabel()))
                    .findFirst()
                    .ifPresent(s -> dto.setLargeUrl(s.getSource()));

            // Si no hay Large, usar Original
            if (dto.getLargeUrl() == null) {
                sizes.stream()
                        .filter(s -> "Original".equals(s.getLabel()))
                        .findFirst()
                        .ifPresent(s -> dto.setLargeUrl(s.getSource()));
            }

            // Si aún no hay largeUrl, usar el tamaño más grande disponible
            if (dto.getLargeUrl() == null && !sizes.isEmpty()) {
                FlickrSize largest = sizes.stream()
                        .max((s1, s2) -> Integer.compare(s1.getWidth(), s2.getWidth()))
                        .orElse(sizes.get(sizes.size() - 1));
                dto.setLargeUrl(largest.getSource());
            }

            // Si no hay thumbnailUrl, usar largeUrl como fallback
            if (dto.getThumbnailUrl() == null && dto.getLargeUrl() != null) {
                dto.setThumbnailUrl(dto.getLargeUrl());
            }

            logger.debug("Image {} URLs - Thumbnail: {}, Large: {}",
                    photo.getId(), dto.getThumbnailUrl(), dto.getLargeUrl());
        } else {
            logger.warn("No sizes available for image {}", photo.getId());
        }

        return dto;
    }

    /**
     * Parsear total de resultados
     */
    private int parseTotal(String total) {
        try {
            return Integer.parseInt(total);
        } catch (NumberFormatException e) {
            logger.warn("Could not parse total: {}", total);
            return 0;
        }
    }
    //Descargar imagen
    public byte[] downloadImageBytes(String imageUrl) throws IOException {
        logger.info("Downloading image from: {}", imageUrl);

        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Añadir headers para que Flickr acepte la request
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        connection.setRequestProperty("Referer", "https://www.flickr.com/");

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] imageBytes = inputStream.readAllBytes();
            logger.info("Successfully downloaded {} bytes", imageBytes.length);
            return imageBytes;
        } finally {
            connection.disconnect();
        }
    }
}

