package com.example.smessenger.service;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;

    public Chat get(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public void createByUser(Long userId, UUID userUuid, Chat chat) {
        Users existingUser = userService.checkUser(userId, userUuid);
        chat.getUsers().add(existingUser);
        chat.getModerators().add(existingUser);
        chatRepository.save(chat);
    }

    public void updateByMod(Long id, Long modId, UUID modUuid, ChatCreateDto chat) {
        userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        existingChat.setTitle(chat.getTitle());
        existingChat.setChatImage(chat.getChatImage());
        chatRepository.save(existingChat);
    }

    public void joinUser(Long id, Long userId, UUID userUuid) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Chat existingChat = get(id);
        if (existingChat.getBannedUsers().contains(existingUser))
            throw new ForbiddenException("User is banned in the chat");
        existingChat.getUsers().add(existingUser);
        chatRepository.save(existingChat);
    }

    public void leaveUser(Long id, Long userId, UUID userUuid) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Chat existingChat = get(id);
        existingChat.getUsers().remove(existingUser);
        existingChat.getModerators().remove(existingUser);
        chatRepository.save(existingChat);
    }

    public void kickUserByMod(Long id, Long userId, Long modId, UUID modUuid) {
        userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        Users existingUser = userService.get(userId);
        existingChat.getUsers().remove(existingUser);
        chatRepository.save(existingChat);
    }

    public void banUserByMod(Long id, Long userId, Long modId, UUID modUuid) {
        userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        Users existingUser = userService.get(userId);
        existingChat.getBannedUsers().add(existingUser);
        existingChat.getUsers().remove(existingUser);
        chatRepository.save(existingChat);
    }

    public void unbanUserByMod(Long id, Long userId, Long modId, UUID modUuid) {
        userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        Users existingUser = userService.get(userId);
        existingChat.getBannedUsers().remove(existingUser);
        chatRepository.save(existingChat);
    }

    public void setModeratorByMod(Long id, Long userId, Long modId, UUID modUuid) {
        userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        Users existingUser = userService.get(userId);
        existingChat.getModerators().add(existingUser);
        chatRepository.save(existingChat);
    }

    public void unsetModeratorByMod(Long id, Long userId, Long modId, UUID modUuid) {
        userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        Users existingUser = userService.get(userId);
        existingChat.getModerators().remove(existingUser);
        chatRepository.save(existingChat);
    }

    public void deleteByMod(Long id, Long modId, UUID modUuid) {
        userService.checkUser(modId, modUuid);
        chatRepository.deleteById(id);
    }
}
