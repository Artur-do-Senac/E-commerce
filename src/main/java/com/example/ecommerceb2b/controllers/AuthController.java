package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.DTOs.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;

@RestController
@RequestMapping("/auth")
@Tag(name= "Autenticação", description = "Controller de autenticação")
public class AuthController {

    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuários", description = "Método de login")
    public ResponseEntity<?> Login (@RequestBody LoginRequest loginRequest){

        if (loginRequest.email().equals("string") && loginRequest.senha().equals("string")){
            //Gerar token
            return ResponseEntity.ok("");
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
    }
}
