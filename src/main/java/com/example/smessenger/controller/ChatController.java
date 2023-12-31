package com.example.smessenger.controller;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.dto.chat.ChatDto;
import com.example.smessenger.dto.chat.ChatInfoDto;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
                             @RequestParam String token) {
        return mapper.toChatDto(chatService.getByUser(id, token));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ChatInfoDto createByUser(@RequestParam String token,
                                    @ModelAttribute ChatCreateDto chatCreateDto) {
        return mapper.toChatInfoDto(chatService.createByUser(token, chatCreateDto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateByMod(@PathVariable Long id,
                            @RequestParam String token,
                            @ModelAttribute ChatCreateDto chatCreateDto) {
        chatService.updateByMod(id, token, chatCreateDto);
    }

    @PutMapping("/{id}/add/{userId}")
    public void addUser(@PathVariable Long id, @PathVariable Long userId,
                        @RequestParam String token) {
        chatService.addUser(id, userId, token);
    }

    @PutMapping("/{id}/leave")
    public void leaveUser(@PathVariable Long id,
                          @RequestParam String token) {
        chatService.leaveUser(id, token);
    }

    @PutMapping("/{id}/kick/{userId}")
    public void kickUserByMod(@PathVariable Long id, @PathVariable Long userId,
                              @RequestParam String token) {
        chatService.kickUserByMod(id, userId, token);
    }

    @PutMapping("/{id}/ban/{userId}")
    public void banUserByMod(@PathVariable Long id, @PathVariable Long userId,
                             @RequestParam String token) {
        chatService.banUserByMod(id, userId, token);
    }

    @PutMapping("/{id}/unban/{userId}")
    public void unbanUserByMod(@PathVariable Long id, @PathVariable Long userId,
                               @RequestParam String token) {
        chatService.unbanUserByMod(id, userId, token);
    }

    @PutMapping("/{id}/mod/{userId}")
    public void setModeratorByMod(@PathVariable Long id, @PathVariable Long userId,
                                  @RequestParam String token) {
        chatService.setModeratorByMod(id, userId, token);
    }

    @PutMapping("/{id}/unmod/{userId}")
    public void unsetModeratorByMod(@PathVariable Long id, @PathVariable Long userId,
                                    @RequestParam String token) {
        chatService.unsetModeratorByMod(id, userId, token);
    }

    @DeleteMapping("/{id}")
    public void deleteByMod(@PathVariable Long id,
                            @RequestParam String token) {
        chatService.deleteByMod(id, token);
    }
}
