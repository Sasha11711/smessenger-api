package com.example.smessenger.controller;

import com.example.smessenger.dto.user.UserCreateDto;
import com.example.smessenger.dto.user.UserDto;
import com.example.smessenger.dto.user.UserInfoDto;
import com.example.smessenger.dto.user.UserUpdateDto;
import com.example.smessenger.mapper.Mapper;
import com.example.smessenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
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

    @GetMapping(value = "/get-full")
    public UserDto get(@RequestParam String token) {
        return mapper.toUserDto(userService.checkUser(token));
    }

    @GetMapping(value = "/get-token")
    public String getToken(@RequestParam String login, @RequestParam String password) {
        return userService.getToken(login, password);
    }

    @PostMapping
    public void create(@ModelAttribute UserCreateDto userCreateDto) {
        userService.create(mapper.toUser(userCreateDto));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void update(@RequestParam String token,
                       @RequestBody UserUpdateDto userUpdateDto) {
        userService.update(token, userUpdateDto);
    }

    @PutMapping("/change-password")
    public String changePassword(@RequestParam String token, @RequestParam String login, @RequestParam String password,
                                 @RequestBody String newPassword) {
        return userService.changePassword(token, login, password, newPassword);
    }

    @PutMapping("/add-request/{userId}")
    public void addFriendRequest(@PathVariable Long userId,
                                 @RequestParam String token) {
        userService.addFriendRequest(token, userId);
    }

    @PutMapping("/remove-request/{userId}")
    public void removeFriendRequest(@PathVariable Long userId,
                                    @RequestParam String token) {
        userService.removeFriendRequest(token, userId);
    }

    @PutMapping("/decline-request/{userId}")
    public void declineFriendRequest(@PathVariable Long userId,
                                     @RequestParam String token) {
        userService.declineFriendRequest(token, userId);
    }

    @PutMapping("/accept-request/{userId}")
    public void acceptFriendRequest(@PathVariable Long userId,
                                    @RequestParam String token) {
        userService.acceptFriendRequest(token, userId);
    }

    @PutMapping("/remove-friend/{userId}")
    public void removeFriend(@PathVariable Long userId,
                             @RequestParam String token) {
        userService.removeFriend(token, userId);
    }

    @PutMapping("/block/{userId}")
    public void blockUser(@PathVariable Long userId,
                          @RequestParam String token) {
        userService.blockUser(token, userId);
    }

    @PutMapping("/unblock/{userId}")
    public void unblockUser(@PathVariable Long userId,
                            @RequestParam String token) {
        userService.unblockUser(token, userId);
    }

    @PutMapping("/reset-uuid")
    public void resetUuid(@RequestParam String token) {
        userService.resetUuid(token);
    }

    @DeleteMapping
    public void delete(@RequestParam String token, @RequestParam String login, @RequestParam String password) {
        userService.delete(token, login, password);
    }
}
