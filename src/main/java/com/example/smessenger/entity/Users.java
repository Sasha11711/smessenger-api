package com.example.smessenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Users {
    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    private UUID uuid = UUID.randomUUID();

    @ToString.Include
    @Column(nullable = false, unique = true)
    private String login;

    @ToString.Include
    @Column(nullable = false)
    private String password;

    @ToString.Include
    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "avatar_id", nullable = false)
    private Image avatar;

    @ToString.Include
    @Column(nullable = false, updatable = false)
    private Instant registrationInstant = Instant.now();

    @ToString.Include
    @Column(nullable = false)
    private Boolean isDeactivated = false;

    @ManyToMany(mappedBy = "users")
    private Set<Chat> chats;

    @ManyToMany(mappedBy = "moderators")
    private Set<Chat> moderatorAt;

    @ManyToMany(mappedBy = "bannedUsers")
    private Set<Chat> bannedAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<Users> friends;

    @ManyToMany
    @JoinTable(name = "user_friend_requested",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_requested_id"))
    private Set<Users> friendRequests;

    @ManyToMany(mappedBy = "friendRequests")
    private Set<Users> FriendRequestedBy;

    @ManyToMany
    @JoinTable(name = "user_blocked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id"))
    private Set<Users> blockedUsers;

    @ManyToMany(mappedBy = "blockedUsers")
    private Set<Users> blockedBy;
}
