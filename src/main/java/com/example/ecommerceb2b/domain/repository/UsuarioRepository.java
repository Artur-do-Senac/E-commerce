package com.example.ecommerceb2b.domain.repository;

import com.example.ecommerceb2b.application.DTOs.EnumStatusUsuario;
import com.example.ecommerceb2b.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsUsuarioByEmailAndSenha(String email, String senha);

    // Login: devolve o próprio usuário autenticado (o token é gerado para ele, e não para "algum usuário com esse e-mail")
    Optional<Usuario> findFirstByEmailAndSenhaAndStatusNot(String email, String senha, EnumStatusUsuario status);

    Optional<List<Usuario>> findByStatusNot(EnumStatusUsuario status);

    Optional<Usuario> findByEmail(String email);

}
