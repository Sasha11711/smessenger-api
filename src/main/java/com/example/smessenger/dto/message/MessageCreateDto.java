package com.example.smessenger.dto.message;

import com.example.smessenger.entity.Image;
import lombok.Data;

@Data
public class MessageCreateDto {
    private String text;
    private Image embed;
}
