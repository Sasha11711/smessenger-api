package com.example.smessenger.service;

import com.example.smessenger.dto.user.UserCreateDto;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.BadRequestException;
import com.example.smessenger.exception.ForbiddenException;
import com.example.smessenger.exception.GoneException;
import com.example.smessenger.exception.NotFoundException;
import com.example.smessenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final Pattern loginRegex = Pattern.compile("^[\\w-]{10,}$");
    private final Pattern passwordRegex = Pattern.compile("^(?=.*\\d)[\\w-]{12,}$");

    public Users get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public String getIdUuid(String login, String password) {
        BadRequestException exception = new BadRequestException("Incorrect username or password");
        Users existingUser = userRepository.findByLogin(login).orElseThrow(() -> exception);
        if (Objects.equals(existingUser.getPassword(), password))
            return existingUser.getId() + "&" + existingUser.getUuid();
        throw exception;
    }

    public void create(Users user) {
        String password = user.getPassword();
        if (!loginRegex.matcher(user.getLogin()).matches())
            throw new BadRequestException("Invalid login format");
        if (!passwordRegex.matcher(password).matches())
            throw new BadRequestException("Invalid password format");
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userRepository.save(user);
    }

    public void update(Long id, UUID uuid, UserCreateDto userInfoDto) {
        Users existingUser = checkUser(id, uuid);
        existingUser.setUsername(userInfoDto.getUsername());
        existingUser.setAvatar(userInfoDto.getAvatar());
        userRepository.save(existingUser);
    }

    public void changePassword(Long id, UUID uuid, String username, String oldPassword, String newPassword) {
        Users existingUser = checkUser(id, uuid);
        if (!Objects.equals(existingUser.getUsername(), username) || !Objects.equals(existingUser.getPassword(), oldPassword))
            throw new BadRequestException("Incorrect username or password");
        if (!passwordRegex.matcher(newPassword).matches())
            throw new BadRequestException("Invalid password format");
        existingUser.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        userRepository.save(existingUser);
    }

    public void addFriendRequest(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = checkUser(userId);
        if (existingUser.getBlockedUsers().contains(existingAnotherUser))
            throw new ForbiddenException("User is blocked");
        if (existingUser.getBlockedBy().contains(existingUser))
            throw new ForbiddenException("You're blocked");
        if (existingUser.getFriendRequestedBy().contains(existingAnotherUser)) {
            acceptFriendRequest(id, uuid, userId);
        } else if (!existingUser.getFriends().contains(existingAnotherUser)) {
            existingUser.getFriendRequests().add(existingAnotherUser);
        }
        userRepository.save(existingUser);
    }

    public void removeFriendRequest(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser.getFriendRequests().remove(existingAnotherUser))
            userRepository.save(existingUser);
    }

    public void declineFriendRequest(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser.getFriendRequestedBy().remove(existingAnotherUser))
            userRepository.save(existingUser);
    }

    public void acceptFriendRequest(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser.getFriendRequestedBy().remove(existingAnotherUser)) {
            existingUser.getFriends().add(existingAnotherUser);
            userRepository.save(existingUser);
        }
    }

    public void removeFriend(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser.getFriends().remove(existingAnotherUser))
            userRepository.save(existingUser);
    }

    public void blockUser(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser.getBlockedUsers().add(existingAnotherUser)) {
            existingUser.getFriendRequests().remove(existingAnotherUser);
            existingUser.getFriendRequestedBy().remove(existingUser);
            existingUser.getFriends().remove(existingAnotherUser);
            userRepository.save(existingUser);
        }
    }

    public void unblockUser(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser.getBlockedUsers().remove(existingAnotherUser))
            userRepository.save(existingUser);
    }

    public void resetUuid(Long id, UUID uuid) {
        Users existingUser = checkUser(id, uuid);
        existingUser.setUuid(UUID.randomUUID());
        userRepository.save(existingUser);
    }

    public void delete(Long id, UUID uuid, String username, String password) {
        Users existingUser = checkUser(id, uuid);
        if (!Objects.equals(existingUser.getUsername(), username) || !Objects.equals(existingUser.getPassword(), password))
            throw new BadRequestException("Incorrect username or password");
        existingUser.setIsDeactivated(true);
        existingUser.setUsername("deactivated user");
        existingUser.getChats().clear();
        existingUser.getFriends().clear();
        existingUser.getFriendRequests().clear();
        existingUser.getFriendRequestedBy().clear();
        existingUser.getModeratorAt().clear();
        userRepository.save(existingUser);
    }

    public Users checkUser(Long id) throws GoneException {
        Users existingUser = get(id);
        if (existingUser.getIsDeactivated())
            throw new GoneException("You're deactivated");
        return existingUser;
    }

    public Users checkUser(Long id, UUID uuid) throws GoneException, BadRequestException {
        Users existingUser = checkUser(id);
        if (!existingUser.getUuid().equals(uuid))
            throw new BadRequestException("Invalid uuid");
        return existingUser;
    }
}
