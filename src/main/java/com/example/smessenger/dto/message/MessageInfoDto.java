package com.example.smessenger.dto.message;

import com.example.smessenger.dto.user.UserInfoDto;
import lombok.Data;

import java.time.Instant;

@Data
public class MessageInfoDto {
    private Long id;

    private String text;
    private Instant sentInstant;
    private Boolean isEdited;
    private Byte[] embedImage;

    private UserInfoDto author;
}
