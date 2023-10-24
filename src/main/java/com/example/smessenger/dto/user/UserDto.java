package com.example.smessenger.dto.user;

import com.example.smessenger.dto.chat.ChatDto;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private String username;

    private Set<ChatDto> chats;
    private Set<Long> moderatorAt;

    private Set<UserInfoDto> friends;
    private Set<UserInfoDto> friendRequests;
    private Set<UserInfoDto> FriendRequestedBy;
    private Set<UserInfoDto> blockedUsers;
}
