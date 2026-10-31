package com.example.ecommerceb2b.domain.entities;

import com.example.ecommerceb2b.application.DTOs.CriarAdminRequest;
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
    private String role = "ROLE_USER";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private Empresa empresa;



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

    public Usuario(CriarAdminRequest criarAdminRequest) {
        this.setCpf(new CPF (criarAdminRequest.cpf()));
        this.setNome(criarAdminRequest.nome());
        this.setSenha(criarAdminRequest.senha());
        this.setEmail(criarAdminRequest.email());
        this.setRole("ROLE_ADMIN");
    }

    // O perfil é ADMIN, GERENTE ou USER. Vazio mantém o que já existe (USER para um usuário novo)
    public void informarPerfil(String perfil) {
        if (perfil == null || perfil.isBlank()) {
            return;
        }
        if (!List.of("ADMIN", "GERENTE", "USER").contains(perfil)) {
            throw new IllegalArgumentException("Perfil Invalido!");
        }
        this.role = "ROLE_" + perfil;
    }

    // Multilocação: todo usuário pertence a uma empresa, só o ADMIN (global) pode ficar sem. Por isso o perfil é definido antes
    public void informarEmpresa(Empresa empresa) {
        if (empresa == null && !"ROLE_ADMIN".equals(this.role)) {
            throw new IllegalArgumentException("Empresa Invalida!");
        }
        this.empresa = empresa;
    }

    public Usuario(UsuarioRequest usuario, Empresa empresa) {
        this.setCpf(new CPF (usuario.cpf()));
        this.informarTelefone(usuario.telefone());
        this.setNome(usuario.nome());
        this.setSenha(usuario.senha());
        this.setEmail(usuario.email());
        this.informarPerfil(usuario.perfil());
        this.informarEmpresa(empresa);
    }

    public UsuarioLogado getUsuarioLogado() {
        return (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
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
