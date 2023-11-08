package com.example.smessenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Image {
    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "image")
    private List<Chat> chats;

    @OneToMany(mappedBy = "embed")
    private List<Message> messages;

    @OneToMany(mappedBy = "avatar")
    private List<Users> users;
}
