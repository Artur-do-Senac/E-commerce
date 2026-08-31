package com.example.ecommerceb2b.repository;

import com.example.ecommerceb2b.entities.EnumStatusUsuario;
import com.example.ecommerceb2b.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsUsuarioByEmailAndSenha(String email, String senha);

    Optional<List<Usuario>> findByStatusNot(EnumStatusUsuario status);

}
