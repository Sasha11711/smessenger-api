package com.example.smessenger.mapper;

import com.example.smessenger.dto.chat.ChatDto;
import com.example.smessenger.dto.chat.ChatInfoDto;
import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.dto.user.UserCreateDto;
import com.example.smessenger.dto.user.UserDto;
import com.example.smessenger.dto.user.UserInfoDto;
import com.example.smessenger.entity.Chat;
import com.example.smessenger.entity.Image;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.BadRequestException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    ChatInfoDto toChatInfoDto(Chat chat);

    @Mappings({
            @Mapping(target = "lastMessage", source = "messages", qualifiedByName = "toLastMessage"),
            @Mapping(target = "moderatorsId", source = "moderators"),
            @Mapping(target = "imageId", source = "image")
    })
    ChatDto toChatDto(Chat chat);

    @Mappings({
            @Mapping(target = "author.avatarId", source = "author.avatar"),
            @Mapping(target = "embedId", source = "embed")
    })
    MessageDto toMessageDto(Message message);

    @Mapping(target = "avatarId", source = "avatar")
    UserInfoDto toUserInfoDto(Users user);

    @Mapping(target = "avatarId", source = "avatar")
    UserDto toUserDto(Users user);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Users toUser(UserCreateDto userCreateDto);

    default Long imageToLong(Image image) {
        if (image == null)
            return null;
        return image.getId();
    }

    default Set<Long> chatsToLongs(Set<Chat> chats) {
        return chats.stream().map(Chat::getId).collect(Collectors.toSet());
    }

    default Set<Long> usersToLongs(Set<Users> users) {
        return users.stream().map(Users::getId).collect(Collectors.toSet());
    }

    @Named("toLastMessage")
    default Message toLastMessage(List<Message> messages) {
        messages.sort(Comparator.comparing(Message::getId));
        int messageCount = messages.size();
        if (messageCount > 0) {
            return messages.get(messages.size() - 1);
        }
        return null;
    }

    static Pair<Long, UUID> splitToken(String token) {
        String[] split = token.split("&");
        return new ImmutablePair<>(Long.parseLong(split[0]), UUID.fromString(split[1]));
    }

    static byte[] toByteArray(MultipartFile file) throws BadRequestException {
        if (file == null)
            return null;
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage(), e);
        }
    }
}
