package com.example.smessenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Message {
    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    private String text;

    @ToString.Include
    @Column(nullable = false, updatable = false)
    private Date sentDate;

    @ToString.Include
    @Column(nullable = false)
    private Boolean isEdited = false;

    @Lob
    private Byte[] embedImage;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User author;
}
