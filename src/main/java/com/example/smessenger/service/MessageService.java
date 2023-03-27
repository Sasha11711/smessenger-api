package com.example.smessenger.service;

import com.example.smessenger.dto.message.MessageCreateDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    public Message get(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public Message get(Long id, Long userId, UUID userUuid) {
        userService.checkUser(userId, userUuid);
        return get(id);
    }

    public void createByUserInChat(Long userId, UUID userUuid, Long chatId, Message message) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Chat existingChat = chatService.get(chatId);
        if (!existingChat.getUsers().contains(existingUser))
            throw new ForbiddenException("User is not in the chat");
        messageRepository.save(message);
    }

    public void updateByAuthor(Long id, Long userId, UUID userUuid, MessageCreateDto messageInfoDto) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Message existingMessage = get(id);
        if (existingMessage.getAuthor() != existingUser)
            throw new ForbiddenException("User isn't author");
        existingMessage.setIsEdited(true);
        existingMessage.setText(messageInfoDto.getText());
        existingMessage.setEmbedImage(messageInfoDto.getEmbedImage());
        messageRepository.save(existingMessage);
    }

    public void deleteByAuthor(Long id, Long userId, UUID userUuid) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Message existingMessage = get(id);
        if (existingMessage.getAuthor() != existingUser)
            throw new ForbiddenException("User isn't author");
        messageRepository.deleteById(id);
    }

    public void deleteByMod(Long id, Long modId, UUID modUuid) {
        Users existingMod = userService.checkUser(modId, modUuid);
        Message existingMessage = get(id);
        if (!existingMessage.getChat().getModerators().contains(existingMod))
            throw new ForbiddenException("User isn't mod");
        messageRepository.deleteById(id);
    }
}
