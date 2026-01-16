package com.flickrapp.backend.controller;

import com.flickrapp.backend.model.ImageDTO;
import com.flickrapp.backend.model.SearchResponse;
import com.flickrapp.backend.service.FlickrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:4200") //Para Angular
public class ImageController {

    private final FlickrService flickrService;

    public ImageController(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchImages(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Validaciones
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (page < 1 || size < 1 || size > 100) {
            return ResponseEntity.badRequest().build();
        }

        SearchResponse response = flickrService.searchImages(query, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageDetail(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ImageDTO image = flickrService.getImageDetail(id);

        if (image == null || image.getId() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(image);
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Obtener detalles de la imagen para conseguir la URL
        ImageDTO image = flickrService.getImageDetail(id);
        if (image == null || image.getLargeUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Descargar bytes de la imagen
            byte[] imageBytes = flickrService.downloadImageBytes(image.getLargeUrl());

            // Crear nombre de archivo seguro
            String filename = (image.getTitle() != null ? image.getTitle() : "image")
                    .replaceAll("[^a-zA-Z0-9.-]", "_") + ".jpg";

            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(imageBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}