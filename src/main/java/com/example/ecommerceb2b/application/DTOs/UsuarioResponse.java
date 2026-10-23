package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Usuario;

public record UsuarioResponse(Long id, String nome, String cpf, String telefone, String email, EnumStatusUsuario status) {

    public UsuarioResponse(Usuario usuarioEntidade) {

      this(
        usuarioEntidade.getId(),
        usuarioEntidade.getNome(),
        usuarioEntidade.getCpf() == null ? null : usuarioEntidade.getCpf().toString(),
        usuarioEntidade.getTelefone() == null ? null : usuarioEntidade.getTelefone().toString(),
        usuarioEntidade.getEmail(),
        usuarioEntidade.getStatus()
        );
    }
}
