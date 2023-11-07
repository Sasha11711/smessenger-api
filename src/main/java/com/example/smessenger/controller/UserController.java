package com.example.smessenger.controller;

import com.example.smessenger.dto.user.UserCreateDto;
import com.example.smessenger.dto.user.UserDto;
import com.example.smessenger.dto.user.UserInfoDto;
import com.example.smessenger.dto.user.UserUpdateDto;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final Mapper mapper;

    @GetMapping(value = "/find-by-username/{username}")
    public Set<UserInfoDto> getAllByUsername(@PathVariable(required = false) String username) {
        return userService.getAllByUsername(username).stream().map(mapper::toUserInfoDto).collect(Collectors.toSet());
    }

    @GetMapping(value = "/{id}")
    public UserInfoDto get(@PathVariable Long id) {
        return mapper.toUserInfoDto(userService.get(id));
    }

    @GetMapping(value = "/get-full/{id}&{uuid}")
    public UserDto get(@PathVariable Long id, @PathVariable UUID uuid) {
        return mapper.toUserDto(userService.checkUser (id, uuid));
    }

    @GetMapping(value = "/get-token/{login}&{password}")
    public String getToken(@PathVariable String login, @PathVariable String password) {
        return userService.getToken(login, password);
    }

    @PostMapping
    public void create(@RequestBody UserCreateDto userInfoDto) {
        userService.create(mapper.toUser(userInfoDto));
    }

    @PutMapping("/{id}&{uuid}")
    public void update(@PathVariable Long id, @PathVariable UUID uuid, @RequestBody UserUpdateDto userUpdateDto) {
        userService.update(id, uuid, userUpdateDto);
    }

    @PutMapping("/{id}&{uuid}/{login}&{oldPassword}/{newPassword}")
    public String changePassword(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable String login, @PathVariable String oldPassword, @PathVariable String newPassword) {
        return userService.changePassword(id, uuid, login, oldPassword, newPassword);
    }

    @PutMapping("/{id}&{uuid}/add-request/{userId}")
    public void addFriendRequest(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable Long userId) {
        userService.addFriendRequest(id, uuid, userId);
    }

    @PutMapping("/{id}&{uuid}/remove-request/{userId}")
    public void removeFriendRequest(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable Long userId) {
        userService.removeFriendRequest(id, uuid, userId);
    }

    @PutMapping("/{id}&{uuid}/decline-request/{userId}")
    public void declineFriendRequest(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable Long userId) {
        userService.declineFriendRequest(id, uuid, userId);
    }

    @PutMapping("/{id}&{uuid}/accept-request/{userId}")
    public void acceptFriendRequest(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable Long userId) {
        userService.acceptFriendRequest(id, uuid, userId);
    }

    @PutMapping("/{id}&{uuid}/remove-friend/{userId}")
    public void removeFriend(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable Long userId) {
        userService.removeFriend(id, uuid, userId);
    }

    @PutMapping("/{id}&{uuid}/block/{userId}")
    public void blockUser(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable Long userId) {
        userService.blockUser(id, uuid, userId);
    }

    @PutMapping("/{id}&{uuid}/unblock/{userId}")
    public void unblockUser(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable Long userId) {
        userService.unblockUser(id, uuid, userId);
    }

    @PutMapping("/{id}&{uuid}/reset-uuid")
    public void resetUuid(@PathVariable Long id, @PathVariable UUID uuid) {
        userService.resetUuid(id, uuid);
    }

    @DeleteMapping("/{id}&{uuid}/{login}&{password}")
    public void delete(@PathVariable Long id, @PathVariable UUID uuid, @PathVariable String login, @PathVariable String password) {
        userService.delete(id, uuid, login, password);
    }
}
