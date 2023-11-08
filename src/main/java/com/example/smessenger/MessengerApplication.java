package com.example.smessenger;

import com.example.smessenger.entity.Image;
import com.example.smessenger.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;

@RequiredArgsConstructor
@SpringBootApplication
public class MessengerApplication {
    private final ImageRepository imageRepository;

    public static void main(String[] args) {
        SpringApplication.run(MessengerApplication.class, args);
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            InputStream is = getClass().getResourceAsStream("/default.png");
            if (is == null) {
                throw new NullPointerException("Default image not found.");
            }
            byte[] byteArray = is.readAllBytes();
            Image defaultImage = new Image();
            defaultImage.setId(1L);
            defaultImage.setImage(byteArray);
            imageRepository.save(defaultImage);
            is.close();
        };
    }
}
