package com.example.ecommerceb2b.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info().title("E-Commerce B2B").version("1.0.0").description("API para aula da 4ª fase"));
    }

}
