package com.example.ecommerceb2b.application.DTOs;

public record CriarAdminRequest(String nome, String email, String senha, String cpf, String secretKey) {
}
