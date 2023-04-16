package com.example.smessenger.controller;

import com.example.smessenger.dto.message.MessageCreateDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;
    private final Mapper mapper;

    @GetMapping(value = "/{id}/{userId}&{userUuid}")
    private MessageDto get(@PathVariable Long id, @PathVariable Long userId, @PathVariable UUID userUuid) {
        return mapper.toMessageDto(messageService.get(id, userId, userUuid));
    }

    @PostMapping("/{userId}&{userUuid}/{chatId}")
    public void createByUserInChat(@PathVariable Long userId, @PathVariable UUID userUuid, @PathVariable Long chatId, @RequestBody MessageCreateDto messageCreateDto) {
        messageService.createByUserInChat(userId, userUuid, chatId, mapper.toMessage(messageCreateDto));
    }

    @PutMapping("/{id}/{userId}&{userUuid}/{newText}")
    public void updateByAuthor(@PathVariable Long id, @PathVariable Long userId, @PathVariable UUID userUuid, @PathVariable String newText) {
        messageService.updateByAuthor(id, userId, userUuid, newText);
    }

    @DeleteMapping("/{id}/{userId}&{userUuid}")
    public void deleteByAuthorOrMod(@PathVariable Long id, @PathVariable Long userId, @PathVariable UUID userUuid) {
        messageService.deleteByAuthorOrMod(id, userId, userUuid);
    }
}