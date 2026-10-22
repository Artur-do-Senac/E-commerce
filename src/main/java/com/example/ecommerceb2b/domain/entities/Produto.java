package com.example.ecommerceb2b.domain.entities;

import com.example.ecommerceb2b.application.DTOs.EnumStatusProduto;
import com.example.ecommerceb2b.application.DTOs.ProdutoRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double preco;
    private Date vencimento;
    private Integer quantidadeEstoque;
    private EnumStatusProduto status = EnumStatusProduto.NA_VALIDADE;

    // Regras do domínio: o que não for válido lança erro (a Controller transforma em 400 com a mensagem)
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome Invalido!");
        }
        this.nome = nome;
    }

    // O preço não pode ser negativo
    public void setPreco(Double preco) {
        if (preco == null || preco < 0) {
            throw new IllegalArgumentException("Preco Invalido!");
        }
        this.preco = preco;
    }

    public void setVencimento(Date vencimento) {
        if (vencimento == null) {
            throw new IllegalArgumentException("Vencimento Invalido!");
        }
        this.vencimento = vencimento;
    }

    // O estoque não pode ficar negativo
    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        if (quantidadeEstoque == null || quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Quantidade em Estoque Invalida!");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Produto(ProdutoRequest produto) {
        this.setNome(produto.nome());
        this.setPreco(produto.preco());
        this.setVencimento(produto.vencimento());
        this.setQuantidadeEstoque(produto.quantidadeEstoque());
    }
}
