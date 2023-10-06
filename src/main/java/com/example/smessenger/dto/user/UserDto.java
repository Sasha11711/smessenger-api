package com.example.smessenger.dto.user;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private Long id;
    private UUID uuid;

    private String username;
    private Byte[] avatar;

    private Set<Long> chats;
    private Set<Long> moderatorAt;
    private Set<Long> bannedAt;

    private Set<Long> friends;
    private Set<Long> friendRequests;
    private Set<Long> FriendRequestedBy;
    private Set<Long> blockedUsers;
    private Set<Long> blockedBy;
}
