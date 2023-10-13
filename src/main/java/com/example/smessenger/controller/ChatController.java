package com.example.smessenger.controller;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.dto.chat.ChatDto;
import com.example.smessenger.dto.chat.ChatInfoDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final Mapper mapper;

    @GetMapping(value = "/{id}")
    public ChatInfoDto get(@PathVariable Long id) {
        return mapper.toChatInfoDto(chatService.get(id));
    }

    @GetMapping(value = "/{id}/full")
    public ChatDto getByUser(@PathVariable Long id,
                             @RequestParam Long userId, @RequestParam UUID userUuid) {
        return mapper.toChatDto(chatService.getByUser(id, userId, userUuid));
    }

    @GetMapping(value = "/{id}/image")
    public ByteArrayResource getImage(@PathVariable Long id,
                             @RequestParam Long userId, @RequestParam UUID userUuid) {
        Byte[] image = chatService.getImage(id, userId, userUuid);
        return new ByteArrayResource(ArrayUtils.toPrimitive(image));
    }

    @GetMapping(value = "/{id}/messages")
    public Page<MessageDto> getMessages(@PathVariable Long id,
                                        @RequestParam Long userId, @RequestParam UUID userUuid,
                                        @RequestParam(defaultValue = "0") Pageable pageable) {
        return chatService.getMessages(id, userId, userUuid, pageable).map(mapper::toMessageDto);
    }

    @PostMapping("/{userId}&{userUuid}")
    public void createByUser(@PathVariable Long userId, @PathVariable UUID userUuid, @RequestBody ChatCreateDto chatCreateDto) {
        chatService.createByUser(userId, userUuid, mapper.toChat(chatCreateDto));
    }

    @PutMapping("/{id}/{modId}&{modUuid}")
    public void updateByMod(@PathVariable Long id, @PathVariable Long modId, @PathVariable UUID modUuid, @RequestBody ChatCreateDto chatCreateDto) {
        chatService.updateByMod(id, modId, modUuid, chatCreateDto);
    }

    @PutMapping("/{id}/join/{userId}/{friendId}&{friendUuid}")
    public void joinUser(@PathVariable Long id, @PathVariable Long userId, @PathVariable Long friendId, @PathVariable UUID friendUuid) {
        chatService.joinUser(id, userId, friendId, friendUuid);
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
