package com.example.ecommerceb2b.application.DTOs;

// Cadastro público (sem token) de um cliente: sempre vira USER
public record CadastroUsuarioRequest(String nome, String email, String senha, String cpf, String telefone) {
}
