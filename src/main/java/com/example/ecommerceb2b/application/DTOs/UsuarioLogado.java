package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Empresa;
import com.example.ecommerceb2b.domain.entities.Usuario;
import com.example.ecommerceb2b.domain.valueobject.CPF;
import org.springframework.security.core.context.SecurityContextHolder;

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

    // O usuário logado é o principal que o JwtFilter colocou no contexto de segurança
    public static UsuarioLogado atual() {
        return (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean admin() {
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    // ADMIN acessa tudo; os demais só acessam o que pertence à própria empresa
    public boolean podeAcessar(Empresa empresa) {
        return admin() || (empresa != null && empresa.getId().equals(empresa_id));
    }
}
