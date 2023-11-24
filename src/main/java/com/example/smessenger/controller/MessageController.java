package com.example.smessenger.controller;

import com.example.smessenger.dto.message.MessageCreateDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.ChatService;
import com.example.smessenger.service.MessageService;
import com.example.smessenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final SimpMessagingTemplate simpMessagingService;
    private final MessageService messageService;
    private final ChatService chatService;
    private final UserService userService;
    private final Mapper mapper;

    @GetMapping
    public Page<MessageDto> getAll(@RequestParam Long chatId, @RequestParam String token, @RequestParam Integer page, @RequestParam Integer size) {
        Users existingUser = userService.checkUser(token);
        Chat existingChat = chatService.get(chatId);
        if (!existingChat.getUsers().contains(existingUser)) {
            throw new ForbiddenException("User is not in the chat");
        }
        return messageService.getAll(chatId, token, page, size).map(mapper::toMessageDto);
    }

    @GetMapping(value = "/{id}")
    public MessageDto get(@PathVariable Long id,
                          @RequestParam String token) {
        return mapper.toMessageDto(messageService.get(id, token));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto createByUserInChat(@RequestParam Long chatId, @RequestParam String token,
                                         @ModelAttribute MessageCreateDto messageCreateDto) {
        MessageDto messageDto = mapper.toMessageDto(messageService.createByUserInChat(chatId, token, messageCreateDto));
        simpMessagingService.convertAndSend("/chat/" + chatId + "/messageSent", messageDto);
        return messageDto;
    }

    @PutMapping("/{id}")
    public void updateByAuthor(@PathVariable Long id,
                               @RequestParam String token,
                               @RequestBody String newText) {
        MessageDto messageDto = mapper.toMessageDto(messageService.updateByAuthor(id, token, newText));
        simpMessagingService.convertAndSend("/chat/" + messageDto.getChat().getId() + "/messageEdited", messageDto);
    }

    @DeleteMapping("/{id}")
    public void deleteByAuthorOrMod(@PathVariable Long id,
                                    @RequestParam String token) {
        MessageDto messageDto = mapper.toMessageDto(messageService.deleteByAuthorOrMod(id, token));
        simpMessagingService.convertAndSend("/chat/" + messageDto.getChat().getId() + "/messageDeleted", messageDto.getId());
    }
}
