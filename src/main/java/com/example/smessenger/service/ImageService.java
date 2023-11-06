package com.example.smessenger.service;

import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Image;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.repository.ImageRepository;
import com.example.smessenger.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public Image get(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public Image get(Byte[] image) {
        return imageRepository.findByImage(image);
    }

    public Image create(Image image) {
        Image exists = get(image.getImage());
        if (exists != null) {
            return exists;
        }
        imageRepository.save(image);
        return image;
    }

    public void deleteIfUnused(Long id) {
        Image image = get(id);
        if (image.getChats().isEmpty() &&
            image.getMessages().isEmpty() &&
            image.getUsers().isEmpty()) {
            imageRepository.deleteById(id);
        }
    }
}
