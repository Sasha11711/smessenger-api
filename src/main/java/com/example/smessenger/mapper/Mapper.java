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
import com.example.smessenger.entity.Image;
import com.example.smessenger.entity.Message;
import com.example.smessenger.entity.Users;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    ChatInfoDto toChatInfoDto(Chat chat);

    @Mappings({
            @Mapping(target = "lastMessage", source = "messages", qualifiedByName = "toLastMessage"),
            @Mapping(target = "moderatorsId", source = "moderators")
    })
    ChatDto toChatDto(Chat chat);

    Chat toChat(ChatCreateDto chatCreateDto);

    MessageDto toMessageDto(Message message);

    Message toMessage(MessageCreateDto messageCreateDto);

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
        return messages.get(messages.size() - 1);
    }

    static <T> Page<T> toPage(List<T> objs, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), objs.size());
        List<T> subObjs = objs.subList(start, end);

        return new PageImpl<>(subObjs, pageable, objs.size());
    }

    static Pair<Long, UUID> splitToken(String token) {
        String[] split = token.split("&");
        return new ImmutablePair<>(Long.parseLong(split[0]), UUID.fromString(split[1]));
    }
}
