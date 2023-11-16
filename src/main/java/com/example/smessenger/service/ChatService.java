package com.example.smessenger.service;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

import static com.example.smessenger.mapper.Mapper.toPage;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ImageService imageService;

    public Chat get(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public Chat getByUser(Long id, String token) {
        Users existingUser = userService.checkUser(token);
        Chat existingChat = get(id);
        if (!existingChat.getUsers().contains(existingUser)) {
            throw new ForbiddenException("User is not in the chat");
        }
        return existingChat;
    }

    public Page<Message> getMessages(Long id, String token, Pageable pageable) {
        Users existingUser = userService.checkUser(token);
        Chat existingChat = get(id);
        if (!existingChat.getUsers().contains(existingUser)) {
            throw new ForbiddenException("User is not in the chat");
        }
        return toPage(existingChat.getMessages(), pageable);
    }

    public Chat createByUser(String token, ChatCreateDto chatCreateDto) {
        Users existingUser = userService.checkUser(token);
        Chat chat = new Chat();
        chat.setTitle(chatCreateDto.getTitle());
        byte[] image = Mapper.toByteArray(chatCreateDto.getImage());
        if (image != null)
            chat.setImage(imageService.create(image));
        else {
            chat.setImage(imageService.get(1L));
        }
        chat.setUsers(Collections.singleton(existingUser));
        chat.setModerators(Collections.singleton(existingUser));
        chatRepository.save(chat);
        return chat;
    }

    public void updateByMod(Long id, String token, ChatCreateDto chat) {
        Users mod = userService.checkUser(token);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            String newTitle = chat.getTitle();
            byte[] newImage = Mapper.toByteArray(chat.getImage());
            boolean titleChangeable = newTitle != null && !newTitle.equals(existingChat.getTitle());
            boolean imageChangeable = newImage != null && !Arrays.equals(newImage, existingChat.getImage().getImage());
            if (titleChangeable || imageChangeable) {
                if (titleChangeable) existingChat.setTitle(newTitle);
                if (imageChangeable) {
                    Long oldImageId = existingChat.getImage().getId();
                    existingChat.setImage(imageService.create(newImage));
                    chatRepository.save(existingChat);
                    imageService.deleteIfUnused(oldImageId);
                } else chatRepository.save(existingChat);
            }
        }
    }

    public void addUser(Long id, Long userId, String token) {
        Users existingUser = userService.checkUser(userId);
        Users existingFriend = userService.checkUser(token);
        Chat existingChat = getByUser(id, token);
        if (existingChat.getBannedUsers().contains(existingUser))
            throw new ForbiddenException("User is banned in the chat");
        if (!existingFriend.getFriends().contains(existingUser))
            throw new ForbiddenException("User is not friend");
        if (existingChat.getUsers().add(existingUser)) {
            chatRepository.save(existingChat);
        }
    }

    public void leaveUser(Long id, String token) {
        Users existingUser = userService.checkUser(token);
        Chat existingChat = get(id);
        if (removeUser(existingChat, existingUser)) {
            if (existingChat.getUsers().isEmpty()) {
                chatRepository.deleteById(id);
            } else {
                chatRepository.save(existingChat);
            }
        }
    }

    public void kickUserByMod(Long id, Long userId, String token) {
        Users mod = userService.checkUser(token);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (removeUser(existingChat, existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void banUserByMod(Long id, Long userId, String token) {
        Users mod = userService.checkUser(token);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (existingChat.getBannedUsers().add(existingUser)) {
                removeUser(existingChat, existingUser);
                chatRepository.save(existingChat);
            }
        }
    }

    public void unbanUserByMod(Long id, Long userId, String token) {
        Users mod = userService.checkUser(token);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (existingChat.getBannedUsers().remove(existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void setModeratorByMod(Long id, Long userId, String token) {
        Users mod = userService.checkUser(token);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod) && existingChat.getUsers().contains(existingUser)) {
            if (existingChat.getModerators().add(existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void unsetModeratorByMod(Long id, Long userId, String token) {
        Users mod = userService.checkUser(token);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (existingChat.getModerators().remove(existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void deleteByMod(Long id, String token) {
        Users mod = userService.checkUser(token);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            chatRepository.deleteById(id);
            imageService.deleteIfUnused(existingChat.getImage().getId());
        }
    }

    private boolean removeUser(Chat chat, Users user) {
        chat.getModerators().remove(user);
        return chat.getUsers().remove(user);
    }
}
