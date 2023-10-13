package com.example.smessenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Chat {
    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    private String title;

    @Lob
    private Byte[] chatImage;

    @ToString.Include
    @Column(nullable = false, updatable = false)
    private Instant creationInstant = Instant.now();

    @OneToMany(mappedBy = "chat", orphanRemoval = true)
    private List<Message> messages;

    @ManyToMany
    @JoinTable(name = "chat_user",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> users;

    @ManyToMany
    @JoinTable(name = "chat_moderator",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "moderator_id"))
    private Set<Users> moderators;

    @ManyToMany
    @JoinTable(name = "chat_banned_user",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "banned_user_id"))
    private Set<Users> bannedUsers;
}
