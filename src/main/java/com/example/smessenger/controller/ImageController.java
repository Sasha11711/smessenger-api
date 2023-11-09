package com.example.smessenger.controller;

import com.example.smessenger.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> get(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", MediaType.IMAGE_PNG_VALUE)
                .body(imageService.get(id).getImage());
    }
}
