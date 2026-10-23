package com.example.ecommerceb2b.domain.entities;

import com.example.ecommerceb2b.application.DTOs.CriarAdminRequest;
import com.example.ecommerceb2b.application.DTOs.EnumRoleUsuario;
import com.example.ecommerceb2b.application.DTOs.EnumStatusUsuario;
import com.example.ecommerceb2b.application.DTOs.UsuarioLogado;
import com.example.ecommerceb2b.application.DTOs.UsuarioRequest;
import com.example.ecommerceb2b.domain.valueobject.CPF;
import com.example.ecommerceb2b.domain.valueobject.Telefone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Embedded
    private CPF cpf;

    @Embedded
    private Telefone telefone;
    private String senha;
    private String email;
    @Enumerated(EnumType.STRING)
    private EnumRoleUsuario role = EnumRoleUsuario.ROLE_USER;

    private EnumStatusUsuario status = EnumStatusUsuario.ATIVO;

    // Regras do domínio: o que não for válido lança erro (a Controller transforma em 400 com a mensagem)
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome Invalido!");
        }
        this.nome = nome;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha Invalida!");
        }
        this.senha = senha;
    }

    // O e-mail é obrigatório e precisa ter "@"
    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email Invalido!");
        }
        this.email = email;
    }

    // O telefone é opcional: só é validado e guardado quando vier preenchido
    public void informarTelefone(String telefone) {
        if (telefone != null && !telefone.isBlank()) {
            this.setTelefone(new Telefone(telefone));
        }
    }

    // O papel (role) é opcional: só é trocado quando vier preenchido (um usuário novo começa como ROLE_USER)
    public void informarRole(EnumRoleUsuario role) {
        if (role != null) {
            this.setRole(role);
        }
    }

    public Usuario(CriarAdminRequest criarAdminRequest) {
        this.setCpf(new CPF (criarAdminRequest.cpf()));
        this.setNome(criarAdminRequest.nome());
        this.setSenha(criarAdminRequest.senha());
        this.setEmail(criarAdminRequest.email());
        this.setRole(EnumRoleUsuario.ROLE_ADMIN);
    }

    public Usuario(UsuarioRequest usuario) {
        this.setCpf(new CPF (usuario.cpf()));
        this.informarTelefone(usuario.telefone());
        this.setNome(usuario.nome());
        this.setSenha(usuario.senha());
        this.setEmail(usuario.email());
        this.informarRole(usuario.role());
    }

    public UsuarioLogado getUsuarioLogado() {
        return (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Usuário sem role (ex.: cadastrado antes da role existir) fica sem nenhuma permissão
        if (this.role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
