package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Usuario;
import com.example.ecommerceb2b.domain.valueobject.CPF;

public record UsuarioLogado(Long id, String email, CPF cpf,
                            java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {

    public UsuarioLogado (Usuario usuario){
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getAuthorities()
        );
    }
}
