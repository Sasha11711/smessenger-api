package com.example.smessenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class User {
    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false, unique = true)
    private String login;

    @ToString.Include
    @Column(nullable = false)
    private String password;

    @ToString.Include
    @Column(nullable = false)
    private String username;

    @Lob
    private Byte[] avatar;

    @ToString.Include
    @Column(nullable = false, updatable = false)
    private Date registrationDate;

    @ManyToMany(mappedBy = "users")
    private Set<Chat> chats;

    @ManyToMany(mappedBy = "moderators")
    private Set<Chat> moderatorAt;

    @ManyToMany(mappedBy = "bannedUsers")
    private Set<Chat> bannedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends;

    @ManyToMany
    @JoinTable(name = "user_friend_requested",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_requested_id"))
    private Set<User> friendRequests;

    @ManyToMany(mappedBy = "friendRequests")
    private Set<User> FriendRequestedBy;

    @ManyToMany
    @JoinTable(name = "user_blocked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id"))
    private Set<User> blockedUsers;

    @ManyToMany(mappedBy = "blockedUsers")
    private Set<User> blockedBy;
}
