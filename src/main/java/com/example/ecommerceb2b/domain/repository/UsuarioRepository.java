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

    boolean existsUsuarioByEmailAndSenhaAndStatusNot(String email, String senha, EnumStatusUsuario status);

    Optional<List<Usuario>> findByStatusNot(EnumStatusUsuario status);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> getUsuariosByEmpresa_id(Long empresa);

}
