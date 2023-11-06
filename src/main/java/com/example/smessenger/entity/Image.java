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
    private Byte[] image;

    @OneToMany(mappedBy = "image")
    private List<Chat> chats;

    @OneToMany(mappedBy = "embed")
    private List<Chat> messages;

    @OneToMany(mappedBy = "avatar")
    private List<Chat> users;
}
