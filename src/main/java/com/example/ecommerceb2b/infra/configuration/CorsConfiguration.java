package com.example.ecommerceb2b.infra.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            // Telas que podem chamar a API (frontadmin em desenvolvimento)
            .allowedOrigins("http://localhost:3000", "http://localhost:3100")
            // OPTIONS é o pré-flight: o navegador pergunta se pode antes de enviar a requisição com o token
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS")
            // Authorization é o cabeçalho que leva o "Bearer <token>"
            .allowedHeaders("Authorization", "Content-Type")
            .maxAge(3600);
    }
}
