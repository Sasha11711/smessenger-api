package com.example.smessenger.dto.user;

import com.example.smessenger.dto.chat.ChatDto;
import com.example.smessenger.dto.chat.ChatInfoDto;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private Long id;
    private UUID uuid;

    private String username;
    private Byte[] avatar;
    private Instant registrationInstant;
    private Boolean isDeactivated;

    private Set<ChatDto> chats;
    private Set<ChatInfoDto> moderatorAt;
    private Set<ChatInfoDto> bannedAt;

    private Set<UserInfoDto> friends;
    private Set<UserInfoDto> friendRequests;
    private Set<UserInfoDto> FriendRequestedBy;
    private Set<UserInfoDto> blockedUsers;
    private Set<UserInfoDto> blockedBy;
}
