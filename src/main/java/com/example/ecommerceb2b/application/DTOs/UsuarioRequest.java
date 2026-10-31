package com.example.ecommerceb2b.application.DTOs;

// perfil: ADMIN, GERENTE ou USER (vazio = USER). empresaId: empresa do usuário (obrigatória, exceto para ADMIN, que é global)
public record UsuarioRequest(String nome, String email, String senha, String cpf, String telefone, String perfil, Long empresaId) {
}
