package com.example.smessenger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiDocConfig implements WebMvcConfigurer {
    private Info apiInfo() {
        return new Info().title("SMessenger API")
                .description("API for examination messenger")
                .version("0.1.0")
                .contact(new Contact().name("Oleksandr Marchuk").email("oleksandrmarchuk2006@gmail.com"));
    }

    @Bean
    public OpenAPI openApiConfig() {
        return new OpenAPI().info(apiInfo());
    }
}