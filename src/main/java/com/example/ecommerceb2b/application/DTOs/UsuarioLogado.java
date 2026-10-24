package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Usuario;
import com.example.ecommerceb2b.domain.valueobject.CPF;

public record UsuarioLogado(Long id, String email, CPF cpf, Long empresa_id,
                            java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {

    public UsuarioLogado (Usuario usuario){
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null,
                usuario.getAuthorities()
        );
    }
}
