package com.example.smessenger.service;

import com.example.smessenger.dto.user.UserUpdateDto;
import com.example.smessenger.entity.Users;
import com.example.smessenger.exception.*;
import com.example.smessenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final Pattern loginRegex = Pattern.compile("^[\\w-]{10,}$");
    private final Pattern passwordRegex = Pattern.compile("^(?=.*\\d)[\\w-]{12,}$");

    public Set<Users> getAllByUsername(String username) {
        return userRepository.findAllByUsernameStartsWith(username);
    }

    public Users get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found"));
    }

    public String getToken(String login, String password) {
        BadRequestException loginPasswordException = new BadRequestException("Incorrect login or password");
        Users existingUser = userRepository.findByLogin(login).orElseThrow(() -> loginPasswordException);

        if (existingUser.getIsDeactivated())
            throw new GoneException("You're deactivated");
        if (!existingUser.getPassword().equals(password))
            throw loginPasswordException;

        return existingUser.getId() + "&" + existingUser.getUuid();
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

    public void update(Long id, UUID uuid, UserUpdateDto userUpdateDto) {
        Users existingUser = checkUser(id, uuid);
        String newUsername = userUpdateDto.getUsername();
        Byte[] newAvatar = userUpdateDto.getAvatar();
        boolean usernameChangeable = newUsername != null && !newUsername.equals(existingUser.getUsername());
        boolean avatarChangeable = newAvatar != null && newAvatar != existingUser.getAvatar().getImage();
        if (usernameChangeable || avatarChangeable) {
            if (usernameChangeable) existingUser.setUsername(newUsername);
            if (avatarChangeable) existingUser.setAvatar(imageService.create(newAvatar));
            userRepository.save(existingUser);
        }
    }

    public String changePassword(Long id, UUID uuid, String login, String oldPassword, String newPassword) {
        Users existingUser = checkUser(id, uuid);
        if (!Objects.equals(existingUser.getLogin(), login) || !Objects.equals(existingUser.getPassword(), oldPassword))
            throw new BadRequestException("Incorrect login or password");
        if (!passwordRegex.matcher(newPassword).matches())
            throw new BadRequestException("Invalid password format");
        existingUser.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        UUID newUuid = UUID.randomUUID();
        existingUser.setUuid(newUuid);
        userRepository.save(existingUser);
        return existingUser.getId() + "&" + newUuid;
    }

    public void addFriendRequest(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = checkUser(userId);
        if (existingUser == existingAnotherUser)
            throw new ConflictException("Cannot do actions with yourself");
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
        if (existingAnotherUser.getFriendRequests().remove(existingUser))
            userRepository.save(existingAnotherUser);
    }

    public void acceptFriendRequest(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingAnotherUser.getFriendRequests().remove(existingUser)) {
            existingUser.getFriends().add(existingAnotherUser);
            existingAnotherUser.getFriends().add(existingUser);
            userRepository.save(existingUser);
            userRepository.save(existingAnotherUser);
        }
    }

    public void removeFriend(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser.getFriends().remove(existingAnotherUser)) {
            existingAnotherUser.getFriends().remove(existingUser);
            userRepository.save(existingUser);
            userRepository.save(existingAnotherUser);
        }
    }

    public void blockUser(Long id, UUID uuid, Long userId) {
        Users existingUser = checkUser(id, uuid);
        Users existingAnotherUser = get(userId);
        if (existingUser == existingAnotherUser)
            throw new ConflictException("Cannot do actions with yourself");
        if (existingUser.getBlockedUsers().add(existingAnotherUser)) {
            existingUser.getFriendRequests().remove(existingAnotherUser);
            existingUser.getFriendRequestedBy().remove(existingAnotherUser);
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

    public void delete(Long id, UUID uuid, String login, String password) {
        Users existingUser = checkUser(id, uuid);
        if (!Objects.equals(existingUser.getLogin(), login) || !Objects.equals(existingUser.getPassword(), password))
            throw new BadRequestException("Incorrect login or password");
        Long avatarId = existingUser.getAvatar().getId();
        existingUser.setUsername("deactivated user");
        existingUser.setIsDeactivated(true);
        existingUser.setAvatar(null);
        existingUser.getChats().clear();
        existingUser.getModeratorAt().clear();
        existingUser.getBlockedUsers().clear();
        Set<Users> friends = existingUser.getFriends();
        for (Users friend : friends) {
            friend.getFriends().remove(existingUser);
            userRepository.save(friend);
        }
        friends.clear();
        existingUser.getFriendRequests().clear();
        existingUser.getFriendRequestedBy().clear();

        userRepository.save(existingUser);
        imageService.deleteIfUnused(avatarId);
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
