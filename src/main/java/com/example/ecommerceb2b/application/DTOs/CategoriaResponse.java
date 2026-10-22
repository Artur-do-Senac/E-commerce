package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Categoria;

public record CategoriaResponse(Long id, String nome, String descricao) {

    public CategoriaResponse(Categoria categoriaEntidade) {

      this(
        categoriaEntidade.getId(),
        categoriaEntidade.getNome(),
        categoriaEntidade.getDescricao()
        );
    }
}
