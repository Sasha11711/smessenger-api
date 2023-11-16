package com.example.smessenger.controller;

import com.example.smessenger.dto.message.MessageCreateDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;
    private final Mapper mapper;

    @GetMapping(value = "/{id}")
    public MessageDto get(@PathVariable Long id,
                          @RequestParam String token) {
        return mapper.toMessageDto(messageService.get(id, token));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto createByUserInChat(@RequestParam Long chatId, @RequestParam String token,
                                         @ModelAttribute MessageCreateDto messageCreateDto) {
        return mapper.toMessageDto(messageService.createByUserInChat(chatId, token, messageCreateDto));
    }

    @PutMapping("/{id}")
    public void updateByAuthor(@PathVariable Long id,
                               @RequestParam String token,
                               @RequestBody String newText) {
        messageService.updateByAuthor(id, token, newText);
    }

    @DeleteMapping("/{id}")
    public void deleteByAuthorOrMod(@PathVariable Long id,
                                    @RequestParam String token) {
        messageService.deleteByAuthorOrMod(id, token);
    }
}
