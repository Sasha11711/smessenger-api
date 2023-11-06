package com.example.smessenger.service;

import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;
    private final ImageService imageService;
    private final SimpMessagingTemplate simpMessagingService;

    public Message get(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public Message get(Long id, Long userId, UUID userUuid) {
        userService.checkUser(userId, userUuid);
        return get(id);
    }

    public Message createByUserInChat(Long userId, UUID userUuid, Long chatId, Message message) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Chat existingChat = chatService.get(chatId);
        if (!existingChat.getUsers().contains(existingUser))
            throw new ForbiddenException("User is not in the chat");
        message.setChat(existingChat);
        message.setAuthor(existingUser);
        messageRepository.save(message);
        simpMessagingService.convertAndSend("/chat/" + chatId + "/messageSent", message);
        return message;
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
            imageService.deleteIfUnused(existingMessage.getEmbed().getId());
            simpMessagingService.convertAndSend("/chat/" + existingMessage.getChat().getId() + "/messageDeleted", existingMessage);
        } else {
            throw new ForbiddenException("User isn't a moderator or an author");
        }
    }
}
