package com.example.smessenger.dto.message;

import lombok.Data;

@Data
public class MessageCreateDto {
    private String text;
    private Byte[] embedImage;
}
