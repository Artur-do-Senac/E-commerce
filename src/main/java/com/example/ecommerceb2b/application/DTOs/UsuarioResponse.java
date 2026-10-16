package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.EnumStatusUsuario;
import com.example.ecommerceb2b.domain.entities.Usuario;

public record UsuarioResponse(Long id, String nome, String cpf, String email, EnumStatusUsuario status) {

    public UsuarioResponse(Usuario usuarioEntidade) {

      this(
        usuarioEntidade.getId(),
        usuarioEntidade.getNome(),
        usuarioEntidade.getCpf().toString(),
        usuarioEntidade.getEmail(),
        usuarioEntidade.getStatus()
        );
    }
}
