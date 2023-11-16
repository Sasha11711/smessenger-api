package com.example.smessenger.dto.message;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MessageCreateDto {
    private String text;
    private MultipartFile embed;
}
