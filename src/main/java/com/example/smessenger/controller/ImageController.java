package com.example.smessenger.controller;

import com.example.smessenger.service.ImageService;
import lombok.RequiredArgsConstructor;
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
    public byte[] get(@PathVariable Long id) {
        return imageService.get(id).getImage();
    }
}
