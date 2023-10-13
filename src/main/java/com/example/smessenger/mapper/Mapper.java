package com.example.smessenger.mapper;

import com.example.smessenger.dto.chat.ChatCreateDto;
import com.example.smessenger.dto.chat.ChatDto;
import com.example.smessenger.dto.chat.ChatInfoDto;
import com.example.smessenger.dto.message.MessageCreateDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.dto.user.UserCreateDto;
import com.example.smessenger.dto.user.UserDto;
import com.example.smessenger.dto.user.UserInfoDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    ChatInfoDto toChatInfoDto(Chat chat);

    @Mapping(target = "lastMessage", source = "messages", qualifiedByName = "toLastMessage")
    ChatDto toChatDto(Chat chat);

    @Named("toLastMessage")
    default Message toLastMessage(List<Message> messages) {
        return messages.get(messages.size() - 1);
    }

    Chat toChat(ChatCreateDto chatCreateDto);

    MessageDto toMessageDto(Message message);

    Message toMessage(MessageCreateDto messageCreateDto);

    UserInfoDto toUserInfoDto(Users user);

    UserDto toUserDto(Users user);

    default Set<Long> chatsToLongs(Set<Chat> chats) {
        return chats.stream().map(Chat::getId).collect(Collectors.toSet());
    }

    default Set<Long> usersToLongs(Set<Users> users) {
        return users.stream().map(Users::getId).collect(Collectors.toSet());
    }

    Users toUser(UserCreateDto userCreateDto);
}
