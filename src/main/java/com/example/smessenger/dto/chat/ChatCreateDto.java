package com.example.smessenger.dto.chat;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChatCreateDto {
    private String title;
    private MultipartFile image;
}
