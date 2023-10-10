package com.example.smessenger.dto.chat;

import com.example.smessenger.dto.message.MessageDto;
import com.example.smessenger.dto.user.UserInfoDto;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class ChatDto {
    private Long id;

    private String title;

    private MessageDto lastMessage;
    private Set<UserInfoDto> users;
    private Set<Long> moderatorsId;
    private Set<UserInfoDto> bannedUsers;
}
