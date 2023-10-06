package com.example.smessenger.service;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;

    public Chat get(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public Chat getByUser(Long id, Long userId, UUID userUuid) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Chat existingChat = get(id);
        if (!existingChat.getUsers().contains(existingUser)) {
            throw new ForbiddenException("User is not in the chat");
        }
        return chatRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public Set<MessageDto> getMessages(Long id, Long userId, UUID userUuid, Integer page, Integer size) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Chat existingChat = get(id);
        if (!existingChat.getUsers().contains(existingUser)) {
            throw new ForbiddenException("User is not in the chat");
        }
    }

    public void createByUser(Long userId, UUID userUuid, Chat chat) {
        Users existingUser = userService.checkUser(userId, userUuid);
        chat.setUsers(Collections.singleton(existingUser));
        chat.setModerators(Collections.singleton(existingUser));
        chatRepository.save(chat);
    }

    public void updateByMod(Long id, Long modId, UUID modUuid, ChatCreateDto chat) {
        Users mod = userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            existingChat.setTitle(chat.getTitle());
            existingChat.setChatImage(chat.getChatImage());
            chatRepository.save(existingChat);
        }
    }

    public void joinUser(Long id, Long userId, Long friendId, UUID friendUuid) {
        Users existingUser = userService.checkUser(userId);
        Users existingFriend = userService.checkUser(friendId, friendUuid);
        Chat existingChat = get(id);
        //TODO check if user is even in chat
        if (existingChat.getBannedUsers().contains(existingUser))
            throw new ForbiddenException("User is banned in the chat");
        if (!existingFriend.getFriends().contains(existingUser))
            throw new ForbiddenException("User is not friend");
        if (existingChat.getUsers().add(existingUser)) {
            chatRepository.save(existingChat);
        }
    }

    public void leaveUser(Long id, Long userId, UUID userUuid) {
        Users existingUser = userService.checkUser(userId, userUuid);
        Chat existingChat = get(id);
        if (removeUser(existingChat, existingUser)) {
            if (existingChat.getUsers().size() == 0) {
                chatRepository.deleteById(id);
            } else {
                chatRepository.save(existingChat);
            }
        }
    }

    public void kickUserByMod(Long id, Long userId, Long modId, UUID modUuid) {
        Users mod =  userService.checkUser(modId, modUuid);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (removeUser(existingChat, existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void banUserByMod(Long id, Long userId, Long modId, UUID modUuid) {
        Users mod =  userService.checkUser(modId, modUuid);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (existingChat.getBannedUsers().add(existingUser)) {
                removeUser(existingChat, existingUser);
                chatRepository.save(existingChat);
            }
        }
    }

    public void unbanUserByMod(Long id, Long userId, Long modId, UUID modUuid) {
        Users mod =  userService.checkUser(modId, modUuid);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (existingChat.getBannedUsers().remove(existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void setModeratorByMod(Long id, Long userId, Long modId, UUID modUuid) {
        Users mod =  userService.checkUser(modId, modUuid);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod) && existingChat.getUsers().contains(existingUser)) {
            if (existingChat.getModerators().add(existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void unsetModeratorByMod(Long id, Long userId, Long modId, UUID modUuid) {
        Users mod =  userService.checkUser(modId, modUuid);
        Users existingUser = userService.get(userId);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            if (existingChat.getModerators().remove(existingUser)) {
                chatRepository.save(existingChat);
            }
        }
    }

    public void deleteByMod(Long id, Long modId, UUID modUuid) {
        Users mod =  userService.checkUser(modId, modUuid);
        Chat existingChat = get(id);
        if (existingChat.getModerators().contains(mod)) {
            chatRepository.deleteById(id);
        }
    }

    private boolean removeUser(Chat chat, Users user) {
        chat.getModerators().remove(user);
        return chat.getUsers().remove(user);
    }
}
