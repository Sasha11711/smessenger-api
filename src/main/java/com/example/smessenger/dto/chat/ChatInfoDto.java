package com.example.smessenger.dto.chat;

import lombok.Data;

import java.time.Instant;

@Data
public class ChatInfoDto {
    private Long id;

    private String title;
    private Byte[] chatImage;
    private Instant creationInstant;
}
