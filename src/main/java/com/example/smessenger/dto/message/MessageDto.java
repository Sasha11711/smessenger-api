package com.example.smessenger.dto.message;

import com.example.smessenger.dto.chat.ChatInfoDto;
import com.example.smessenger.dto.user.UserInfoDto;
import lombok.Data;

import java.time.Instant;

@Data
public class MessageDto {
    private Long id;

    private String text;
    private Instant sentInstant;
    private Boolean isEdited;
    private Byte[] embedImage;

    private ChatInfoDto chat;
    private UserInfoDto author;
}
