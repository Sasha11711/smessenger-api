package com.example.smessenger.dto.user;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;
    private Byte[] avatar;
}
