package com.example.smessenger.dto.user;

import lombok.Data;

@Data
public class UserCreateDto {
    private String login;
    private String password;

    private String username;
    private Byte[] avatar;
}
