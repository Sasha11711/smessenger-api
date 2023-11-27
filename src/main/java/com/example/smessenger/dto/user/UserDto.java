package com.example.smessenger.dto.user;

import com.example.smessenger.dto.chat.ChatDto;
import lombok.Data;
import java.util.Set;

@Data
public class UserDto {
    private Long id;

    private String username;
    private Long avatarId;

    private Set<ChatDto> chats;
    private Set<Long> moderatorAt;

    private Set<UserInfoDto> friends;
    private Set<UserInfoDto> friendRequests;
    private Set<UserInfoDto> FriendRequestedBy;
    private Set<UserInfoDto> blockedUsers;
}
