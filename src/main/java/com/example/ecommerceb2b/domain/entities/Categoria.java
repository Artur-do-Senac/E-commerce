package com.example.ecommerceb2b.domain.entities;

import com.example.ecommerceb2b.application.DTOs.CategoriaRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    // Regra do domínio: a categoria precisa ter nome (a Controller transforma o erro em 400 com a mensagem)
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome Invalido!");
        }
        this.nome = nome;
    }

    public Categoria(CategoriaRequest categoria) {
        this.setNome(categoria.nome());
        this.setDescricao(categoria.descricao());
    }
}
