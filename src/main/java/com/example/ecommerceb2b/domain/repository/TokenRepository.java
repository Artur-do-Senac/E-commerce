package com.example.ecommerceb2b.domain.repository;

import com.example.ecommerceb2b.domain.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    // Marca todos os tokens ativos como inativos (continuam salvos no banco, mas não são mais aceitos)
    @Modifying
    @Transactional
    @Query("update Token t set t.ativo = false where t.ativo = true")
    int desativarTodos();
}
