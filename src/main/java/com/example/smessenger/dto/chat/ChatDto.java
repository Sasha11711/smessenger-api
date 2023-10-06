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
    private Byte[] chatImage;

    private MessageDto lastMessage;
    private Set<Long> usersId;
    private Set<Long> moderatorsId;
    private Set<Long> bannedUsersId;
}
