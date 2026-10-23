package com.example.ecommerceb2b.application.DTOs;

// role é opcional: quando não vier preenchida, o usuário novo é criado como ROLE_USER (e na edição a role atual é mantida)
public record UsuarioRequest(String nome, String email, String senha, String cpf, String telefone, EnumRoleUsuario role) {
}
