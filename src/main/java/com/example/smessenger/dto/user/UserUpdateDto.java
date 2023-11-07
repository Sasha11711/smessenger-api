package com.example.smessenger.dto.user;

import com.example.smessenger.entity.Image;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;
    private Image avatar;
}
