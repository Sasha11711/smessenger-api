package com.example.smessenger.dto.user;

import lombok.Data;

import java.time.Instant;

@Data
public class UserInfoDto {
    private Long id;

    private String username;
    private Byte[] avatar;
    private Instant registrationInstant;
    private Boolean isDeactivated;
}
