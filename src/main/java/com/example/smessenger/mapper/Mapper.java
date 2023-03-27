package com.example.smessenger.mapper;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.dto.chat.ChatDto;
import com.example.smessenger.dto.chat.ChatInfoDto;
import com.example.smessenger.dto.message.MessageCreateDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.dto.message.MessageInfoDto;
import com.example.smessenger.dto.user.UserCreateDto;
import com.example.smessenger.dto.user.UserDto;
import com.example.smessenger.dto.user.UserInfoDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    ChatInfoDto toChatInfoDto(Chat chat);

    ChatDto toChatDto(Chat chat);

    Chat toChat(ChatCreateDto chatCreateDto);

    MessageInfoDto toMessageInfoDto(Message message);

    MessageDto toMessageDto(Message message);

    Message toMessage(MessageCreateDto messageCreateDto);

    UserInfoDto toUserInfoDto(Users user);

    UserDto toUserDto(Users user);

    Users toUser(UserCreateDto userCreateDto);
}
