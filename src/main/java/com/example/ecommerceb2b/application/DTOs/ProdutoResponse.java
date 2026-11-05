package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Produto;

import java.util.Date;

public record ProdutoResponse(Long id, String nome, Double preco, Date vencimento, Integer quantidadeEstoque, EnumStatusProduto status, Long empresaId, String empresaNome) {

    public ProdutoResponse(Produto produtoEntidade) {

      this(
        produtoEntidade.getId(),
        produtoEntidade.getNome(),
        produtoEntidade.getPreco(),
        produtoEntidade.getVencimento(),
        produtoEntidade.getQuantidadeEstoque(),
        produtoEntidade.getStatus(),
        produtoEntidade.getEmpresa() == null ? null : produtoEntidade.getEmpresa().getId(),
        produtoEntidade.getEmpresa() == null ? null : produtoEntidade.getEmpresa().getRazaoSocial()
        );
    }
}
