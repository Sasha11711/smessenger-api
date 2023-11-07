package com.example.smessenger.dto.chat;

import com.example.smessenger.entity.Image;
import lombok.Data;

@Data
public class ChatCreateDto {
    private String title;
    private Image image;
}
