package com.example.ecommerceb2b.repository;

import com.example.ecommerceb2b.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
