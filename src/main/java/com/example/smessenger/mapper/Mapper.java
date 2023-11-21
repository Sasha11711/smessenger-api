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
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

    MessageDto toMessageDto(Message message);

    UserInfoDto toUserInfoDto(Users user);

    UserDto toUserDto(Users user);

    Users toUser(UserCreateDto userCreateDto);

    default Long imageToLong(Image image) {
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
        int messageCount = messages.size();
        if (messageCount > 0) {
            return messages.get(messages.size() - 1);
        }
        return null;
    }

    static <T> Page<T> toPage(List<T> items, int page, int size) {
        int pageIndex = page - 1;
        int start = pageIndex * size;
        if (start > items.size())
            return new PageImpl<>(Collections.emptyList());
        int end = Math.min(start + size, items.size());
        List<T> subItems = items.subList(start, end);
        return new PageImpl<>(subItems, PageRequest.of(page, size), items.size());
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
