package com.example.smessenger.controller;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.dto.chat.ChatInfoDto;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final Mapper mapper;

    @GetMapping(value = "/{id}")
    private ChatInfoDto get(@PathVariable Long id) {
        return mapper.toChatInfoDto(chatService.get(id));
    }

    @PostMapping("/{userId}&{userUuid}")
    public void createByUser(@PathVariable Long userId, @PathVariable UUID userUuid, @RequestBody ChatCreateDto chatCreateDto) {
        chatService.createByUser(userId, userUuid, mapper.toChat(chatCreateDto));
    }

    @PutMapping("/{id}/{modId}&{modUuid}")
    public void updateByMod(@PathVariable Long id, @PathVariable Long modId, @PathVariable UUID modUuid, @RequestBody ChatCreateDto chatCreateDto) {
        chatService.updateByMod(id, modId, modUuid, chatCreateDto);
    }

    @PutMapping("/{id}/join/{userId}&{userUuid}")
    public void joinUser(@PathVariable Long id, @PathVariable Long userId, @PathVariable UUID userUuid) {
        chatService.joinUser(id, userId, userUuid);
    }

    @PutMapping("/{id}/leave/{userId}&{userUuid}")
    public void leaveUser(@PathVariable Long id, @PathVariable Long userId, @PathVariable UUID userUuid) {
        chatService.leaveUser(id, userId, userUuid);
    }

    @PutMapping("/{id}/kick/{userId}/{modId}&{modUuid}")
    public void kickUserByMod(@PathVariable Long id, @PathVariable Long userId, @PathVariable Long modId, @PathVariable UUID modUuid) {
        chatService.kickUserByMod(id, userId, modId, modUuid);
    }

    @PutMapping("/{id}/ban/{userId}/{modId}&{modUuid}")
    public void banUserByMod(@PathVariable Long id, @PathVariable Long userId, @PathVariable Long modId, @PathVariable UUID modUuid) {
        chatService.banUserByMod(id, userId, modId, modUuid);
    }

    @PutMapping("/{id}/unban/{userId}/{modId}&{modUuid}")
    public void unbanUserByMod(@PathVariable Long id, @PathVariable Long userId, @PathVariable Long modId, @PathVariable UUID modUuid) {
        chatService.unbanUserByMod(id, userId, modId, modUuid);
    }

    @PutMapping("/{id}/mod/{userId}/{modId}&{modUuid}")
    public void setModeratorByMod(@PathVariable Long id, @PathVariable Long userId, @PathVariable Long modId, @PathVariable UUID modUuid) {
        chatService.setModeratorByMod(id, userId, modId, modUuid);
    }

    @PutMapping("/{id}/unmod/{userId}/{modId}&{modUuid}")
    public void unsetModeratorByMod(@PathVariable Long id, @PathVariable Long userId, @PathVariable Long modId, @PathVariable UUID modUuid) {
        chatService.unsetModeratorByMod(id, userId, modId, modUuid);
    }

    @DeleteMapping("/{id}/{modId}&{modUuid}")
    public void deleteByMod(@PathVariable Long id, @PathVariable Long modId, @PathVariable UUID modUuid) {
        chatService.deleteByMod(id, modId, modUuid);
    }
}
