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

    public Image create(Image image) {
        imageRepository.save(image);
        return image;
    }

    public void updateByAuthor(Long id, Long userId, UUID userUuid, String newText) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Message existingMessage = get(id);
        if (existingMessage.getAuthor() != existingUser)
            throw new ForbiddenException("User isn't author");
        if (newText != null && !newText.equals(existingMessage.getText())) {
            existingMessage.setIsEdited(true);
            existingMessage.setText(newText);
            messageRepository.save(existingMessage);
            simpMessagingService.convertAndSend("/chat/" + existingMessage.getChat().getId() + "/messageEdited", existingMessage);
        }
    }

    public void deleteByAuthorOrMod(Long id, Long userId, UUID userUuid) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Message existingMessage = get(id);
        if (existingMessage.getAuthor() == existingUser || existingMessage.getChat().getModerators().contains(existingUser)){
            messageRepository.deleteById(id);
            simpMessagingService.convertAndSend("/chat/" + existingMessage.getChat().getId() + "/messageDeleted", existingMessage);
        } else {
            throw new ForbiddenException("User isn't a moderator or an author");
        }
    }
}
