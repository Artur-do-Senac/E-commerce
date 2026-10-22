package com.example.ecommerceb2b.application.DTOs;

import java.util.Date;

public record ProdutoRequest(String nome, Double preco, Date vencimento, Integer quantidadeEstoque) {
}
