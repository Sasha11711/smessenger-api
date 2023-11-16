package com.example.smessenger.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateDto {
    private String username;
    private MultipartFile avatar;
}
