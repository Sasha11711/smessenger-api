package com.example.smessenger.service;

import com.example.smessenger.dto.message.MessageCreateDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.BadRequestException;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;
    private final ImageService imageService;

    public Message get(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public Message get(Long id, String token) {
        userService.checkUser(token);
        return get(id);
    }

    public Page<Message> getAll(Long chatId, String token, int page, int size) {
        userService.checkUser(token);
        return messageRepository.findMessagesByChat_Id(chatId, PageRequest.of(page, size));
    }

    public Message createByUserInChat(Long chatId, String token, MessageCreateDto messageCreateDto) {
        Users existingUser = userService.checkUser(token);
        Chat existingChat = chatService.get(chatId);
        if (!existingChat.getUsers().contains(existingUser))
            throw new ForbiddenException("User is not in the chat");
        Message message = new Message();
        message.setText(messageCreateDto.getText());
        byte[] embed = Mapper.toByteArray(messageCreateDto.getEmbed());
        if (embed != null)
            message.setEmbed(imageService.create(embed));
        message.setChat(existingChat);
        message.setAuthor(existingUser);
        messageRepository.save(message);
        return message;
    }

    public Message updateByAuthor(Long id, String token, String newText) {
        Users existingUser = userService.checkUser(token);
        Message existingMessage = get(id);
        if (existingMessage.getAuthor() != existingUser)
            throw new ForbiddenException("User isn't author");
        if (newText == null || newText.equals(existingMessage.getText()))
            throw new BadRequestException("Message is empty or the same");
        existingMessage.setIsEdited(true);
        existingMessage.setText(newText);
        messageRepository.save(existingMessage);
        return existingMessage;
    }

    public Message deleteByAuthorOrMod(Long id, String token) {
        Users existingUser = userService.checkUser(token);
        Message existingMessage = get(id);
        if (existingMessage.getAuthor() == existingUser || existingMessage.getChat().getModerators().contains(existingUser)) {
            messageRepository.deleteById(id);
            imageService.deleteIfUnused(existingMessage.getEmbed().getId());
            return existingMessage;
        } else {
            throw new ForbiddenException("User isn't a moderator or an author");
        }
    }
}
